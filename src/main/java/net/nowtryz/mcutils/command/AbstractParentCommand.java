package net.nowtryz.mcutils.command;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Abstract implementation for base commands that has sub commands
 *
 * @param <P> the class of the plugin provided at each call
 * @param <D> the class of the description, this enables you to handle i18n or other stuff like that using e.g.
 *           <code>{@link java.util.function.Function Function}&lt;{@link org.bukkit.entity.Player Player},
 *           {@link String}&gt;</code>
 */
public abstract class AbstractParentCommand<P extends Plugin,D> extends AbstractCommand<P,D> {
    protected Map<String, ICommand<P,D>> commandsMap;
    protected ICommand<P,D>[] commands;


    @SafeVarargs
    public AbstractParentCommand(String label, String usage, String permission, ICommand<P,D>... commands) {
        super(label, usage, permission);
        this.registerCommands(commands);
    }

    @SafeVarargs
    public AbstractParentCommand(String label, String usage, String permission,
                                 Predicate<String[]> validator, ICommand<P,D>... commands) {
        super(label, usage, permission, validator);
        this.registerCommands(commands);
    }

    @Override
    public boolean process(P plugin, CommandSender sender, String[] args) {
        // if no sub commands, we run the default command
        if (args.length == 0 || this.commands == null) return super.process(plugin, sender, args);

        // grab sub command and execute it
        ICommand<P,?> cmd = this.commandsMap.get(args[0].toLowerCase());
        if (cmd == null) return super.process(plugin, sender, args);
        else {
            // arguments to pass
            String[] subArgs = extractSubArgs(args);

            return cmd.process(plugin, sender, subArgs);
        }
    }

    /**
     * Create a new array and strip the first value
     * @param array the array to strip
     * @return an array with the first value stripped
     */
    private static String[] extractSubArgs(String[] array) {
        int newLength = array.length - 1;
        String[] subArgs = new String[newLength];
        System.arraycopy(array, 1, subArgs, 0, newLength);
        return subArgs;
    }

    @Override
    public List<String> tabComplete(P plugin, CommandSender sender, String[] args) {
        if (args.length == 0) return null;
        if (args.length == 1 && this.commands != null) {
            List<String> complete = Arrays.stream(this.commands)
                    .filter(c -> c.getPermission() == null || sender.hasPermission(c.getPermission()) || sender.isOp())
                    .map(ICommand::getKeyword)
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());

            Optional.ofNullable(this.tabDefaultComplete(plugin, sender, args)).ifPresent(complete::addAll);
            return complete;
        }

        return Optional.ofNullable(this.commandsMap.get(args[0].toLowerCase()))
                .map(cmd -> cmd.tabComplete(plugin, sender, extractSubArgs(args)))
                .orElseGet(() -> this.tabComplete(plugin, sender, args));
    }

    @SafeVarargs
    protected final void registerCommands(ICommand<P,D>... commands) {
        this.commands = commands;

        if (commands != null && commands.length > 0) try {
            this.commandsMap = Arrays.stream(commands)
                    .flatMap(c -> c.getAliases().stream().map(v -> ImmutablePair.of(v, c)))
                    .collect(Collectors.toMap(p -> p.left.toLowerCase(), Pair::getRight));
        } catch (IllegalArgumentException e) {
            System.err.println("Cannot load subcommands for " + this.getClass() + ": " + e.getMessage());
            System.err.println("Maybe you have duplicated keywords/aliases");
        }
        else this.commandsMap = new HashMap<>();
    }

    /**
     * Add commands to the already registered ones. If no commands have been registered using
     * {@link AbstractParentCommand#registerCommands(ICommand[])}, it will be called instead.<br>
     * This enables to add some commands bases on conditions, e.g.:
     * <code><pre>
     * if (useTreeCommands) super.addCommands(
     *     new TreeCommand(this),
     *     new BigTreeCommand(this),
     * );
     * </pre></code>
     * @param commands the commands to add
     */
    @SafeVarargs
    protected final void addCommands(ICommand<P,D>... commands) {
        if (this.commands == null) this.registerCommands(commands);
        else {
            this.commands = ArrayUtils.addAll(this.commands, commands);
            this.commandsMap.putAll(Arrays.stream(commands)
                    .flatMap(c -> c.getAliases().stream().map(v -> ImmutablePair.of(v, c)))
                    .collect(Collectors.toMap(p -> p.left.toLowerCase(), Pair::getRight)));
        }

    }

    @Override
    public final boolean handleResult(CommandSender sender, CommandResult result) {
        return this.handleResult(this, sender, result);
    }

    /**
     * Return possible completion for the default command behavior (when no sub-commands can be called)
     * @param plugin the instance of the plugin that is running on the server
     * @param sender the command sender
     * @param args arguments of the command
     * @return true on success
     */
    @Nullable
    protected List<String> tabDefaultComplete(P plugin, CommandSender sender, String[] args) {
        return null;
    }

    /**
     * Perform post command action based on the result after children commands
     * @param command the command executed
     * @param sender the sender of the command
     * @param result result of the executed command
     * @return true if usage is correct
     */
    public abstract boolean handleResult(ICommand<P,D> command, CommandSender sender, CommandResult result);
}
