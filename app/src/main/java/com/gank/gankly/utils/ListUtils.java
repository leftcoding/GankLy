package com.gank.gankly.utils;

import java.util.List;

/**
 * Create by LingYan on 2016-04-06
 */
public class ListUtils {
    public static <E> int getSize(List<E> list) {
        return list == null ? 0 : list.size();
    }

    public static <E> boolean isEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }
}
