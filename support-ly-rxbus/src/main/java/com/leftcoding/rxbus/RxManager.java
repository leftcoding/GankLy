package com.leftcoding.rxbus;

import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2017-10-14
 */

public class RxManager {
    private final HashMap<String, CompositeDisposable> mDisposableHashMap = new HashMap<>();

    private RxManager() {

    }

    public static RxManager get() {
        return RxMangerHolder.INSTANCE;
    }

    private static final class RxMangerHolder {
        private static final RxManager INSTANCE = new RxManager();
    }

    public synchronized void add(String key, Disposable disposable) {
        CompositeDisposable compositeDisposable;
        if (mDisposableHashMap.containsKey(key)) {
            compositeDisposable = mDisposableHashMap.get(key);
            compositeDisposable.add(disposable);
        } else {
            compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
            mDisposableHashMap.put(key, compositeDisposable);
        }
    }

    public synchronized void clear(String key) {
        if (mDisposableHashMap.containsKey(key)) {
            CompositeDisposable disposable = mDisposableHashMap.get(key);
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
            mDisposableHashMap.remove(key);
        }
    }
}
