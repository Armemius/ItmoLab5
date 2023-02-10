package com.armemius.lab5.commands.nodes;

import com.armemius.lab5.commands.exceptions.CommandBuildException;

/**
 * Node that represent data input from the user
 */
public class DataNode extends Node {
    /**
     * <b>DataNode</b> doesn't have <i>content</i>, because
     * nodes that represent data doesn't have fixed name
     */
    public DataNode() {
        super(null);
    }

    /**
     * @see Node#then(Node)
     */
    @Override
    public Node then(Node node) throws CommandBuildException {
        if (node instanceof CommandNode) {
            throw new CommandBuildException("Can't place command extension nodes after data nodes");
        }
        getChildren().add(node);
        return this;
    }
}
