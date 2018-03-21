package com.leftcoding.rxbus;

/**
 * Create by LingYan on 2018-03-20
 */

public interface RxConsumer<T> {
    void next(T t) throws Exception;

    void onError(Throwable throwable);
}
