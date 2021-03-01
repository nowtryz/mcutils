package net.nowtryz.mcutils.command.graph;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.nowtryz.mcutils.command.CommandResult;
import net.nowtryz.mcutils.command.ResultHandler;
import net.nowtryz.mcutils.command.SenderType;
import net.nowtryz.mcutils.command.contexts.ExecutionContext;
import net.nowtryz.mcutils.command.contexts.NodeSearchContext;
import net.nowtryz.mcutils.command.execution.Executor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;

public class CommandAdapter extends Command implements TabExecutor {
    @Setter @Getter
    private CommandRoot node;

    @Setter
    private @NonNull ResultHandler handler = ResultHandler.FALL_BACK;

    public CommandAdapter(CommandRoot node) {
        super(node.getKey());
        this.node = node;
    }

    @Override
    public @NotNull String getDescription() {
        return this.node.getDescription();
    }

    @Override
    public @NotNull String getUsage() {
        return this.node.getUsage();
    }

    /**
     * Proxies Commands from PluginCommand to our node
     * @param sender the command sender
     * @param command the plugin command being called
     * @param label the label used to call the command
     * @param args the command arguments
     * @return the result we should return if the command was executed normally
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return this.execute(sender, label, args);
    }

    @Override
    public boolean execute(@NonNull @NotNull CommandSender sender, @NonNull @NotNull String commandLabel, @NonNull String[] args) {

        if (!this.node.getPlugin().isEnabled()) throw new CommandException(String.format(
                "Cannot execute command '%s' in plugin %s - plugin is disabled.",
                commandLabel,
                this.node.getPlugin().getDescription().getFullName()
        ));

        NodeSearchContext context = NodeSearchContext.builder()
                .sender(sender)
                .commandLabel(commandLabel)
                .args(args)
                .build();

        CommandNode node = this.node.findExecutor(context);
        Executor executor = Optional.ofNullable(node).map(CommandNode::getExecutor).orElse(null);

        if (executor == null) {
            this.handle(context.execution().node(node).build(), CommandResult.UNKNOWN);
            return CommandResult.UNKNOWN.isValid();
        }

        if (executor.isAsync()) {
            Bukkit.getScheduler().runTaskAsynchronously(this.node.getPlugin(), () -> this.execute(node, executor, context));
            // As we cannot know the effective result, we prevent the default failure behavior
            return true;
        } else return this.execute(node, executor, context);
    }

    private boolean execute(@NotNull CommandNode node, @NotNull Executor executor, NodeSearchContext context) {
        ExecutionContext executionContext = context.execution(executor).node(node).build();
        SenderType senderType = executor.getType();

        if (!context.checkPermission(executor)) {
            return this.handle(executionContext, CommandResult.MISSING_PERMISSION);
        }

        if(!senderType.check(context)) {
            return this.handle(executionContext, CommandResult.WRONG_TARGET);
        }

        try {
            CommandResult result = executor.execute(executionContext);
            return this.handle(executionContext, result);
        } catch (Exception exception) {
            Supplier<String> errorMessage = () -> String.format(
                    "[MCUtils] Unhandled exception executing command '%s' in plugin %s",
                    context.getCommandLabel(),
                    this.node.getPlugin().getDescription().getFullName());
            Bukkit.getLogger().log(Level.SEVERE, exception, errorMessage);
            executionContext.setThrownError(exception);
            return this.handle(executionContext, CommandResult.INTERNAL_ERROR);
        }
    }

    private boolean handle(ExecutionContext context, @NonNull CommandResult result) {
        this.handler.handle(context, result);
        return result.isValid();
    }

    /**
     * Proxies tab completion from PluginCommand to our node
     * @param sender the command sender
     * @param command the plugin command being called
     * @param label the label used to call the command
     * @param args the command arguments
     * @return the result we should return if the command was executed normally
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return this.tabComplete(sender, label, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NonNull @NotNull CommandSender sender, @NonNull @NotNull String alias, @NonNull String[] args) {
        NodeSearchContext context = NodeSearchContext.builder()
                .sender(sender)
                .commandLabel(alias)
                .args(args)
                .build();


        try {
            CommandNode completer = this.node.findCompleter(context);
            if (completer == null) return new ArrayList<>();
            return completer.complete(context);
        } catch (Throwable throwable) {
            StringBuilder message = new StringBuilder()
                    .append("Unhandled exception during tab completion for command '/")
                    .append(alias)
                    .append(' ');

            for (String arg : args) message.append(arg).append(' ');

            message.deleteCharAt(message.length() - 1)
                    .append("' in plugin ")
                    .append(this.node.getPlugin().getDescription().getFullName());

            throw new CommandException(message.toString(), throwable);
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + '(' + node.getKey() + ", " + node.getPlugin() + ')';
    }
}
