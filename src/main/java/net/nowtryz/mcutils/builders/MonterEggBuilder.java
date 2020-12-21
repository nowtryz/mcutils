package net.nowtryz.mcutils.builders;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;

public abstract class MonterEggBuilder extends ItemBuilder<SpawnEggMeta> {
    public static MonterEggBuilder create(ItemStack itemStack) {
        return BellowThirteenMonsterEggBuilder.createEggFrom(itemStack);
    }

    public static MonterEggBuilder create() {
        return new BellowThirteenMonsterEggBuilder();
    }

    protected MonterEggBuilder(Material material, Class<SpawnEggMeta> metaClass) {
        super(material, metaClass);
    }

    protected MonterEggBuilder(@NotNull ItemStack item, SpawnEggMeta itemMeta) {
        super(item, itemMeta);
    }

    /**
     * Set the type of entity this egg will spawn.
     *
     * @param type The entity type. May be null for implementation specific
     * default.
     */
    public abstract MonterEggBuilder setSpawnedType(EntityType type);
}
