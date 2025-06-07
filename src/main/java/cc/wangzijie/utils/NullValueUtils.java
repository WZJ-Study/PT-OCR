package cc.wangzijie.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 空值处理。
 *
 * @author 王子杰
 * @since 2021-11-15
 */
public class NullValueUtils {

    public static boolean isAllNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (Object o : objects) {
            if (o != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnyNull(Object... objects) {
        if (objects == null) {
            return false;
        }
        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoneNull(Object... objects) {
        return !isAnyNull(objects);
    }

    public static <T> T onNull(T input, T valueIfNull) {
        if (input == null) {
            return valueIfNull;
        }
        return input;
    }

    public static <T> T onNullBatch(T input, T... valueIfNull) {
        if (valueIfNull == null || valueIfNull.length == 0) {
            return input;
        }
        if (input == null) {
            for (T t : valueIfNull) {
                if (t != null) {
                    return t;
                }
            }
            return null;
        }
        return input;
    }

    public static <T> T onNull(T input, Supplier<T> supplierIfNull) {
        if (input == null) {
            return supplierIfNull.get();
        }
        return input;
    }

    public static <T, R> R onNull(R input, T paramIfNull, Function<T, R> funcIfNull) {
        if (input == null) {
            return funcIfNull.apply(paramIfNull);
        }
        return input;
    }

    public static <T> void onNotNull(T input, Consumer<T> consumerIfNotNull) {
        if (input != null) {
            consumerIfNotNull.accept(input);
        }
    }

    public static <T, R> R onNotNull(T input, Function<T, R> funcIfNotNull) {
        if (input != null) {
            return funcIfNotNull.apply(input);
        }
        return null;
    }

    public static <T, R> R onNotNull(T input, Function<T, R> funcIfNotNull, R valueIfNull) {
        if (input != null) {
            return funcIfNotNull.apply(input);
        }
        return valueIfNull;
    }

    public static <T, P> void onNotNull(T input, P paramIfNotNull, Consumer<P> consumerIfNotNull) {
        if (input != null) {
            consumerIfNotNull.accept(paramIfNotNull);
        }
    }

    public static <T, P, R> R onNotNull(T input, P paramIfNotNull, Function<P, R> funcIfNotNull) {
        if (input != null) {
            return funcIfNotNull.apply(paramIfNotNull);
        }
        return null;
    }

    public static <T, P, R> R onNotNull(T input, P paramIfNotNull, Function<P, R> funcIfNotNull, R valueIfNull) {
        if (input != null) {
            return funcIfNotNull.apply(paramIfNotNull);
        }
        return valueIfNull;
    }

    public static String onEmpty(String input, String valueIfEmpty) {
        if (StringUtils.isEmpty(input)) {
            return valueIfEmpty;
        }
        return input;
    }

    public static String onEmptyBatch(String input, String... valueIfEmpty) {
        if (valueIfEmpty == null || valueIfEmpty.length == 0) {
            return input;
        }
        if (StringUtils.isEmpty(input)) {
            for (String t : valueIfEmpty) {
                if (StringUtils.isNotEmpty(t)) {
                    return t;
                }
            }
            return null;
        }
        return input;
    }

    public static String onEmpty(String input, Supplier<String> supplierIfEmpty) {
        if (StringUtils.isEmpty(input)) {
            return supplierIfEmpty.get();
        }
        return input;
    }

    public static <T> String onEmpty(String input, T paramIfEmpty, Function<T, String> funcIfEmpty) {
        if (StringUtils.isEmpty(input)) {
            return funcIfEmpty.apply(paramIfEmpty);
        }
        return input;
    }

    public static String onBlank(String input, String valueIfBlank) {
        if (StringUtils.isBlank(input)) {
            return valueIfBlank;
        }
        return input;
    }

    public static String onBlankBatch(String input, String... valueIfBlank) {
        if (valueIfBlank == null || valueIfBlank.length == 0) {
            return input;
        }
        if (StringUtils.isBlank(input)) {
            for (String t : valueIfBlank) {
                if (StringUtils.isNotBlank(t)) {
                    return t;
                }
            }
            return null;
        }
        return input;
    }

    public static String onBlank(String input, Supplier<String> supplierIfBlank) {
        if (StringUtils.isBlank(input)) {
            return supplierIfBlank.get();
        }
        return input;
    }

    public static <T> String onBlank(String input, T paramIfBlank, Function<T, String> funcIfBlank) {
        if (StringUtils.isBlank(input)) {
            return funcIfBlank.apply(paramIfBlank);
        }
        return input;
    }

    public static void onNotEmpty(String input, Consumer<String> consumerIfNotEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            consumerIfNotEmpty.accept(input);
        }
    }

    public static <R> R onNotEmpty(String input, Function<String, R> funcIfNotEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            return funcIfNotEmpty.apply(input);
        }
        return null;
    }

    public static <R> R onNotEmpty(String input, Function<String, R> funcIfNotEmpty, R valueIfEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            return funcIfNotEmpty.apply(input);
        }
        return valueIfEmpty;
    }

