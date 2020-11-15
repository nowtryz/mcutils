package net.nowtryz.mcutils.command;

import net.nowtryz.mcutils.command.contexts.ExecutionContext;
import net.nowtryz.mcutils.legacycommand.CommandResult;

@FunctionalInterface
public interface ResultHandler {
    ResultHandler FALL_BACK = (context, result) -> {};

    void handle(ExecutionContext context, CommandResult result);
}
