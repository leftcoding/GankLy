package com.gank.gankly.ui.baisi.image;

import android.content.Context;
import androidx.annotation.NonNull;

import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.mvp.base.LoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-12-05
 */

public interface BaiSiImageContract {
    interface View extends SupportView {
        void refillData(List<BuDeJieBean.ListBean> list);

        void appendData(List<BuDeJieBean.ListBean> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }
    }
}
