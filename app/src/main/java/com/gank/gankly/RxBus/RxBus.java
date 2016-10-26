package com.gank.gankly.RxBus;


import android.support.annotation.NonNull;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    private static volatile RxBus sRxBus;
    private final SerializedSubject<Object, Object> subject;

    private RxBus() {
        subject = new SerializedSubject<>(PublishSubject.create());
    }

    @SuppressWarnings("rawtypes")
    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<Object, List<Subject>>();

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

    /**
     * 注册事件源
     *
     * @param tag
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<Subject>();
            subjectMapper.put(tag, subjectList);
        }
        Subject<T, T> subject;
        subjectList.add(subject = PublishSubject.create());
        KLog.d("register", tag + "  size:" + subjectList.size());
        return subject;
    }

    /**
     * 取消监听
     *
     * @param tag
     * @param observable
     * @return
     */
    @SuppressWarnings("rawtypes")
    public RxBus unregister(@NonNull Object tag,
                                                   @NonNull Observable<?> observable) {
        if (null == observable)
            return getInstance();
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove((Subject<?, ?>) observable);
            if (isEmpty(subjects)) {
                subjectMapper.remove(tag);
                KLog.d("unregister", tag + "  size:" + subjects.size());
            }
        }
        return getInstance();
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }
}