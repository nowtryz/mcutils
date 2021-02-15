package net.nowtryz.mcutils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemStackUtil {
    public static void setName(@NotNull ItemStack item, @NotNull String name) {
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
    }

    public static ItemStack createItem(@NotNull Material material, @NotNull String name) {
        ItemStack itemStack = new ItemStack(material);
        setName(itemStack, name);
        return itemStack;
    }

    public static void clearEnchants(@NotNull ItemStack item) {
        item.getEnchantments().keySet().forEach(item::removeEnchantment);
    }
}
