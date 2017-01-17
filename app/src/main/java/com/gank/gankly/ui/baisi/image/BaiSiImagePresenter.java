package com.gank.gankly.ui.baisi.image;

import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.BuDeJieDataSource;
import com.socks.library.KLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-05
 * Email:137387869@qq.com
 */

public class BaiSiImagePresenter extends FetchPresenter implements BaiSiImageContract.Presenter {
    private BuDeJieDataSource mTask;
    private BaiSiImageContract.View mView;
    private int np;

    public BaiSiImagePresenter(BuDeJieDataSource task, BaiSiImageContract.View view) {
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
        mTask.fetchImage(np).subscribe(new Observer<BuDeJieBean>() {
            @Override
            public void onError(Throwable e) {
                mView.hideRefresh();
                KLog.e(e);
            }

            @Override
            public void onComplete() {
                mView.hideRefresh();
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BuDeJieBean buDeJieBean) {
                KLog.d("buDeJieBean:" + buDeJieBean);
                if (buDeJieBean != null) {
                    np = buDeJieBean.getInfo().getNp();
                    KLog.d("np:" + np);
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
