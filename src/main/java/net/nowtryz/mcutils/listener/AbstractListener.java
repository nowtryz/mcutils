package net.nowtryz.mcutils.listener;

import net.nowtryz.mcutils.api.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class AbstractListener<P extends Plugin> implements Listener {
    protected P plugin;
    private boolean registered;

    public AbstractListener(P plugin) {
        this.plugin = plugin;
        this.registered = false;
    }

    public void register() {
        if (!registered) {
            Bukkit.getPluginManager().registerEvents(this, this.plugin);
            this.registered = true;
        }
    }

    public void unRegister() {
        HandlerList.unregisterAll(this);
        this.registered = false;
    }
}
