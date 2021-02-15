package net.nowtryz.mcutils.command.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArgProviders {
    ProvidesArg[] value();
}
