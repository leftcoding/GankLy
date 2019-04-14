package com.leftcoding.network.base;

import android.content.Context;

/**
 * Create by LingYan on 2018-09-21
 */

public abstract class Server extends ServerDisposable {

    protected Server(Context context) {
        super(context);
    }

    public abstract void init(Context context);
}
