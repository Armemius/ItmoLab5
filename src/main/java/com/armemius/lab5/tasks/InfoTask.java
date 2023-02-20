package com.armemius.lab5.tasks;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.params.Param;
import com.armemius.lab5.commands.params.Parametrized;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.Task;

import java.time.format.DateTimeFormatter;

@Parametrized
public class InfoTask implements Task {
    /**
     * Action for <b>info</b> command
     * Doesn't receive arguments
     * @param context
     */
    @Override
    public void execute(CommandContext context) {
        OutputHandler output = context.outputHandler();
        if (context.params().contains("h")) {
            output.put("""
                        Syntax:
                        > info
                        This command outputs info about collection
                        PARAMS:
                        -h / --help\t\tShow this menu
                        """);
            return;
        }
        output.put("Collection statistics:");
        output.put("Init time:\t" + CollectionManager.getCreationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));
        output.put("Elements:\t" + CollectionManager.getElementsCount());
        output.put("Type:\t\t" + CollectionManager.getCollectionType());
    }
}
