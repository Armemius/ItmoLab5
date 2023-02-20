package com.armemius.lab5.commands.nodes;


import com.armemius.lab5.commands.CommandContext;
import com.armemius.lab5.commands.exceptions.CommandArgumentException;
import com.armemius.lab5.commands.exceptions.CommandBuildException;
import com.armemius.lab5.commands.params.Parametrized;
import com.armemius.lab5.tasks.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <b>Node</b> class represents possible path
 * variations for commands
 */
public abstract class Node {
    private final String content;
    private final List<Node> children = new ArrayList<>();
    private Task task;

    /**
     * Constructor for <b>Node</b> requires <i>String</i>
     * to match it while parsing, this value represents
     * name of the command
     * @param content
     */
    public Node(String content) {
        this.content = content;
    }

    /**
     * This method returns children nodes of current node
     * @return List of children nodes
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Getter for content
     * @return <b>Node</b>'s name that represents command
     */
    public String getContent() {
        return content;
    }

    /**
     * This method is called when parser matched a node
     * @param context {@link CommandContext} contains necessary information for command to run
     * @return Returns True if command have an execution task, otherwise returns False
     */
    public boolean run(CommandContext context) {
        if (task == null) {
            return false;
        }
        var taskClass = task.getClass();
        var annotation = taskClass.getAnnotation(Parametrized.class);
        if (annotation != null) {
            var raw = context.params();
            Set<String> baked = new HashSet<>();
            for (var it : annotation.params()) {
                if (raw.contains(it.letter())) {
                    if (raw.contains(it.name())) {
                        throw new CommandArgumentException("Duplicate parameters met");
                    } else {
                        if (baked.contains(it.letter())) {
                            throw new CommandArgumentException("Duplicate parameters met");
                        }
                        baked.add(it.letter());
                        raw.remove(it.letter());
                    }
                } else if (raw.contains(it.name())) {
                    if (baked.contains(it.letter())) {
                        throw new CommandArgumentException("Duplicate parameters met");
                    }
                    baked.add(it.letter());
                    raw.remove(it.name());
                }
            }
            if (!raw.isEmpty()) {
                throw new CommandArgumentException("Unknown parameter options");
            }
            for (var it : annotation.incompatible()) {
                int counter = 0;
                for (var jt : it.value()) {
                    if (baked.contains(jt)) {
                        counter++;
                    }
                }
                if (counter > 1) {
                    throw new CommandArgumentException("Incompatible parameters met");
                }
            }
            raw.addAll(baked);
        } else if (!context.params().isEmpty()) {
            throw new CommandArgumentException("Command doesn't have parameters");
        }

        task.execute(context);
        return true;
    }

    /**
     * Adds new node to the current node
     * @param node Node to add
     * @return Returns pointer to the class itself for chaining
     * @throws CommandBuildException Throws an exception if there are troubles with tree construction
     */
    public Node then(Node node) throws CommandBuildException {
        if (node instanceof CommandNode) {
            children.add(0, node);
        } else {
            children.add(node);
        }
        return this;
    }

    /**
     * Sets an execution task for current node
     * @param task <b>Task</b> to execute
     * @return Pointer to the current node for chaining
     */
    public Node executes(Task task) {
        this.task = task;
        return this;
    }
}
