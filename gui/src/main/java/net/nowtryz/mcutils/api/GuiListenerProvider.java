package net.nowtryz.mcutils.api;

import net.nowtryz.mcutils.api.listener.GuiListener;

public interface GuiListenerProvider {
    /**
     * Retrieves the {@link GuiListener} registered by the plugin
     * @return the inventory listener of the plugin
     */
    GuiListener getInventoryListener();
}
