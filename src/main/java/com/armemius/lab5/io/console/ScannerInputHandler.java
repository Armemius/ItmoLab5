package com.armemius.lab5.io.console;

import com.armemius.lab5.io.InputHandler;

import java.util.Scanner;

/**
 * ScannerInputHandler uses Scanner to
 * receive input from console
 */
public class ScannerInputHandler implements InputHandler {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * @see InputHandler#get()
     */
    @Override
    public String get() {
        return scanner.nextLine();
    }

    /**
     * @see InputHandler#hasNextLine()
     */
    @Override
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * <b>close</b> method is used to
     * close Scanner
     * @see InputHandler#close()
     */
    @Override
    public void close() {
        scanner.close();
    }
}
