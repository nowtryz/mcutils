package net.nowtryz.mcutils.builder;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface SkullBuilder extends ItemBuilder<SkullBuilder> {
    net.nowtryz.mcutils.builder.SkullBuilder setOwningPlayer(OfflinePlayer player);

    net.nowtryz.mcutils.builder.SkullBuilder setTextureUrl(String url);

    net.nowtryz.mcutils.builder.SkullBuilder setUUID(UUID uuid);

    net.nowtryz.mcutils.builder.SkullBuilder setName(String name);

    net.nowtryz.mcutils.builder.SkullBuilder setValue(String value);
}
