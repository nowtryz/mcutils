package net.nowtryz.mcutils.command.graph;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.nowtryz.mcutils.command.execution.Completer;
import net.nowtryz.mcutils.command.execution.Executor;

import java.util.HashMap;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class CommandForest extends HashMap<String, CommandRoot> {
    private static final long serialVersionUID = 6366145974492100941L;
    private final CommandRootFactory commandFactory;

    @Inject
    public CommandForest(CommandRootFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public CommandRoot get(String root) {
        return this.computeIfAbsent(root, this.commandFactory::create);
    }

    public void registerCommand(Executor executor) {
        Queue<String> commandLine = executor.getCommandLine();
        this.get(commandLine.remove()).registerCommand(commandLine, executor);
    }

    public void registerCompleter(Completer completer) {
        Queue<String> commandLine = completer.getCommandLine();
        this.get(commandLine.remove()).registerCompleter(commandLine, completer);
    }

    public String toStringGraph() {
        return "Forest {\n" +
                this.values()
                    .stream()
                    .map(node -> node.toStringGraph(1))
                    .collect(Collectors.joining("\n")) + "\n" +
                "}";
    }

    public Stream<Executor> listExecutors() {
        return this.values().stream().flatMap(CommandNode::listExecutors);
    }
}
