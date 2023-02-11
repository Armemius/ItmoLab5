package com.armemius.lab5.tasks;

import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.Task;

public class GetEnvTask implements Task {
    /**
     * Action for <b>getenv</b> command
     * Doesn't receive arguments
     * @param context
     */
    public void execute (CommandContext context) {
        OutputHandler outputHandler = context.outputHandler();
        InputHandler inputHandler = context.inputHandler();
        if (context.params().contains("h")) {
            outputHandler.put("""
                        Syntax:
                        > getenv
                        This command shows the value of the environment variable 'LAB_5_PATH'
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
            return;
        }
        String path = System.getenv("LAB_5_PATH");
        if (path == null)
            context.outputHandler().put("Environment variable 'LAB_5_PATH' is not set");
        else
            context.outputHandler().put("Environment variable 'LAB_5_PATH' value: '" + path + "'");
    }
}
