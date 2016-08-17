package com.example;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class MyClass {
    private long mLastTime;

    public static void main(String[] args) throws InterruptedException {
        MyClass myClass = new MyClass();
        String[] names = {"A", "B", "C", "D"};
        //  System.out.println

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onCompleted();
            }
        }).ignoreElements().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println(integer);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println(throwable);
            }
        }, new Action0() {
            @Override
            public void call() {
                System.out.println("onComplete");
            }
        });
    }


    public static String getSuffixImageName(String url) {
//        if (!TextUtils.isEmpty(url)) {
        String[] strings = url.split("/");
//        }
        int size = strings.length;
        return strings[size - 1];
//        return null;
    }

    public static <E> boolean isListEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }

    private boolean checkMore() {
        if (System.currentTimeMillis() - mLastTime > 500) {
            mLastTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
