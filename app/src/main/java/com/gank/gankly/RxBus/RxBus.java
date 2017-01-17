package com.gank.gankly.RxBus;


import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 *
 */
public class RxBus {
    private final Subject<Object> subject;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private RxBus() {
        subject = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        return RxBusHolder.sInstance;
    }

    private static class RxBusHolder {
        private static final RxBus sInstance = new RxBus();
    }

    /**
     * 发送
     */
    public void post(Object object) {
        subject.onNext(object);
    }

    /**
     * 接收
     */
    public <T> Observable<T> toObservable(final Class<T> type) {
        return subject.ofType(type);
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }
}