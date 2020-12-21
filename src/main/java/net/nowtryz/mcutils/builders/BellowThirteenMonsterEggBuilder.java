package net.nowtryz.mcutils.builders;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class BellowThirteenMonsterEggBuilder extends MonterEggBuilder {
    private static final Material EGG = Material.valueOf("MONSTER_EGG");

    public static MonterEggBuilder createEggFrom(ItemStack itemStack) {
        if (EGG != itemStack.getType()) {
            throw new IllegalArgumentException("Provided item isn't an egg");
        }

        return new BellowThirteenMonsterEggBuilder(itemStack, (SpawnEggMeta) itemStack.getItemMeta());
    }

    BellowThirteenMonsterEggBuilder() {
        super(Material.valueOf("MONSTER_EGG"), SpawnEggMeta.class);
    }

    BellowThirteenMonsterEggBuilder(ItemStack item, SpawnEggMeta meta) {
        super(item, meta);
    }

    @Override
    @SuppressWarnings("deprecation")
    public MonterEggBuilder setSpawnedType(EntityType type) {
        this.itemMeta.setSpawnedType(type);
        return this;
    }
}
