package com.gank.gankly.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.base.BasePresenter;
import com.gank.gankly.mvp.base.BaseView;

/**
 * Create by LingYan on 2017-10-03
 */

public interface IndexContract {
    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<View> {

       public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }
    }
}
