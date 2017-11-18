package com.example.javalib;

/**
 * Create by LingYan on 2017-10-17
 */

abstract class B {
    public B() {
        A.getA().setA(this);
    }

    public void HH() {

    }

    public abstract void change(String s);

    public static void main(String[] args) {

    }
}
