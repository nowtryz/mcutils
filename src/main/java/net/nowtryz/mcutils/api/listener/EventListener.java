package net.nowtryz.mcutils.api.listener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public interface EventListener extends Listener {
    /**
     * Registers this listener to the Bukkit's {@link PluginManager}
     */
    void register();

    /**
     * Unregisters this listener to the Bukkit's {@link PluginManager}
     */
    void unregister();

    /**
     * Check if the listener has been registered to the Bukkit's {@link PluginManager}
     * @return true if it is the case
     */
    boolean isRegistered();
}
