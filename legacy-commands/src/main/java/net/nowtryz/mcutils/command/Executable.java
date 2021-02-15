package net.nowtryz.mcutils.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Generic command, with execute and tab complete methods. This interface is used internally by the API to manipulate
 * generic commands without dealing with the description type &lt;D&gt; in {@link ICommand}
 * @param <P> the class of the plugin provided at each call
 * @see ICommand the bases interface shered by the API
 * @see PlayerCommand an interface implementing {@link Executable}
 */
interface Executable<P extends Plugin> {

    /**
     * Executes the command
     * @param plugin the instance of the plugin that is running on the server
     * @param sender the command sender
     * @param args arguments of the command
     * @return the command result descriptor
     */
    @NotNull
    CommandResult execute(P plugin, CommandSender sender, String[] args);

    /**
     * Return possible completion for the command
     * @param plugin the instance of the plugin that is running on the server
     * @param sender the command sender
     * @param args arguments of the command
     * @return true on success
     */
    @Nullable
    List<String> tabComplete(P plugin, CommandSender sender, String[] args);
}
