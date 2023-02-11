package com.armemius.lab5.tasks;

import com.armemius.lab5.commands.CommandContext;

/**
 * Represents the execution task for command
 */
@FunctionalInterface
public interface Task {
    /**
     * Method that executes the command
     * @param context {@link CommandContext} for command
     */
    void execute(CommandContext context);
}
