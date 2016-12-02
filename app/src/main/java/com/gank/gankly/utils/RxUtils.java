package com.gank.gankly.utils;

import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.bean.RxCollect;

import rx.Observable;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Create by LingYan on 2016-05-20
 */
public class RxUtils {
    private static volatile RxUtils instance;
    private Subscriber<RxCollect> mSubscriber;

    public static RxUtils getInstance() {
        if (instance == null) {
            synchronized (RxUtils.class) {
                if (instance == null) {
                    instance = new RxUtils();
                }
            }
        }
        return instance;
    }

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public CompositeSubscription getCompositeSubscription() {
        return mCompositeSubscription;
    }

    public void manage(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public void unsubscribe() {
        mCompositeSubscription.unsubscribe();
        mCompositeSubscription = new CompositeSubscription();
    }

    public void unSubscribe() {
        if (mSubscriber != null) {
            mSubscriber.unsubscribe();
        }
    }

    public void OnUnCollect() {
        Observable.create(new Observable.OnSubscribe<RxCollect>() {
            @Override
            public void call(Subscriber<? super RxCollect> subscriber) {
                subscriber.onNext(new RxCollect(true));
                subscriber.onCompleted();
            }
        }).subscribe(mSubscriber);
    }

    public void unCollect(Subscriber<RxCollect> subscriber) {
        mSubscriber = subscriber;
    }
}
