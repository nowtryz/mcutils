package net.nowtryz.mcutils.inventory;

import lombok.Getter;
import net.nowtryz.mcutils.api.Gui;
import net.nowtryz.mcutils.api.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static net.nowtryz.mcutils.MCUtils.runOnPrimary;

public abstract class AbstractGui<P extends Plugin> implements Gui {
    private final Map<ItemStack, Consumer<? super InventoryClickEvent>> clickableItems = new HashMap<>();
    protected final Gui previousInventory;
    protected final P plugin;
    protected final Player player;
    @Getter(onMethod_={@Override})
    private Inventory inventory;

    public AbstractGui(P plugin, Player player) {
        this.previousInventory = null;
        this.plugin = plugin;
        this.player = player;
    }

    public AbstractGui(P plugin, Player player, Gui previousInventory) {
        this.previousInventory = previousInventory;
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public final void onClick(@NotNull InventoryClickEvent event) {
        if (!this.inventory.equals(event.getClickedInventory())) return;
        event.setCancelled(true);

        // prevent event from occurring twice (once for the second click, and twice for the double click event)
        if (event.getClick() == ClickType.DOUBLE_CLICK) return;

        Optional.ofNullable(event.getCurrentItem())
                .filter(itemStack -> !itemStack.getType().equals(Material.AIR))
                .map(this.clickableItems::get)
                // Schedule task to enable inventory actions as proposed in InventoryClickEvent documentation
                .ifPresent(c -> Bukkit.getScheduler().runTask(this.plugin, () -> c.accept(event)));
    }

    @Override
    public final void closeInventory() {
        if (this.isOpen()) {
            if (Bukkit.isPrimaryThread()) this.player.closeInventory();
            else Bukkit.getScheduler().runTask(this.plugin, this.player::closeInventory);
        }
        // No need to call onClose, it will be called by the listener
    }

    /**
     * Sets the inventory and open it
     *
     * <strong>Don't forget to call <code>plugin.getInventoryListener().register(this, inventory)</code> before
     * setInventory</strong> if your plugin implements {@link Plugin}, or hook your own Listener
     * @param inventory the bukkit inventory to use
     */
    protected final void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void createInventory(int size, String name) {
        this.setInventory(Bukkit.createInventory(this.player, size, name));
    }

    public boolean isOpen() {
        return this.inventory != null && this.inventory.equals(this.player.getOpenInventory().getTopInventory());
    }

    public boolean hasPrevious() {
        return this.previousInventory != null;
    }

    @Override
    public final void open() {
        this.onOpen();

        // Packet sent to open an inventory must be sent from primary thread
        // see https://www.spigotmc.org/threads/receiving-a-cance.lledpackethandleexception.222476/
        runOnPrimary(this.plugin, () -> this.player.openInventory(inventory));
        this.plugin.getInventoryListener().register(this, this.inventory);
    }

    public final void addClickableItem(ItemStack item, Consumer<? super InventoryClickEvent> consumer) {
        this.clickableItems.put(item, consumer);
    }

    protected final void addClickableItem(int position, ItemStack item, Consumer<? super InventoryClickEvent> consumer) {
        // Not sure but async inventory manipulation may mess things up
        runOnPrimary(this.plugin, () -> {
            this.inventory.setItem(position, item);
            this.clickableItems.put(this.inventory.getItem(position), consumer);
        });
    }

    /**
     * Keep the clickable action of the previous item in the slot and transfert it to the new item
     * @param slot the slot where the item to swap is
     * @param next the new item to put instead
     */
    public final void swapClickableItem(int slot, ItemStack next) {
        Consumer<? super InventoryClickEvent> action = this.clickableItems.remove(this.getInventory().getItem(slot));
        this.addClickableItem(slot, next, action);
    }

    public final void removeClickableItem(ItemStack item) {
        this.clickableItems.remove(item);
    }

    public final void removeClickableItem(int slot) {
        this.removeClickableItem(this.inventory.getItem(slot));
    }

    public void onBack(Event event) {
        if (this.previousInventory != null) this.previousInventory.open();
        else this.closeInventory();
    }

    protected void registerBackItem(ItemStack item, int pos) {
        this.inventory.setItem(pos, item);
        this.addClickableItem(item, this::onBack);
    }
}
