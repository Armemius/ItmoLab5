package com.armemius.lab5.commands;

/**
 * Command parser stands for parsing
 * raw input and executing the commands
 */
public interface CommandParser {
    /**
     * <b>parse</b> method is used to process
     * the input
     * @param raw String to parse
     */
    void parse(String raw);
}
