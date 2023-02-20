package com.armemius.lab5.commands.params;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Parametrized</b> annotation is applied to Tasks
 * whose behaviour depends on parameters
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Parametrized {
    Param[] params() default @Param(letter = "h", name = "help");
    Conflict[] incompatible() default {};
}
