package com.armemius.lab5.commands.exceptions;

public class CommandNotFoundException extends CommandRuntimeException {
    public CommandNotFoundException(String message) {
        super(message);
    }
}