package net.nowtryz.mcutils.templating;

import lombok.RequiredArgsConstructor;
import net.nowtryz.mcutils.builder.ItemBuilders;
import net.nowtryz.mcutils.builder.api.ItemBuilder;
import net.nowtryz.mcutils.inventory.AbstractGui;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor
public class Template<T extends AbstractGui<?>> {
    private final Pattern pattern;
    private ItemStack[] inventory;
    private List<TemplateAction<?>> actions;

    public void init() {
        this.inventory = pattern.toInventory();
    }

    public TemplateAction.TemplateActionBuilder<T> hook(String hook) {
        return TemplateAction.<T>builder()
                .template(this)
                .key(this.pattern.getHook(hook).orElse(null));
    }

    public void hook(TemplateAction<T> action) {
        if (action.getKey() != null) {
            this.actions.add(action);
            PatternKey key = action.getKey();

            if (action.getUpdate() != null && key.getItem() != null && key.isPresent()) {
                ItemBuilder builder = ItemBuilders.from(key.getItem());
                ItemStack newItem = action.getUpdate().build(builder).build();
                for (int i : key.getPositions()) this.inventory[i] = newItem;
            }
        }
    }
}
