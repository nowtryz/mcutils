package net.nowtryz.mcutils.command.contexts;

import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import net.nowtryz.mcutils.command.SenderType;
import net.nowtryz.mcutils.command.execution.Executor;
import net.nowtryz.mcutils.command.graph.CommandNode;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ExecutionContext extends Context {
    @Default boolean isAsync = Bukkit.isPrimaryThread();
    @NotNull SenderType target;
    @Nullable CommandNode node;
    @Nullable Executor executor;
}
