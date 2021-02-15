package net.nowtryz.mcutils.builder.api;

import net.nowtryz.mcutils.api.Translation;
import org.bukkit.DyeColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface ItemBuilder<T extends ItemBuilder<T>> extends Cloneable {
    /**
     * Sets the display name.
     *
     * @param name the name to set
     * @return this ItemBuilder
     */
    T setDisplayName(String name);

    /**
     * Sets the display name.
     *
     * @param translation the name to set
     * @param values values for the translation
     * @return this ItemBuilder
     */
    T setDisplayName(Translation translation, Object... values);

    /**
     * Set the name of the item to a space, to hide it
     * @return this ItemBuilder
     */
    T dropName();

    /**
     * Sets the localized name.
     *
     * @param name the name to set
     * @return this ItemBuilder
     */
    T setLocalizedName(String name);

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     * @return this ItemBuilder
     */
    T setLore(List<String> lore);

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param translation the lore that will be set
     * @param values values for the translation
     * @return this ItemBuilder
     */
    T setLore(Translation translation, Object... values);

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client. This Method does silently ignore double set itemFlags.
     *
     * @param itemFlags The hide flags which shouldn't be rendered
     * @return this ItemBuilder
     */
    T addItemFlags(ItemFlag... itemFlags);

    /**
     * Ignore all properties while rendering on the client
     * @return this ItemBuilder
     */
    T addAllItemFlags();

    /**
     * Adds the specified enchantment to this item meta.
     *
     * @param enchantment Enchantment to add
     * @param level Level for the enchantment
     * @param ignoreLevelRestriction this indicates the enchantment should be
     *     applied, ignoring the level limit
     * @return this ItemBuilder
     */
    T addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction);

    /**
     * Make the item glowing if the provided condition is true
     * @param glowing the condition
     * @return this ItemBuilder
     */
    T setGlowing(boolean glowing);

    /**
     * Make the item glowing
     * @return this ItemBuilder
     */
    T setGlowing();

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     *
     * @param unbreakable true if set unbreakable
     * @return this ItemBuilder
     */
    T setUnbreakable(boolean unbreakable);

    /**
     * Removes the specified enchantment from this item meta.
     *
     * @param enchantment Enchantment to remove
     * @return this ItemBuilder
     */
    T removeEnchant(Enchantment enchantment);

    /**
     * Remove specific set of itemFlags. This tells the Client it should render it again. This Method does silently ignore double removed itemFlags.
     *
     * @param itemFlags Hide flags which should be removed
     * @return this ItemBuilder
     */
    T removeItemFlags(ItemFlag... itemFlags);

    /**
     * Remove all enchantements added to this item
     * @return this ItemBuilder
     */
    T clearEnchants();

    /**
     * Sets the amount of the item stack
     *
     * @param amount stack size
     * @return this ItemBuilder
     */
    T setAmount(int amount);

    /**
     * Sets the durability of the item stack
     *
     * @param damage durability / damage
     * @return this ItemBuilder
     */
    T setDurability(short damage);

    /**
     * Sets the color of this item
     * @param color the color to use
     * @return this ItemBuilder
     */
    T setColor(DyeColor color);

    T setWoolColor(DyeColor color);

    T setDyeColor(DyeColor color);

    /**
     * Convert this builder to a monster egg builder
     * @return a monster egg builder
     */
    MonterEggBuilder toEgg();

    /**
     * Convent this builder to a skull builder
     * @return a skull builder
     */
    SkullBuilder toSkull();

    // Build item

    /**
     * Builds the item
     * @return the built item
     */
    ItemStack build();

    /**
     * Builds the item
     * /!\ Create a copy of the initial item
     *
     * @param data the data value or null
     * @deprecated this method uses an ambiguous data byte object
     * @return the built item
     */
    @Deprecated
    ItemStack build(Byte data);

    interface DecorableItemBuilder<M extends ItemMeta, T extends ItemBuilder<T>> extends ItemBuilder<T> {
        ItemStack getItem();
        M getMeta();
    }
}
