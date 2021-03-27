package net.nowtryz.mcutils.builder.api;

import net.nowtryz.mcutils.api.Translation;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.function.Consumer;

public interface ItemBuilder extends Cloneable {
    /**
     * Sets the display name.
     *
     * @param name the name to set
     * @return this ItemBuilder
     */
    ItemBuilder setDisplayName(String name);

    /**
     * Sets the display name.
     *
     * @param translation the name to set
     * @param values values for the translation
     * @return this ItemBuilder
     */
    ItemBuilder setDisplayName(Translation translation, Object... values);

    /**
     * Set the name of the item to a space, to hide it
     * @return this ItemBuilder
     */
    ItemBuilder dropName();

    /**
     * Sets the localized name.
     *
     * @param name the name to set
     * @return this ItemBuilder
     */
    ItemBuilder setLocalizedName(String name);

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     * @return this ItemBuilder
     */
    ItemBuilder setLore(List<String> lore);

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param translation the lore that will be set
     * @param values values for the translation
     * @return this ItemBuilder
     */
    ItemBuilder setLore(Translation translation, Object... values);

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set. This lore is split on each line to create a new line on the item
     * @return this ItemBuilder
     */
    ItemBuilder setLore(String lore);

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client. This Method does silently ignore double set itemFlags.
     *
     * @param itemFlags The hide flags which shouldn't be rendered
     * @return this ItemBuilder
     */
    ItemBuilder addItemFlags(ItemFlag... itemFlags);

    /**
     * Ignore all properties while rendering on the client
     * @return this ItemBuilder
     */
    ItemBuilder addAllItemFlags();

    /**
     * Adds the specified enchantment to this item meta.
     *
     * @param enchantment Enchantment to add
     * @param level Level for the enchantment
     * @param ignoreLevelRestriction this indicates the enchantment should be
     *     applied, ignoring the level limit
     * @return this ItemBuilder
     */
    ItemBuilder addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction);

    /**
     * Make the item glowing if the provided condition is true
     * @param glowing the condition
     * @return this ItemBuilder
     */
    ItemBuilder setGlowing(boolean glowing);

    /**
     * Make the item glowing
     * @return this ItemBuilder
     */
    ItemBuilder setGlowing();

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     *
     * @param unbreakable true if set unbreakable
     * @return this ItemBuilder
     */
    ItemBuilder setUnbreakable(boolean unbreakable);

    /**
     * Removes the specified enchantment from this item meta.
     *
     * @param enchantment Enchantment to remove
     * @return this ItemBuilder
     */
    ItemBuilder removeEnchant(Enchantment enchantment);

    /**
     * Remove specific set of itemFlags. This tells the Client it should render it again. This Method does silently ignore double removed itemFlags.
     *
     * @param itemFlags Hide flags which should be removed
     * @return this ItemBuilder
     */
    ItemBuilder removeItemFlags(ItemFlag... itemFlags);

    /**
     * Remove all enchantements added to this item
     * @return this ItemBuilder
     */
    ItemBuilder clearEnchants();

    /**
     * Sets the amount of the item stack
     *
     * @param amount stack size
     * @return this ItemBuilder
     */
    ItemBuilder setAmount(int amount);

    /**
     * Sets the durability of the item stack
     *
     * @param damage durability / damage
     * @return this ItemBuilder
     */
    ItemBuilder setDurability(short damage);

    /**
     * Sets the color of this item
     * @param color the color to use
     * @return this ItemBuilder
     */
    ItemBuilder setColor(DyeColor color);

    ItemBuilder setWoolColor(DyeColor color);

    ItemBuilder setDyeColor(DyeColor color);

    /**
     * Set the type of entity this egg will spawn (if this is an egg).
     * @param type The entity type. May be null for implementation specific default.
     * @return this builder
     */
    ItemBuilder setSpawnedType(EntityType type);

    /**
     * Set the leather color of this piece of armor
     * @param color the color to set
     * @throws IllegalArgumentException if the material is not a leather armor piece
     * @return this builder
     */
    ItemBuilder setLeatherColor(Color color);


    /**
     * Set the base potion effect if this item is a potion
     * @param type the type to apply
     * @return this builder
     */
    ItemBuilder setPotionType(PotionType type);

    /**
     * Directly used the meta held by this item builder if it is an instance of the given meta class
     * @param metaClass the meta class to use
     * @param metaConsumer the action to run if the meta match the given class
     * @param <T> the generic type of the class
     * @return this builder
     */
    <T extends ItemMeta> ItemBuilder asMeta(Class<T> metaClass, Consumer<T> metaConsumer);

    /**
     * Convert this builder to a monster egg builder
     * @return a monster egg builder
     */
    ItemBuilder toEgg();

    /**
     * Convert this builder to a skull builder
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

    interface DecorableItemBuilder extends ItemBuilder {
        ItemStack getItem();
        ItemMeta getMeta();
    }
}
