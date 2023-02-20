package com.armemius.lab5.tasks;

import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.commands.params.Parametrized;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Parametrized
public class ExecuteTask implements Task {
    /**
     * Action for <b>execute</b> command
     * Receives one argument
     * @param context
     */
    @Override
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
        String test = checkRecursion(scriptPath, new HashSet<>());
        if (test != null) {
            throw new CommandArgumentException("Recursion detected: " + test);
        }
        try (Scanner scanner = new Scanner(new File(scriptPath))) {
            while (scanner.hasNextLine()) {
                context.parser().parse(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new CommandArgumentException("Error while reading the file");
        }
    }

    private String checkRecursion(String path, Set<String> used) {
        StringBuilder script = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                script.append(scanner.nextLine()).append('\n');
            }
            Matcher matcher = Pattern.compile("execute\\s+(.+)\n", Pattern.MULTILINE).matcher(script);
            while (matcher.find()) {
                String match = matcher.group(1);
                if (used.contains(path)) {
                    return path;
                }
                used.add(path);
                System.out.println(match);
                String recursion = checkRecursion(match, new HashSet<>(used));
                if (recursion == null)
                    return null;
                return path + " -> " + recursion;
            }
        } catch (FileNotFoundException e) {
            throw new CommandArgumentException("Error while reading the file");
        }
        return null;
    }
}
