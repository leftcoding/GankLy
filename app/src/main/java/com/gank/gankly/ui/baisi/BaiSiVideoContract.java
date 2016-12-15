package com.gank.gankly.ui.baisi;

import com.gank.gankly.bean.BuDeJieVideo;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
 */

public interface BaiSiVideoContract {
    interface View extends IFetchView {
        void refillData(List<BuDeJieVideo.ListBean> list);

        void appendData(List<BuDeJieVideo.ListBean> list);
    }

    interface Presenter extends IFetchPresenter {

    }
}
