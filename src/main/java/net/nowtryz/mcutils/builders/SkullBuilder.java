package net.nowtryz.mcutils.builders;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullBuilder extends ItemBuilder<SkullMeta> {
    private static Field PROFILE_FIELD = null;


    public SkullBuilder() {
        super(Material.SKULL_ITEM, SkullMeta.class);
        this.setDurability((short) SkullType.PLAYER.ordinal());
    }

    public SkullBuilder setOwningPlayer(OfflinePlayer player) {
        this.itemMeta.setOwningPlayer(player);
        return this;
    }

    public SkullBuilder setTextureUrl(String url) {
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        return this.setValue(new String(encodedData));
    }

    public SkullBuilder setUUID(UUID uuid) {
        GameProfile profile = new GameProfile(uuid, null);

        this.setProfile(profile);
        return this;
    }

    public SkullBuilder setName(String name) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);

        this.setProfile(profile);
        return this;
    }

    public SkullBuilder setValue(String value) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));

        this.setProfile(profile);
        return this;
    }

    private void setProfile(GameProfile profile) {
        try {
            if (PROFILE_FIELD == null) {
                PROFILE_FIELD = this.itemMeta.getClass().getDeclaredField("profile");
                PROFILE_FIELD.setAccessible(true);
            }

            PROFILE_FIELD.set(this.itemMeta, profile);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Unable to set skull's skin", exception);
        }
    }
}
