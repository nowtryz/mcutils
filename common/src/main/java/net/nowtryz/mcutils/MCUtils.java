package net.nowtryz.mcutils;

import com.vdurmont.semver4j.Semver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MCUtils {
    public static boolean THIRTEEN_COMPATIBLE = getBukkitVersion().isGreaterThanOrEqualTo("1.13.0");
    public static boolean FIFTEEN_COMPATIBLE = getBukkitVersion().isGreaterThanOrEqualTo("1.15.0");
    public static boolean SIXTEEN_COMPATIBLE = getBukkitVersion().isGreaterThanOrEqualTo("1.16.0");

    /**
     * Find the real damager, find the shooter if it was a projectile
     * @param event the damage event
     * @return the real damager
     */
    public static @Nullable Entity getRealDamager(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
            if (shooter instanceof Entity) return (Entity) shooter;
            return null;
        } else return event.getDamager();
    }

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

    /**
     * Ensure that the code is run on primary thread
     *
     * If the current thread is the primary one, action is fulfilled right away. If not, a task is created to run the
     * conde on primary thread.
     * @param plugin the plugin to register the task with
     * @param runnable the action
     */
    public static void runOnPrimary(Plugin plugin, Runnable runnable) {
        if (Bukkit.isPrimaryThread()) runnable.run();
        else Bukkit.getScheduler().runTask(plugin, runnable);
    }

    /**
     * Replace '&amp;' by '§'
     * @param message the message to transform
     * @return the transformed message
     */
    @Contract("null->null;!null -> !null")
    public static String parseColors(String message) {
        if (message == null) return null;
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    public static  <T,R> Function<T, R> tryCatch(Function<T, R> tryFunction, BiConsumer<Exception, T> catchFunction,
                                                 boolean printStackTrace) {
        return t -> {
            try { return tryFunction.apply(t); }
            catch (Exception e) {
                catchFunction.accept(e, t);
                if (printStackTrace) e.printStackTrace();
                return null;
            }
        };
    }

    public static Semver getBukkitVersion() {
        return new Semver(Bukkit.getBukkitVersion());
    }
}
