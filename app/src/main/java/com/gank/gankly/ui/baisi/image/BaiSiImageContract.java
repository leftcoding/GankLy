package com.gank.gankly.ui.baisi.image;

import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-12-05
 * Email:137387869@qq.com
 */

public interface BaiSiImageContract {
    interface View extends IFetchView {
        //        void refillData(List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list);
//
//        void appendData(List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list);
        void refillData(List<BuDeJieBean.ListBean> list);

        void appendData(List<BuDeJieBean.ListBean> list);
    }

    interface Presenter extends IFetchPresenter {

    }
}
