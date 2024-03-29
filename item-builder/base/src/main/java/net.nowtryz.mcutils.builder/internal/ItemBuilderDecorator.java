package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.api.Translation;
import net.nowtryz.mcutils.builder.api.ItemBuilder;
import net.nowtryz.mcutils.builder.api.SkullBuilder;
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

abstract class ItemBuilderDecorator<T extends ItemBuilder> implements ItemBuilder {
    protected DecorableItemBuilder delegate;

    ItemBuilderDecorator(DecorableItemBuilder delegate) {
        this.delegate = delegate;
    }

    public abstract T self();

    @Override
    public T setDisplayName(String name) {
        this.delegate.setDisplayName(name);
        return self();
    }

    @Override
    public T setDisplayName(Translation translation, Object... values) {
        this.delegate.setDisplayName(translation, values);
        return self();
    }

    @Override
    public T dropName() {
        this.delegate.dropName();
        return self();
    }

    @Override
    public T setLocalizedName(String name) {
        this.delegate.setLocalizedName(name);
        return self();
    }

    @Override
    public T setLore(List<String> lore) {
        this.delegate.setLore(lore);
        return self();
    }

    @Override
    public T setLore(Translation translation, Object... args) {
        this.delegate.setLore(translation, args);
        return self();
    }

    @Override
    public T setLore(String lore) {
        this.delegate.setLore(lore);
        return self();
    }

    @Override
    public T addItemFlags(ItemFlag... itemFlags) {
        this.delegate.addItemFlags(itemFlags);
        return self();
    }

    @Override
    public T addAllItemFlags() {
        this.delegate.addAllItemFlags();
        return self();
    }

    @Override
    public T addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        this.delegate.addEnchant(enchantment, level, ignoreLevelRestriction);
        return self();
    }

    @Override
    public T setGlowing(boolean glowing) {
        this.delegate.setGlowing(glowing);
        return self();
    }

    @Override
    public T setGlowing() {
        this.delegate.setGlowing();
        return self();
    }

    @Override
    public T setUnbreakable(boolean unbreakable) {
        this.delegate.setUnbreakable(unbreakable);
        return self();
    }

    @Override
    public T removeEnchant(Enchantment enchantment) {
        this.delegate.removeEnchant(enchantment);
        return self();
    }

    @Override
    public T removeItemFlags(ItemFlag... itemFlags) {
        this.delegate.removeItemFlags(itemFlags);
        return self();
    }

    @Override
    public T clearEnchants() {
        this.delegate.clearEnchants();
        return self();
    }

    @Override
    public T setAmount(int amount) {
        this.delegate.setAmount(amount);
        return self();
    }

    @Override
    public T setDurability(short damage) {
        this.delegate.setDurability(damage);
        return self();
    }

    @Override
    public T setColor(DyeColor color) {
        this.delegate.setColor(color);
        return self();
    }

    @Override
    public T setWoolColor(DyeColor color) {
        this.delegate.setWoolColor(color);
        return self();
    }

    @Override
    public T setDyeColor(DyeColor color) {
        this.delegate.setDyeColor(color);
        return self();
    }

    @Override
    public T setSpawnedType(EntityType type) {
        this.delegate.setSpawnedType(type);
        return self();
    }

    @Override
    public T setLeatherColor(Color color) {
        this.delegate.setLeatherColor(color);
        return self();
    }

    @Override
    public ItemBuilder setPotionType(PotionType type) {
        this.delegate.setPotionType(type);
        return self();
    }

    @Override
    public <M extends ItemMeta> T asMeta(Class<M> metaClass, Consumer<M> metaConsumer) {
        this.delegate.asMeta(metaClass, metaConsumer);
        return self();
    }

    @Override
    public ItemBuilder toEgg() {
        return this.delegate.toEgg();
    }

    @Override
    public SkullBuilder toSkull() {
        return this.delegate.toSkull();
    }

    @Override
    public ItemStack build() {
        return this.delegate.build();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack build(Byte data) {
        return this.delegate.build(data);
    }
}
