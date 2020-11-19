package net.nowtryz.mcutils.command.graph;

import net.nowtryz.mcutils.command.contexts.NodeSearchContext;
import net.nowtryz.mcutils.command.exceptions.CompleterDuplicationException;
import net.nowtryz.mcutils.command.execution.Completer;

import java.util.List;
import java.util.Optional;

class GenericCommandNode extends CommandNode {
    private Completer completer;

    public GenericCommandNode() {
        super("<argument>");
    }

    protected GenericCommandNode(String key) {
        super(key);
    }

    List<String> completeArgument(NodeSearchContext context) {
        return Optional.ofNullable(this.completer)
                .map(c -> c.complete(context.completion().build()))
                .orElse(null);
    }

    void setCompleter(Completer completer) {
        if (this.completer != null) throw new CompleterDuplicationException(this.completer, completer);
        this.completer = completer;
    }

    @Override
    protected void appendToStringGraph(String tabs, StringBuilder builder) {
        if (this.completer != null) builder.append(tabs).append("  completer: ").append(completer).append("\n");
    }
}
