package com.gank.gankly.RxBus;


import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 *
 */
public class RxBus {
    private static volatile RxBus sRxBus;
    private final Subject<Object> subject;

    private RxBus() {
//        subject = new SerializedSubject<>(PublishSubject.create());
        subject = PublishSubject.create().toSerialized();
    }

    @SuppressWarnings("rawtypes")
    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

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

    /**
     * 发送
     *
     * @param object
     */
    public void post(Object object) {
        subject.onNext(object);
    }

    /**
     * 接收
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(final Class<T> type) {
        return subject.ofType(type);
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }
}