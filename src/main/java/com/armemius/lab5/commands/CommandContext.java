package com.armemius.lab5.commands;

import com.armemius.lab5.io.InputHandler;
import com.armemius.lab5.io.OutputHandler;

import java.util.List;
import java.util.Set;

/**
 * This record is essential for transferring data
 * from parser to command execution, it contains
 * all the necessary information about command
 * @param rawCommand Raw input command
 * @param args Arguments for command
 * @param params Parameters for command
 * @param inputHandler {@link InputHandler} from {@link CommandParser}
 * @param outputHandler {@link OutputHandler} from {@link CommandParser}
 */
public record CommandContext(
        String rawCommand,
        List<String> args,
        Set<String> params,
        InputHandler inputHandler,
        OutputHandler outputHandler
) {}
