package com.armemius.lab5.io.console;

import com.armemius.lab5.io.OutputHandler;

/**
 * ConsoleOutputHandler uses System.out to
 * output data
 */
public class ConsoleOutputHandler implements OutputHandler {
    /**
     * @see OutputHandler#put(String)
     * @param line String to output
     */
    @Override
    public void put(String line) {
        System.out.println(line);
    }

    /**
     * @see OutputHandler#hold(String)
     * @param line String to output
     */
    @Override
    public void hold(String line) {
        System.out.print(line);
    }
}
