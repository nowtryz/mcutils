package net.nowtryz.mcutils.listener;

import lombok.NonNull;
import net.nowtryz.mcutils.MCUtils;
import net.nowtryz.mcutils.api.Gui;
import net.nowtryz.mcutils.api.Plugin;
import net.nowtryz.mcutils.api.listener.GuiListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.bukkit.event.inventory.InventoryAction.COLLECT_TO_CURSOR;
import static org.bukkit.event.inventory.InventoryAction.MOVE_TO_OTHER_INVENTORY;

@Singleton
public class SimpleInventoryListener extends AbstractListener implements GuiListener {
    private final Map<Inventory, Gui> inventories = new HashMap<>();

    @Inject
    public SimpleInventoryListener(Plugin plugin) {
        super(plugin);
    }

    /** {@inheritDoc} */
    @Override
    public void register(Gui controller, Inventory inventory) {
        if (this.inventories.isEmpty()) this.register();
        this.inventories.put(inventory, controller);
    }

    /** {@inheritDoc} */
    @Override
    public void register(Gui controller) {
        this.register(controller, controller.getInventory());
    }

    @Override
    public void closeAll() {
        MCUtils.runOnPrimary(this.plugin, () -> this.inventories.values().forEach(Gui::closeInventory));
    }

    @Override
    public Optional<Gui> getOpenedGui(@NonNull Inventory inventory) {
        return Optional.ofNullable(this.inventories.get(inventory));
    }

    /**
     * Notice the {@link Gui} related to this event that its {@link Inventory} got closed
     * @param event the close event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent event) {
        Optional.ofNullable(this.inventories.remove(event.getInventory())).ifPresent(Gui::onClose);
        if (this.inventories.isEmpty()) this.unregister();
    }

    /**
     * Handle click event and find the controller bound the clicked inventory to run its core logic. If the inventory is
     * not bound to any controller, the listener will simple ignore this event
     * @param event the click event
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        // We use the hashcode of the inventory to have direct access to the related gui
        Gui gui = this.inventories.get(event.getClickedInventory());

        // Handle click event
        if (gui != null) {
            gui.onClick(event);
            return;
        }

        // Check if the top inventory is a known one
        Gui topGui = this.inventories.get(event.getWhoClicked().getOpenInventory().getTopInventory());
        if (topGui != null) {
            // prevent item swap with the top inventory
            if (event.getAction() == COLLECT_TO_CURSOR || event.getAction() == MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
            }

            // close the inventory if the player clicked outside of the inventory
            if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                topGui.closeInventory();
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        // We use the hashcode of the inventory to have direct access to the related gui
        Gui gui = this.inventories.get(event.getInventory());

        event.getRawSlots().forEach(System.out::println);

        // Handle drag event
        if (gui != null) gui.onDrag(event);
    }
}
