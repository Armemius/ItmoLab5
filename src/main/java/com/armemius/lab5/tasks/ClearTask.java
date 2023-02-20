package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.params.Param;
import com.armemius.lab5.commands.params.Parametrized;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.InputTask;

@Parametrized(
        params = {
                @Param(letter = "h", name = "help"),
                @Param(letter = "f", name = "force")
        }
)
public class ClearTask extends InputTask {
    /**
     * Action for <b>clear</b> command
     * Doesn't receive arguments
     * @param context
     */
    @Override
    public void execute(CommandContext context) {
        OutputHandler outputHandler = context.outputHandler();
        InputHandler inputHandler = context.inputHandler();
        if (context.params().contains("h")) {
            outputHandler.put("""
                        Syntax:
                        > clear
                        Command responsive for clearing the collection
                        PARAMS:
                        -h / --help\tShow this menu
                        -f / --force\tSkip all confirmation steps
                        """);
            return;
        }
        if (!context.params().contains("f")) {
            outputHandler.hold("Are you sure, all unsaved data will be lost? (Empty input if yes) ");
            String response = inputHandler.get();
            if (!response.isBlank()) {
                outputHandler.put("Operation aborted");
                return;
            }
        }
        outputHandler.put("Clearing the collection");
        CollectionManager.clear();
    }
}
