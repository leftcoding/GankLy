package com.gank.gankly.mvp.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gank.gankly.App;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.mvp.BaseModel;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import rx.Observable;
import rx.Subscriber;

/**
 * Create by LingYan on 2016-10-24
 * Email:137387869@qq.com
 */

public class LocalDataSource extends BaseModel {
    @Nullable
    private static LocalDataSource INSTANCE = null;

    @NonNull
    private UrlCollectDao mUrlCollectDao;

    public static LocalDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSource();
                }
            }
        }
        return INSTANCE;
    }

    private LocalDataSource() {
        mUrlCollectDao = App.getDaoSession().getUrlCollectDao();
    }

    public Observable<List<UrlCollect>> getCollect(final int offset, final int limit) {
        return Observable.create(new Observable.OnSubscribe<List<UrlCollect>>() {
            @Override
            public void call(Subscriber<? super List<UrlCollect>> subscriber) {
                QueryBuilder<UrlCollect> queryBuilder = mUrlCollectDao.queryBuilder();
                queryBuilder.orderDesc(UrlCollectDao.Properties.Date);
                queryBuilder.offset(offset).limit(limit);
                subscriber.onNext(queryBuilder.list());
                subscriber.onCompleted();
            }
        });
    }

    public Observable<String> toDelete(final long id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                mUrlCollectDao.deleteByKey(id);
                subscriber.onNext("");
                subscriber.onCompleted();
            }
        });
    }
}
