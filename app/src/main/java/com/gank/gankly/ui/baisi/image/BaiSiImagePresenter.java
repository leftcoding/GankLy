package com.gank.gankly.ui.baisi.image;

import android.content.Context;

import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.mvp.source.remote.BuDeJieDataSource;
import com.socks.library.KLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-05
 */

public class BaiSiImagePresenter extends BaiSiImageContract.Presenter {
    private BuDeJieDataSource mTask;
    private BaiSiImageContract.View mView;
    private int np;
    private Context context;

    public BaiSiImagePresenter(Context context, BaiSiImageContract.View view) {
        super(context, view);
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
    public void destroy() {

    }

    @Override
    protected void onDestroy() {

    }
}
