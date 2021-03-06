package net.nowtryz.mcutils.command.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface ContextProviders {
    ProvidesContext[] value();
}
