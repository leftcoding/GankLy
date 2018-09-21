package com.gank.gankly.ui.baisi;

import com.gank.gankly.bean.BuDeJieVideo;
import com.gank.gankly.mvp.source.remote.BuDeJieDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-11-30
 */

public class BaiSiVideoPresenter implements BaiSiVideoContract.Presenter {
    private final BuDeJieDataSource mTask;
    private final BaiSiVideoContract.View mView;
    private int mNextPage;

    public BaiSiVideoPresenter(BuDeJieDataSource task, BaiSiVideoContract.View view) {
        mTask = task;
        mView = view;
    }

    @Override
    public void fetchNew() {
        mNextPage = 0;
        fetchData(0);
    }

    @Override
    public void fetchMore() {
        fetchData(mNextPage);
    }

    private void fetchData(int page) {
        mTask.fetchVideo(page).subscribe(new Observer<BuDeJieVideo>() {
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
            public void onNext(BuDeJieVideo baiSiBean) {
                List<BuDeJieVideo.ListBean> list = baiSiBean.getList();
                if (!ListUtils.isEmpty(list)) {
                    if (mNextPage == 0) {
                        mView.refillData(list);
                    } else {
                        mView.appendData(list);
                    }
                    mNextPage = baiSiBean.getInfo().getNp();
                }
            }
        });
    }
}
