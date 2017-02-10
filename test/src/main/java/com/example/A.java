package com.example;

/**
 * Create by LingYan on 2017-02-10
 * Email:137387869@qq.com
 */

public class A implements IA {
    @Override
    public void show() {
        System.out.print("AImpl show");
    }

    @Override
    public void hide() {
        System.out.print("AImpl hide");
    }
}
