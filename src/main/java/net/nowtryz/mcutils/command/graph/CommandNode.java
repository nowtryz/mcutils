package net.nowtryz.mcutils.command.graph;

import com.google.common.base.Strings;
import net.nowtryz.mcutils.command.contexts.NodeSearchContext;
import net.nowtryz.mcutils.command.exceptions.ExecutorDuplicationException;
import net.nowtryz.mcutils.command.execution.Completer;
import net.nowtryz.mcutils.command.execution.Execution;
import net.nowtryz.mcutils.command.execution.Executor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class CommandNode {
    private static final Pattern GENERIC_MATCHER = Execution.GENERIC_ARG;

    @Getter(AccessLevel.NONE)
    private final HashMap<String, CommandNode> children = new HashMap<>();
    private final String key;

    private GenericCommandNode genericNode;
    private Executor executor;

    @NotNull
    public CommandNode children(String key) {
        return this.children.computeIfAbsent(key, CommandNode::new);
    }

    private synchronized GenericCommandNode getOrCreateGenericNode(String argument) {
        boolean varArgs = Execution.VAR_ARGS.matcher(argument).matches();

        if (this.genericNode == null) this.genericNode = varArgs ? new VarArgsCommandNode() : new GenericCommandNode();
        else if (varArgs && !(this.genericNode instanceof VarArgsCommandNode)) throw new IllegalStateException(
                "Trying to register " + argument + "witch is a varargs, while the registered node does not support it. "
                + "If you fall into this issue and your system work lie this, please report this issue in order to find"
                + " a workaround"
        );

        return this.genericNode;
    }

    CommandNode findExecutor(NodeSearchContext context, Queue<String> remainingArgs) {
        if (remainingArgs.isEmpty()) return this;

        String command = remainingArgs.remove();
        CommandNode child = this.children.getOrDefault(command, this.genericNode);

        if (child != null) return child.findExecutor(context, remainingArgs);
        else return null;
    }

    CommandNode findCompleter(NodeSearchContext context, Queue<String> remainingArgs) {
        if (remainingArgs.isEmpty()) return null;

        String command = remainingArgs.remove().toLowerCase();

        return remainingArgs.isEmpty() ?  this :  Optional
                .ofNullable(this.children.getOrDefault(command, this.genericNode))
                .map(node -> node.findCompleter(context, remainingArgs))
                .orElse(null);
    }

    List<String> complete(NodeSearchContext context) {
        ArrayList<String> list = new ArrayList<>();
        this.children.values()
                .stream()
                .filter(n -> n.getKey().startsWith(context.getLastArgument()))
                .filter(context::checkPermission)
                .map(CommandNode::getKey)
                .forEach(list::add);

        Optional.ofNullable(this.genericNode)
                .map(node -> node.completeArgument(context))
                .ifPresent(list::addAll);

        return list;
    }

    void setCommand(Executor executor) {
        if (this.executor != null) throw new ExecutorDuplicationException(this.executor, executor);
        this.executor = executor;
    }

    void registerCommand(Queue<String> commandLine, Executor executor) {
        if (commandLine.isEmpty()) {
            this.setCommand(executor);
            return;
        }

        String node = commandLine.remove();
        CommandNode children = GENERIC_MATCHER.matcher(node).matches() ? this.getOrCreateGenericNode(node) : this.children(node.toLowerCase());
        children.registerCommand(commandLine, executor);
    }

    void registerCompleter(Queue<String> commandLine, Completer completer) {
        if (commandLine.isEmpty()) throw new IllegalStateException("Cannot register a completer for an empty command");

        String node = commandLine.remove();

        if (commandLine.isEmpty()) {
            this.getOrCreateGenericNode(node).setCompleter(completer);
            return;
        }

        CommandNode children = GENERIC_MATCHER.matcher(node).matches() ? this.getOrCreateGenericNode(node) : this.children(node.toLowerCase());
        children.registerCompleter(commandLine, completer);
    }

    public String toStringGraph(int level) {
        int nextLevel = level + 1;
        String tabs = Strings.repeat("  ", level);
        StringBuilder builder = new StringBuilder().append(tabs).append(this.key).append(" {\n");
        this.appendToStringGraph(tabs, builder);
        if (this.executor != null) builder.append(tabs).append("  executor: ").append(executor).append("\n");
        if (this.genericNode != null) builder.append(this.genericNode.toStringGraph(nextLevel)).append("\n");
        if (!this.children.isEmpty()) for (CommandNode node : this.children.values()) {
            builder.append(node.toStringGraph(nextLevel)).append("\n");
        }

        return builder.append(tabs).append("}").toString();
    }

    /**
     * Method that can be overridden to add information to the string visualisation the the graph
     * @param tabs the number of tabs to display before outputs
     * @param builder the string builder to which the output must be append
     */
    protected void appendToStringGraph(String tabs, StringBuilder builder) {}

    @Override
    public String toString() {
        return this.toStringGraph(0);
    }

    public Stream<Executor> listExecutors() {
        return Stream.concat(
                Stream.of(this.executor, Optional.ofNullable(this.genericNode)
                        .map(GenericCommandNode::getExecutor)
                        .orElse(null)).filter(Objects::nonNull),
                this.children.values().stream().flatMap(CommandNode::listExecutors)
        );
    }
}
