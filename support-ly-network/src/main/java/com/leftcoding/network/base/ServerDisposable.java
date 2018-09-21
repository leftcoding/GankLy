package com.leftcoding.network.base;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2018-09-21
 */

public class ServerDisposable {
    private Map<String, CompositeDisposable> disposableMap = new ConcurrentHashMap<>();
    protected Context context;

    ServerDisposable(Context context) {
        this.context = context;
    }

    public void addExec(String tag, Disposable disposable) {
        CompositeDisposable _disposable = disposableMap.get(tag);
        if (_disposable == null) {
            _disposable = new CompositeDisposable();
            disposableMap.put(tag, _disposable);
        }
        _disposable.add(disposable);
    }

    public void remove(String tag) {
        CompositeDisposable _disposable = disposableMap.get(tag);
        if (_disposable != null && !_disposable.isDisposed()) {
            _disposable.dispose();
        }
    }

    public void clear() {
        for (Map.Entry<String, CompositeDisposable> entry : disposableMap.entrySet()) {
            CompositeDisposable compositeDisposable = entry.getValue();
            if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
                compositeDisposable.dispose();
            }
        }
        disposableMap.clear();
    }
}
