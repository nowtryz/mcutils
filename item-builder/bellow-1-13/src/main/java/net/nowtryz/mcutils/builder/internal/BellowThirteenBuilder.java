package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.builder.api.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;

class BellowThirteenBuilder extends AbstractItemBuilder<BellowThirteenBuilder> {
    BellowThirteenBuilder(Material material) {
        super(material);
    }

    BellowThirteenBuilder(@NotNull ItemStack item, ItemMeta itemMeta) {
        super(item, itemMeta);
    }

    @Override
    BellowThirteenBuilder self() {
        return this;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BellowThirteenBuilder setColor(DyeColor color) {
        if (this.itemMeta instanceof BannerMeta) return this.asMeta(BannerMeta.class, meta -> meta.setBaseColor(color));
        else return this.setWoolColor(color);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BellowThirteenBuilder setWoolColor(DyeColor color) {
        return this.setDurability(color.getWoolData());
    }

    @Override
    @SuppressWarnings("deprecation")
    public BellowThirteenBuilder setDyeColor(DyeColor color) {
        return this.setDurability(color.getDyeData());
    }

    @Override
    public BellowThirteenBuilder setSpawnedType(EntityType type) {
        this.asMeta(SpawnEggMeta.class, meta -> meta.setSpawnedType(type));
        return this;
    }

    @Override
    public BellowThirteenBuilder toEgg() {
        this.itemStack.setItemMeta(this.itemMeta);
        this.itemStack.setType(Material.MONSTER_EGG);
        this.itemMeta = this.itemStack.getItemMeta();
        return this;
    }

    @Override
    public BellowThirteenBuilder setDurability(short damage) {
        this.itemStack.setDurability(damage);
        return this.self();
    }

    static class SimpleSkullBuilder extends SkullBuilder {

        SimpleSkullBuilder(DecorableItemBuilder delegate) {
            super(delegate);
        }

        @Override
        public SkullBuilder setOwningPlayer(OfflinePlayer player) {
            return this.setName(player.getName());
        }

        @Override
        @SuppressWarnings("deprecation")
        public SkullBuilder setName(String name) {
            return this.asMeta(SkullMeta.class, meta -> meta.setOwner(name));
        }
    }
}
