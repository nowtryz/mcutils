package net.nowtryz.mcutils.builders;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class BellowThirteenGlassBuilder {
    @SuppressWarnings("deprecation")
    public static ItemBuilder<ItemMeta> createGlassPane(DyeColor color) {
        return ItemBuilder.create(Material.valueOf("STAINED_GLASS_PANE")).setDurability(color.getWoolData());
    }

    @SuppressWarnings("deprecation")
    public static ItemBuilder<ItemMeta> createGlass(DyeColor color) {
        return ItemBuilder.create(Material.valueOf("STAINED_GLASS")).setDurability(color.getWoolData());
    }
}
