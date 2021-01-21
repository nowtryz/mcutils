package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.builder.ItemBuilder;
import net.nowtryz.mcutils.builder.MonterEggBuilder;
import net.nowtryz.mcutils.builder.SimpleBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;

abstract class BellowThirteenBuilder<M extends ItemMeta, T extends ItemBuilder<T>> extends AbstractItemBuilder<M,T> {
    BellowThirteenBuilder(Material material, Class<M> metaClass) {
        super(material, metaClass);
    }

    BellowThirteenBuilder(@NotNull ItemStack item, M itemMeta) {
        super(item, itemMeta);
    }

    @Override
    public T setColor(DyeColor color) {
        return this.setWoolColor(color);
    }

    @Override
    @SuppressWarnings("deprecation")
    public T setWoolColor(DyeColor color) {
        return this.setDurability(color.getWoolData());
    }

    @Override
    @SuppressWarnings("deprecation")
    public T setDyeColor(DyeColor color) {
        return this.setDurability(color.getDyeData());
    }

    static class SimpleItemBuilder<M extends ItemMeta> extends BellowThirteenBuilder<M, SimpleBuilder> implements SimpleBuilder {
        SimpleItemBuilder(Material material, Class<M> metaClass) {
            super(material, metaClass);
        }

        SimpleItemBuilder(@NotNull ItemStack item, M itemMeta) {
            super(item, itemMeta);
        }

        @Override
        SimpleItemBuilder<M> self() {
            return this;
        }
    }

    static class BellowThirteenMonsterEggBuilder extends BellowThirteenBuilder<SpawnEggMeta, MonterEggBuilder> implements MonterEggBuilder {

        BellowThirteenMonsterEggBuilder() {
            super(Material.valueOf("MONSTER_EGG"), SpawnEggMeta.class);
        }

        BellowThirteenMonsterEggBuilder(ItemStack item, SpawnEggMeta meta) {
            super(item, meta);
        }

        @Override
        MonterEggBuilder self() {
            return this;
        }

        @Override
        public MonterEggBuilder toEgg() {
            return this;
        }

        @Override
        @SuppressWarnings("deprecation")
        public MonterEggBuilder setSpawnedType(EntityType type) {
            this.itemMeta.setSpawnedType(type);
            return this;
        }
    }
}
