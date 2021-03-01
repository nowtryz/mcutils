package net.nowtryz.mcutils.builder.internal;

import lombok.NonNull;
import net.nowtryz.mcutils.builder.api.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public interface LeatherArmorBuilderTrait extends ItemBuilder.DecorableItemBuilder<LeatherArmorMeta, net.nowtryz.mcutils.builder.api.LeatherArmorBuilder>, net.nowtryz.mcutils.builder.api.LeatherArmorBuilder {
    @Override
    default LeatherArmorBuilderTrait setColor(@NonNull Color color) {
        this.getMeta().setColor(color);
        return this;
    }
}
