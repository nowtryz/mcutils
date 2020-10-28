package net.nowtryz.mcutils.listener;

import lombok.Getter;
import net.nowtryz.mcutils.api.Plugin;
import net.nowtryz.mcutils.api.listener.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class AbstractListener implements EventListener {
    protected Plugin plugin;
    /** {@inheritDoc} */
    @Getter(onMethod_={@Override})
    private boolean registered;

    public AbstractListener(Plugin plugin) {
        this.plugin = plugin;
        this.registered = false;
    }

    /** {@inheritDoc} */
    @Override
    public void register() {
        if (!registered) {
            Bukkit.getPluginManager().registerEvents(this, this.plugin);
            this.registered = true;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
        this.registered = false;
    }
}
