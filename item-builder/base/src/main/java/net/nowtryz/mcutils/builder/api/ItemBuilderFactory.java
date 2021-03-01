package net.nowtryz.mcutils.builder.api;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface ItemBuilderFactory {

    /**
     * Create a simple item build from the given material
     * @param material the material of the item
     * @return an item builder for the specified material
     */
    SimpleBuilder create(Material material);

    /**
     * Creates a builder to edit an exiting item
     * @param item the initial item
     * @return a builder
     */
    SimpleBuilder from(ItemStack item);

    /**
     * Create a monster egg
     * @return an item builder for monster egg
     */
    MonterEggBuilder createEgg();

    /**
     * Create an item builder from the provided item
     * @param itemStack the item to edit
     * @return an item builder for monster egg
     * @throws IllegalArgumentException if the provided item is not a monster egg
     */
    MonterEggBuilder createEggFrom(ItemStack itemStack);

    /**
     * Create an item builder from the provided item
     * @param itemStack the item to edit
     * @param meta the item meta to copy
     * @return an item builder for monster egg
     * @throws IllegalArgumentException if the provided item is not a monster egg
     */
    MonterEggBuilder createEggFrom(ItemStack itemStack, ItemMeta meta);

    /**
     * Create builder able to create a player skull and edit its properties
     * @return a skull builder
     */
    SkullBuilder createSkull();

    /**
     * Create an item builder from the provided item
     * @param itemStack the item to edit
     * @param meta the item meta to copy
     * @return an item builder for player skulls
     * @throws IllegalArgumentException if the provided item is not a monster egg
     */
    SkullBuilder createSkull(ItemStack itemStack, ItemMeta meta);

    /**
     * Create builder able to create a skull for the specified player and edit its properties
     * @param player the player owning the skull
     * @return a skull builder
     */
    default SkullBuilder skullForPlayer(OfflinePlayer player) {
        return createSkull().setOwningPlayer(player);
    }

    /**
     * Create an armor builder from the given piece
     * @param armor the material of the armor piece to use
     * @throws IllegalArgumentException if the material is not a leather armor piece
     * @return a leather armor builder
     */
    LeatherArmorBuilder createLeatherArmor(Material armor);

    /**
     * Create a leather armor builder from the given item
     * @param itemStack the item to edit
     * @param meta the item meta to copy
     * @throws IllegalArgumentException if the material is not a leather armor piece
     * @return a leather armor builder created from the given item
     */
    LeatherArmorBuilder createLeatherArmor(ItemStack itemStack, ItemMeta meta);

    /**
     * Create a glass pane with the specified color
     * @param color the color to apply to the pane
     * @return an item builder for the glass pane
     */
    SimpleBuilder createGlassPane(DyeColor color);

    /**
     * Create a glass block with the specified color
     * @param color the color to apply to the block
     * @return an item builder for the glass block
     */
    SimpleBuilder createGlass(DyeColor color);
}
