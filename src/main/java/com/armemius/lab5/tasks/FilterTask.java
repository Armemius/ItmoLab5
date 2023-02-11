package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.Task;

public class FilterTask implements Task {
    /**
     * Action for <b>filter</b> command
     * Receives one argument
     * @param context
     */
    public void execute(CommandContext context) {
        OutputHandler outputHandler = context.outputHandler();
        InputHandler inputHandler = context.inputHandler();
        if (context.params().contains("h")) {
            outputHandler.put("""
                        Syntax:
                        > filter <pattern>
                        This command allows you to get all the groups where name contains specified substring
                        PARAMS:
                        -h / --help\tShow this menu
                        -r / --regex\tSubstring will be interpreted as regex pattern
                        """);
            return;
        }
        if (context.args().size() < 1)
            throw new CommandArgumentException("Argument wasn't provided");
        String substring = context.args().get(0);
        outputHandler.put("Filtering results" + (context.params().contains("r") ? "(regex)" : ""));
        if (context.params().contains("r")) {
            for (var it : CollectionManager.filterContentRegex(substring)) {
                outputHandler.put(it.toString());
            }
        } else {
            for (var it : CollectionManager.filterContent(substring)) {
                outputHandler.put(it.toString());
            }
        }
    }
}
