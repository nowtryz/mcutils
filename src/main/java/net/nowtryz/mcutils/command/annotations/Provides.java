package net.nowtryz.mcutils.command.annotations;

import net.nowtryz.mcutils.command.Provider;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Providers.class)
public @interface Provides {
    String target();
    Class<? extends Provider<?>> provider();
}
