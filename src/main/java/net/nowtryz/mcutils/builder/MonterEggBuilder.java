package net.nowtryz.mcutils.builder;

import org.bukkit.entity.EntityType;

public interface MonterEggBuilder extends ItemBuilder<MonterEggBuilder> {
    /**
     * Set the type of entity this egg will spawn.
     *
     * @param type The entity type. May be null for implementation specific
     *             default.
     */
    MonterEggBuilder setSpawnedType(EntityType type);
}
