package com.gank.gankly.ui.discovered.video;

import android.ly.business.domain.Gank;

import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.mvp.FetchPresenter;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.socks.library.KLog;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2017-01-03
 */

public class VideoPresenter extends FetchPresenter implements VideoContract.Presenter {
    private GankDataSource mTask;
    private VideoContract.View mModelView;

    VideoPresenter(GankDataSource task, VideoContract.View view) {
        mTask = task;
        mModelView = view;
    }

    @Override
    public void fetchNew() {
        fetchData(getInitPage());
    }

    @Override
    public void fetchMore() {
        fetchData(getFetchPage());
    }

    private void fetchData(int page) {
        Observable<GankResult> observable;
        if (MeiziArrayList.getInstance().isOneItemsEmpty()) {
            observable = mTask.fetchVideoAndImages(page, getFetchLimit());
        } else {
            observable = mTask.fetchVideo(page, getFetchLimit());
        }

        observable.subscribe(new Observer<GankResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GankResult gankResult) {
                List<Gank> list = filterData(gankResult.getResults(), mModelView);
                if (list != null) {
                    if (page == 1) {
                        mModelView.refillData(list);
                    } else {
                        mModelView.appendData(list);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                parseError(mModelView);
            }

            @Override
            public void onComplete() {
                setFetchPage(page + 1);
            }
        });
    }

    @Override
    public void unSubscribe() {

    }
}
