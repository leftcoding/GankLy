package com.gank.gankly.RxBus;


import rx.Observer;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

public class RxBus {

    private static volatile RxBus sRxBus;
    private final SerializedSubject<Object, Object> subject;

    private RxBus() {
        subject = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        RxBus bus = sRxBus;
        if (bus == null) {
            synchronized (RxBus.class) {
                bus = sRxBus;
                if (sRxBus == null) {
                    bus = new RxBus();
                    sRxBus = bus;
                }
            }
        }
        return bus;
    }

    public void post(Object object) {
        subject.onNext(object);
    }

    private <T> Observable<T> toObservable(final Class<T> type) {
        return subject.ofType(type);
    }

    public boolean hasObservers() {
        return subject.hasObservers();
    }

    public <T> Subscription toSubscription(final Class<T> type, Observer<T> observer) {
        return toObservable(type).subscribe(observer);
    }

    public <T> Subscription toSubscription(final Class<T> type, Action1<T> action1) {
        return toObservable(type).subscribe(action1);
    }
}