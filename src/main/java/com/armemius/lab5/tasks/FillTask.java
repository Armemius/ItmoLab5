package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;

public class FillTask extends InsertTask {
    /**
     * Action for <b>fill</b> command
     * Doesn't receive arguments
     * @param context
     */
    @Override
    public void execute(CommandContext context) {
        OutputHandler outputHandler = context.outputHandler();
        if (context.params().contains("h")) {
            outputHandler.put("""
                        Syntax:
                        > fill <amount>
                        Inserts specified amount of random groups
                        PARAMS:
                        -h / --help\t\tShow this menu
                        """);
            return;
        }
        if (context.args().size() < 1)
            throw new CommandArgumentException("Argument wasn't provided");
        try {
            int id = Integer.parseInt(context.args().get(0));
            if (id < 1)
                throw new CommandRuntimeException("Incorrect value type provided");
            for (int it = 0; it < id; ++it) {
                CollectionManager.add(super.genRandomGroup());
            }
            outputHandler.put("Inserted " + id + " element(s)");
        }
        catch (NumberFormatException ex) {
            throw new CommandRuntimeException("Incorrect value type provided");
        }
    }
}
