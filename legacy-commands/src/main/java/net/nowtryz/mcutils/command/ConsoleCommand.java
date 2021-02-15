package net.nowtryz.mcutils.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a commend that intends to only be executed by the console
 */
public interface ConsoleCommand<P extends Plugin> extends Executable<P> {

    @Override
    @SuppressWarnings("deprecation")
    default @NotNull CommandResult execute(Plugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) return CommandResult.NOT_A_CONSOLE;
        else return this.execute(plugin, (ConsoleCommandSender) sender, args);
    }

    @Override
    default List<String> tabComplete(Plugin plugin, CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) return tabComplete(plugin, (ConsoleCommandSender) sender, args);
        return null;
    }

    CommandResult execute(Plugin plugin, ConsoleCommandSender player, String[] args);
    List<String> tabComplete(Plugin plugin, ConsoleCommandSender player, String[] args);
}
