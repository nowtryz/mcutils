package net.nowtryz.mcutils.templating;

import net.nowtryz.mcutils.builder.api.ItemBuilder;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ItemProvider {
    @NotNull
    ItemBuilder build(ItemBuilder builder);
}
