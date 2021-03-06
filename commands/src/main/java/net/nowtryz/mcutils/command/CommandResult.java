package net.nowtryz.mcutils.command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
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
    @Deprecated
    NOT_A_PLAYER(true, false),
    /**
     * The command failed because the sender is not the console
     * @deprecated use {@link CommandResult#WRONG_TARGET} instead
     */
    @Deprecated
    NOT_A_CONSOLE(true, false),
    /**
     * The command failed because the sender was not in the specified targeted senders list.
     * This is a sort of super-type of {@link CommandResult#NOT_A_CONSOLE} and {@link CommandResult#NOT_A_PLAYER} which
     * are deprecated. You can catch those three to handle any wrong target call.
     */
    WRONG_TARGET(true, false),

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
