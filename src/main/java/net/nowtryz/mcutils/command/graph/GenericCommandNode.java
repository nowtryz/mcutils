package net.nowtryz.mcutils.command.graph;

import net.nowtryz.mcutils.command.contexts.NodeSearchContext;
import net.nowtryz.mcutils.command.exceptions.DuplicationException;
import net.nowtryz.mcutils.command.execution.Completer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class GenericCommandNode extends CommandNode {
    private Completer completer;

    public GenericCommandNode(String key) {
        super(key);
    }

    List<String> completeArgument(NodeSearchContext context) {
        ArrayList<String> list = new ArrayList<>();
        Optional.ofNullable(super.complete(context)).ifPresent(list::addAll);
        Optional.ofNullable(this.completer)
                .map(c -> c.complete(context.completion().build()))
                .ifPresent(list::addAll);

        return list;
    }

    void setCompleter(Completer completer) {
        if (this.completer != null) throw new DuplicationException(this.completer, completer);
        this.completer = completer;
    }

    @Override
    protected void appendToStringGraph(String tabs, StringBuilder builder) {
        if (this.completer != null) builder.append(tabs).append("  completer: ").append(completer).append("\n");
    }
}
