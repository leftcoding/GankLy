package com.gank.gankly.ui.baisi.image;

import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.mvp.ILoadMorePresenter;
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

    interface Presenter extends ILoadMorePresenter {

    }
}
