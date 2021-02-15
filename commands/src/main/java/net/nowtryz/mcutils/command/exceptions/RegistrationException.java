package net.nowtryz.mcutils.command.exceptions;

import net.nowtryz.mcutils.command.execution.Execution;

public class RegistrationException extends RuntimeException {
    private static final long serialVersionUID = 2452115394669475078L;

    public RegistrationException(Execution execution, Throwable cause) {
        super("Got an exception while registering " + execution.getCommand(), cause);
    }
}
