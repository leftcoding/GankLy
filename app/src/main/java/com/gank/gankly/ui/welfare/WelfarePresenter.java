package com.gank.gankly.ui.welfare;

import android.content.Context;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.socks.library.KLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-23
 */

public class WelfarePresenter extends WelfareContract.Presenter {
    private WelfareContract.View mModelView;
    private GankDataSource mTask;

    WelfarePresenter(Context context, WelfareContract.View view) {
        super(context, view);
    }

    //    @Override
    public void fetchNew() {
//        fetchData(getInitPage());
    }

    private void fetchData(final int page) {
        mTask.fetchWelfare(page, 20)
                .subscribe(new Observer<GankResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GankResult gankResult) {
//                        List<Gank> list = filterData(gankResult.getResults(), mModelView);
//                        if (list != null) {
//                            if (page == 1) {
//                                mModelView.refreshData(list);
//                            } else {
//                                mModelView.appendData(list);
//                            }
//                            MeiziArrayList.getInstance().addImages(list, page);
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
//                        parseError(mModelView);
                    }

                    @Override
                    public void onComplete() {
//                        setFetchPage(page + 1);
                    }
                });
    }

    //    @Override
    public void fetchMore() {
//        if (hasMore()) {
//            mModelView.showProgress();
//            fetchData(getFetchPage());
//        }
    }

    @Override
    public void unSubscribe() {

    }
}
