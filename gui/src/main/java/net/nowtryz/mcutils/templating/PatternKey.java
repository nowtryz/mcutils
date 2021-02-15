package net.nowtryz.mcutils.templating;

import lombok.Value;
import net.nowtryz.mcutils.builder.ItemBuilders;
import net.nowtryz.mcutils.builder.api.SimpleBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Value
public class PatternKey {
    String key;
    int[] positions;
    ItemStack item;
    @Nullable
    ItemStack fallback;

    public SimpleBuilder builder() {
        return ItemBuilders.from(this.item.clone());
    }

    public boolean isBuildable() {
        return item != null && item.getType() != Material.AIR;
    }

    public boolean isFallbackBuildable() {
        return fallback != null && fallback.getType() != Material.AIR;
    }

    public SimpleBuilder safeBuilder() {
        return this.isBuildable() ? this.builder() : ItemBuilders.create(Material.STONE);
    }

    public SimpleBuilder fallbackBuilder() {
        return this.isFallbackBuildable() ? ItemBuilders.from(this.fallback) : ItemBuilders.create(Material.STONE);
    }

    public @Nullable ItemStack getFallback() {
        return this.fallback == null ? this.item : this.fallback;
    }

    /**
     * Weather or not this key is present on the pattern
     * @return true if the key is present on the pattern, false otherwise
     */
    public boolean isPresent() {
        return this.positions.length != 0;
    }
}
