package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.builder.api.ItemBuilder;
import net.nowtryz.mcutils.builder.api.LeatherArmorBuilder;
import net.nowtryz.mcutils.builder.api.MonterEggBuilder;
import net.nowtryz.mcutils.builder.api.SimpleBuilder;
import net.nowtryz.mcutils.builder.api.SkullBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;

import static net.nowtryz.mcutils.builder.internal.FactoryProvider.FACTORY;

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

    @Override
    public T setDurability(short damage) {
        this.itemStack.setDurability(damage);
        return this.self();
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

    static class SimpleMonsterEggBuilder extends BellowThirteenBuilder<SpawnEggMeta, MonterEggBuilder> implements MonterEggBuilder {

        SimpleMonsterEggBuilder() {
            super(Material.MONSTER_EGG, SpawnEggMeta.class);
        }

        SimpleMonsterEggBuilder(ItemStack item, SpawnEggMeta meta) {
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
        public MonterEggBuilder setSpawnedType(EntityType type) {
            this.itemMeta.setSpawnedType(type);
            return this;
        }
    }

    static class SimpleLeatherArmorBuilder extends BellowThirteenBuilder<LeatherArmorMeta, LeatherArmorBuilder> implements LeatherArmorBuilderTrait {
        SimpleLeatherArmorBuilder(Material material) {
            super(material, LeatherArmorMeta.class);
        }

        SimpleLeatherArmorBuilder(@NotNull ItemStack item, LeatherArmorMeta itemMeta) {
            super(item, itemMeta);
        }

        @Override
        public LeatherArmorBuilder toLeatherArmor() {
            return this;
        }

        @Override
        LeatherArmorBuilderTrait self() {
            return this;
        }
    }
}
