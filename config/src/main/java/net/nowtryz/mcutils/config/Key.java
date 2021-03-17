package net.nowtryz.mcutils.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.Configuration;

import java.util.function.Function;


@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class Key<T> implements Comparable<Key<?>> {
    public static final int UNKNOWN_KEY = -1;

    @Getter int ordinal = UNKNOWN_KEY;
    @Getter String name = "";

    private final Function<Configuration, T> extractor;

    public T get(Configuration configuration) {
        return extractor.apply(configuration);
    }

    @Override
    public final int compareTo(Key<?> other) {
        return this.ordinal - other.ordinal();
    }

    public final boolean equals(Object other) {
        return this==other;
    }

    public final String toString() {
        return name;
    }

    public final int hashCode() {
        return super.hashCode();
    }
}
