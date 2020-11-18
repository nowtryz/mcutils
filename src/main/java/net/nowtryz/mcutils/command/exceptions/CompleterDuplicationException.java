package net.nowtryz.mcutils.command.exceptions;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.nowtryz.mcutils.command.execution.Completer;

@Getter
@RequiredArgsConstructor
public class CompleterDuplicationException extends RuntimeException {
    private static final long serialVersionUID = -7887942900292971696L;
    private final @NonNull Completer present;
    private final @NonNull Completer duplicate;

    @Override
    public String getMessage() {
        return String.format(
                "%s is duplicated, got two completers:\n\t- %s\n\t- %s",
                this.present.getCommand(),
                this.present,
                this.duplicate
        );
    }
}
