package net.nowtryz.mcutils.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public abstract class CommandHandler<P extends JavaPlugin,D> extends AbstractCommand<P,D> implements TabExecutor {
    protected final P plugin;
    private final boolean canComplete;

    public CommandHandler(@NotNull P plugin, @NotNull String label, @NotNull String usage,
                          @NotNull String permission, boolean canComplete) {
        super(label, usage, permission);
        this.canComplete = canComplete;
        this.plugin = plugin;

        PluginCommand command = plugin.getCommand(label);
        if (command != null) {
            if (canComplete) command.setTabCompleter(this);
            command.setExecutor(this);
        } else {
            plugin.getLogger().severe("Unable to register command " + label);
        }
    }

    public CommandHandler(@NotNull P plugin, @NotNull String label, @NotNull String usage,
                          @NotNull String permission, @Nullable Predicate<String[]> validator, boolean canComplete) {
        super(label, usage, permission, validator);
        this.canComplete = canComplete;
        this.plugin = plugin;

        PluginCommand command = plugin.getCommand(label);
        if (command != null) {
            if (canComplete) command.setTabCompleter(this);
            command.setExecutor(this);
        } else {
            plugin.getLogger().severe("Unable to register command " + label);
        }
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @Nullable Command command, @NotNull String label, String[] args) {
        return super.process(this.plugin, sender, args);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @Nullable Command command, @NotNull String alias, String[] args) {
        return this.tabComplete(this.plugin, sender, args);
    }

    public boolean canComplete() { return this.canComplete; }
}
