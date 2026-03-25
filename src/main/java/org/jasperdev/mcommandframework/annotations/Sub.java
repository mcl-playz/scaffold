package org.jasperdev.mcommandframework.annotations;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method as a subcommand within a {@link Command}-annotated class.
 * <p>
 * The {@link #value()} specifies the subcommand name. Method parameters annotated
 * with {@link Arg} define the subcommand's arguments, which can be linked to
 * {@link org.jasperdev.mcommandframework.api.MCommand#choices()} for tab-completion.
 *
 * @see Command
 * @see Arg
 * @see Permission
 * @see ExecutableBy
 * @see org.jasperdev.mcommandframework.api.MCommand
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sub {
	@Nonnull String value();
	@Nonnull String description() default "";
}
