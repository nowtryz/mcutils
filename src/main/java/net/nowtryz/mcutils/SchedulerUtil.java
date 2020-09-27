package net.nowtryz.mcutils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class SchedulerUtil {
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
}
