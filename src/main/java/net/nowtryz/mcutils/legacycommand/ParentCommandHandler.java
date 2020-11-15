package net.nowtryz.mcutils.legacycommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public abstract class ParentCommandHandler<P extends JavaPlugin,D> extends AbstractParentCommand<P,D> implements TabExecutor {
    protected final P plugin;
    private final boolean canComplete;

    @SafeVarargs
    public ParentCommandHandler(@NotNull P plugin, String label, String usage, String permission,
                                boolean canComplete, ICommand<P,D>... commands) {
        super(label, usage, permission, commands);
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

    @SafeVarargs
    public ParentCommandHandler(@NotNull P plugin, String label, String usage, String permission,
                                Predicate<String[]> validator, boolean canComplete, ICommand<P,D>... commands) {
        super(label, usage, permission, validator, commands);
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
    public boolean onCommand(@NotNull CommandSender sender, @Nullable Command command, @NotNull String label, String[] args) {
        return super.process(this.plugin, sender, args);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @Nullable Command command, @NotNull String alias, String[] args) {
        return super.tabComplete(this.plugin, sender, args);
    }

    public boolean canComplete() { return this.canComplete; }
}
