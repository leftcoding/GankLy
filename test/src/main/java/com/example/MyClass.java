package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;

public class MyClass {
    private long mLastTime;

    public static void main(String[] args) throws InterruptedException {
        MyClass myClass = new MyClass();
        String[] names = {"A", "B", "C", "D"};
        //  System.out.println
        ArrayList<Integer> arrays = new ArrayList<>();
        arrays.add(0);
        arrays.add(1);
        arrays.add(2);
        arrays.add(3);
        arrays.add(4);
        Observable.interval(1, TimeUnit.SECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("------ onNext onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("------ onNext " + aLong);
                    }
                });
    }

    private Observable<Integer> FromArray() {
        ArrayList<Integer> arrays = new ArrayList<>();
        arrays.add(0);
        arrays.add(1);
        arrays.add(2);
        arrays.add(3);
        arrays.add(4);
        return Observable.from(arrays);
    }

    private Observable<Integer> FromIterable() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        return Observable.from(list);
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
