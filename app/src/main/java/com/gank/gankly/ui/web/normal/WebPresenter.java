package com.gank.gankly.ui.web.normal;

import android.support.annotation.NonNull;

import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.BasePresenter;
import com.gank.gankly.mvp.source.LocalDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-10-27
 * Email:137387869@qq.com
 */

public class WebPresenter extends BasePresenter implements WebContract.Presenter {
    private LocalDataSource mTask;
    private WebContract.View mView;
    private long endTime;
    private List<UrlCollect> mCollects;

    public WebPresenter(LocalDataSource task, WebContract.View view) {
        mTask = task;
        mView = view;
        mCollects = new ArrayList<>();
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
                mCollects = urlCollects;
                KLog.d("mCollects:" + mCollects.size());
                if (ListUtils.getListSize(urlCollects) > 0) {
                    mView.onCollect();
                }
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
    public void cancelCollect() {
        long deleteByKey = mCollects.get(0).getId();
        mTask.cancelCollect(deleteByKey).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String string) {
                KLog.d("cancelCollect:" + string);
            }
        });
    }

    @Override
    public void collect() {
        UrlCollect urlCollect = mView.getCollect();
        mTask.insertCollect(urlCollect).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                KLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
            }

            @Override
            public void onNext(Long aLong) {
                KLog.d("收藏成功，aLong:" + aLong);
            }
        });
    }

    @Override
    public void collectAction(final boolean isCollect) {
        long curTime = System.currentTimeMillis();
        if (curTime - endTime < 1000) {
            mRxManager.clear();
        } else {
            endTime = curTime;
        }
        mRxManager.add(Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(isCollect);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        KLog.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        KLog.d("aBoolean:" + aBoolean);
                        if (aBoolean) {
                            collect();
                        } else {
                            cancelCollect();
                        }
                    }
                }));
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mRxManager.clear();
    }
}
