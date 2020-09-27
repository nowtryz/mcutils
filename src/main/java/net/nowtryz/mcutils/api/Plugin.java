package net.nowtryz.mcutils.api;

import net.nowtryz.mcutils.listener.InventoryListener;

public interface Plugin extends org.bukkit.plugin.Plugin {
    InventoryListener<? extends net.nowtryz.mcutils.api.Plugin> getInventoryListener();
}
