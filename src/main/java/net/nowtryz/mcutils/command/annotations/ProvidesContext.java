package net.nowtryz.mcutils.command.annotations;

import net.nowtryz.mcutils.command.ArgProvider;
import net.nowtryz.mcutils.command.ContextProvider;
import net.nowtryz.mcutils.command.contexts.ExecutionContext;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ContextProviders.class)
public @interface ProvidesContext {
    Class<? extends ContextProvider<?>> value();


    /**
     * Avoid the command annotated to be run if the context value provided is null.
     *
     * If the provider returns a null value, its
     * {@link ContextProvider#onNull(ExecutionContext) onNull(context)} in order to run a generic logic
     * and provide a generic {@link net.nowtryz.mcutils.command.CommandResult result}
     *
     * @see ContextProvider#onNull(ExecutionContext)
     */
    boolean ignoreNulls() default false;
}
