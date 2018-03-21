package com.gank.gankly.ui.discovered.video;

import android.ly.business.domain.Gank;

import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2017-01-03
 */

public interface VideoContract {
    interface View extends SupportView {
        void refillData(List<Gank> list);

        void appendData(List<Gank> list);
    }

    interface Presenter extends ILoadMorePresenter {

    }
}

