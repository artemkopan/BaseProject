package com.artemkopan.baseproject.utils;

import java.util.Collection;

/**
 * Created by Artem Kopan for jabrool
 * 18.11.16
 */

public class CollectionUtils {

    /**
     * Check your collections on null and {@link Collection#isEmpty()}
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
