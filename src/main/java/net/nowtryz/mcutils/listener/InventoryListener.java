package net.nowtryz.mcutils.listener;

import net.nowtryz.mcutils.api.Gui;
import net.nowtryz.mcutils.api.Plugin;
import net.nowtryz.mcutils.inventory.AbstractGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class InventoryListener<P extends Plugin> extends AbstractListener<P> {
    private final Map<Inventory, Gui> inventories = new HashMap<>();

    @Inject
    public InventoryListener(P plugin) {
        super(plugin);
    }

    /**
     * Register a controller to the listener to handle events related to its inventory.
     *
     * <p>You should not call the method if your inventory extends
     * {@link AbstractGui} as it automatically register itself to the
     * manager bound to the plugin used to initialize it.
     * @param controller the controller to register
     * @param inventory the inventory to bind to the controller
     */
    public void register(Gui controller, Inventory inventory) {
        if (this.inventories.isEmpty()) this.register();
        this.inventories.put(inventory, controller);
    }

    /**
     * Register a controller to the listener to handle events related to its inventory.
     *
     * <p>You should not call the method if your inventory extends
     * {@link AbstractGui} as it automatically register itself to the
     * manager bound to the plugin used to initialize it.
     * @param controller the controller to register
     */
    public void register(Gui controller) {
        this.register(controller, controller.getInventory());
    }


    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Optional.ofNullable(this.inventories.get(event.getInventory())).ifPresent(Gui::onClose);
        this.inventories.remove(event.getInventory());
        if (this.inventories.isEmpty()) this.unRegister();
    }

    /**
     * Handle click event and find the controller bound the clicked inventory to run its core logic. If the inventory is
     * not bound to any controller, the listener will simple ignore this event
     * @param event the click event
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Gui gui = this.inventories.get(event.getClickedInventory());

        // Handle click event
        if (gui != null) gui.onClick(event);
        // Avoid shift clicks from the bottom inventory
        else if (event.getClick().isShiftClick() && this.inventories.containsKey(event
                        .getWhoClicked()
                        .getOpenInventory()
                        .getTopInventory()
        )) {
            event.setCancelled(true);
        }
    }
}
