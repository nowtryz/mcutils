package net.nowtryz.mcutils.command.execution;

import net.nowtryz.mcutils.command.SenderType;
import net.nowtryz.mcutils.command.contexts.ExecutionContext;
import net.nowtryz.mcutils.command.CommandResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface Executor extends Execution {
    @NotNull
    CommandResult execute(ExecutionContext context) throws Throwable;

    @NotNull
    SenderType getType();

    @Nullable
    String getDescription();

    @Nullable
    String getPermission();

    @Nullable
    String getUsage();

    default boolean isAsync() {
        return false;
    }

    static SimpleExecutor.SimpleExecutorBuilder create(String command) {
        return SimpleExecutor.builder().command(command);
    }
}
