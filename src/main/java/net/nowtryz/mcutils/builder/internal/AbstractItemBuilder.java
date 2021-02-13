package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.api.Translation;
import net.nowtryz.mcutils.builder.ItemBuilder;
import net.nowtryz.mcutils.builder.ItemBuilderFactory;
import net.nowtryz.mcutils.builder.MonterEggBuilder;
import net.nowtryz.mcutils.builder.SkullBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

abstract class AbstractItemBuilder<M extends ItemMeta, T extends ItemBuilder<T>> implements ItemBuilder.DecorableItemBuilder<M,T> {
    protected ItemStack itemStack;
    protected M itemMeta;

    protected AbstractItemBuilder(Material material, Class<M> metaClass) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = metaClass.cast(this.itemStack.getItemMeta());
    }

    protected AbstractItemBuilder(@NotNull ItemStack item, M itemMeta) {
        this.itemStack = Objects.requireNonNull(item);
        this.itemMeta = Objects.requireNonNull(itemMeta);
    }

    abstract T self();

    @Override
    public ItemStack getItem() {
        return this.itemStack;
    }

    @Override
    public M getMeta() {
        return this.itemMeta;
    }

    @Override
    public T setDisplayName(String name) {
        this.itemMeta.setDisplayName(name);
        return self();
    }

    @Override
    public T setDisplayName(Translation translation, Object... values) {
        this.itemMeta.setDisplayName(translation.get(values));
        return self();
    }

    @Override
    public T dropName() {
        return self().setDisplayName(ChatColor.WHITE.toString());
    }

    @Override
    public T setLocalizedName(String name) {
        this.itemMeta.setLocalizedName(name);
        return self();
    }

    @Override
    public T setLore(List<String> lore) {
        this.itemMeta.setLore(lore);
        return self();
    }

    @Override
    public T setLore(Translation translation, Object... args) {
        this.itemMeta.setLore(Arrays.asList(translation.get(args).split(StringUtils.LF)));
        return self();
    }

    @Override
    public T addItemFlags(ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);
        return self();
    }

    @Override
    public T addAllItemFlags() {
        this.itemMeta.addItemFlags(ItemFlag.values());
        return self();
    }

    @Override
    public T addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        this.itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return self();
    }

    @Override
    public T setGlowing(boolean glowing) {
        if (glowing) this
                .addEnchant(Enchantment.KNOCKBACK, 1, false)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return self();
    }

    @Override
    public T setGlowing() {
        return setGlowing(true);
    }

    @Override
    public T setUnbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return self();
    }

    @Override
    public T removeEnchant(Enchantment enchantment) {
        this.itemMeta.removeEnchant(enchantment);
        return self();
    }

    @Override
    public T removeItemFlags(ItemFlag... itemFlags) {
        this.itemMeta.removeItemFlags(itemFlags);
        return self();
    }

    @Override
    public T clearEnchants() {
        this.itemMeta.getEnchants().keySet().forEach(this.itemMeta::removeEnchant);
        return self();
    }

    @Override
    public T setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return self();
    }

    @Override
    public MonterEggBuilder toEgg() {
        return ItemBuilderFactory.FACTORY.createEggFrom(this.itemStack, this.itemMeta);
    }

    @Override
    public SkullBuilder toSkull() {
        return ItemBuilderFactory.FACTORY.createSkull(this.itemStack, this.itemMeta);
    }

    // Build item

    @Override
    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return itemStack;
    }

    @Override
    @Deprecated
    public ItemStack build(Byte data) {
        ItemStack itemStack = new ItemStack(this.itemStack.getType(), this.itemStack.getAmount(), this.itemStack.getDurability(), data);
        itemStack.setItemMeta(this.itemMeta);
        return itemStack;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractItemBuilder<M,T> clone() {
        try {
            AbstractItemBuilder<M,T> clone = (AbstractItemBuilder<M,T>) super.clone();
            if (this.itemStack != null) clone.itemStack = this.itemStack.clone();
            if (this.itemMeta != null) clone.itemMeta = (M) this.itemMeta.clone();
            return clone;
        } catch (CloneNotSupportedException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
