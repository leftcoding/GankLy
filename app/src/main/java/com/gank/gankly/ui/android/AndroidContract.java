package com.gank.gankly.ui.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;
import com.leftcoding.http.bean.ResultsBean;

import java.util.List;

/**
 * Create by LingYan on 2016-10-25
 */

public interface AndroidContract {
    interface View extends SupportView {
        void refreshAndroidSuccess(List<ResultsBean> list);

        void refreshAndroidFailure(String msg);

        void appendAndroidSuccess(List<ResultsBean> list);

        void appendAndroidFailure(String msg);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }

        protected abstract void refreshAndroid();

        protected abstract void appendAndroid();
    }
}
