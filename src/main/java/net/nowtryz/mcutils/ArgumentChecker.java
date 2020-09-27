package net.nowtryz.mcutils;

import java.util.function.Predicate;

public class ArgumentChecker {
    public static boolean noArgs(String[] args) {
        return args.length == 0;
    }

    public static boolean anyArgs(String[] args) {
        return true;
    }

    public static Predicate<String[]> max(int maxArgs) {
        return args -> args.length <= maxArgs;
    }

    public static Predicate<String[]> min(int maxArgs) {
        return args -> args.length >= maxArgs;
    }

    public static Predicate<String[]> exact(int argsCount) {
        return args -> args.length == argsCount;
    }
}
