package com.armemius.lab5.commands.exceptions;

public class CommandRuntimeException extends RuntimeException {
    public CommandRuntimeException(String message) {
        super(message);
    }
}
