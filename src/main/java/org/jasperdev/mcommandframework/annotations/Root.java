package org.jasperdev.mcommandframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as the root executor of a command.
 * <p>
 * The annotated method will be invoked when the base command is executed
 * with no subcommand arguments. It follows the same rules as {@link Sub}
 * regarding parameter binding, {@link Permission}, and {@link ExecutableBy}.
 * <p>
 * Only one method per {@link Command} class should be annotated with {@code @Root}.
 * If multiple methods are annotated, behaviour is undefined as the last one
 * processed will overwrite any previously registered root executor.
 *
 * @see Sub
 * @see Command
 * @see Permission
 * @see ExecutableBy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Root {}
