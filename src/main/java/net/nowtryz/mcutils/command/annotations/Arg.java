package net.nowtryz.mcutils.command.annotations;

import net.nowtryz.mcutils.command.ArgProvider;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>String-based {@linkplain Qualifier qualifier} to identify a generic argument.
 * The argument class can either be a {@link String} or any other class that will be provided to the executor by a
 * {@link ProvidesArg} annotation.
 *
 * <p>Example usage:
 *
 * <pre>
 *   public class TestCommand {
 *     // Where the provider provides the class Clazz
 *     &#064;Provides(target = "arg", provider = MyProvider.class)
 *     &#064;Command("command with &lt;arg&gt;")
 *     public void test(<b>@Arg("arg")</b> String arg, ... other arguments) {
 *         // actual command
 *     }
 *     ...
 *   }</pre>
 *
 * @see ArgProvider
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Arg {

    /** The name. */
    String value() default "";
}
