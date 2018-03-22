package com.gank.gankly.ui.android;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.PageView;

/**
 * Create by LingYan on 2016-10-25
 */

public interface AndroidContract {
    interface View extends PageView<Gank> {
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }

        protected abstract void refreshAndroid();

        protected abstract void appendAndroid();
    }
}
