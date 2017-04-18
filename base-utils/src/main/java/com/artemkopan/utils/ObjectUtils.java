package com.artemkopan.utils;

import android.support.annotation.Nullable;

/**
 * Created by Artem Kopan for jabrool
 * 05.01.17
 */

@SuppressWarnings("WeakerAccess")
public class ObjectUtils {

    /**
     * Equals two objects with check for null;
     *
     * @return true if objects equals;
     */
    public static boolean equalsObject(Object first, Object second) {
        return (first == null && second == null) || (first != null && first.equals(second));
    }

    /**
     * Check obj instance of String and cast them. If obj != String or empty return empty string;
     */
    public static String castToString(Object obj) {
        return instanceOfString(obj) ? (String) obj : "";
    }

    public static int castToInteger(Object obj) {
        return castToInteger(obj, 0);
    }
    public static int castToInteger(Object obj, int defaultValue) {
        return instanceOfInt(obj) ? (int) obj : defaultValue;
    }

    /**
     * Check object instance and return with cast;
     *
     * @return If instance wrong or object null then return null
     */
    @Nullable
    public static <T> T castObject(Object obj, Class<T> clazz) {
        return instanceOf(obj, clazz) ? clazz.cast(obj) : null;
    }

    /**
     * @return If object instance is Integer return true
     */
    public static boolean instanceOfInt(Object object) {
        return instanceOf(object, Integer.class);
    }

    /**
     * @return If object instance is String return true
     */
    public static boolean instanceOfString(Object object) {
        return instanceOf(object, String.class);
    }

    /**
     * Check instance of Object; If object is null then return false;
     */
    public static <T> boolean instanceOf(Object obj, Class<T> clazz) {
        return clazz.isInstance(obj);
    }
}
