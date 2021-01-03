package net.nowtryz.mcutils.command;

import net.nowtryz.mcutils.command.contexts.ExecutionContext;

public interface ArgProvider<T> {
    Class<T> getProvidedClass();
    T provide(String argument);

    default CommandResult onNull(ExecutionContext context, String argument) {
        return CommandResult.NOT_IMPLEMENTED;
    }

    // Able to complete a generic argument ?
}
