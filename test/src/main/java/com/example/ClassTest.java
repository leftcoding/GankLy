package com.example;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Create by LingYan on 2017-01-09
 * Email:137387869@qq.com
 */

public class ClassTest {

    public static void main(String[] args) {
//        ClassTest g = new ClassTest();
//        B b = new B();
//        g.getA(b);
//        System.out.print("123" + g.getList());
//
//        if (g.getList() == null) {
//            System.out.print("getList == null");
//        } else {
//            System.out.print("getList != null");
//        }
//        if (g.getList().isEmpty()) {
//            System.out.print("getList().isEmpty()");
//        }


//        List<String> a = new ArrayList<String>();
//        a.add("1");
//        a.add("2");
//        for (String temp : a) {
//            if ("1".equals(temp)) {
//                a.remove(temp);
//            }
//        }
//
//        for (String a1 : a){
//            System.out.println(a1);
//        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        simpleDateFormat.setTimeZone(timeZone);
//        long time = Long.valueOf("1486709632118");
        System.out.println(simpleDateFormat.format(new Date()));
        System.out.print("" +System.currentTimeMillis());
    }

    public void getA(IA a) {
        System.out.print("getA");
    }

    public List<A> getList() {
        return Collections.EMPTY_LIST;
    }

    public static class A {
        private String ss;

        public String getSs() {
            return ss;
        }

        public void setSs(String ss) {
            this.ss = ss;
        }
    }
}
