package com.armemius.lab5.commands;

import com.armemius.lab5.collection.exceptions.CollectionRuntimeException;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.commands.exceptions.CommandBuildException;
import com.armemius.lab5.commands.exceptions.CommandNotFoundException;
import com.armemius.lab5.commands.exceptions.CommandRuntimeException;
import com.armemius.lab5.commands.nodes.CommandNode;
import com.armemius.lab5.commands.nodes.Node;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <b>TreeCommandParser</b> uses commands tree
 * to parse commands
 */
public class TreeCommandParser implements CommandParser {
    private final List<Node> root = new ArrayList<>();
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    /**
     * Initializes the root of commands' tree
     * @param inputHandler {@link InputHandler} for user input in commands
     * @param outputHandler {@link OutputHandler} for commands' output
     */
    public TreeCommandParser(InputHandler inputHandler, OutputHandler outputHandler) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    /**
     * Adds new node to the root of the tree
     * @param node Node to add
     * @return Returns pointer to the class itself for chaining
     * @throws CommandBuildException Throws an exception if there are troubles with tree construction
     */
    public TreeCommandParser add(Node node) throws CommandBuildException {
        if (!(node instanceof CommandNode)) {
            throw new CommandBuildException("Root nodes should be command nodes");
        }
        root.add(node);
        return this;
    }

    /**
     * @see CommandParser#parse(String)
     * @param raw String to parse
     */
    @Override
    public void parse(@NotNull String raw) {
        raw = raw.trim();
        // Auxiliary variables
        String[] command = raw.split("\\s+");
        int depth = 0;
        List<Node> tmp = root;
        boolean found;
        Node lastNode = null;
        // Command context variables
        List<String> args = new ArrayList<>();
        Set<String> params = new HashSet<>();

        try {
            if (raw.isEmpty()) {
                throw new CommandArgumentException("Empty command");
            }
            if (command[0].charAt(0) == '-') {
                throw new CommandArgumentException("Command can't start with parameter");
            }
            while (depth < command.length) {
                found = false;
                if (command[depth].charAt(0) == '-') {
                    if (command[depth].length() > 1) {
                        if (command[depth].charAt(1) == '-') {
                            var param = command[depth].substring(2);
                            if (params.contains(param))
                                throw new CommandArgumentException("Duplicate parameters met");
                            params.add(param);
                        } else {
                            for (String jt : command[depth].substring(1).split("")) {
                                if (params.contains(jt))
                                    throw new CommandArgumentException("Duplicate parameters met");
                                params.add(jt);
                            }
                        }
                        depth++;
                        continue;
                    } else {
                        throw new CommandArgumentException("Empty parameter provided");
                    }
                }
                for (Node it : tmp) {
                    if (it instanceof CommandNode) {
                        if (command[depth].equals(it.getContent())) {
                            lastNode = it;
                            found = true;
                            depth++;
                            tmp = it.getChildren();
                            break;
                        }
                    } else {
                        args.add(command[depth]);
                        lastNode = it;
                        found = true;
                        depth++;
                        tmp = it.getChildren();
                        break;
                    }
                }
                if (!found) {
                    throw new CommandNotFoundException(command[depth]);
                }
            }
            if (lastNode == null)
                throw new CommandNotFoundException("Empty command provided");
            if (!lastNode.run(new CommandContext(raw, args, params, this.inputHandler, this.outputHandler)))
                throw new CommandRuntimeException("Incomplete or incorrect command");
        }
        catch (CommandNotFoundException ex) {
            outputHandler.put("Unable to match commands tree with command '" + raw + "'");
        }
        catch (CommandRuntimeException | CollectionRuntimeException ex) {
            outputHandler.put("Error while processing the command: '" + ex.getMessage() + "'");
        }
    }
}
