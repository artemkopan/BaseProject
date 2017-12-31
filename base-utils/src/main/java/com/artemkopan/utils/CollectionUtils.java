package com.artemkopan.utils;

import java.util.Collection;

/**
 * Created by Artem Kopan for jabrool
 * 18.11.16
 */

public final class CollectionUtils {

    /**
     * Check your collections on null and {@link Collection#isEmpty()}
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Get null-safely collection size
     *
     * @param collection -
     * @return if collection is null then return 0 else return {@link Collection#size()}
     */
    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

}
