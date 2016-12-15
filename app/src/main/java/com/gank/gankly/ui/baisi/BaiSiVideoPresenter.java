package com.gank.gankly.ui.baisi;

import com.gank.gankly.bean.BuDeJieVideo;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.BaiSiDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
 */

public class BaiSiVideoPresenter extends FetchPresenter implements BaiSiVideoContract.Presenter {
    private BaiSiDataSource mTask;
    private BaiSiVideoContract.View mView;
    private int mNextPage;

    public BaiSiVideoPresenter(BaiSiDataSource task, BaiSiVideoContract.View view) {
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
        mTask.fetchVideo(page).subscribe(new Subscriber<BuDeJieVideo>() {
            @Override
            public void onCompleted() {
                mView.hideRefresh();
                setFetchPage(getFetchPage() + 1);
            }

            @Override
            public void onError(Throwable e) {
                mView.hideRefresh();
                KLog.e(e);
            }

            @Override
            public void onNext(BuDeJieVideo baiSiBean) {
                List<BuDeJieVideo.ListBean> list = baiSiBean.getList();
                if (!ListUtils.isListEmpty(list)) {
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

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
