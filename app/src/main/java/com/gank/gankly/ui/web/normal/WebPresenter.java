package com.gank.gankly.ui.web.normal;

import androidx.annotation.NonNull;

import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.bean.RxCollect;
import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.source.LocalDataSource;
import com.gank.gankly.utils.ListUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-10-27
 */

public class WebPresenter implements WebContract.Presenter {
    private LocalDataSource mTask;
    private WebContract.View mView;
    private long endTime;
    private List<UrlCollect> mCollects;
    private Disposable subscription;

    private boolean isCollect;

    public WebPresenter(LocalDataSource task, WebContract.View view) {
        mTask = task;
        mView = view;
        mCollects = new ArrayList<>();
    }

    @Override
    public void findCollectUrl(@NonNull String url) {
        mTask.findUrlCollect(url).subscribe(new Observer<List<UrlCollect>>() {
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
            public void onNext(List<UrlCollect> urlCollects) {
                mCollects = urlCollects;
                boolean isCollect_ = ListUtils.getSize(urlCollects) > 0;
                isCollect = isCollect_;
                mView.setCollectIcon(isCollect_);
            }
        });
    }

    @Override
    public void insetHistoryUrl(final ReadHistory readHistory) {
        mTask.insertOrReplaceHistory(readHistory).subscribe(new Observer<Long>() {
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
            public void onNext(Long aLong) {
            }
        });

    }

    @Override
    public void cancelCollect() {
        if (isCollect) {
            long deleteByKey = mCollects.get(0).getId();
            mTask.cancelCollect(deleteByKey).subscribe(new Observer<String>() {
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
                public void onNext(String string) {
                    isCollect = false;

                    RxBus_.getInstance().post(new RxCollect(true));
                }
            });
        }
    }

    @Override
    public void collect() {
        if (!isCollect) {
            UrlCollect urlCollect = mView.getCollect();
            mTask.insertCollect(urlCollect).subscribe(new Observer<Long>() {
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
                public void onNext(Long aLong) {
                    isCollect = true;
                }
            });
        }
    }

    @Override
    public void collectAction(final boolean isCollect) {
        KLog.d("isCollect:" + isCollect);
        long curTime = System.currentTimeMillis();
        if (curTime - endTime < 2000) {
            subscription.dispose();
        }
        endTime = curTime;

        subscription = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(isCollect);
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(1, TimeUnit.SECONDS)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        collect();
                    } else {
                        cancelCollect();
                    }
                });
    }

    public void destroy() {
    }
}
