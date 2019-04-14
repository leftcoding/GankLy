package com.leftcoding.rxbus;

import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2018-03-20
 */

public interface RxActionManager {
    void add(String tag, Disposable disposable);

    void clear(String tag);

    void clearAll();
}
