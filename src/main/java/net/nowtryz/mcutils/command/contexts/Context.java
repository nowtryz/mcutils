package net.nowtryz.mcutils.command.contexts;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import net.nowtryz.mcutils.api.Translation;
import org.bukkit.command.CommandSender;

@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode
@FieldDefaults(makeFinal=true, level= AccessLevel.PROTECTED)
public abstract class Context {
    CommandSender sender;
    String commandLabel;
    String[] args;

    public void reply(String message) {
        this.sender.sendMessage(message);
    }

    public void reply(@NonNull Translation translation) {
        translation.send(sender);
    }
}
