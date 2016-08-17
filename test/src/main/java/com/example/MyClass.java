package com.example;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class MyClass {
    private long mLastTime;

    public static void main(String[] args) throws InterruptedException {
        MyClass myClass = new MyClass();
        String[] names = {"A", "B", "C", "D"};

        Observable.range(1, 10)
//                .firstOrDefault(3)
                .firstOrDefault(4, new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return false;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("result: " + integer);
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
