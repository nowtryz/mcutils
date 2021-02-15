package net.nowtryz.mcutils.api;

import org.bukkit.command.CommandSender;

import java.util.Optional;

public interface Translation {
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
