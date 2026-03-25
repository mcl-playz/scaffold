package org.jasperdev.scaffold.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restricts who can execute a {@link Command} or {@link Sub}.
 * <p>
 * When applied at the class level, it gates the entire command.
 * When applied at the method level, it gates that specific subcommand
 * and overrides any class-level restriction.
 * <p>
 * Defaults to {@link SenderType#ALL} if not present.
 *
 * @see Command
 * @see Sub
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ExecutableBy {
	SenderType value();

	enum SenderType {
		PLAYER,
		CONSOLE,
		ALL
	}
}
