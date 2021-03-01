package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.builder.api.ItemBuilderFactory;
import net.nowtryz.mcutils.builder.api.LeatherArmorBuilder;
import net.nowtryz.mcutils.builder.api.MonterEggBuilder;
import net.nowtryz.mcutils.builder.api.SimpleBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class BellowThirteenFactory implements ItemBuilderFactory {

    @Override
    public BellowThirteenBuilder.SimpleItemBuilder<?> create(Material material) {
        return new BellowThirteenBuilder.SimpleItemBuilder<>(material, ItemMeta.class);
    }

    @Override
    public SimpleBuilder from(ItemStack item) {
        return new BellowThirteenBuilder.SimpleItemBuilder<>(item, item.getItemMeta());
    }

    @Override
    public MonterEggBuilder createEgg() {
        return new BellowThirteenBuilder.SimpleMonsterEggBuilder();
    }

    @Override
    public MonterEggBuilder createEggFrom(ItemStack itemStack) {
        if (Material.MONSTER_EGG != itemStack.getType()) {
            throw new IllegalArgumentException("Provided item isn't an egg");
        }

        return new BellowThirteenBuilder.SimpleMonsterEggBuilder(itemStack, (SpawnEggMeta) itemStack.getItemMeta());
    }

    @Override
    public MonterEggBuilder createEggFrom(ItemStack itemStack, ItemMeta meta) {
        itemStack.setItemMeta(meta);
        itemStack.setType(Material.MONSTER_EGG);
        return new BellowThirteenBuilder.SimpleMonsterEggBuilder(itemStack, (SpawnEggMeta) itemStack.getItemMeta());
    }

    @Override
    public net.nowtryz.mcutils.builder.api.SkullBuilder createSkull() {
        SkullBuilder builder = new SkullBuilder(new BellowThirteenBuilder.SimpleItemBuilder<>(Material.valueOf("SKULL_ITEM"), SkullMeta.class));
        builder.setDurability((short) SkullType.PLAYER.ordinal());
        return builder;
    }

    @Override
    public net.nowtryz.mcutils.builder.api.SkullBuilder createSkull(ItemStack itemStack, ItemMeta meta) {
        itemStack.setItemMeta(meta);
        itemStack.setType(Material.SKULL_ITEM);

        SkullBuilder builder = new SkullBuilder(new BellowThirteenBuilder.SimpleItemBuilder<>(itemStack, (SkullMeta) itemStack.getItemMeta()));
        builder.setDurability((short) SkullType.PLAYER.ordinal());
        return builder;
    }

    @Override
    public LeatherArmorBuilder createLeatherArmor(Material armor) {
        switch (armor) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return new BellowThirteenBuilder.SimpleLeatherArmorBuilder(armor);
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
                return new BellowThirteenBuilder.SimpleLeatherArmorBuilder(itemStack, (LeatherArmorMeta) itemStack.getItemMeta());
            default:
                throw new IllegalArgumentException("The given material must be a piece of leather armor, got " + itemStack.getType());
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public SimpleBuilder createGlassPane(DyeColor color) {
        return create(Material.STAINED_GLASS_PANE).setDurability(color.getWoolData());
    }

    @Override
    @SuppressWarnings("deprecation")
    public SimpleBuilder createGlass(DyeColor color) {
        return create(Material.STAINED_GLASS).setDurability(color.getWoolData());
    }
}
