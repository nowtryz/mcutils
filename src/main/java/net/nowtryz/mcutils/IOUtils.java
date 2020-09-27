package net.nowtryz.mcutils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class IOUtils {
    public static void exportResource(JavaPlugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        File parent = file.getParentFile();

        if (!parent.exists() && !parent.mkdirs()) {
            plugin.getLogger().severe("Unable to create " + parent + "directory");
            return;
        }

        if (!file.exists()) plugin.saveResource(fileName, false);
        else if (file.isDirectory() && file.delete()) plugin.saveResource(fileName, false);
    }
}
