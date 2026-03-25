package org.jasperdev.scaffold.annotations;

import org.jasperdev.scaffold.api.CommandBase;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method parameter to be treated as a command argument.
 * <p>
 * The {@link #value()} of this annotation identifies the argument name.
 * If this name matches a key in {@link CommandBase#choices()},
 * the corresponding provider will be used for tab-completion and validation.
 *
 * @see Sub
 * @see Command
 * @see CommandBase#choices()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Arg {
	@Nonnull String value();
	boolean optional() default false;
}
