package android.lectcoding.ui.adapter;

import android.content.Context;

/**
 * Create by LingYan on 2017-11-27
 */

public abstract class BasicItem implements Item {
    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
