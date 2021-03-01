package net.nowtryz.mcutils.builder.api;

import org.bukkit.Color;

public interface LeatherArmorBuilder extends ItemBuilder<LeatherArmorBuilder> {
    /**
     * Set the leather color of this piece of armor
     * @param color the color to set
     * @return this builder
     */
    LeatherArmorBuilder setColor(Color color);
}
