package com.gank.gankly.utils;

import java.util.List;

/**
 * Create by LingYan on 2016-04-06
 */
public class ListUtils {
    public static <E> int getListSize(List<E> list) {
        if (list != null && !list.isEmpty()) {
            return list.size();
        }
        return 0;
    }

    public static <E> boolean isListEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }
}
