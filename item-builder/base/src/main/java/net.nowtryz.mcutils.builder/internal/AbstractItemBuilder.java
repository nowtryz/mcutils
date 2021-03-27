package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.MCUtils;
import net.nowtryz.mcutils.api.Translation;
import net.nowtryz.mcutils.builder.api.ItemBuilder;
import net.nowtryz.mcutils.builder.api.SkullBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.nowtryz.mcutils.builder.internal.FactoryProvider.FACTORY;

abstract class AbstractItemBuilder<T extends ItemBuilder> implements ItemBuilder.DecorableItemBuilder {
    protected ItemStack itemStack;
    protected ItemMeta itemMeta;

    protected AbstractItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    protected AbstractItemBuilder(@NotNull ItemStack item, ItemMeta itemMeta) {
        this.itemStack = Objects.requireNonNull(item);
        this.itemMeta = Objects.requireNonNull(itemMeta);
    }

    abstract T self();

    @Override
    public ItemStack getItem() {
        return this.itemStack;
    }

    @Override
    public ItemMeta getMeta() {
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
        return this.setDisplayName(ChatColor.WHITE.toString());
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
        this.itemMeta.setLore(Arrays.asList(translation.get(args).split("\n")));
        return self();
    }

    @Override
    public ItemBuilder setLore(String lore) {
        this.itemMeta.setLore(Stream.of(lore)
                .filter(Objects::nonNull)
                .map(s -> s.split("\n"))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList()));
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
    public T setLeatherColor(Color color) {
        this.asMeta(LeatherArmorMeta.class, meta -> meta.setColor(color));
        return self();
    }

    @Override
    @SuppressWarnings("deprecation") // there is no cross-version solution for the moment
    public T setPotionType(PotionType type) {
        return this.asMeta(PotionMeta.class, meta -> {
            if (MCUtils.getBukkitVersion().isLowerThan("1.9.0")) {
                if (type.getEffectType() != null) meta.setMainEffect(type.getEffectType());
            } else meta.setBasePotionData(new PotionData(type));
        });
    }

    @Override
    public <C extends ItemMeta> T asMeta(Class<C> metaClass, Consumer<C> metaConsumer) {
        if (metaClass.isInstance(this.itemMeta)) metaConsumer.accept(metaClass.cast(this.itemMeta));
        return self();
    }

    @Override
    public SkullBuilder toSkull() {
        return FACTORY.createSkull(this.itemStack, this.itemMeta);
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
    public AbstractItemBuilder<T> clone() {
        try {
            AbstractItemBuilder<T> clone = (AbstractItemBuilder<T>) super.clone();
            if (this.itemStack != null) clone.itemStack = this.itemStack.clone();
            if (this.itemMeta != null) clone.itemMeta = this.itemMeta.clone();
            return clone;
        } catch (CloneNotSupportedException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
