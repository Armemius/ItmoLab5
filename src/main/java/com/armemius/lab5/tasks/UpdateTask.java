package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.collection.data.StudyGroup;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.RequestTask;

public class UpdateTask extends RequestTask {
    /**
     * Action for <b>update</b> command
     * Receives one argument
     * @param context
     */
    public void execute(CommandContext context) {
        OutputHandler outputHandler = context.outputHandler();
        InputHandler inputHandler = context.inputHandler();
        if (context.params().contains("h")) {
            outputHandler.put("""
                        Syntax:
                        > update <id>
                        Command responsive for updating collection
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
            return;
        }
        try {
            int id = Integer.parseInt(context.args().get(0));
            if (!CollectionManager.checkId(id))
                throw new CommandRuntimeException("Collection manager doesn't have element with such id");
            outputHandler.put("Updating element with id " + id);
            StudyGroup group = null;
            while (group == null) {
                group = requestGroup(inputHandler, outputHandler);
                group.setId(id);
                outputHandler.put("You want to update group with id " + id + " with the following group: " + group);
                outputHandler.hold("Proceed? (Input empty line if yes) ");
                String response = inputHandler.get();
                if (!response.isBlank())
                    group = null;
            }
            CollectionManager.update(id, group);
        }
        catch (NumberFormatException ex) {
            throw new CommandRuntimeException("Incorrect value for argument");
        }
    }
}
