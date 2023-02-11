package com.armemius.lab5.tasks;

import com.armemius.lab5.ConsoleManager;
import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.collection.exceptions.CollectionFileException;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.InputTask;

public class ExitTask extends InputTask {
    /**
     * Action for <b>exit</b> command
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
                        > exit
                        Command responsive for stopping the program
                        PARAMS:
                        -h / --help\tShow this menu
                        -s / --save\tSaves the collection before exit (won't continue if save was unsuccessful and --force is not set)
                        -f / --force\tSkips all the confirmation steps
                        """);
            return;
        }
        if (context.params().contains("s")) {
            try {
                outputHandler.put("Saving the collection");
                CollectionManager.save();
                outputHandler.put("Done");
            }
            catch (CommandRuntimeException ex) {
                if (!context.params().contains("f")) {
                    outputHandler.put("Error while saving the collection");
                    outputHandler.put("Operation aborted");
                    return;
                }
            }
            catch (CollectionFileException ex) {
                throw new CommandRuntimeException(ex.getMessage());
            }
        }
        if (!context.params().contains("f") && !context.params().contains("s")) {
            outputHandler.hold("Are you sure, all unsaved data will be lost? (Empty input if yes) ");
            String response = inputHandler.get();
            if (!response.isBlank()) {
                outputHandler.put("Operation aborted");
                return;
            }
        }
        outputHandler.put("Stopping");
        ConsoleManager.stop();
    }
}
