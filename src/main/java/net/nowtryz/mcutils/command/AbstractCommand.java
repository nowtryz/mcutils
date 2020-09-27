package net.nowtryz.mcutils.command;

import net.nowtryz.mcutils.ArgumentChecker;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Abstract implementation of a base command
 * @param <P> the class of the plugin provided at each call
 * @param <D> the class of the description, this enables you to handle i18n or other stuff like that using e.g.
 *           <code>{@link java.util.function.Function Function}&lt;{@link org.bukkit.entity.Player Player},
 *           {@link String}&gt;</code>
 */
public abstract class AbstractCommand<P extends Plugin, D> implements ICommand<P, D> {
    private final Set<String> aliases = new HashSet<>();
    private final String label;
    private final String usage;
    private final String permission;
    private final Predicate<String[]> validator;
    private boolean async = true;
    private D description = null;

    public AbstractCommand(String label, String usage, String permission) {
        this.label = label;
        this.usage = usage;
        this.permission = permission;
        this.validator = ArgumentChecker::noArgs;
        this.aliases.add(label);
    }

    public AbstractCommand(String label, String usage, String permission, Predicate<String[]> validator) {
        this.label = label;
        this.usage = usage;
        this.permission = permission;
        this.validator = validator;
        this.aliases.add(label);
    }

    @Override
    public @NotNull String getKeyword() {
        return this.label;
    }

    @Override
    public @NotNull Set<String> getAliases() {
        return Collections.unmodifiableSet(this.aliases);
    }

    @Override
    public boolean isAsync() {
        return this.async;
//        return false; // FIXME DEBUG
    }

    @Override
    public boolean canAccept(String[] args) {
        return this.validator.test(args);
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public D getDescription() {
        return description;
    }

    protected void setAsync(boolean async) {
        this.async = async;
    }

    protected void setDescription(@Nullable D description) {
        this.description = description;
    }

    protected void registerAliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }
}
