package com.gank.gankly.ui.history;

import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.LocalDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-10-31
 * Email:137387869@qq.com
 */

public class BrowseHistoryPresenter extends FetchPresenter implements BrowseHistoryContract.Presenter {
    private LocalDataSource mTask;
    private BrowseHistoryContract.View mModelView;

    public BrowseHistoryPresenter(LocalDataSource task, BrowseHistoryContract.View view) {
        mTask = task;
        mModelView = view;
    }

    @Override
    public void fetchNew() {
        fetch(initOffSet());
    }

    @Override
    public void fetchMore() {
        fetch(getOffSet());
    }

    private void fetch(int offset) {
        mTask.selectReadHistory(offset, getFetchLimit())
                .subscribe(new Subscriber<List<ReadHistory>>() {
                    @Override
                    public void onCompleted() {
                        mModelView.hideRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onNext(List<ReadHistory> readHistories) {
                        int size = ListUtils.getListSize(readHistories);
                        KLog.d("size:" + size);
                        if (size > 0) {
                            mModelView.refillData(readHistories);
                        } else {
                            mModelView.showEmpty();
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
