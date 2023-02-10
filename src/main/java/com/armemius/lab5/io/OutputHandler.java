package com.armemius.lab5.io;

/**
 * Class that handles output of
 * the program in different ways
 */
public interface OutputHandler {
    /**
     * <b>put</b> function outputs String
     * with a line break
     * @param line String to output
     */
    void put(String line);

    /**
     * <b>hold</b> function outputs String
     * without a line break
     * @param line String to output
     */
    void hold(String line);
}
