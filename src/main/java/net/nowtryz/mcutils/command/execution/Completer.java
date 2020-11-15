package net.nowtryz.mcutils.command.execution;

import net.nowtryz.mcutils.command.contexts.CompletionContext;

import java.util.List;

public interface Completer extends Execution {
    List<String> complete(CompletionContext context);
}
