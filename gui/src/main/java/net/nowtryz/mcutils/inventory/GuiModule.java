package net.nowtryz.mcutils.inventory;

import com.google.inject.AbstractModule;
import net.nowtryz.mcutils.api.listener.GuiListener;
import net.nowtryz.mcutils.listener.SimpleInventoryListener;

public class GuiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GuiListener.class).to(SimpleInventoryListener.class);
    }
}
