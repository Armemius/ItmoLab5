package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.collection.exceptions.CollectionFileException;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.Task;

public class SaveTask implements Task {
    /**
     * Action for <b>save</b> command
     * Doesn't receive arguments
     * @param context
     */
    public void execute(CommandContext context) {
        OutputHandler outputHandler = context.outputHandler();
        if (context.params().contains("h")) {
            outputHandler.put("""
                        Syntax:
                        > save
                        Command responsive for saving collection
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
            return;
        }
        try {
            outputHandler.put("Saving the collection");
            CollectionManager.save();
            outputHandler.put("Done");
        } catch (CollectionFileException ex) {
            throw new CommandRuntimeException(ex.getMessage());
        }

    }
}
