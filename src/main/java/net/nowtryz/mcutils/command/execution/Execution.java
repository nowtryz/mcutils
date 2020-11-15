package net.nowtryz.mcutils.command.execution;

import lombok.Value;

public interface Execution {
    String getCommand();

    @Value
    class GenericArg {
        String arg;
        int index;
    }
}
