package net.nowtryz.mcutils.builders;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public abstract class SkullBuilder extends ItemBuilder<SkullMeta> {
    private static Field PROFILE_FIELD = null;

    protected SkullBuilder(Material material, Class<SkullMeta> metaClass) {
        super(material, metaClass);
    }

    protected SkullBuilder(@NotNull ItemStack item, SkullMeta itemMeta) {
        super(item, itemMeta);
    }


    public SkullBuilder setOwningPlayer(OfflinePlayer player) {
        this.itemMeta.setOwningPlayer(player);
        return this;
    }

    public SkullBuilder setTextureUrl(String url) {
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
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

    private synchronized void setProfile(GameProfile profile) {
        try {
            if (PROFILE_FIELD == null) {
                PROFILE_FIELD = this.itemMeta.getClass().getDeclaredField("profile");
            }

            PROFILE_FIELD.setAccessible(true);
            PROFILE_FIELD.set(this.itemMeta, profile);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Unable to set skull's skin", exception);
        }
    }
}
