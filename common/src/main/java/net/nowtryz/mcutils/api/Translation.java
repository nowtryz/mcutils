package net.nowtryz.mcutils.api;

import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Translation {
    Pattern INDEXED = Pattern.compile(".*\\{[^}]+}.*");
    Pattern INDEXABLE = Pattern.compile("\\{}");

    static MessageFormat parseToMessageFormat(@NonNull String key, @NonNull String message) {
        try {
            if (message.contains("{}")) {
                if (!Translation.INDEXED.matcher(message).matches()) {
                    Matcher matcher = Translation.INDEXABLE.matcher(message);
                    StringBuffer sb = new StringBuffer();
                    for (int i=0; matcher.find(); i++) {
                        matcher.appendReplacement(sb, "{" + i + "}");
                    }
                    matcher.appendTail(sb);
                    return new MessageFormat(sb.toString());
                } else {
                    throw new IllegalArgumentException(key + " mixes non-indexed ({}) and indexed ({n}) parameters");
                }
            } else {
                return new MessageFormat(message);
            }
        } catch (IllegalArgumentException exception) {
            if (exception.getCause() != null) {
                throw new IllegalArgumentException("Could not parse " + key, exception.getCause());
            } else {
                throw new IllegalArgumentException("Could not parse " + key, exception);
            }
        }
    }

    /**
     * Gets the translated message
     * @return the translated message
     */
    String get();

    /**
     * Extracts the translated message and format it with given arguments
     * @param args message arguments
     * @return the translated and formatted message
     */
    String get(Object... args);

    /**
     * Send the message to a command sender, usually a player
     * @param p the CommandSender that will receive the message
     */
    default void send(CommandSender p) {
        Optional.ofNullable(this.get())
                .filter(s -> !s.isEmpty())
                .map(s -> s.split("\n"))
                .ifPresent(p::sendMessage);
    }

    /**
     * Send a formatted and translated message to a command sender, usually a player
     * @param p the CommandSender that will receive the message
     * @param args message arguments
     */
    default void send(CommandSender p, Object... args) {
        Optional.ofNullable(this.get(args))
                .filter(s -> !s.isEmpty())
                .map(s -> s.split("\n"))
                .ifPresent(p::sendMessage);
    }
}
