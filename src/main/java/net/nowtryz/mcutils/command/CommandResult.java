package net.nowtryz.mcutils.command;

public enum CommandResult {
    /**
     * The error succeeded without error
     */
    SUCCESS(true, true),
    /**
     * An error occurred but an informative message has been send to the command sender
     */
    FAILED(true, false),
    /**
     * Unknown command, caused by invalid input
     */
    UNKNOWN(false, false),

    /**
     * The command failed because the sender is not a player
     */
    NOT_A_PLAYER(true, false),
    /**
     * The command failed because the sender is not the console
     */
    NOT_A_CONSOLE(true, false),

    /**
     * An unexpected exception occurred and has been logged
     */
    INTERNAL_ERROR(true, false),
    /**
     * The arguments sent to the command are invalid so, a usage message should be printed
     */
    INVALID_ARGUMENTS(false, false),
    /**
     * The sender hasn't the required permissions to execute the command
     */
    MISSING_PERMISSION(true, false),
    /**
     * This command is not implemented for the moment
     */
    NOT_IMPLEMENTED(true, false);

    /**
     * Weather Bukkit should consider this command to be correct
     */
    boolean valid;
    /**
     * Does this command succeeded
     */
    boolean success;

    CommandResult(boolean valid, boolean success) {
        this.valid = valid;
        this.success = success;
    }

    /**
     * The boolean that should be returned to bukkit
     * @return the result
     */
    public boolean isValid() {
        return valid;
    }

    public boolean succeeded() {
        return success;
    }

    public boolean failed() {
        return !success;
    }
}
