package net.nowtryz.mcutils.builders;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.meta.SkullMeta;

public class BellowThirteenSkullBuilder extends SkullBuilder {
    public BellowThirteenSkullBuilder() {
        super(Material.valueOf("SKULL_ITEM"), SkullMeta.class);
        this.setDurability((short) SkullType.PLAYER.ordinal());
    }
}
