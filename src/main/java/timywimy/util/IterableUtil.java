package timywimy.util;

import com.google.common.collect.Iterables;

public class IterableUtil {

    private IterableUtil() {
    }

    public static <T> boolean equals(Iterable<T> iterable1, Iterable<T> iterable2) {
        return (iterable1 == null && iterable2 == null ||
                iterable1 != null && iterable2 != null && Iterables.elementsEqual(iterable1, iterable2));
    }
}
