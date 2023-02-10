package com.armemius.lab5;

import com.armemius.lab5.collection.CollectionManager;
import com.armemius.lab5.commands.CommandParser;
import com.armemius.lab5.commands.TreeCommandParser;
import com.armemius.lab5.commands.exceptions.CommandBuildException;
import com.armemius.lab5.commands.nodes.CommandNode;
import com.armemius.lab5.commands.nodes.DataNode;
import com.armemius.lab5.io.console.ConsoleOutputHandler;
import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;
import com.armemius.lab5.io.console.ScannerInputHandler;
import org.javatuples.Pair;

import java.util.ArrayList;

/**
 * Main class for lab 5
 * Loads collection into collection manager, generates
 * commands tree and then starts console manager
 * @see TreeCommandParser
 * @see ConsoleManager#start(InputHandler, OutputHandler, CommandParser)
 * @see CollectionManager#load()
 * @author Stepanov Arseniy P3109
 */
public class Lab5 {
    public static void main(String[] args) {
        InputHandler inputHandler = new ScannerInputHandler();
        OutputHandler outputHandler = new ConsoleOutputHandler();
        CollectionManager.load();
        try {
            TreeCommandParser parser = new TreeCommandParser(inputHandler, outputHandler);
            ParserRegistry.buildTree(parser);
            ConsoleManager.start(inputHandler, outputHandler, parser);
        }
        catch (CommandBuildException e) {
            throw new RuntimeException(e);
        }
        finally {
            inputHandler.close();
        }
    }
}
