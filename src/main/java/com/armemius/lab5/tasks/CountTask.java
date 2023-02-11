package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;

public class CountTask implements Task {
    /**
     * Action for <b>count</b> command
     * Receives one or two arguments
     * @param context
     */
    public void execute(CommandContext context) {
        OutputHandler outputHandler = context.outputHandler();
        InputHandler inputHandler = context.inputHandler();
        if (context.params().contains("h")) {
            outputHandler.put("""
                        Syntax:
                        > count <mark> [delta]
                        This command allows you to count the number of the groups with specified average mark (you can specify delta)
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
            return;
        }
        if (context.args().size() < 1)
            throw new CommandArgumentException("Argument wasn't provided");
        try {
            double mark = Double.parseDouble(context.args().get(0));
            if (context.args().size() > 1) {
                double delta = Double.parseDouble(context.args().get(1));
                outputHandler.put("Count: " + CollectionManager.countAvgMarkDelta(mark, delta));
            } else {
                outputHandler.put("Count: " + CollectionManager.countAvgMark(mark));
            }
        }
        catch (NumberFormatException ex) {
            throw new CommandRuntimeException("Incorrect value type provided");
        }
    }
}
