package com.armemius.lab5.io;

/**
 * <b>InputHandler</b> handles input in
 * the program in different approaches
 */
public interface InputHandler {

    /**
     * <b>get</b> function is used to receive
     * a line of input from user or any other source
     */
    String get();

    /**
     * <b>hasNext</b> function is used to receive
     * a line of input from user or any other source
     */
    boolean hasNextLine();

    /**
     * <b>close</b> function is used to close
     * InputHandler if needed (usually for networking
     * or file streams)
     */
    void close();
}
