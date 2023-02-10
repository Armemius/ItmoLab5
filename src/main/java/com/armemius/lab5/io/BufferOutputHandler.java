package com.armemius.lab5.io;

/**
 * BufferOutputHandler uses StringBuffer for
 * output, then you can get the output data
 * from the buffer
 */
public class BufferOutputHandler implements OutputHandler {
    StringBuffer buffer;

    /**
     * <b>BufferOutputHandler</b> needs only one value for constructor:
     * @param buffer Link to buffer, where you need to store output
     */
    public BufferOutputHandler(StringBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * @see OutputHandler#put(String)
     * @param line String to output
     */
    @Override
    public void put(String line) {
        buffer.append(line).append("\n");
    }

    /**
     * @see OutputHandler#hold(String)
     * @param line String to output
     */
    @Override
    public void hold(String line) {
        buffer.append(line);
    }
}
