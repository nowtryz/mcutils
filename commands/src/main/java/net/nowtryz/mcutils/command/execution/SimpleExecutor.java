package net.nowtryz.mcutils.command.execution;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import net.nowtryz.mcutils.command.CommandResult;
import net.nowtryz.mcutils.command.SenderType;
import net.nowtryz.mcutils.command.contexts.ExecutionContext;
import org.jetbrains.annotations.NotNull;

@Value
@Builder
@Getter(onMethod_={@Override})
public class SimpleExecutor implements Executor {
    @NonNull
    @Getter Handler onCommand;

    @NonNull @Builder.Default
    SenderType type = SenderType.ANY;

    @Builder.Default
    String description = "";

    @Builder.Default
    String permission = null;

    @Builder.Default
    String usage = "";

    @NonNull
    String command;


    @Override
    public @NotNull CommandResult execute(ExecutionContext context) throws Exception {
        return this.onCommand.execute(context);
    }

    public interface Handler {
        CommandResult execute(ExecutionContext context) throws Exception;
    }
}
