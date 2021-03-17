package net.nowtryz.mcutils.config;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import net.nowtryz.mcutils.exceptions.ConfigurationInitializationException;
import org.bukkit.configuration.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class PluginConfiguration {
    private final Object[] configurations;

    public PluginConfiguration(Configuration config, Class<?> keyHolder) {
        List<Key<?>> keys = collectKeysFromClass(keyHolder);
        this.configurations = new Object[keys.size()];

        for (Key<?> key : keys) {
            this.configurations[key.ordinal()] = key.get(config);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(@NonNull Key<T> key) {
        if (key.ordinal() == Key.UNKNOWN_KEY) throw new IllegalArgumentException("Unknown key");
        return (T) this.configurations[key.ordinal()];
    }

    @SuppressWarnings("UnstableApiUsage")
    protected static List<Key<?>> collectKeysFromClass(Class<?> clazz) {
        // get a list of all keys
        List<Field> fields = Arrays.stream(clazz.getFields())
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> Key.class.isAssignableFrom(f.getType()))
                .collect(ImmutableList.toImmutableList());

        List<Key<?>> keys = fields.stream().map(f -> {
            try {
                return (Key<?>) f.get(null);
            } catch (IllegalAccessException e) {
                throw new ConfigurationInitializationException(e);
            }
        }).collect(ImmutableList.toImmutableList());

        // set ordinal values and names
        for (int i = 0; i < keys.size(); i++) {
            Field field = fields.get(i);
            Key<?> value = keys.get(i);
            value.name = field.getName();
            value.ordinal = i;
        }

        return keys;
    }
}
