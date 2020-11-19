package net.nowtryz.mcutils.command.graph;

import net.nowtryz.mcutils.command.contexts.NodeSearchContext;
import net.nowtryz.mcutils.command.execution.Completer;
import net.nowtryz.mcutils.command.execution.Executor;

import java.util.List;
import java.util.Queue;

public class VarArgsCommandNode extends GenericCommandNode {
    public VarArgsCommandNode() {
        super("<arguments...>");
    }

    @Override
    CommandNode findExecutor(NodeSearchContext context, Queue<String> remainingArgs) {
        return this;
    }

    @Override
    CommandNode findCompleter(NodeSearchContext context, Queue<String> remainingArgs) {
        return this;
    }

    @Override
    List<String> complete(NodeSearchContext context) {
        return this.completeArgument(context);
    }

    @Override
    void registerCommand(Queue<String> commandLine, Executor executor) {
        if (commandLine.isEmpty()) this.setCommand(executor);
        else throw new IllegalStateException("Cannot register sub commands while a varargs node is in place");
    }

    @Override
    void registerCompleter(Queue<String> commandLine, Completer completer) {
        throw new IllegalStateException("Cannot register sub commands while a varargs node is in place");
    }
}
