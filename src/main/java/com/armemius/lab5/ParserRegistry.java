package com.armemius.lab5;

import com.armemius.lab5.commands.TreeCommandParser;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.commands.exceptions.CommandBuildException;
import com.armemius.lab5.commands.nodes.CommandNode;
import com.armemius.lab5.commands.nodes.DataNode;
import com.armemius.lab5.tasks.*;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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
        var helpParameters = paramsChecker(new ArrayList<>(){{
            add(new Pair<>("h", "help"));
        }});
        var compareParameters = paramsChecker(
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
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("info")
                        .executes(infoTask)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("show")
                        .executes(showTask)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("insert")
                        .executes(insertTask)
                        .paramsHandler(paramsChecker(new ArrayList<>(){{
                            add(new Pair<>("h", "help"));
                            add(new Pair<>("r", "random"));
                        }}))
        ).add(
                new CommandNode("update")
                        .then(
                                new DataNode()
                                        .executes(updateTask)
                        )
                        .executes(updateTask)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("remove")
                        .then(
                                new DataNode()
                                        .executes(removeTask)
                        )
                        .executes(removeTask)
                        .paramsHandler(compareParameters)
        ).add(
                new CommandNode("clear")
                        .executes(clearTask)
                        .paramsHandler(paramsChecker(new ArrayList<>(){{
                            add(new Pair<>("h", "help"));
                            add(new Pair<>("f", "force"));
                        }}))
        ).add(
                new CommandNode("save")
                        .executes(saveTask)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("execute")
                        .then(
                                new DataNode()
                                        .executes(executeTask)
                        )
                        .executes(executeTask)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("exit")
                        .executes(exitTask)
                        .paramsHandler(paramsChecker(new ArrayList<>(){{
                            add(new Pair<>("h", "help"));
                            add(new Pair<>("s", "save"));
                            add(new Pair<>("f", "force"));
                        }}))
        ).add(
                new CommandNode("replace")
                        .then(
                                new DataNode().executes(replaceTask)
                        )
                        .executes(replaceTask)
                        .paramsHandler(compareParameters)
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
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("filter")
                        .then(
                                new DataNode()
                                        .executes(filterTask)
                        )
                        .executes(filterTask)
                        .paramsHandler(paramsChecker(new ArrayList<>(){{
                            add(new Pair<>("h", "help"));
                            add(new Pair<>("r", "regex"));
                        }}))
        ).add(
                new CommandNode("getenv")
                        .executes(getEnvTask)
                        .paramsHandler(helpParameters)
        ).add(
                new CommandNode("fill")
                        .then(
                                new DataNode().executes(fillTask)
                        )
                        .executes(fillTask)
                        .paramsHandler(helpParameters)
        );
    }

    /**
     * This method is used to generate <i>Consumer</i> to
     * check parameters of specified command
     * @param params All possible parameters for the command
     * @param conflicts Parameters that conflict with each other
     * @return <i>Consumer</i> that checks parameter
     */
    @SafeVarargs
    private static Consumer<Set<String>> paramsChecker(ArrayList<Pair<String, String>> params, ArrayList<String>... conflicts) {
        return (raw) -> {
            Set<String> baked = new HashSet<>();
            for (var it : params) {
                if (raw.contains(it.getValue0())) {
                    if (raw.contains(it.getValue1())) {
                        throw new CommandArgumentException("Duplicate parameters met");
                    } else {
                        if (baked.contains(it.getValue0())) {
                            throw new CommandArgumentException("Duplicate parameters met");
                        }
                        baked.add(it.getValue0());
                        raw.remove(it.getValue0());
                    }
                } else if (raw.contains(it.getValue1())) {
                    if (baked.contains(it.getValue0())) {
                        throw new CommandArgumentException("Duplicate parameters met");
                    }
                    baked.add(it.getValue0());
                    raw.remove(it.getValue1());
                }
            }
            if (!raw.isEmpty()) {
                throw new CommandArgumentException("Unknown parameter options");
            }
            for (var it : conflicts) {
                int counter = 0;
                for (var jt : it) {
                    if (baked.contains(jt)) {
                        counter++;
                    }
                }
                if (counter > 1) {
                    throw new CommandArgumentException("Incompatible parameters met");
                }
            }
            raw.addAll(baked);
        };
    }
}
