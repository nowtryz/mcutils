package net.nowtryz.mcutils.api.listener;

import net.nowtryz.mcutils.api.Gui;
import org.bukkit.inventory.Inventory;

public interface GuiListener extends EventListener {
    /**
     * Register a controller to the listener to handle events related to its inventory.
     *
     * <p>You should not call the method if your inventory extends
     * AbstractGui as it automatically register itself to the
     * manager bound to the plugin used to initialize it.
     * @param controller the controller to register
     * @param inventory the inventory to bind to the controller
     */
    void register(Gui controller, Inventory inventory);

    /**
     * Register a controller to the listener to handle events related to its inventory.
     *
     * <p>You should not call the method if your inventory extends
     * AbstractGui as it automatically register itself to the
     * manager bound to the plugin used to initialize it.
     * @param controller the controller to register
     */
    void register(Gui controller);

    /**
     * Close all opened Guis registered to this listener.
     */
    void closeAll();
}
