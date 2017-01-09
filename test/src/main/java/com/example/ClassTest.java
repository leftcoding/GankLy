package com.example;

import java.util.Collections;
import java.util.List;

/**
 * Create by LingYan on 2017-01-09
 * Email:137387869@qq.com
 */

public class ClassTest {

    public static void main(String[] args) {
        ClassTest g = new ClassTest();
        System.out.print("123" + g.getList());

        if (g.getList() == null) {
            System.out.print("getList == null");
        } else {
            System.out.print("getList != null");
        }
        if (g.getList().isEmpty()) {
            System.out.print("getList().isEmpty()");
        }

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
