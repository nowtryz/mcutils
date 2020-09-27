package net.nowtryz.mcutils.builders;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class MonsterEggBuilder extends ItemBuilder<SpawnEggMeta> {
    MonsterEggBuilder() {
        super(Material.MONSTER_EGG, SpawnEggMeta.class);
    }

    MonsterEggBuilder(ItemStack item, SpawnEggMeta meta) {
        super(item, meta);
    }

    /**
     * Set the type of entity this egg will spawn.
     *
     * @param type The entity type. May be null for implementation specific
     * default.
     */
    public MonsterEggBuilder setSpawnedType(EntityType type) {
        this.itemMeta.setSpawnedType(type);
        return this;
    }
}
