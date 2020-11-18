package net.nowtryz.mcutils.command;

import net.nowtryz.mcutils.command.contexts.ExecutionContext;

@FunctionalInterface
public interface ResultHandler {
    ResultHandler FALL_BACK = (context, result) -> {};

    void handle(ExecutionContext context, CommandResult result);
}
