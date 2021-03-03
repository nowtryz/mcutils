package net.nowtryz.mcutils.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import net.nowtryz.mcutils.api.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

/**
 * A module responsible from creating all bindings utilities for bukkit plugin development.
 *
 * <p>As the module uses {@link com.google.inject.internal.BindingBuilder#toInstance(Object) toInstance()}, injection
 * will be requested for the plugin. So you must avoid tu use {@link com.google.inject.Injector#injectMembers(Object)
 * Injector#injectMembers()} on the plugin.
 *
 * @param <T> the type of the plugin used for binding
 */
@RequiredArgsConstructor
public class BukkitModule<T extends JavaPlugin & Plugin> extends AbstractModule {
    private final T plugin;
    private final Class<T> classOfT;

    @Override
    protected void configure() {
        // Plugin binding
        bind(Plugin.class).to(classOfT);
        bind(JavaPlugin.class).to(classOfT);
        bind(org.bukkit.plugin.Plugin.class).to(classOfT);
        bind(classOfT).toInstance(this.plugin);
    }

    @Provides
    @PluginLogger
    Logger provideLogger() {
        return this.plugin.getLogger();
    }

    @Provides
    @DefaultConfig
    Configuration providePluginConfiguration() {
        return this.providePluginFileConfiguration();
    }

    @Provides
    @DefaultConfig
    FileConfiguration providePluginFileConfiguration() {
        return this.plugin.getConfig();
    }

    @Provides
    @Singleton
    @DataFolder
    File provideDataFolder() {
        return this.plugin.getDataFolder();
    }

    @Provides
    @BukkitExecutor
    Executor provideExecutor() {
        return runnable -> {
            if (this.plugin.isReady()) Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
            else ForkJoinPool.commonPool().execute(runnable);
        };
    }
}
