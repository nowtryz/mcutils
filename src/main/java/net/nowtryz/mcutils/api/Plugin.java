package net.nowtryz.mcutils.api;

import net.nowtryz.mcutils.listener.InventoryListener;

/**
 * A Bukkit plug that implements methods needed by the mcutils api to interact with the plugin
 */
public interface Plugin extends org.bukkit.plugin.Plugin {
    /**
     * Retrieves the {@link InventoryListener} registered by the plugin
     * @return the inventory listener of the plugin
     */
    InventoryListener<? extends net.nowtryz.mcutils.api.Plugin> getInventoryListener();

    /**
     * Is the plugin enabled and not disabling
     * @return true if the plugin is ready
     */
    default boolean isReady() {
        return true;
    }
}
