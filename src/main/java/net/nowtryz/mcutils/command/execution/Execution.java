package net.nowtryz.mcutils.command.execution;

import com.google.common.collect.ImmutableList;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

public interface Execution {
    Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
    Pattern GENERIC_ARG = Pattern.compile("<(\\w+)(\\.{3})?>");
    Pattern VAR_ARGS = Pattern.compile("<(\\w+)\\.{3}>");

    @NotNull
    String getCommand();

    @NotNull
    default List<String> getArguments() {
        return ImmutableList.copyOf(PATTERN_ON_SPACE.split(this.getCommand()));
    }

    @NotNull
    default Queue<String> getCommandLine() {
        return new LinkedList<>(this.getArguments());
    }

    @Value
    class GenericArg {
        String arg;
        boolean varArgs;
        int index;
    }
}
