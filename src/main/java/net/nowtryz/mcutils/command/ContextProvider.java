package net.nowtryz.mcutils.command;

import net.nowtryz.mcutils.command.contexts.ExecutionContext;

public interface ContextProvider<T> {
    Class<T> getProvidedClass();
    SenderType getTarget();
    T provide(ExecutionContext context);

    default CommandResult onNull(ExecutionContext context) {
        return CommandResult.NOT_IMPLEMENTED;
    }
}
