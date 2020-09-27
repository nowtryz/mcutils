package net.nowtryz.mcutils.api;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface Gui {
    /**
     * Notify the controller that the inventory will be open by the player. Used to run pre-open logic
     */
    void onOpen();

    /**
     * Make the player open the inventory
     */
    void open();

    /**
     * Notify the controller that the inventory has been closed, this method is used mainly by the api itself. Prefer
     * not to use it, in favor to {@link Gui#closeInventory()}} if you want to force a player to close
     * on inventory
     *
     * <p>A common reason to call this method is if you create your own implementation of
     * {@link net.nowtryz.mcutils.listener.InventoryListener InventoryListener} and notice inventories while clicks
     * happen
     */
    void onClose();

    /**
     * Force the player to close the inventory and run close logic
     */
    void closeInventory();

    /**
     * Handles a click event and run click logic, e.g. run actions bound to items
     * @param event the click event that occurred.
     */
    void onClick(InventoryClickEvent event);

    /**
     * Get the minecraft inventory bound to this controller
     * @return the inventory
     */
    Inventory getInventory();
}
