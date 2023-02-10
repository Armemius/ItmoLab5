package com.armemius.lab5;

import com.armemius.lab5.commands.TreeCommandParser;
import com.armemius.lab5.commands.exceptions.CommandBuildException;
import com.armemius.lab5.commands.nodes.CommandNode;
import com.armemius.lab5.commands.nodes.DataNode;
import org.javatuples.Pair;

import java.util.ArrayList;

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
        var helpParameters = ConsoleManager.Actions.paramsChecker(new ArrayList<>(){{
            add(new Pair<>("h", "help"));
        }});
        var compareParameters = ConsoleManager.Actions.paramsChecker(
                new ArrayList<>(){{
                    add(new Pair<>("h", "help"));
                    add(new Pair<>("l", "lower"));
                    add(new Pair<>("g", "greater"));
                    add(new Pair<>("s", "students"));
                    add(new Pair<>("e", "expelled"));
                    add(new Pair<>("a", "avg-mark"));
                    add(new Pair<>("d", "admin"));
                }},
                new ArrayList<>(){{add("g"); add("l");}},
                new ArrayList<>(){{add("s"); add("e"); add("a"); add("d");}}
        );
        parser.add(
                new CommandNode("help")
                        .executes(ConsoleManager.Actions::showHelp)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("info")
                        .executes(ConsoleManager.Actions::showInfo)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("show")
                        .executes(ConsoleManager.Actions::showElements)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("insert")
                        .executes(ConsoleManager.Actions::insertElement)
                        .paramsHandler(ConsoleManager.Actions.paramsChecker(new ArrayList<>(){{
                            add(new Pair<>("h", "help"));
                            add(new Pair<>("r", "random"));
                        }}))
        ).add(
                new CommandNode("update")
                        .then(
                                new DataNode()
                                        .executes(ConsoleManager.Actions::updateElement)
                        )
                        .executes(ConsoleManager.Actions::updateElement)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("remove")
                        .then(
                                new DataNode()
                                        .executes(ConsoleManager.Actions::removeElement)
                        )
                        .executes(ConsoleManager.Actions::removeElement)
                        .paramsHandler(compareParameters)
        ).add(
                new CommandNode("clear")
                        .executes(ConsoleManager.Actions::clearCollection)
                        .paramsHandler(ConsoleManager.Actions.paramsChecker(new ArrayList<>(){{
                            add(new Pair<>("h", "help"));
                            add(new Pair<>("f", "force"));
                        }}))
        ).add(
                new CommandNode("save")
                        .executes(ConsoleManager.Actions::saveCollection)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("execute")
                        .then(
                                new DataNode()
                                        .executes(ConsoleManager.Actions::executeScript)
                        )
                        .executes(ConsoleManager.Actions::executeScript)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("exit")
                        .executes(ConsoleManager.Actions::exitProgram)
                        .paramsHandler(ConsoleManager.Actions.paramsChecker(new ArrayList<>(){{
                            add(new Pair<>("h", "help"));
                            add(new Pair<>("s", "save"));
                            add(new Pair<>("f", "force"));
                        }}))
        ).add(
                new CommandNode("replace")
                        .then(
                                new DataNode().then(
                                        new DataNode()
                                                .executes(ConsoleManager.Actions::replaceElement)
                                )
                        )
                        .executes(ConsoleManager.Actions::replaceElement)
                        .paramsHandler(compareParameters)
        ).add(
                new CommandNode("count")
                        .then(
                                new DataNode()
                                        .executes(ConsoleManager.Actions::countElements)
                                        .then(
                                                new DataNode()
                                                        .executes(ConsoleManager.Actions::countElements)
                                        )
                        )
                        .executes(ConsoleManager.Actions::countElements)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("filter")
                        .then(
                                new DataNode()
                                        .executes(ConsoleManager.Actions::filterElement)
                        )
                        .executes(ConsoleManager.Actions::filterElement)
                        .paramsHandler(ConsoleManager.Actions.paramsChecker(new ArrayList<>(){{
                            add(new Pair<>("h", "help"));
                            add(new Pair<>("r", "regex"));
                        }}))
        ).add(
                new CommandNode("getenv")
                        .executes(ConsoleManager.Actions::getEnv)
                        .paramsHandler(helpParameters)
        );
    }
}
