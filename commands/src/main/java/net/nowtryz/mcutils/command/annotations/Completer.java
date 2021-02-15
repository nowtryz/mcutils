package net.nowtryz.mcutils.command.annotations;

import net.nowtryz.mcutils.command.SenderType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Completer {
    String value();
    SenderType type() default SenderType.PLAYER;
}
