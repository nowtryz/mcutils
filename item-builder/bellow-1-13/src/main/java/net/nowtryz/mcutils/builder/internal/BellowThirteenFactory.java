package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.builder.api.ItemBuilderFactory;
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
    public BellowThirteenBuilder create(Material material) {
        return new BellowThirteenBuilder(material);
    }

    @Override
    public BellowThirteenBuilder from(ItemStack item) {
        return new BellowThirteenBuilder(item, item.getItemMeta());
    }

    @Override
    public BellowThirteenBuilder createEgg() {
        return new BellowThirteenBuilder(Material.MONSTER_EGG);
    }

    @Override
    public BellowThirteenBuilder createEggFrom(ItemStack itemStack) {
        if (Material.MONSTER_EGG != itemStack.getType()) {
            itemStack.setType(Material.MONSTER_EGG);
        }

        return new BellowThirteenBuilder(itemStack,itemStack.getItemMeta());
    }

    @Override
    public SkullBuilder createSkull() {
        return new BellowThirteenBuilder.SimpleSkullBuilder(new BellowThirteenBuilder(Material.SKULL_ITEM))
                                        .setDurability((short) SkullType.PLAYER.ordinal());
    }

    @Override
    public SkullBuilder createSkull(ItemStack itemStack, ItemMeta meta) {
        itemStack.setItemMeta(meta);
        itemStack.setType(Material.SKULL_ITEM);

        SkullBuilder builder = new BellowThirteenBuilder.SimpleSkullBuilder(new BellowThirteenBuilder(itemStack, itemStack.getItemMeta()));
        builder.setDurability((short) SkullType.PLAYER.ordinal());
        return builder;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BellowThirteenBuilder createGlassPane(DyeColor color) {
        return create(Material.STAINED_GLASS_PANE).setDurability(color.getWoolData());
    }

    @Override
    @SuppressWarnings("deprecation")
    public BellowThirteenBuilder createGlass(DyeColor color) {
        return create(Material.STAINED_GLASS).setDurability(color.getWoolData());
    }
}
