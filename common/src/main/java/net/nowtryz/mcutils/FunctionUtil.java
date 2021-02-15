package net.nowtryz.mcutils;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FunctionUtil {
    public static <T,R> Function<T,R> tryOrReturn(Function<T,R> tryFunction, R orElse) {
        return t -> {
            try { return tryFunction.apply(t); }
            catch (Exception ignored) { return orElse; }
        };
    }

    public static  <T,R> Function<T, R> tryCatch(Function<T, R> tryFunction, BiConsumer<Exception, T> catchFunction,
                                                 R resultOnError, boolean printStackTrace) {
        return t -> {
            try { return tryFunction.apply(t); }
            catch (Exception e) {
                catchFunction.accept(e, t);
                if (printStackTrace) e.printStackTrace();
                return resultOnError;
            }
        };
    }

    public static  <T,R> Function<T, R> tryCatch(Function<T, R> tryFunction, BiConsumer<Exception, T> catchFunction,
                                                 boolean printStackTrace) {
        return tryCatch(tryFunction, catchFunction, null, printStackTrace);
    }

    public static  <T,R> Function<T, R> tryCatch(Function<T, R> tryFunction, BiConsumer<Exception, T> catchFunction) {
        return tryCatch(tryFunction, catchFunction, null, false);
    }
}
