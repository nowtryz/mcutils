package net.nowtryz.mcutils.inventory;

import lombok.Getter;
import net.nowtryz.mcutils.SchedulerUtil;
import net.nowtryz.mcutils.api.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

public abstract class PaginatedGui<P extends Plugin, V> extends AbstractGui<P> {
    private final int previousPos, nexPos;
    private ItemStack previous, next;
    @Getter private int page = 0, count;
    private int[] availablePos;
    private List<V> values;
    private Map<V,ItemStack> items = new HashMap<>();

    public PaginatedGui(P plugin, Player player, Collection<V> values, int previousPos, int nextPos) {
        super(plugin, player);
        this.values = new ArrayList<>(values);
        this.previousPos = previousPos;
        this.nexPos = nextPos;
        this.init();
    }

    public PaginatedGui(P plugin, Player player, AbstractGui<P> previousInventory, Collection<V> values, int previousPos, int nextPos) {
        super(plugin, player, previousInventory);
        this.values = new ArrayList<>(values);
        this.previousPos = previousPos;
        this.nexPos = nextPos;
        this.init();
    }

    private void init() {
        super.setInventory(this.createTemplateInventory());
        this.availablePos = IntStream.range(0, this.getInventory().getSize()).toArray();
    }

    protected final void fill() {
        int first = this.page * this.availablePos.length;
        List<V> pageContent = this.values.subList(
                first,
                Math.min(this.values.size(), first + this.availablePos.length)
        );

        int i = 0;

        for (; i < pageContent.size(); i++) {
            V object = pageContent.get(i);
            ItemStack item = this.getItem(object);
            int pos = this.availablePos[i];

            // will override handler for the same item if already placed
            this.addClickableItem(pos, item, event -> this.onClick(event, object));
        }

        if (i < this.availablePos.length) {
            final int finalI = i;
            SchedulerUtil.runOnPrimary(this.plugin, () -> {
                for (int j = finalI; j < this.availablePos.length; j++) {
                    this.getInventory().setItem(this.availablePos[j], null);
                }
            });
        }

        this.removeClickableItem(this.previous);
        this.removeClickableItem(this.next);
        this.previous = this.getPreviousIcon();
        this.next = this.getNextIcon();

        if (this.page > 0)
            this.addClickableItem(this.previousPos, this.previous, event -> this.setPage(this.page - 1));
        if (this.page < this.count - 1)
            this.addClickableItem(this.nexPos, this.next, event -> this.setPage(this.page + 1));
    }

    protected void setPage(int page) {
        if (page >= this.count) this.page = this.count - 1;
        else this.page = Math.max(page, 0);

        if (this.isOpen()) this.fill();
    }

    protected final void setAvailablePositions(int[] pos) {
        this.availablePos = pos;
        this.count = this.values.size() / pos.length + 1;
    }

    protected final void setValues(Collection<V> values) {
        this.values = new ArrayList<>(values);
        this.count = this.values.size() / this.availablePos.length + 1;
        this.setPage(this.page); // update pag to fit the count
    }

    private ItemStack getItem(V object) {
        if (this.items.containsKey(object)) return this.items.get(object);

        ItemStack item = this.createItemForObject(object);
        this.items.put(object, item);
        return item;
    }

    @Override
    public void onOpen() {
        super.onOpen();
        this.fill();
    }

    /**
     * Create an icon based on the page number for the previous page
     * @return the icon
     */
    @NotNull
    protected abstract ItemStack getPreviousIcon();

    /**
     * Create an icon based on the page number for the new page
     * @return the icon
     */
    @NotNull
    protected abstract ItemStack getNextIcon();

    /**
     * Create a base inventory with fillers and decorations. This inventory will the be filled with item for each page
     * @return the base inventory
     */
    @NotNull
    protected abstract Inventory createTemplateInventory();

    /**
     * Create the icon bound to the object in the inventory
     * @param object the object
     * @return the item to bind
     */
    @NotNull
    protected abstract ItemStack createItemForObject(V object);

    /**
     * Handle click on an item
     * @param event the click event
     * @param object the object bound to the clicked item
     */
    protected abstract void onClick(InventoryClickEvent event, V object);
}
