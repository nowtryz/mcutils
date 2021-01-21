package net.nowtryz.mcutils.command.execution;

import com.google.common.collect.ImmutableList;
import com.google.inject.*;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import lombok.Value;
import net.nowtryz.mcutils.command.CommandResult;
import net.nowtryz.mcutils.command.SenderType;
import net.nowtryz.mcutils.command.annotations.*;
import net.nowtryz.mcutils.command.contexts.Context;
import net.nowtryz.mcutils.command.contexts.ExecutionContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodExecutor implements Executor {
    private static final Pattern PROVIDER_ARG = Pattern.compile("^\\s*<?(\\w+)>?\\s*$");

    private final @NotNull Method method;
    private final @NotNull SenderType target;
    private final @NotNull ImmutableList<String> argLine;
    private final @NotNull ImmutableList<GenericArg> genericArgs;
    private final @NotNull ImmutableList<Dependency<?>> dependencies;
    private final @NotNull Map<Key<?>, Provider<?>> cache = new HashMap<>();
    private final @NotNull ThreadLocal<ExecutionContext> localContext = new ThreadLocal<>();
    private final @NotNull InjectionPoint injectionPoint;
    private final @NotNull Command command;
    private final boolean staticMethod;

    private @Nullable Object object;

    private ImmutableList<ArgProvider<?>> argProviders;
    private ImmutableList<ArgProvider<?>> nullIgnoredArgProviders;
    private ImmutableList<ContextProvider<?>> contextProviders;
    private ImmutableList<ContextProvider<?>> nullIgnoredContextProviders;
    private Injector childInjector;
    private Injector injector;

    public interface Factory {
        MethodExecutor create(Method method);
    }

    @Inject
    public MethodExecutor(@Assisted Method method) {
        this.method = method;
        this.object = null;
        this.command = method.getAnnotation(Command.class);
        this.target = this.command.type();
        // overrides getArguments() for computing optimisation
        this.argLine = ImmutableList.copyOf(PATTERN_ON_SPACE.split(this.getCommand()));
        this.staticMethod = (method.getModifiers() & Modifier.STATIC) != 0;

        ImmutableList.Builder<GenericArg> argBuilder = ImmutableList.builder();
        Matcher matcher = GENERIC_ARG.matcher(this.getCommand());
        while (matcher.find()) argBuilder.add(new GenericArg(
                matcher.group(1),
                matcher.group(2) != null,
                this.argLine.indexOf(matcher.group()) - 1)
        );

        this.genericArgs = argBuilder.build();
        this.injectionPoint = InjectionPoint.forMethod(method, TypeLiteral.get(method.getDeclaringClass()));

        ImmutableList.Builder<Dependency<?>> builder = ImmutableList.builder();
        for (Dependency<?> dep : injectionPoint.getDependencies()) {
            if (isCacheable(dep.getKey())) {
                builder.add(dep);
            }
        }

        this.dependencies = builder.build();
    }

    private boolean isCacheable(Key<?> key) {
        Class<?> rawType = key.getTypeLiteral().getRawType();
        if (CommandSender.class.isAssignableFrom(rawType) || rawType.isAssignableFrom(ExecutionContext.class)) {
            return false;
        }

        Class<? extends Annotation> annotationType = key.getAnnotationType();
        return annotationType == null || (!annotationType.equals(Arg.class) && !annotationType.equals(net.nowtryz.mcutils.command.annotations.Context.class));
    }

    private ExecutionContext getLocalContext() {
        ExecutionContext context = this.localContext.get();
        if (context == null) throw new IllegalStateException("There is no context in this scope");
        return context;
    }

    @Inject
    @SuppressWarnings("UnstableApiUsage")
    void init(Provider<Injector> injector) {
        // We use a provider, so AssistedInject doesn't complain about the fact we use the injector
        this.injector = injector.get();

        // crete providers dor the child injector
        this.argProviders = Arrays.stream(method.getAnnotationsByType(ProvidesArg.class))
                .map(this::toArgProvider)
                .collect(ImmutableList.toImmutableList());
        this.nullIgnoredArgProviders = this.argProviders
                .stream()
            .filter(ArgProvider::isIgnoreNulls)
                .collect(ImmutableList.toImmutableList());

        this.contextProviders = Arrays.stream(method.getAnnotationsByType(ProvidesContext.class))
                .map(this::toContextProvider)
                .collect(ImmutableList.toImmutableList());
        this.nullIgnoredContextProviders = this.contextProviders
                .stream()
                .filter(ContextProvider::isIgnoreNulls)
                .collect(ImmutableList.toImmutableList());

        // Create the actual injector
        this.childInjector = this.injector.createChildInjector(this::bindProviders);

        // Cache dependencies for optimisations
        this.dependencies.stream()
                .map(Dependency::getKey)
                .forEach(key -> this.cache.put(key, this.childInjector.getProvider(key)));

        // Create an instance of the class if needed
        if (!this.staticMethod) this.object = this.injector.getInstance(this.method.getDeclaringClass());
    }

    // TODO find a way to provide the varargs to context

    // Type Consistency is ensured by the Provider implementations due to their generic behavior
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void bindProviders(Binder binder) {
        binder.bind(CommandSender.class).toProvider(() -> this.getLocalContext().getSender());
        binder.bind(String.class).annotatedWith(net.nowtryz.mcutils.command.annotations.Context.class).toProvider(() -> this.getLocalContext().getCommandLabel());
        binder.bind(String[].class).annotatedWith(net.nowtryz.mcutils.command.annotations.Context.class).toProvider(() -> this.getLocalContext().getArgs());
        binder.bind(Context.class).toProvider(this::getLocalContext);
        binder.bind(ExecutionContext.class).toProvider(this::getLocalContext);

        for (Class<? extends CommandSender> sender : this.target.getAcceptableSenders()) {
            // CommandSender is already bound, skipping it
            if (!CommandSender.class.equals(sender)) binder.bind(sender)
                    .toProvider((javax.inject.Provider) () -> sender.cast(this.getLocalContext().getSender()));
        }

        for (ArgProvider provider : this.argProviders) binder
                .bind(provider.getProvider().getProvidedClass())
                .annotatedWith(new ArgImpl(provider.getArg()))
                .toProvider((javax.inject.Provider) () -> provider
                        .getProvider().provide(getLocalContext().getArgs()[provider.index]));

        for (ContextProvider provider : this.contextProviders) {
            // we bind the provider with and without the Context annotation, so the annotation is optional
            binder.bind(provider.instance.getProvidedClass())
                    .toProvider(() -> provider.instance.provide(getLocalContext()));
            binder.bind(provider.instance.getProvidedClass())
                    .annotatedWith(net.nowtryz.mcutils.command.annotations.Context.class)
                    .toProvider(() -> provider.instance.provide(getLocalContext()));
        }

        for (GenericArg genericArg : this.genericArgs) {
            if (!genericArg.isVarArgs()) binder.bind(String.class)
                    .annotatedWith(new ArgImpl(genericArg.getArg()))
                    .toProvider(() -> this.getLocalContext().getArgs()[genericArg.getIndex()]);
            else binder.bind(String[].class)
                .annotatedWith(new ArgImpl(genericArg.getArg()))
                .toProvider(() -> {
                    String[] args = this.getLocalContext().getArgs();
                    return Arrays.copyOfRange(args, genericArg.getIndex(), args.length);
                });
        }
    }

    public ContextProvider<?> toContextProvider(ProvidesContext annotation) {
        net.nowtryz.mcutils.command.ContextProvider<?> provider = this.injector.getInstance(annotation.value());
        return new ContextProvider<>(annotation.ignoreNulls(), provider);
    }

    public ArgProvider<?> toArgProvider(ProvidesArg provides) {
        String arg = PROVIDER_ARG.matcher(provides.target()).replaceFirst("$1");
        int index = this.argLine.indexOf('<' + arg + '>');

        if (index == -1) throw new IllegalArgumentException(String.format(
                "Unknown <%s> argument from command `%s` at %s(%s.java:1)",
                arg, this.command.value(), this.methodID(), this.method.getDeclaringClass().getSimpleName())
        );

        net.nowtryz.mcutils.command.ArgProvider<?> provider = this.injector.getInstance(provides.provider());
        // -1 -> command label is not present in the arguments list
        return new ArgProvider<>(arg, index - 1, provides.ignoreNulls(), provider);
    }

    @Value
    static class ContextProvider<T> {
        boolean ignoreNulls;
        net.nowtryz.mcutils.command.ContextProvider<T> instance;
    }

    @Value
    static class ArgProvider<T> {
        String arg;
        int index;
        boolean ignoreNulls;
        net.nowtryz.mcutils.command.ArgProvider<T> provider;
    }

    // we put a provider for the given key, so we know types are corrects
    @SuppressWarnings("unchecked")
    private <T> T getCacheInstance(Injector injector, Key<T> key) {
        return Optional.ofNullable((Provider<T>) this.cache.get(key))
                .map(Provider::get)
                .orElseGet(() -> injector.getInstance(key));
    }

    private static Throwable unwrap(ReflectiveOperationException e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            if (cause instanceof ReflectiveOperationException) {
                return unwrap((ReflectiveOperationException) cause);
            } else return cause;
        } else return e;
    }

    public String methodID() {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    @Override
    public @NotNull CommandResult execute(ExecutionContext context) throws Throwable {
        this.localContext.set(context);

        // TODO use a "context cache" in order to call providers only once per execution

        // If a provided argument is null while null is ignored, call its provider's onNull
        for (ArgProvider<?> provider : this.nullIgnoredArgProviders) {
            String argument = getLocalContext().getArgs()[provider.index];
            if (provider.getProvider().provide(argument) == null) {
                return provider.getProvider().onNull(context, argument);
            }
        }

        for (ContextProvider<?> provider : this.nullIgnoredContextProviders) {
            if (provider.getInstance().provide(context) == null) {
                return provider.getInstance().onNull(context);
            }
        }

        Object[] args = this.injectionPoint.getDependencies()
                .stream()
                .map(Dependency::getKey)
                .map(key -> this.getCacheInstance(childInjector, key))
                .toArray(Object[]::new);

        try {
            return (CommandResult) method.invoke(this.object, args);
        } catch (ReflectiveOperationException e) {
            // unwrap to hide reflection and simply show exception thrown by the method
            throw unwrap(e);
        }
    }

    @Override
    public @NotNull List<String> getArguments() {
        return this.argLine;
    }

    @Override
    public @NotNull SenderType getType() {
        return this.target;
    }

    @Override
    public @NotNull String getCommand() {
        return this.command.value();
    }

    @Override
    public boolean isAsync() {
        return this.command.async();
    }

    @Override
    public String getDescription() {
        return this.command.description();
    }

    @Override
    public String getPermission() {
        return this.command.permission();
    }

    @Override
    public String getUsage() {
        return this.command.usage();
    }

    @Override
    public String toString() {
        return "MethodExecutor[method=" + this.methodID() + ']';
    }
}
