package net.nowtryz.mcutils.api.listener;

import com.google.inject.ImplementedBy;
import net.nowtryz.mcutils.api.Gui;
import net.nowtryz.mcutils.inventory.AbstractGui;
import net.nowtryz.mcutils.listener.SimpleInventoryListener;
import org.bukkit.inventory.Inventory;

@ImplementedBy(SimpleInventoryListener.class)
public interface InventoryListener extends EventListener {
    /**
     * Register a controller to the listener to handle events related to its inventory.
     *
     * <p>You should not call the method if your inventory extends
     * {@link AbstractGui} as it automatically register itself to the
     * manager bound to the plugin used to initialize it.
     * @param controller the controller to register
     * @param inventory the inventory to bind to the controller
     */
    void register(Gui controller, Inventory inventory);

    /**
     * Register a controller to the listener to handle events related to its inventory.
     *
     * <p>You should not call the method if your inventory extends
     * {@link AbstractGui} as it automatically register itself to the
     * manager bound to the plugin used to initialize it.
     * @param controller the controller to register
     */
    void register(Gui controller);
}
