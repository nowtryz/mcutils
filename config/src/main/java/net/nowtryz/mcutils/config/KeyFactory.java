package net.nowtryz.mcutils.config;

import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class KeyFactory {
    public static <T> Key<T> key(Function<Configuration, T> extractor) {
        return new Key<>(extractor);
    }

    public static Key<Boolean> booleanKey(String path, boolean def) {
        return key(configuration -> configuration.getBoolean(path, def));
    }

    public static Key<Boolean> booleanKey(String path) {
        return key(configuration -> configuration.getBoolean(path));
    }

    public static Key<Integer> intKey(String path) {
        return key(configuration -> configuration.getInt(path));
    }

    public static Key<Integer> intKey(String path, int def) {
        return key(configuration -> configuration.getInt(path, def));
    }

    public static Key<Long> longKey(String path) {
        return key(configuration -> configuration.getLong(path));
    }

    public static Key<Long> longKey(String path, long def) {
        return key(configuration -> configuration.getLong(path, def));
    }

    public static Key<String> stringKey(String path, String def) {
        return key(configuration -> configuration.getString(path, def));
    }

    public static Key<String> stringKey(String path) {
        return key(configuration -> configuration.getString(path));
    }

    public static Key<List<String>> stringListKey(String path) {
        return key(configuration -> configuration.getStringList(path));
    }

    public static Key<Material> materialKey(String path, Material def) {
        return key(configuration -> Optional.ofNullable(configuration.getString(path))
                .map(Material::matchMaterial)
                .orElse(def));
    }

    public static Key<BarColor> barColorKey(String path, BarColor def) {
        return key(configuration -> {
            try {
                return Optional.ofNullable(configuration.getString(path, ""))
                        .map(String::toUpperCase)
                        .map(BarColor::valueOf)
                        .orElse(def);
            } catch (IllegalArgumentException ignored) {}
            return def;
        });
    }
}
