package com.gank.gankly.ui.baisi;

import com.gank.gankly.bean.BuDeJieVideo;
import com.gank.gankly.mvp.ILoadMorePresenter;
import com.gank.gankly.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-11-30
 */

public interface BaiSiVideoContract {
    interface View extends SupportView {
        void refillData(List<BuDeJieVideo.ListBean> list);

        void appendData(List<BuDeJieVideo.ListBean> list);
    }

    interface Presenter extends ILoadMorePresenter {

    }
}
