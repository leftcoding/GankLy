package com.gank.gankly.ui.history;

import android.content.Context;

import com.gank.gankly.mvp.source.LocalDataSource;
import com.socks.library.KLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-10-31
 */

public class BrowseHistoryPresenter extends BrowseHistoryContract.Presenter {
    private LocalDataSource mTask;
    private BrowseHistoryContract.View mModelView;

    public BrowseHistoryPresenter(Context context, BrowseHistoryContract.View view) {
        super(context, view);
    }

//    @Override
    public void fetchNew() {
//        setOffSetPage(0);
//        setHasMore(true);
        fetch();
    }

//    @Override
    public void fetchMore() {
//        if (hasMore()) {
//            mModelView.showProgress();
//            fetch();
//        }
    }

    private void fetch() {
//        int offset = getOffSet();
//        mTask.selectReadHistory(offset, getFetchLimit())
//                .subscribe(new Observer<List<ReadHistory>>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        KLog.e(e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mModelView.hideProgress();
//                        int nextOffsetPage = getOffSetPage() + 1;
//                        setOffSetPage(nextOffsetPage);
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<ReadHistory> readHistories) {
//                        List<ReadHistory> list = filterDataBase(readHistories, mModelView);
//                        int size = ListUtils.getSize(list);
//                        if (size > 0) {
//                            if (getOffSetPage() == 0) {
//                                mModelView.refillData(list);
//                            } else {
//                                mModelView.appendData(list);
//                            }
//                        }
//                    }
//                });
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void onDestroy() {

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
