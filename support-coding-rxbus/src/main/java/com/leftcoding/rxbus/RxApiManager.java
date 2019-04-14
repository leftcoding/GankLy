package com.leftcoding.rxbus;

import android.util.ArrayMap;

import java.util.Iterator;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2018-03-20
 */

public class RxApiManager implements RxActionManager {
    private static RxApiManager sInstance = null;
    private final ArrayMap<String, CompositeDisposable> maps;

    public static RxApiManager get() {
        if (sInstance == null) {
            synchronized (RxApiManager.class) {
                if (sInstance == null) {
                    sInstance = new RxApiManager();
                }
            }
        }
        return sInstance;
    }

    private RxApiManager() {
        maps = new ArrayMap<>();
    }

    @Override
    public void add(String tag, Disposable disposable) {
        CompositeDisposable compositeDisposable = null;
        if (isContainsKey(tag)) {
            compositeDisposable = maps.get(tag);
        }
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            maps.put(tag, compositeDisposable);
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void clear(String tag) {
        if (isContainsKey(tag)) {
            CompositeDisposable disposable = maps.remove(tag);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.isDisposed();
            }
        }
    }

    private boolean isContainsKey(String key) {
        return !isEmpty() && maps.containsKey(key);
    }

    private boolean isEmpty() {
        return maps.isEmpty();
    }

    @Override
    public void clearAll() {
        if (!isEmpty()) {
            Iterator<Map.Entry<String, CompositeDisposable>> it = maps.entrySet().iterator();
            while (it.hasNext()) {
                it.remove();
            }
        }
    }
}
