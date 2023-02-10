package com.armemius.lab5.commands.exceptions;

public class CommandArgumentException extends CommandRuntimeException {
    public CommandArgumentException(String message) {
        super(message);
    }
}
