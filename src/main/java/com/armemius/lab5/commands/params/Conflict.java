package com.armemius.lab5.commands.params;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <b>Conflict</b> annotation is used to describe
 * incompatible set of parameters, only one parameter
 * from set can be used at a time
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Conflict {
    String[] value();
}
