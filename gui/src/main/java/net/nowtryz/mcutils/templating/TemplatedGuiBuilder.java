package net.nowtryz.mcutils.templating;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.nowtryz.mcutils.api.Translation;
import net.nowtryz.mcutils.inventory.AbstractGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TemplatedGuiBuilder {
    private final Pattern pattern;
    private final AbstractGui<?> gui;

    public TemplatedGuiBuilder name(@NonNull Translation translation, Object... args) {
        return this.name(translation.get(args));
    }

    public TemplatedGuiBuilder name(@NonNull String name) {
        this.gui.createInventory(pattern.getSize(), name);
        this.gui.getInventory().setContents(pattern.toInventory());
        return this;
    }

    public final TemplatedGuiBuilder hookAction(String name, Consumer<? super InventoryClickEvent> action, ItemProvider provider) {
        this.pattern.getHook(name).ifPresent(hook -> {
            ItemStack item = provider.build(hook.safeBuilder()).build();
            this.hookAction(hook, action, item);
        });

        return this;
    }

    public final TemplatedGuiBuilder hookAction(String name, Runnable action, ItemProvider provider) {
        return this.hookAction(name, event -> action.run(), provider);
    }

    public final TemplatedGuiBuilder hookItem(String name, ItemStack item) {
        this.pattern.getHook(name).ifPresent(hook -> this.hookItem(hook, item));
        return this;
    }

    public final TemplatedGuiBuilder hookProvider(String name, ItemProvider provider) {
        this.pattern.getHook(name).ifPresent(hook -> {
            ItemStack item = provider.build(hook.safeBuilder()).build();
            this.hookItem(hook, item);
        });

        return this;
    }

    public final TemplatedGuiBuilder hookAction(String name, Consumer<? super InventoryClickEvent> action, ItemStack item) {
        this.pattern.getHook(name).ifPresent(hook -> this.hookAction(hook, action, item));
        return this;
    }

    public final TemplatedGuiBuilder hookAction(String name, Runnable action, ItemStack item) {
        return this.hookAction(name, event -> action.run(), item);
    }

    public final TemplatedGuiBuilder hookAction(String name, Consumer<? super InventoryClickEvent> action) {
        this.pattern.getHook(name).ifPresent(hook -> this.hookAction(hook, action, hook.getItem()));
        return this;
    }

    public TemplatedGuiBuilder hookAction(PatternKey hook, Consumer<? super InventoryClickEvent> action, ItemStack item) {
        if (hook.getPositions().length == 0) return this;
        Inventory inventory = this.gui.getInventory();

        for (int pos : hook.getPositions()) inventory.setItem(pos, item);
        this.gui.addClickableItem(inventory.getItem(hook.getPositions()[0]), action);

        return this;
    }

    /**
     * Sets the item for the desired hook
     * @param hook the hook to use
     * @param item the item to put where the hook is used
     * @return this builder
     */
    public TemplatedGuiBuilder hookItem(PatternKey hook, ItemStack item) {
        if (hook.getPositions().length == 0) return this;
        for (int pos : hook.getPositions()) gui.getInventory().setItem(pos, item);
        return this;
    }

    /**
     * Hook an action to the desired hook with an item based on the fallback item of the template for this hook
     * @param hookName the hook to use
     * @param action the action to perform when the item is clicked
     * @param provider the provider used to alter the fallback item
     * @return this builder
     */
    public TemplatedGuiBuilder fallback(String hookName, Consumer<? super InventoryClickEvent> action, ItemProvider provider) {
        this.pattern.getHook(hookName).ifPresent(hook -> {
            ItemStack item = provider.build(hook.fallbackBuilder()).build();
            this.hookAction(hook, action, item);
        });

        return this;
    }

    /**
     * Hook an action to the desired hook with an item based on the fallback item of the template for this hook
     * @param hookName the hook to use
     * @param action the action to perform when the item is clicked
     * @param provider the provider used to alter the fallback item
     * @return this builder
     */
    public TemplatedGuiBuilder fallback(String hookName, Runnable action, ItemProvider provider) {
        return this.fallback(hookName, event -> action.run(), provider);
    }

    /**
     * Sets items for the desired hook based on fallback item
     * @param hookName the hook to use
     * @param provider the provider used to alter the fallback item
     * @return this builder
     */
    public TemplatedGuiBuilder fallback(String hookName, ItemProvider provider) {
        this.pattern.getHook(hookName).ifPresent(hook -> {
            ItemStack item = provider.build(hook.fallbackBuilder()).build();
            this.hookItem(hook, item);
        });

        return this;
    }

    /**
     * Hook an item to the "back' action of the gui. It will only add the hook if the gui has a previous
     * gui registered
     * @param hookName the name of the hook in the pattern
     * @param provider a provider to add element to the item from the pattern
     * @return this builder
     */
    public final TemplatedGuiBuilder hookBack(String hookName, ItemProvider provider) {
        if (this.gui.hasPrevious()) this.hookAction(hookName, this.gui::onBack, provider);
        return this;
    }

    /**
     * Hook to "back' action of the gui without updating the item. It will only add the hook if the gui has a previous
     * gui registered
     * @param hookName the name of the hook in the pattern
     * @return this builder
     */
    public final TemplatedGuiBuilder hookBack(String hookName) {
        if (gui.hasPrevious())this.hookAction(hookName, this.gui::onBack);
        return this;
    }

    /**
     * Remove all action previously linked to the hook and rewire to the new item
     * @param hookName the name of the hook in the pattern
     * @param provider the provider to use to alter the item
     * @return this builder
     */
    public final TemplatedGuiBuilder reHook(String hookName, ItemProvider provider) {
        this.pattern.getHook(hookName).ifPresent(hook -> {
            int[] positions = hook.getPositions();

            // skip if there is nothing to update
            if (positions.length == 0) return;

            ItemStack item = provider.build(hook.safeBuilder()).build();
            this.gui.swapClickableItem(positions[0], item);
            for (int i = 1; i < positions.length; i++) this.gui.getInventory().setItem(positions[i], item);
        });

        return this;
    }

    public final TemplatedGuiBuilder hookIf(boolean condition, Consumer<TemplatedGuiBuilder> consumer) {
        if (condition) consumer.accept(this);
        return this;
    }
}
