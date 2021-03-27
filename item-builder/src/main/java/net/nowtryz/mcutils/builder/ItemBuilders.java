package net.nowtryz.mcutils.builder;

import net.nowtryz.mcutils.MCUtils;
import net.nowtryz.mcutils.builder.api.*;
import net.nowtryz.mcutils.builder.internal.BellowThirteenFactory;
import net.nowtryz.mcutils.builder.internal.FactoryProvider;
import net.nowtryz.mcutils.builder.internal.ThirteenFactory;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public final class ItemBuilders {
    private final static ItemBuilderFactory FACTORY = MCUtils.THIRTEEN_COMPATIBLE ? new ThirteenFactory() : new BellowThirteenFactory();

    static {
        FactoryProvider.setFactory(FACTORY);
    }

    /**
     * Create a simple item build from the given material
     * @param material the material of the item
     * @return an item builder for the specified material
     */
    public static ItemBuilder create(Material material) {
        return FACTORY.create(material);
    }

    /**
     * Creates a builder to edit an exiting item
     * @param item the initial item
     * @return an item builder
     */
    public static ItemBuilder from(ItemStack item) {
        return FACTORY.from(item);
    }

    /**
     * Create a monster egg
     * @return an item builder for monster egg
     */
    public static ItemBuilder createEgg() {
        return FACTORY.createEgg();
    }

    /**
     * Create an item builder from the provided item
     * @param itemStack the item to edit
     * @return an item builder for monster egg
     * @throws IllegalArgumentException if the provided item is not a monster egg
     */
    public static ItemBuilder createEggFrom(ItemStack itemStack) {
        return FACTORY.createEggFrom(itemStack);
    }

    /**
     * Create builder able to create a player skull and edit its properties
     * @return a skull builder
     */
    public static SkullBuilder createSkull() {
        return FACTORY.createSkull();
    }

    /**
     * Create builder able to create a skull for the specified player and edit its properties
     * @param player the player owning the skull
     * @return a skull builder
     */
    public static SkullBuilder skullForPlayer(OfflinePlayer player) {
        return FACTORY.skullForPlayer(player);
    }

    /**
     * Create a glass pane with the specified color
     * <p>This is a utility method create glass pane whichever is the minecraft version
     *
     * @param color the color to apply to the pane
     * @return an item builder for the glass pane
     */
    public static ItemBuilder createGlassPane(DyeColor color) {
        return FACTORY.createGlassPane(color);
    }

    /**
     * Create a glass block with the specified color
     * <p>This is a utility method create glass block whichever is the minecraft version
     *
     * @param color the color to apply to the block
     * @return an item builder for the glass block
     */
    public static ItemBuilder createGlass(DyeColor color) {
        return FACTORY.createGlass(color);
    }
}
