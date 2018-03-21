package com.gank.gankly.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.support.annotation.NonNull;

import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-12-20
 * Email:137387869@qq.com
 */

public interface IosContract {
    interface View extends SupportView {
        void refreshIosSuccess(List<Gank> list);

        void refreshIosFailure(String msg);

        void appendIosSuccess(List<Gank> list);

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
