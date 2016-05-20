package com.gank.gankly.utils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Create by LingYan on 2016-05-20
 */
public class RxUtils {
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public void manage(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public void unsubscribe() {
        mCompositeSubscription.unsubscribe();
        mCompositeSubscription = new CompositeSubscription();
    }
}
