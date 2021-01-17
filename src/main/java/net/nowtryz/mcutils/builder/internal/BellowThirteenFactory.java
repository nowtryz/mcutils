package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.builder.ItemBuilderFactory;
import net.nowtryz.mcutils.builder.MonterEggBuilder;
import net.nowtryz.mcutils.builder.SimpleBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class BellowThirteenFactory implements ItemBuilderFactory {
    private static final Material EGG = Material.valueOf("MONSTER_EGG");

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
        return new BellowThirteenBuilder.BellowThirteenMonsterEggBuilder();
    }

    @Override
    public MonterEggBuilder createEggFrom(ItemStack itemStack) {
        if (EGG != itemStack.getType()) {
            throw new IllegalArgumentException("Provided item isn't an egg");
        }

        return new BellowThirteenBuilder.BellowThirteenMonsterEggBuilder(itemStack, (SpawnEggMeta) itemStack.getItemMeta());
    }

    @Override
    public MonterEggBuilder createEggFrom(ItemStack itemStack, ItemMeta meta) {
        itemStack.setItemMeta(meta);
        itemStack.setType(Material.valueOf("MONSTER_EGG"));
        return new BellowThirteenBuilder.BellowThirteenMonsterEggBuilder(itemStack, (SpawnEggMeta) itemStack.getItemMeta());
    }

    @Override
    @SuppressWarnings("deprecation")
    public net.nowtryz.mcutils.builder.SkullBuilder createSkull() {
        SkullBuilder builder = new SkullBuilder(new BellowThirteenBuilder.SimpleItemBuilder<>(Material.valueOf("SKULL_ITEM"), SkullMeta.class));
        builder.setDurability((short) SkullType.PLAYER.ordinal());
        return builder;
    }

    @Override
    @SuppressWarnings("deprecation")
    public net.nowtryz.mcutils.builder.SkullBuilder createSkull(ItemStack itemStack, ItemMeta meta) {
        itemStack.setItemMeta(meta);
        itemStack.setType(Material.valueOf("SKULL_ITEM"));

        SkullBuilder builder = new SkullBuilder(new BellowThirteenBuilder.SimpleItemBuilder<>(itemStack, (SkullMeta) itemStack.getItemMeta()));
        builder.setDurability((short) SkullType.PLAYER.ordinal());
        return builder;
    }

    @Override
    @SuppressWarnings("deprecation")
    public SimpleBuilder createGlassPane(DyeColor color) {
        return create(Material.valueOf("STAINED_GLASS_PANE")).setDurability(color.getWoolData());
    }

    @Override
    @SuppressWarnings("deprecation")
    public SimpleBuilder createGlass(DyeColor color) {
        return create(Material.valueOf("STAINED_GLASS")).setDurability(color.getWoolData());
    }
}
