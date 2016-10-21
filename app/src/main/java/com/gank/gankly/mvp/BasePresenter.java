package com.gank.gankly.mvp;

import com.gank.gankly.mvp.rx.RxManager;

/**
 * Create by LingYan on 2016-10-21
 * Email:137387869@qq.com
 */

public abstract class BasePresenter<M extends BaseModel, V extends BaseView> {
    protected M mModel;
    protected V mModelView;
    protected RxManager mRxManager = new RxManager();

    public void setModel(M m, V v) {
        this.mModel = m;
        this.mModelView = v;
    }

    public void onDestroy() {
        mRxManager.clear();
    }
}
