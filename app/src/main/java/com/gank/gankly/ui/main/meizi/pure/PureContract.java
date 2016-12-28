package com.gank.gankly.ui.main.meizi.pure;

import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-12-27
 * Email:137387869@qq.com
 */

public interface PureContract {
    interface View extends IFetchView {
        void refillData(List<GiftBean> list);

        void appendData(List<GiftBean> list);

        void openGalleryActivity(ArrayList<GiftBean> list);

        void disLoadingDialog();
    }

    interface Presenter extends IFetchPresenter {
        void fetchImages(String url);
    }
}
