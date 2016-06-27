package com.example;

import java.util.List;

public class MyClass {
    private long mLastTime;

    public static void main(String[] args) throws InterruptedException {
        MyClass myClass = new MyClass();
        int s = 10 & 2;
        int ss = 5 << 2;
        System.out.print("s:" + s + ",0xFF:" + 0xFF + ",5>>2:"+ss);
//        System.out.println("" + getSuffixImageName("http://ww4.sinaimg.cn/large/610dc034jw1f3litmfts1j20qo0hsac7.jpg"));
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
