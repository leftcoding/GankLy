package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Create by LingYan on 2017-02-10
 * Email:137387869@qq.com
 */

public class B {

    private void p(List<Xx> list) {
        List<Xx> list2 = new ArrayList<>();
        Collections.copy(list, list2);
//        list2.set(0, new Xx("123", 2));
        list2.add(new Xx("1323", 2));

        for (Xx s : list2) {
            Print.print("l2:" + s.toString());
        }
    }

    public static void main(String[] args) {
        ArrayList<Xx> list = new ArrayList<>();
        list.add(new Xx("123", 1));
        B b = new B();
        b.p(list);

        for (Xx s : list) {
            Print.print(s.toString());
        }
    }
}

class Xx {
    String mString;
    int x;

    public Xx(String string, int x) {
        mString = string;
        this.x = x;
    }

    public String getString() {
        return mString;
    }

    public void setString(String string) {
        mString = string;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Xx{" +
                "mString='" + mString + '\'' +
                ", x=" + x +
                '}';
    }
}
