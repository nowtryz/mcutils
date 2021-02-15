package net.nowtryz.mcutils.builder.api;

import org.bukkit.entity.EntityType;

public interface MonterEggBuilder extends ItemBuilder<MonterEggBuilder> {
    /**
     * Set the type of entity this egg will spawn.
     *
     * @param type The entity type. May be null for implementation specific
     *             default.
     * @return this builder
     */
    MonterEggBuilder setSpawnedType(EntityType type);
}
