package com.armemius.lab5.commands.params;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Param</b> annotation represents possible
 * parameter of the Task
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String letter();
    String name();
}
