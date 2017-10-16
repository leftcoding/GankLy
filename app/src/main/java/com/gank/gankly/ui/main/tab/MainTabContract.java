package com.gank.gankly.ui.main.tab;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.base.BaseView;
import com.gank.gankly.mvp.base.BasePresenter;

/**
 * Create by LingYan on 2017-09-28
 */

public interface MainTabContract {
    interface View extends BaseView {

    }

    public abstract class Presenter extends BasePresenter<View> {
        public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }

        public abstract void loadPicture();
    }

}
