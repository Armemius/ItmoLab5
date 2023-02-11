package com.armemius.lab5.tasks;

import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ExecuteTask implements Task {
    /**
     * Action for <b>execute</b> command
     * Receives one argument
     * @param context
     */
    public void execute(CommandContext context) {
        OutputHandler outputHandler = context.outputHandler();
        if (context.params().contains("h")) {
            outputHandler.put("""
                        Syntax:
                        > execute <filename>
                        Command responsive for executing scripts from files
                        PARAMS:
                        -h / --help\tShow this menu
                        """);
            return;
        }
        if (context.args().size() < 1)
            throw new CommandArgumentException("Argument wasn't provided");
        outputHandler.put("Executing script");
        String scriptPath = context.args().get(0);

        try (Scanner scanner = new Scanner(new File(scriptPath))) {
            while (scanner.hasNextLine()) {
                context.parser().parse(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error while reading the file");
        }
    }
}
