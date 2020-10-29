package net.nowtryz.mcutils.api;

import net.nowtryz.mcutils.api.listener.InventoryListener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface Gui {
    /**
     * Notify the controller that the inventory will be opened by the player. Used to run pre-open logic
     */
    default void onOpen() {}

    /**
     * Make the player open the inventory
     */
    void open();

    /**
     * Notify the controller that the inventory has been closed, this method is used mainly by the api itself. Prefer
     * not to use it, in favor to {@link Gui#closeInventory()}} if you want to force a player to close
     * on inventory
     *
     * <p>Do not forget to call <code>super</code>'s {@link Gui#onOpen()} method when overriding it to ensure all parent
     * classes get the event, or else some strange behavior can occurre.
     *
     * <p>A common reason to call this method is if you create your own implementation of
     * {@link InventoryListener InventoryListener} and notice inventories while one
     * is closed
     */
    default void onClose() {}

    /**
     * Force the player to close the inventory and run close logic
     */
    void closeInventory();

    /**
     * Handles a click event and run click logic, e.g. run actions bound to items
     * @param event the click event that occurred.
     */
    void onClick(@NotNull InventoryClickEvent event);

    /**
     * Handles a drag event in order to generally cancel it
     * @param event the drag event
     */
    default void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    /**
     * Get the minecraft inventory bound to this controller
     * @return the inventory
     */
    @NotNull
    Inventory getInventory();
}
