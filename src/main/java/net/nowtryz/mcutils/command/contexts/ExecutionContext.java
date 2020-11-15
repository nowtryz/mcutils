package net.nowtryz.mcutils.command.contexts;

import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import net.nowtryz.mcutils.command.SenderType;
import net.nowtryz.mcutils.command.execution.Executor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ExecutionContext extends Context {
    @Default
    boolean isAsync = Bukkit.isPrimaryThread();
    SenderType target;
    @Nullable
    Executor executor;
}