    public static <R> R onNotEmpty(String input, Function<String, R> funcIfNotEmpty, Supplier<R> supplierIfEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            return funcIfNotEmpty.apply(input);
        }
        return supplierIfEmpty.get();
    }

    public static <T, R> R onNotEmpty(String input,
                                      Function<String, R> funcIfNotEmpty,
                                      T paramIfEmpty,
                                      Function<T, R> funcIfIfEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            return funcIfNotEmpty.apply(input);
        }
        return funcIfIfEmpty.apply(paramIfEmpty);
    }

    public static <P> void onNotEmpty(String input, P paramIfNotEmpty, Consumer<P> consumerIfNotEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            consumerIfNotEmpty.accept(paramIfNotEmpty);
        }
    }

    public static <P, R> R onNotEmpty(String input, P paramIfNotEmpty, Function<P, R> funcIfNotEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            return funcIfNotEmpty.apply(paramIfNotEmpty);
        }
        return null;
    }

    public static <P, R> R onNotEmpty(String input, P paramIfNotEmpty, Function<P, R> funcIfNotEmpty, R valueIfEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            return funcIfNotEmpty.apply(paramIfNotEmpty);
        }
        return valueIfEmpty;
    }

    public static <P, R> R onNotEmpty(String input,
                                      P paramIfNotEmpty,
                                      Function<P, R> funcIfNotEmpty,
                                      Supplier<R> supplierIfEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            return funcIfNotEmpty.apply(paramIfNotEmpty);
        }
        return supplierIfEmpty.get();
    }

    public static <P, T, R> R onNotEmpty(String input,
                                         P paramIfNotEmpty,
                                         Function<P, R> funcIfNotEmpty,
                                         T paramIfEmpty,
                                         Function<T, R> funcIfIfEmpty) {
        if (StringUtils.isNotEmpty(input)) {
            return funcIfNotEmpty.apply(paramIfNotEmpty);
        }
        return funcIfIfEmpty.apply(paramIfEmpty);
    }

    public static void onNotBlank(String input, Consumer<String> consumerIfNotBlank) {
        if (StringUtils.isNotBlank(input)) {
            consumerIfNotBlank.accept(input);
        }
    }

    public static <R> R onNotBlank(String input, Function<String, R> funcIfNotBlank) {
        if (StringUtils.isNotBlank(input)) {
            return funcIfNotBlank.apply(input);
        }
        return null;
    }

    public static <R> R onNotBlank(String input, Function<String, R> funcIfNotBlank, R valueIfBlank) {
        if (StringUtils.isNotBlank(input)) {
            return funcIfNotBlank.apply(input);
        }
        return valueIfBlank;
    }


    public static <R> R onNotBlank(String input, Function<String, R> funcIfNotBlank, Supplier<R> supplierIfBlank) {
        if (StringUtils.isNotBlank(input)) {
            return funcIfNotBlank.apply(input);
        }
        return supplierIfBlank.get();
    }


    public static <T, R> R onNotBlank(String input,
                                      Function<String, R> funcIfNotBlank,
                                      T paramIfBlank,
                                      Function<T, R> funcIfBlank) {
        if (StringUtils.isNotBlank(input)) {
            return funcIfNotBlank.apply(input);
        }
        return funcIfBlank.apply(paramIfBlank);
    }

    public static <P> void onNotBlank(String input, P paramIfNotBlank, Consumer<P> consumerIfNotBlank) {
        if (StringUtils.isNotBlank(input)) {
            consumerIfNotBlank.accept(paramIfNotBlank);
        }
    }

    public static <P, R> R onNotBlank(String input, P paramIfNotBlank, Function<P, R> funcIfNotBlank) {
        if (StringUtils.isNotBlank(input)) {
            return funcIfNotBlank.apply(paramIfNotBlank);
        }
        return null;
    }

    public static <P, R> R onNotBlank(String input, P paramIfNotBlank, Function<P, R> funcIfNotBlank, R valueIfBlank) {
        if (StringUtils.isNotBlank(input)) {
            return funcIfNotBlank.apply(paramIfNotBlank);
        }
        return valueIfBlank;
    }

    public static <P, R> R onNotBlank(String input,
                                      P paramIfNotBlank,
                                      Function<P, R> funcIfNotBlank,
                                      Supplier<R> supplierIfBlank) {
        if (StringUtils.isNotBlank(input)) {
            return funcIfNotBlank.apply(paramIfNotBlank);
        }
        return supplierIfBlank.get();
    }

    public static <P, T, R> R onNotBlank(String input,
                                         P paramIfNotBlank,
                                         Function<P, R> funcIfNotBlank,
                                         T paramIfBlank,
                                         Function<T, R> funcIfBlank) {
        if (StringUtils.isNotBlank(input)) {
            return funcIfNotBlank.apply(paramIfNotBlank);
        }
        return funcIfBlank.apply(paramIfBlank);
    }

    public static <T> String toString(T input) {
        if (input == null) {
            return "";
        }
        return input.toString();
    }

    public static <T> String toString(T input, String valueIfNull) {
        if (input == null) {
            return valueIfNull;
        }
        return input.toString();
    }

    public static <T> String toString(T input, Function<T, String> toStringFunc) {
        if (input == null) {
            return "";
        }
        return toStringFunc.apply(input);
    }

    public static <T> String toString(T input, Function<T, String> toStringFunc, String valueIfNull) {
        if (input == null) {
            return valueIfNull;
        }
        return toStringFunc.apply(input);
    }


    public static <K, V> List<V> getListFromMap(Map<K, List<V>> map, K key, Supplier<List<V>> emptyListSupplier) {
        if (map.containsKey(key)) {
            List<V> list = map.get(key);
            if (list != null) {
                return list;
            }
        }
        return emptyListSupplier.get();
    }
}
