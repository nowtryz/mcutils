package net.nowtryz.mcutils.command.annotations;

import net.nowtryz.mcutils.command.ArgProvider;
import net.nowtryz.mcutils.command.contexts.ExecutionContext;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ArgProviders.class)
public @interface ProvidesArg {
    String target();
    Class<? extends ArgProvider<?>> provider();

    /**
     * Avoid the command annotated to be ran if the argument provided is null.
     *
     * If the provider returns a null value, its
     * {@link ArgProvider#onNull(ExecutionContext, String) onNull(context, argument)} in order to run a generic logic
     * and provide a generic {@link net.nowtryz.mcutils.command.CommandResult result}
     *
     * @see ArgProvider#onNull(ExecutionContext, String)
     */
    boolean ignoreNulls() default false;
}
