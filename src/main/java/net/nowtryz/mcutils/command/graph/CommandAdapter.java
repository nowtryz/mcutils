package net.nowtryz.mcutils.command.graph;

import net.nowtryz.mcutils.command.SenderType;
import net.nowtryz.mcutils.command.contexts.ExecutionContext;
import net.nowtryz.mcutils.command.contexts.NodeSearchContext;
import net.nowtryz.mcutils.command.execution.Executor;
import net.nowtryz.mcutils.command.ResultHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.nowtryz.mcutils.command.CommandResult;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    public String getDescription() {
        return this.node.getDescription();
    }

    @Override
    public String getUsage() {
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return this.execute(sender, label, args);
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull String commandLabel, @NonNull String[] args) {

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

        Executor executor = this.node.findExecutor(context);

        if (executor == null) {
            this.handle(context.execution().build(), CommandResult.UNKNOWN);
            return CommandResult.UNKNOWN.isValid();
        }

        if (executor.isAsync()) {
            Bukkit.getScheduler().runTaskAsynchronously(this.node.getPlugin(), () -> this.execute(executor, context));
            // As we cannot know the effective result, we prevent the default failure behavior
            return true;
        } else return this.execute(executor, context);
    }

    private boolean execute(@NotNull Executor executor, NodeSearchContext context) {
        ExecutionContext executionContext = context.execution(executor).build();
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
        } catch (Throwable throwable) {
            this.handle(executionContext, CommandResult.INTERNAL_ERROR);
            throw new CommandException(String.format(
                    "Unhandled exception executing command '%s' in plugin %s",
                    context.getCommandLabel(),
                    this.node.getPlugin().getDescription().getFullName()
            ), throwable);
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
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return this.tabComplete(sender, label, args);
    }

    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull String alias, @NonNull String[] args) {
        NodeSearchContext context = NodeSearchContext.builder()
                .sender(sender)
                .commandLabel(alias)
                .args(args)
                .build();


        try {
            CommandNode completer = this.node.findCompleter(context);
            if (completer == null) return null;
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
