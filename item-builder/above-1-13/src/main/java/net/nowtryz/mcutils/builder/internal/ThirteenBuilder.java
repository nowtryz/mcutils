package net.nowtryz.mcutils.builder.internal;

import lombok.NonNull;
import net.nowtryz.mcutils.builder.api.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

class ThirteenBuilder extends AbstractItemBuilder<ThirteenBuilder> {
    private static final String COLOR_MATCHER;

    static {
        String colors = Arrays.stream(DyeColor.values())
                .map(Enum::name)
                .collect(Collectors.joining("|"));
        //language=regexp
        COLOR_MATCHER = "^(?:" + colors + ")";
    }

    ThirteenBuilder(Material material) {
        super(material);
    }

    ThirteenBuilder(@NotNull ItemStack item, ItemMeta itemMeta) {
        super(item, itemMeta);
    }

    @Override
    ThirteenBuilder self() {
        return this;
    }

    @Override
    public ThirteenBuilder setColor(@NonNull DyeColor color) {
        String name = this.itemStack.getType().name().replaceFirst(COLOR_MATCHER, color.name());
        this.itemStack.setType(Material.valueOf(name));
        return self();
    }

    @Override
    public ThirteenBuilder setWoolColor(DyeColor color) {
        return this.setColor(color);
    }

    @Override
    public ThirteenBuilder setDyeColor(DyeColor color) {
        return this.setColor(color);
    }

    @Override
    public ThirteenBuilder setSpawnedType(EntityType type) {
        if (!type.isAlive()) {
            Bukkit.getLogger().warning("[MCUtils] ItemBuilder tried to set a non living entity type to an egg");
            return this;
        }
        this.itemStack.setType(Material.valueOf(type.name() + "_SPAWN_EGG"));
        return this;
    }

    @Override
    public ThirteenBuilder toEgg() {
        this.itemStack.setType(Material.SKELETON_SPAWN_EGG);
        return this;
    }

    @Override
    public ThirteenBuilder setDurability(short damage) {
        if (this.itemMeta instanceof Damageable) ((Damageable) this.itemMeta).setDamage(damage);
        else Bukkit.getLogger().warning("[MCUtils] ItemBuilder tried to set the durability of a non damageable item");
        return self();
    }
}
