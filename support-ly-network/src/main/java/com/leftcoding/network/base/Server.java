package com.leftcoding.network.base;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Create by LingYan on 2018-09-21
 */

public abstract class Server extends ServerDisposable {

    protected Server(Context context) {
        super(context);
    }

    protected <T> void addExec(String tag, Observable<T> observable, final ConsumerCall<T> call) {
        addExec(tag, observable.subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        call.onNext(t);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        call.accept(throwable);
                    }
                })
        );
    }

    public interface ConsumerCall<T> {
        void onNext(T t) throws Exception;

        void accept(Throwable throwable);
    }
}
