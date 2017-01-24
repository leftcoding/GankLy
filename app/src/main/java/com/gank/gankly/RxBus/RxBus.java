package com.gank.gankly.RxBus;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Create by LingYan on 2016-12-15
 * Email:137387869@qq.com
 */
public class RxBus {
    private final Subject<Object> bus;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private RxBus() {
        bus = PublishSubject.create().toSerialized();
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
        bus.onNext(object);
    }

    /**
     * 接收
     */
    public <T> Observable<T> toObservable(final Class<T> type) {
        return bus.ofType(type);
    }
}