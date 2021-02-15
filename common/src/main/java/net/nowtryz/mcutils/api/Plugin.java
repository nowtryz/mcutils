package net.nowtryz.mcutils.api;

/**
 * A Bukkit plug that implements methods needed by the mcutils api to interact with the plugin
 */
public interface Plugin extends org.bukkit.plugin.Plugin {
    /**
     * Is the plugin enabled and not disabling
     * @return true if the plugin is ready
     */
    default boolean isReady() {
        return true;
    }
}
