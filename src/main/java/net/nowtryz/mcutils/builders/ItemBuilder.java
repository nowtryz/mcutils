package net.nowtryz.mcutils.builders;

import net.nowtryz.mcutils.api.Translation;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemBuilder<M extends ItemMeta> implements Cloneable {
    protected ItemStack itemStack;
    protected M itemMeta;

    // initializers

    /**
     *
     * @param material the material of the item
     * @return an item builder for the specified material
     */
    public static ItemBuilder<ItemMeta> create(Material material) {
        return new ItemBuilder<>(material, ItemMeta.class);
    }

    /**
     * Creates a builder to edit an exiting item
     * @param item the initial item
     * @return the itembuilder
     */
    public static ItemBuilder<ItemMeta> from(ItemStack item) {
        return new ItemBuilder<>(item, item.getItemMeta());
    }

    /**
     * Create a monster egg
     * @return an item builder for monster egg
     */
    public static MonsterEggBuilder createEgg() {
        return new MonsterEggBuilder();
    }

    /**
     * Create an item builder from the provided item
     * @param itemStack the item to edit
     * @return an item builder for monster egg
     * @throws IllegalArgumentException if the provided item is not a {@link Material#MONSTER_EGG}
     */
    public static MonsterEggBuilder createEggFrom(ItemStack itemStack) {
        if (Material.MONSTER_EGG !=itemStack.getType()) {
            throw new IllegalArgumentException("Provided item isn't an egg");
        }

        return new MonsterEggBuilder(itemStack, (SpawnEggMeta) itemStack.getItemMeta());
    }

    public static SkullBuilder createSkull() {
        return new SkullBuilder();
    }

    public static SkullBuilder skullForPlayer(OfflinePlayer player) {
        return new SkullBuilder().setOwningPlayer(player);
    }

    @SuppressWarnings("deprecation")
    public static ItemBuilder<ItemMeta> createGlassPane(DyeColor color) {
        return create(Material.STAINED_GLASS_PANE).setDurability(color.getWoolData());
    }

    // Constructor

    protected ItemBuilder(Material material, Class<M> metaClass) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = metaClass.cast(this.itemStack.getItemMeta());
    }

    protected ItemBuilder(@NotNull ItemStack item, M itemMeta) {
        this.itemStack = Objects.requireNonNull(item);
        this.itemMeta = Objects.requireNonNull(itemMeta);
    }

    // Setters

    /**
     * Sets the display name.
     *
     * @param name the name to set
     * @return this ItemBuilder
     */
    public ItemBuilder<M> setDisplayName(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    /**
     * Sets the display name.
     *
     * @param translation the name to set
     * @return this ItemBuilder
     */
    public ItemBuilder<M> setDisplayName(Translation translation, Object... values) {
        this.itemMeta.setDisplayName(translation.get(values));
        return this;
    }

    /**
     * Set the name of the item to a space, to hide it
     * @return this ItemBuilder
     */
    public ItemBuilder<M> dropName() {
        return this.setDisplayName(ChatColor.WHITE.toString());
    }

    /**
     * Sets the localized name.
     *
     * @param name the name to set
     * @return this ItemBuilder
     */
    public ItemBuilder<M> setLocalizedName(String name) {
        this.itemMeta.setLocalizedName(name);
        return this;
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     * @return this ItemBuilder
     */
    public ItemBuilder<M> setLore(List<String> lore) {
        this.itemMeta.setLore(lore);
        return this;
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param translation the lore that will be set
     * @return this ItemBuilder
     */
    public ItemBuilder<M> setLore(Translation translation, Object... args) {
        this.itemMeta.setLore(Arrays.asList(translation.get(args).split(StringUtils.LF)));
        return this;
    }

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client. This Method does silently ignore double set itemFlags.
     *
     * @param itemFlags The hide flags which shouldn't be rendered
     * @return this ItemBuilder
     */
    public ItemBuilder<M> addItemFlags(ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);
        return this;
    }

    /**
     * Adds the specified enchantment to this item meta.
     *
     * @param enchantment Enchantment to add
     * @param level Level for the enchantment
     * @param ignoreLevelRestriction this indicates the enchantment should be
     *     applied, ignoring the level limit
     * @return this ItemBuilder
     */
    public ItemBuilder<M> addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        this.itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    /**
     * Make the item glowing if the provided condition is true
     * @param glowing the condition
     * @return this ItemBuilder
     */
    public ItemBuilder<M> setGlowing(boolean glowing) {
        if (glowing) this
                    .addEnchant(Enchantment.KNOCKBACK, 1, false)
                    .addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     *
     * @param unbreakable true if set unbreakable
     * @return this ItemBuilder
     */
    public ItemBuilder<M> setUnbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Removes the specified enchantment from this item meta.
     *
     * @param enchantment Enchantment to remove
     * @return this ItemBuilder
     */
    public ItemBuilder<M> removeEnchant(Enchantment enchantment) {
        this.itemMeta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Remove specific set of itemFlags. This tells the Client it should render it again. This Method does silently ignore double removed itemFlags.
     *
     * @param itemFlags Hide flags which should be removed
     * @return this ItemBuilder
     */
    public ItemBuilder<M> removeItemFlags(ItemFlag... itemFlags) {
        this.itemMeta.removeItemFlags(itemFlags);
        return this;
    }

    /**
     * Remove all enchantements added to this item
     * @return this ItemBuilder
     */
    public ItemBuilder<M> clearEnchants() {
        this.itemMeta.getEnchants().keySet().forEach(this.itemMeta::removeEnchant);
        return this;
    }

    /**
     * Sets the amount of the item stack
     *
     * @param amount stack size
     * @return this ItemBuilder
     */
    public ItemBuilder<M> setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    /**
     * Sets the durability of the item stack
     *
     * @param damage durability / damage
     * @return this ItemBuilder
     */
    public ItemBuilder<M> setDurability(short damage) {
        this.itemStack.setDurability(damage);
        return this;
    }

    // Build item

    /**
     * Builds the item
     */
    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return itemStack;
    }

    /**
     * Builds the item
     * /!\ Create a copy of the initial item
     *
     * @param data the data value or null
     * @deprecated this method uses an ambiguous data byte object
     */
    @Deprecated
    public ItemStack build(Byte data) {
        ItemStack itemStack = new ItemStack(this.itemStack.getType(), this.itemStack.getAmount(), this.itemStack.getDurability(), data);
        itemStack.setItemMeta(this.itemMeta);
        return itemStack;
    }

    @SuppressWarnings("unchecked")
    public ItemBuilder<ItemMeta> clone() {
        try {
            ItemBuilder<ItemMeta> clone = (ItemBuilder<ItemMeta>) super.clone();
            if (this.itemStack != null) clone.itemStack = this.itemStack.clone();
            if (this.itemMeta != null) clone.itemMeta = this.itemMeta.clone();
            return clone;
        } catch (CloneNotSupportedException exception) {
            throw new Error(exception);
        }
    }
}
