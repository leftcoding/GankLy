package com.gank.gankly.ui.baisi.image;

import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.BaiSiDataSource;
import com.socks.library.KLog;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-12-05
 * Email:137387869@qq.com
 */

public class BaiSiImagePresenter extends FetchPresenter implements BaiSiImageContract.Presenter {
    private static final String TYPE_IMAGE = "10";
    private BaiSiDataSource mTask;
    private BaiSiImageContract.View mView;
    private int np;

    public BaiSiImagePresenter(BaiSiDataSource task, BaiSiImageContract.View view) {
        mTask = task;
        mView = view;
    }

    @Override
    public void fetchNew() {
        np = 0;
        fetchData();
    }

    @Override
    public void fetchMore() {
        mView.showRefresh();
        fetchData();
    }

    private void fetchData() {
        mTask.fetchImage(np).subscribe(new Subscriber<BuDeJieBean>() {
            @Override
            public void onCompleted() {
                mView.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideRefresh();
                KLog.e(e);
            }

            @Override
            public void onNext(BuDeJieBean buDeJieBean) {
                if (buDeJieBean != null) {
                    np = buDeJieBean.getInfo().getNp();
                    if (np == 0) {
                        mView.refillData(buDeJieBean.getList());
                    } else {
                        mView.appendData(buDeJieBean.getList());
                    }
                }
            }
        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
