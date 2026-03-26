package org.jasperdev.scaffold.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Enforces a cooldown between executions of a {@link Sub} command for each sender.
 * <p>
 * When applied to a method, the framework tracks the last execution time per sender
 * and prevents execution until the specified duration has elapsed.
 * <p>
 * Defaults to {@link TimeUnit#SECONDS} for the time unit if not specified.
 *
 * @see Root
 * @see Sub
 * @see TimeUnit
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cooldown {
	long value();
	TimeUnit unit() default TimeUnit.SECONDS;
}
