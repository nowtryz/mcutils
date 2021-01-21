package net.nowtryz.mcutils.builder.internal;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

class SkullBuilder extends ItemBuilderDecorator<SkullMeta, net.nowtryz.mcutils.builder.SkullBuilder> implements net.nowtryz.mcutils.builder.SkullBuilder {
    private static Field PROFILE_FIELD = null;

    SkullBuilder(DecorableItemBuilder<SkullMeta, ?> delegate) {
        super(delegate);
    }

    @Override
    public SkullBuilder self() {
        return this;
    }

    @Override
    public SkullBuilder setOwningPlayer(OfflinePlayer player) {
        this.delegate.getMeta().setOwningPlayer(player);
        return this;
    }

    @Override
    public SkullBuilder setTextureUrl(String url) {
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        return this.setValue(new String(encodedData));
    }

    @Override
    public SkullBuilder setUUID(UUID uuid) {
        GameProfile profile = new GameProfile(uuid, null);

        this.setProfile(profile);
        return this;
    }

    @Override
    public SkullBuilder setName(String name) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);

        this.setProfile(profile);
        return this;
    }

    @Override
    public SkullBuilder setValue(String value) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));

        this.setProfile(profile);
        return this;
    }

    @Override
    public SkullBuilder toSkull() {
        return this;
    }

    private synchronized void setProfile(GameProfile profile) {
        try {
            if (PROFILE_FIELD == null) {
                PROFILE_FIELD = this.delegate.getMeta().getClass().getDeclaredField("profile");
            }

            PROFILE_FIELD.setAccessible(true);
            PROFILE_FIELD.set(this.delegate.getMeta(), profile);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to set skull's skin", exception);
        }
    }
}
