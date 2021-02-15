package net.nowtryz.mcutils.command.exceptions;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.nowtryz.mcutils.command.execution.Executor;

@Getter
@RequiredArgsConstructor
public class ExecutorDuplicationException extends RuntimeException {
    private static final long serialVersionUID = -7887942900292971696L;
    private final @NonNull Executor present;
    private final @NonNull Executor duplicate;

    @Override
    public String getMessage() {
        return String.format(
                "%s is duplicated, got two executors:\n\t- %s\n\t- %s",
                this.present.getCommand(),
                this.present,
                this.duplicate
        );
    }
}
