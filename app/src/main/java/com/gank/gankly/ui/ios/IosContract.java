package com.gank.gankly.ui.ios;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;
import com.leftcoding.http.bean.ResultsBean;

import java.util.List;

/**
 * Create by LingYan on 2016-12-20
 * Email:137387869@qq.com
 */

public interface IosContract {
    interface View extends SupportView {
        void refreshIosSuccess(List<ResultsBean> list);

        void refreshIosFailure(String msg);

        void appendIosSuccess(List<ResultsBean> list);

        void appendIosFailure(String msg);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void refreshIos();

        abstract void appendIos();
    }
}
