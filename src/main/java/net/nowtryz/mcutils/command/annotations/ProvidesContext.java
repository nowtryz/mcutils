package net.nowtryz.mcutils.command.annotations;

import net.nowtryz.mcutils.command.ContextProvider;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ContextProviders.class)
public @interface ProvidesContext {
    Class<? extends ContextProvider<?>> value();
}
