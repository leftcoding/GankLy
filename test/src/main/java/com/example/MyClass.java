package com.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyClass {
    private long mLastTime;

    public static void main(String[] args) throws InterruptedException {
        long mLastTime = 0;
        MyClass myClass = new MyClass();
        List<String> list = null;
        list = new ArrayList<>();
        System.out.println(isListEmpty(list));

        System.out.println(new Date());

        for (int i = 0; i < 3; i++) {
            if(i == 2){
                Thread.sleep(1000);
            }
            System.out.println("check:" + myClass.checkMore());
        }
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
