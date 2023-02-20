package com.armemius.lab5;

import com.armemius.lab5.commands.TreeCommandParser;
import com.armemius.lab5.commands.exceptions.CommandBuildException;
import com.armemius.lab5.commands.nodes.CommandNode;
import com.armemius.lab5.commands.nodes.DataNode;
import com.armemius.lab5.tasks.*;

/**
 * <b>ParserRegistry</b> class stands for initialization
 * of parsers that needs that
 */
public class ParserRegistry {
    /**
     * Method that initializes {@link TreeCommandParser} with default commands
     * @param parser Link to {@link TreeCommandParser}, which needs to be initialized
     * @throws CommandBuildException Throws an exception if there are troubles with tree structure
     */
    public static void buildTree(TreeCommandParser parser) throws CommandBuildException {
        var getEnvTask = new GetEnvTask();
        var clearTask = new ClearTask();
        var countTask = new CountTask();
        var executeTask = new ExecuteTask();
        var exitTask = new ExitTask();
        var filterTask = new FilterTask();
        var helpTask = new HelpTask();
        var infoTask = new InfoTask();
        var insertTask = new InsertTask();
        var removeTask = new RemoveTask();
        var replaceTask = new ReplaceTask();
        var saveTask = new SaveTask();
        var showTask = new ShowTask();
        var updateTask = new UpdateTask();
        var fillTask = new FillTask();
        parser.add(
                new CommandNode("help")
                        .executes(helpTask)
        ).add(
                new CommandNode("info")
                        .executes(infoTask)
        ).add(
                new CommandNode("show")
                        .executes(showTask)
        ).add(
                new CommandNode("insert")
                        .executes(insertTask)
        ).add(
                new CommandNode("update")
                        .then(
                                new DataNode()
                                        .executes(updateTask)
                        )
                        .executes(updateTask)
        ).add(
                new CommandNode("remove")
                        .then(
                                new DataNode()
                                        .executes(removeTask)
                        )
                        .executes(removeTask)
        ).add(
                new CommandNode("clear")
                        .executes(clearTask)
        ).add(
                new CommandNode("save")
                        .executes(saveTask)
        ).add(
                new CommandNode("execute")
                        .then(
                                new DataNode()
                                        .executes(executeTask)
                        )
                        .executes(executeTask)
        ).add(
                new CommandNode("exit")
                        .executes(exitTask)
        ).add(
                new CommandNode("replace")
                        .then(
                                new DataNode().executes(replaceTask)
                        )
                        .executes(replaceTask)
        ).add(
                new CommandNode("count")
                        .then(
                                new DataNode()
                                        .executes(countTask)
                                        .then(
                                                new DataNode()
                                                        .executes(countTask)
                                        )
                        )
                        .executes(countTask)
        ).add(
                new CommandNode("filter")
                        .then(
                                new DataNode()
                                        .executes(filterTask)
                        )
                        .executes(filterTask)
        ).add(
                new CommandNode("getenv")
                        .executes(getEnvTask)
        ).add(
                new CommandNode("fill")
                        .then(
                                new DataNode().executes(fillTask)
                        )
                        .executes(fillTask)
        );
    }
}
