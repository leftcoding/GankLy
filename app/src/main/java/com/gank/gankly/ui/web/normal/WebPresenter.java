package com.gank.gankly.ui.web.normal;

import android.support.annotation.NonNull;

import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.BasePresenter;
import com.gank.gankly.mvp.source.LocalDataSource;

import java.util.List;

import rx.Subscriber;

/**
 * Create by LingYan on 2016-10-27
 * Email:137387869@qq.com
 */

public class WebPresenter extends BasePresenter implements WebContract.Presenter {
    private LocalDataSource mTask;
    private WebContract.View mView;

    public WebPresenter(LocalDataSource task, WebContract.View view) {
        mTask = task;
        mView = view;
    }

    @Override
    public void findCollectUrl(@NonNull String url) {
        mTask.findUrlCollect(url).subscribe(new Subscriber<List<UrlCollect>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<UrlCollect> urlCollects) {

            }
        });
    }

    @Override
    public void findHistoryUrl(@NonNull String url) {
        mTask.findReadHistory(url).subscribe(new Subscriber<List<ReadHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ReadHistory> readHistories) {

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
