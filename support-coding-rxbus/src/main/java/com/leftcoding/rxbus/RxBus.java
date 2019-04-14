package com.leftcoding.rxbus;

import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Create by LingYan on 2017-10-15
 */

public class RxBus {
    private final FlowableProcessor<Object> mBus;

    private RxBus() {
        mBus = PublishProcessor.create().toSerialized();
    }

    public synchronized <T> void onNext(T t) {
        mBus.onNext(t);
    }

    public synchronized <T> void toObservable(Class<T> t) {
        mBus.ofType(t);
    }

    public static RxBus get() {
        return RxBusHolder.INSTANCE;
    }

    private static final class RxBusHolder {
        private static final RxBus INSTANCE = new RxBus();
    }
}
