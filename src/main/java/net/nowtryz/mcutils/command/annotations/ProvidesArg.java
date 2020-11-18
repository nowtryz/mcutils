package net.nowtryz.mcutils.command.annotations;

import net.nowtryz.mcutils.command.ArgProvider;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ArgProviders.class)
public @interface ProvidesArg {
    String target();
    Class<? extends ArgProvider<?>> provider();
}
