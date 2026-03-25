package org.jasperdev.scaffold.annotations;

import org.jasperdev.scaffold.api.ScaffoldCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restricts access to a {@link Command} or {@link Sub} to players with the specified permission node.
 * <p>
 * Can be applied at the class level to gate the entire command, or at the method level
 * to gate individual subcommands.
 *
 * @see Command
 * @see Sub
 * @see ScaffoldCommand
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Permission {
	String value();
}
