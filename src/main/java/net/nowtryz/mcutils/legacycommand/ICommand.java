package net.nowtryz.mcutils.legacycommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.logging.Level;

/**
 * Base command
 * @param <P> the class of the plugin provided at each call
 * @param <D> the class of the description, this enables you to handle i18n or other stuff like that using e.g.
 *           <code>{@link java.util.function.Function Function}&lt;{@link org.bukkit.entity.Player Player},
 *           {@link String}&gt;</code>
 */
public interface ICommand<P extends Plugin, D> extends Executable<P> {
    /**
     * Get the keyword used by the command<br>
     *     Ex: /jump &lt;keyword&gt; [args...]
     * @return the keyword
     */
    @NotNull String getKeyword();

    /**
     * Get all aliases of this command, including the keyword itself
     * @return all aliases of this command
     */
    @NotNull Set<String> getAliases();

    /**
     * Is this commands intends and is able to run in an asynchronous thread
     * @return true if able
     */
    boolean isAsync();

    /**
     * Validates if arguments are applicable to this command
     * @param args the arguments to test
     * @return true if the command can accept de specified arguments
     */
    boolean canAccept(String[] args);

    /**
     * Gets the permission required by this command to be executed
     * @return the permission string
     */
    String getPermission();

    /**
     * Gets the description of this command to use in help menus
     * @return the description
     * @see ICommand
     */
    @Nullable
    D getDescription();

    /**
     * Get the usage string of the command
     * @return a translated message
     */
    String getUsage();

    /**
     * Perform post command action based on the result
     * @param result result of the executed command
     * @return true if usage is correct
     */
    boolean handleResult(CommandSender sender, CommandResult result);

    /**
     * Processes the command and check arguments, permission and if this command can run asynchronously
     * @param plugin the instance of the plugin that is running on the server
     * @param sender the command sender
     * @param args arguments of the command
     * @return the command result descriptor
     */
    default boolean process(P plugin, CommandSender sender, String[] args) {
        try {
            // checks
            if (this.getPermission() != null && !sender.hasPermission(this.getPermission()) && !sender.isOp()) {
                return this.handleResult(sender, CommandResult.MISSING_PERMISSION);
            }
            else if (!this.canAccept(args)) return this.handleResult(sender, CommandResult.INVALID_ARGUMENTS);

            // execution
            else if (!this.isAsync()) return this.handleResult(sender, this.execute(plugin, sender, args));
            else Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> this.handleResult(sender, this.execute(plugin, sender, args)));
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to process " + this.getKeyword() + " command:", e);
            this.handleResult(sender, CommandResult.INTERNAL_ERROR);
        }

        return true;
    }
}
