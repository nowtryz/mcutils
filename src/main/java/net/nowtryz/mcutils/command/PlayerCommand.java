package net.nowtryz.mcutils.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a commend that intends to only be executed by a player
 */
public interface PlayerCommand<Plugin extends JavaPlugin> extends Executable<Plugin> {

    @Override
    default @NotNull CommandResult execute(Plugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return CommandResult.NOT_A_PLAYER;
        else return this.execute(plugin, (Player) sender, args);
    }

    @Override
    default List<String> tabComplete(Plugin plugin, CommandSender sender, String[] args) {
        if (sender instanceof Player) return tabComplete(plugin, (Player) sender, args);
        return null;
    }

    CommandResult execute(Plugin plugin, Player player, String[] args);
    List<String> tabComplete(Plugin plugin, Player player, String[] args);
}
