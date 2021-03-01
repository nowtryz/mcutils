package net.nowtryz.mcutils.builder.internal;

import lombok.NonNull;
import net.nowtryz.mcutils.builder.api.ItemBuilderFactory;
import net.nowtryz.mcutils.builder.api.LeatherArmorBuilder;
import net.nowtryz.mcutils.builder.api.MonterEggBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class ThirteenFactory implements ItemBuilderFactory {
    @Override
    public ThirteenBuilder.SimpleItemBuilder<?> create(Material material) {
        return new ThirteenBuilder.SimpleItemBuilder<>(material, ItemMeta.class);
    }

    @Override
    public ThirteenBuilder.SimpleItemBuilder<?> from(ItemStack item) {
        return new ThirteenBuilder.SimpleItemBuilder<>(item, item.getItemMeta());
    }

    @Override
    public ThirteenBuilder.EggBuilder createEgg() {
        return new ThirteenBuilder.EggBuilder();
    }

    @Override
    public ThirteenBuilder.EggBuilder createEggFrom(ItemStack itemStack) {
        itemStack.setType(Material.SKELETON_SPAWN_EGG);
        return new ThirteenBuilder.EggBuilder(itemStack, (SpawnEggMeta) itemStack.getItemMeta());
    }

    @Override
    public MonterEggBuilder createEggFrom(ItemStack itemStack, ItemMeta meta) {
        itemStack.setItemMeta(meta);
        if (!itemStack.getType().name().endsWith("_SPAWN_EGG")) itemStack.setType(Material.SKELETON_SPAWN_EGG);
        return new ThirteenBuilder.EggBuilder(itemStack, (SpawnEggMeta) itemStack.getItemMeta());
    }

    @Override
    public net.nowtryz.mcutils.builder.api.SkullBuilder createSkull() {
        return new SkullBuilder(new ThirteenBuilder.SimpleItemBuilder<>(Material.PLAYER_HEAD, SkullMeta.class));
    }

    @Override
    public net.nowtryz.mcutils.builder.api.SkullBuilder createSkull(ItemStack itemStack, ItemMeta meta) {
        itemStack.setItemMeta(meta);
        itemStack.setType(Material.PLAYER_HEAD);

        return new SkullBuilder(new ThirteenBuilder.SimpleItemBuilder<>(itemStack, (SkullMeta) itemStack.getItemMeta()));
    }

    @Override
    public LeatherArmorBuilder createLeatherArmor(Material armor) {
        switch (armor) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return new ThirteenBuilder.SimpleLeatherArmorBuilder(armor);
            default:
                throw new IllegalArgumentException("The given material must be a piece of leather armor, got " + armor);
        }
    }

    @Override
    public LeatherArmorBuilder createLeatherArmor(ItemStack itemStack, ItemMeta meta) {
        switch (itemStack.getType()) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                itemStack.setItemMeta(meta);
                return new ThirteenBuilder.SimpleLeatherArmorBuilder(itemStack, (LeatherArmorMeta) itemStack.getItemMeta());
            default:
                throw new IllegalArgumentException("The given material must be a piece of leather armor, got " + itemStack.getType());
        }
    }

    @Override
    public ThirteenBuilder.SimpleItemBuilder<?> createGlassPane(@NonNull DyeColor color) {
        return create(Material.valueOf(color.name() + "_STAINED_GLASS_PANE"));
    }

    @Override
    public ThirteenBuilder.SimpleItemBuilder<?> createGlass(@NonNull DyeColor color) {
        return create(Material.valueOf(color.name() + "_STAINED_GLASS"));
    }
}
