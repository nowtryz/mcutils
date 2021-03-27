package net.nowtryz.mcutils.builder.internal;

import lombok.NonNull;
import net.nowtryz.mcutils.builder.api.ItemBuilderFactory;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class ThirteenFactory implements ItemBuilderFactory {
    @Override
    public ThirteenBuilder create(Material material) {
        return new ThirteenBuilder(material);
    }

    @Override
    public ThirteenBuilder from(ItemStack item) {
        return new ThirteenBuilder(item, item.getItemMeta());
    }

    @Override
    public ThirteenBuilder createEgg() {
        return new ThirteenBuilder(Material.SKELETON_SPAWN_EGG);
    }

    @Override
    public ThirteenBuilder createEggFrom(ItemStack itemStack) {
        itemStack.setType(Material.SKELETON_SPAWN_EGG);
        return new ThirteenBuilder(itemStack, itemStack.getItemMeta());
    }

    @Override
    public SkullBuilder createSkull() {
        return new SkullBuilder(new ThirteenBuilder(Material.PLAYER_HEAD));
    }

    @Override
    public SkullBuilder createSkull(ItemStack itemStack, ItemMeta meta) {
        itemStack.setItemMeta(meta);
        itemStack.setType(Material.PLAYER_HEAD);

        return new SkullBuilder(new ThirteenBuilder(itemStack, itemStack.getItemMeta()));
    }

    @Override
    public ThirteenBuilder createGlassPane(@NonNull DyeColor color) {
        return create(Material.valueOf(color.name() + "_STAINED_GLASS_PANE"));
    }

    @Override
    public ThirteenBuilder createGlass(@NonNull DyeColor color) {
        return create(Material.valueOf(color.name() + "_STAINED_GLASS"));
    }
}
