package com.gank.gankly.ui.history;

import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.LocalDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
        setOffSetPage(0);
        setHasMore(true);
        fetch();
    }

    @Override
    public void fetchMore() {
        if (isHasMore()) {
            mModelView.showRefresh();
            fetch();
        }
    }

    private void fetch() {
        int offset = getOffSet();
        mTask.selectReadHistory(offset, getFetchLimit())
                .subscribe(new Observer<List<ReadHistory>>() {
                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onComplete() {
                        mModelView.hideRefresh();
                        int nextOffsetPage = getOffSetPage() + 1;
                        setOffSetPage(nextOffsetPage);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ReadHistory> readHistories) {
                        List<ReadHistory> list = filterDataBase(readHistories, mModelView);
                        int size = ListUtils.getListSize(list);
                        if (size > 0) {
                            if (getOffSetPage() == 0) {
                                mModelView.refillData(list);
                            } else {
                                mModelView.appendData(list);
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

    @Override
    public void deleteHistory(long id) {
        mTask.deleteHistory(id).subscribe(new Observer<String>() {
            @Override
            public void onError(Throwable e) {
                KLog.e(e);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
            }
        });
    }
}
