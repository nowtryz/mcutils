package net.nowtryz.mcutils.builder.internal;

import net.nowtryz.mcutils.builder.api.ItemBuilderFactory;

public final class FactoryProvider {
    static ItemBuilderFactory FACTORY;

    public static void setFactory(ItemBuilderFactory factory) {
        if (factory != null) FACTORY = factory;
    }
}
