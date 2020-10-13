package net.nowtryz.mcutils.inventory;

import lombok.Getter;
import net.nowtryz.mcutils.SchedulerUtil;
import net.nowtryz.mcutils.api.Gui;
import net.nowtryz.mcutils.api.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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
    public final void onClick(InventoryClickEvent event) {
        // Prevent shift click from payer inventory that would interact with the opened inventory
        if (ClickType.DOUBLE_CLICK.equals(event.getClick()) && this.player.equals(event.getWhoClicked())) {
            event.setCancelled(true);
            return;
        }

        if (!this.inventory.equals(event.getClickedInventory())) return;
        event.setCancelled(true);

        // close the inventory if the player clicked outside of the inventory
        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
            this.closeInventory();
            return;
        }

        Optional.ofNullable(event.getCurrentItem())
                .filter(itemStack -> !itemStack.getType().equals(Material.AIR))
                .map(this.clickableItems::get)
                // Schedule task to enable inventory actions as proposed in InventoryClickEvent documentation
                .ifPresent(c -> Bukkit.getScheduler().runTask(this.plugin, () -> c.accept(event)));
    }

    @Override
    public void onClose() {}

    @Override
    public void onOpen() {}

    @Override
    public final void closeInventory() {
        if (this.isOpen()) {
            if (Bukkit.isPrimaryThread()) this.player.closeInventory();
            else Bukkit.getScheduler().runTask(this.plugin, this.player::closeInventory);
        }

        this.onClose();
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

    public boolean isOpen() {
        return this.inventory != null && this.inventory.equals(this.player.getOpenInventory().getTopInventory());
    }

    @Override
    public final void open() {
        this.onOpen();

        // Packet sent to open an inventory must be sent from primary thread
        // see https://www.spigotmc.org/threads/receiving-a-cance.lledpackethandleexception.222476/
        if (Bukkit.isPrimaryThread()) this.player.openInventory(inventory);
        else Bukkit.getScheduler().runTask(this.plugin, this::open);
        this.plugin.getInventoryListener().register(this, this.inventory);
    }

    protected final void addClickableItem(ItemStack item, Consumer<? super InventoryClickEvent> consumer) {
        this.clickableItems.put(item, consumer);
    }

    protected final void addClickableItem(int position, ItemStack item, Consumer<? super InventoryClickEvent> consumer) {
        // Not sure but async inventory manipulation may mess things up
        SchedulerUtil.runOnPrimary(this.plugin, () -> {
            this.inventory.setItem(position, item);
            this.clickableItems.put(this.inventory.getItem(position), consumer);
        });
    }

    protected final void removeClickableItem(ItemStack item) {
        this.clickableItems.remove(item);
    }

    protected void onBack(Event event) {
        this.closeInventory();
        if (this.previousInventory != null) this.previousInventory.open();
    }

    protected void registerBackItem(ItemStack item, int pos) {
        this.inventory.setItem(pos, item);
        this.addClickableItem(item, this::onBack);
    }
}
