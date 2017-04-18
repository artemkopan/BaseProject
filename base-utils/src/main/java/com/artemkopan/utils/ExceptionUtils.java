package com.artemkopan.utils;

/**
 * Created for Jabrool
 * Author: Kopan Artem
 * 28.09.2016
 */

public final class ExceptionUtils {

    /**
     * Check object for NULL and return {@link IllegalAccessException} if true
     */
    public static <T> void checkNull(Object object, Class<T> clazz) {
        if (object == null) {
            throw new IllegalArgumentException(clazz.getName() + "is null!");
        }
    }

}
