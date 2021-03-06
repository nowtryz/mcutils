package net.nowtryz.mcutils.command.execution;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import lombok.NonNull;
import net.nowtryz.mcutils.command.annotations.Completer;
import net.nowtryz.mcutils.command.contexts.CompletionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.regex.Pattern;

public class MethodCompleter implements net.nowtryz.mcutils.command.execution.Completer {
    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);

    private final @NonNull Method method;
    private final @NonNull Completer completer;
    private final boolean staticMethod;
    private @Nullable Object object;

    public interface Factory {
        MethodCompleter create(Method method);
    }

    @Inject
    private MethodCompleter(@Assisted Method method) {
        this.method = method;
        this.object = null;
        this.completer = method.getAnnotation(Completer.class);
        this.staticMethod = (method.getModifiers() & Modifier.STATIC) != 0;

        if (PATTERN_ON_SPACE.split(this.getCommand()).length < 2) {
            throw new IllegalArgumentException("Cannot register a command completer that has less than an argument: " +
                    this.getCommand());
        }
    }

    @Inject
    void init(Provider<Injector> provider) {
        // We use a provider, so AssistedInject doesn't complain about the fact we use the injector
        if (!this.staticMethod) this.object = provider.get().getInstance(this.method.getDeclaringClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> complete(CompletionContext context) {
        try {
            return (List<String>) method.invoke(this.object, context);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public @NotNull String getCommand() {
        return this.completer.value();
    }

    @Override
    public String toString() {
        return "MethodCompleter[method=" +  method.getDeclaringClass().getName() + "." + method.getName() + ']';
    }
}
