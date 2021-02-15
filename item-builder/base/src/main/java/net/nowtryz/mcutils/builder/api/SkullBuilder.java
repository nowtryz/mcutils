package net.nowtryz.mcutils.builder.api;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface SkullBuilder extends ItemBuilder<SkullBuilder> {
    /**
     * Set the player owning the skull
     * @param player the new player owning the skull
     * @return this builder
     */
    SkullBuilder setOwningPlayer(OfflinePlayer player);

    /**
     * Sets the texture of the skull
     * @param url the url of the skull
     * @return this builder
     */
    SkullBuilder setTextureUrl(String url);

    /**
     * Sets the texture of the skull
     * @param uuid the uuid of the player to use as texture
     * @return this builder
     * @deprecated no longer working, game profiles used to set the texture does not support uuid
     */
    @Deprecated
    SkullBuilder setUUID(UUID uuid);

    /**
     * Sets the texture of the skull
     * @param name the name of the player to use as texture
     * @return this builder
     */
    SkullBuilder setName(String name);

    /**
     * Sets the texture of the skull
     * @param value the value of the texture - a base64 encoded json object containing the texture's url
     * @return this builder
     */
    SkullBuilder setValue(String value);
}
