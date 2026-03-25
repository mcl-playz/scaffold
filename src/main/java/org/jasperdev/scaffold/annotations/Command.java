package org.jasperdev.scaffold.annotations;

import org.jasperdev.scaffold.api.ScaffoldCommand;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a class as a top-level command within the MCommandFramework.
 * <p>
 * The annotated class must implement {@link ScaffoldCommand}.
 * The {@link #value()} specifies the command name, and methods within the class
 * can be annotated with {@link Sub} to define subcommands. Command arguments are
 * declared using {@link Arg} on method parameters, with optional tab-completion
 * provided via {@link ScaffoldCommand#choices()}.
 * <p>
 * The root executor can be defined with a function annotated by {@link Root}
 *
 * @see Root
 * @see Sub
 * @see Arg
 * @see Permission
 * @see ExecutableBy
 * @see ScaffoldCommand
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
	@Nonnull String value();
	@Nonnull String description() default "";
}
