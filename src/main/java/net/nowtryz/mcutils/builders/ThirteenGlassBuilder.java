package net.nowtryz.mcutils.builders;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class ThirteenGlassBuilder {
    public static ItemBuilder<ItemMeta> createGlassPane(DyeColor color) {
        return ItemBuilder.create(Material.valueOf(color.name() + "_STAINED_GLASS_PANE"));
    }

    public static ItemBuilder<ItemMeta> createGlass(DyeColor color) {
        return ItemBuilder.create(Material.valueOf(color.name() + "_STAINED_GLASS"));
    }
}
