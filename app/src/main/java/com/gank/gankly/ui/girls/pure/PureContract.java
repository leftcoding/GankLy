package com.gank.gankly.ui.girls.pure;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-12-27
 */

public interface PureContract {
    interface View extends SupportView {
        void refillData(List<GiftBean> list);

        void appendData(List<GiftBean> list);

        void openGalleryActivity(ArrayList<GiftBean> list);

        void disLoadingDialog();
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }

        abstract void refreshImages(String url);

        abstract void refreshPure();

        abstract void appendPure();
    }
}
