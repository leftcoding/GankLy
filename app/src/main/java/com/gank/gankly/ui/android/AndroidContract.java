package com.gank.gankly.ui.android;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-25
 */

public interface AndroidContract {
    interface View extends SupportView {
        void refreshAndroidSuccess(List<Gank> list);

        void refreshAndroidFailure(String msg);

        void appendAndroidSuccess(List<Gank> list);

        void appendAndroidFailure(String msg);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }

        protected abstract void refreshAndroid(int page);

        protected abstract void appendAndroid(int page);
    }
}
