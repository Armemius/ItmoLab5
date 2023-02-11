package com.armemius.lab5.tasks;

import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.tasks.Task;

public class HelpTask implements Task {
    /**
     * Action for <b>help</b> command
     * Doesn't receive arguments
     * @param context
     */
    @Override
    public void execute(CommandContext context) {
        OutputHandler output = context.outputHandler();
        if (context.params().contains("h")) {
            output.put("""
                        Syntax:
                        > help
                        This command shows the list of all commands
                        PARAMS:
                        -h / --help\t\tShow this menu
                        
                        Cute cat 4 U
                         /\\_/\\\s
                        ( o.o )
                         > ^ <\s
                        """);
            return;
        }
        // Commands for task
        output.put("""
                    List of all commands:
                    help -- Outputs the list of all the commands with some info (you can use -h or --help parameter to gain more info about specific command)
                    info -- Show information about collection
                    show -- Output all the collection's elements into console
                    insert -- Inserts new element with specified key
                    update <id> -- Updates an element with specified key
                    remove <value> -- Removes an element with specified value (-h or --help for more information)
                    clear -- Clears the collection
                    save -- Save collection to file
                    execute <filename> -- Executes script in specified filename
                    exit -- Stops execution of the program
                    replace <id> -- Replaces an element with specified id with a new value if condition met
                    count <value> [delta] -- Counts the number of elements where condition met
                    filter <value> -- Outputs the elements where specified value is substring of the 'name' field in collection's elements""");
        // Auxiliary commands
        output.put("getenv -- Outputs the value of 'LAB_5_PATH'");
    }
}
