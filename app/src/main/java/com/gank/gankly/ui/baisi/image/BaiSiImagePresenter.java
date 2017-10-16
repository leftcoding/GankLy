package com.gank.gankly.ui.baisi.image;

import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.BuDeJieDataSource;
import com.socks.library.KLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-05
 */

public class BaiSiImagePresenter extends FetchPresenter implements BaiSiImageContract.Presenter {
    private final BuDeJieDataSource mTask;
    private final BaiSiImageContract.View mView;
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
        mView.showProgress();
        fetchData();
    }

    private void fetchData() {
        mTask.fetchImage(np).subscribe(new Observer<BuDeJieBean>() {
            @Override
            public void onError(Throwable e) {
                mView.hideProgress();
                KLog.e(e);
            }

            @Override
            public void onComplete() {
                mView.hideProgress();
            }

            @Override
            public void onSubscribe(Disposable d) {

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
    public void unSubscribe() {

    }
}
