package com.timywimy.util;

public class StringUtil {

    private StringUtil() {
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isOnlySpaces(String s) {
        return isEmpty(s) || isEmpty(s.trim());
    }
}
