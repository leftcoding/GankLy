package com.example.javalib;

import java.util.UUID;

/**
 * Create by LingYan on 2017-10-17
 */

public class A {
    public static B mB;
    public static A ma;

    public A() {

    }

    public static A getA() {
        if (ma == null) {
            ma = new A();
        }
        return ma;
    }

    public B getB() {
        return mB;
    }

    public class W extends B {
        public W() {

        }

        @Override
        public void change(String s) {

        }
    }

    public class C extends B {

        @Override
        public void change(String s) {
            System.out.println("s" + s + ".C");
        }
    }

    public class D extends B {

        @Override
        public void change(String s) {
            System.out.println("s" + s + ".D");
        }
    }

    public void setA(B b) {
        this.mB = b;
        b.change("333");
    }

    public class E extends B {

        @Override
        public void change(String s) {
            System.out.println("s" + s + ".E");
        }
    }

    public static void main(String[] args) {
//        B b = new A().new E();
//        A.getA().getB().change("333");
        for (int i = 0; i < 1000; i++) {
            System.out.println("xxx:" + UUID.randomUUID().toString());
        }
    }
}



