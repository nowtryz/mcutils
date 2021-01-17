package net.nowtryz.mcutils.builder.internal;

import lombok.NonNull;
import net.nowtryz.mcutils.builder.ItemBuilder;
import net.nowtryz.mcutils.builder.MonterEggBuilder;
import net.nowtryz.mcutils.builder.SimpleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

abstract class ThirteenBuilder<M extends ItemMeta, T extends ItemBuilder<T>> extends AbstractItemBuilder<M, T> {
    private static final Pattern colorMatcher = Pattern.compile("^[A-Z]+(_.*)$");
    private static final String colorMatcher2;

    static {
        String colors = Arrays.stream(DyeColor.values())
                .map(Enum::name)
                .collect(Collectors.joining("|"));
        //language=regexp
        colorMatcher2 = "^(?:" + colors + ")";
    }

    ThirteenBuilder(Material material, Class<M> metaClass) {
        super(material, metaClass);
    }

    ThirteenBuilder(@NotNull ItemStack item, M itemMeta) {
        super(item, itemMeta);
    }

    @Override
    public T setColor(@NonNull DyeColor color) {
        String name = this.itemStack.getType().name().replaceFirst(colorMatcher2, color.name());
        this.itemStack.setType(Material.valueOf(name));
        return self();
    }

    @Override
    public T setWoolColor(DyeColor color) {
        return this.setColor(color);
    }

    @Override
    public T setDyeColor(DyeColor color) {
        return this.setColor(color);
    }

    static class SimpleItemBuilder<M extends ItemMeta> extends ThirteenBuilder<M, SimpleBuilder> implements SimpleBuilder {

        SimpleItemBuilder(Material material, Class<M> metaClass) {
            super(material, metaClass);
        }

        SimpleItemBuilder(@NotNull ItemStack item, M itemMeta) {
            super(item, itemMeta);
        }

        @Override
        SimpleBuilder self() {
            return this;
        }
    }

    static class EggBuilder extends ThirteenBuilder<SpawnEggMeta, MonterEggBuilder> implements MonterEggBuilder {
        public EggBuilder() {
            super(Material.SKELETON_SPAWN_EGG, SpawnEggMeta.class);
        }

        public EggBuilder(ItemStack item, SpawnEggMeta meta) {
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
            if (!type.isAlive()) {
                Bukkit.getLogger().warning("[MCUtils] ItemBuilder tried to set a non living entity type to an egg");
                return this;
            }
            this.itemStack.setType(Material.valueOf(type.name() + "_SPAWN_EGG"));
            return this;
        }
    }
}