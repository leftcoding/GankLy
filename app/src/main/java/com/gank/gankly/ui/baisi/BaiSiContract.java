package com.gank.gankly.ui.baisi;

import com.gank.gankly.bean.BaiSiBean;
import com.gank.gankly.mvp.IFetchPresenter;
import com.gank.gankly.mvp.IFetchView;

import java.util.List;

/**
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
 */

public interface BaiSiContract {
    interface View extends IFetchView {
        void refillData(List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list);

        void appendData(List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list);
    }

    interface Presenter extends IFetchPresenter {

    }
}
