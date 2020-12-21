package net.nowtryz.mcutils.builders;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class ThirteenEggBuilder extends MonterEggBuilder {

    public ThirteenEggBuilder() {
        super(Material.SKELETON_SPAWN_EGG, SpawnEggMeta.class);
    }

    @Override
    public MonterEggBuilder setSpawnedType(EntityType type) {
        this.itemStack.setType(Material.valueOf(type.name() + "_SPAWN_EGG"));
        return this;
    }
}
