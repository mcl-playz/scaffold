package org.jasperdev.mcommandframework.annotations;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a class as a top-level command within the MCommandFramework.
 * <p>
 * The annotated class must implement {@link org.jasperdev.mcommandframework.api.MCommand}.
 * The {@link #value()} specifies the command name, and methods within the class
 * can be annotated with {@link Sub} to define subcommands. Command arguments are
 * declared using {@link Arg} on method parameters, with optional tab-completion
 * provided via {@link org.jasperdev.mcommandframework.api.MCommand#choices()}.
 *
 * @see Sub
 * @see Arg
 * @see org.jasperdev.mcommandframework.api.MCommand
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    @Nonnull String value();
    @Nonnull String description() default "";
}
