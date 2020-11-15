package net.nowtryz.mcutils.legacycommand;

import net.nowtryz.mcutils.api.Plugin;
import org.bukkit.command.CommandSender;

import java.util.function.Predicate;

public abstract class AbstractChildrenCommand<P extends Plugin,D> extends AbstractCommand<P,D> {
    protected final AbstractParentCommand<P,D> parent;

    public AbstractChildrenCommand(AbstractParentCommand<P,D> parent, String label, String usage,
                                   String permission) {
        super(label, usage, permission);
        this.parent = parent;
    }

    public AbstractChildrenCommand(AbstractParentCommand<P,D> parent, String label, String usage,
                                   String permission, Predicate<String[]> validator) {
        super(label, usage, permission, validator);
        this.parent = parent;
    }

    @Override
    public final boolean handleResult(CommandSender sender, CommandResult result) {
        return parent.handleResult(this, sender, result);
    }
}
