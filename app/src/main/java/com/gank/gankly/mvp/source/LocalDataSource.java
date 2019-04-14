package com.gank.gankly.mvp.source;

import androidx.annotation.NonNull;

import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.data.entity.ReadHistoryDao;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.mvp.BaseModel;
import com.gank.gankly.utils.ListUtils;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * 本地数据库
 * Create by LingYan on 2016-10-24
 */

public class LocalDataSource extends BaseModel {
    private volatile static LocalDataSource mInstance ;

    @NonNull
    private UrlCollectDao mUrlCollectDao;

    @NonNull
    private ReadHistoryDao mReadHistoryDao;

    public static LocalDataSource getInstance() {
        if (mInstance == null) {
            synchronized (LocalDataSource.class) {
                if (mInstance == null) {
                    mInstance = new LocalDataSource();
                }
            }
        }
        return mInstance;
    }

    private LocalDataSource() {
//        mUrlCollectDao = AppConfig.getDaoSession().getUrlCollectDao();
//        mReadHistoryDao = AppConfig.getDaoSession().getReadHistoryDao();
    }

    public Observable<List<UrlCollect>> getCollect(final int offset, final int limit) {
        return toObservable(Observable.create(new ObservableOnSubscribe<List<UrlCollect>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UrlCollect>> subscriber) throws Exception {
                QueryBuilder<UrlCollect> queryBuilder = mUrlCollectDao.queryBuilder();
                queryBuilder.orderDesc(UrlCollectDao.Properties.Date);
                queryBuilder.offset(offset).limit(limit);
                subscriber.onNext(queryBuilder.list());
                subscriber.onComplete();
            }
        }));
    }

    public Observable<String> cancelCollect(final long id) {
        return toObservable(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> subscriber) throws Exception {
                mUrlCollectDao.deleteByKey(id);
                subscriber.onNext("");
                subscriber.onComplete();
            }
        }));
    }

    public Observable<String> deleteHistory(final long id) {
        return toObservable(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> subscriber) throws Exception {
                mReadHistoryDao.deleteByKey(id);
                subscriber.onNext("");
                subscriber.onComplete();
            }
        }));
    }

    public Observable<List<ReadHistory>> selectReadHistory(final int offset, final int limit) {
        return toObservable(Observable.create(new ObservableOnSubscribe<List<ReadHistory>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ReadHistory>> subscriber) throws Exception {
                QueryBuilder<ReadHistory> queryBuilder = mReadHistoryDao.queryBuilder();
                queryBuilder.orderDesc(ReadHistoryDao.Properties.Date);
                queryBuilder.offset(offset).limit(limit);
                subscriber.onNext(queryBuilder.list());
                subscriber.onComplete();
            }
        }));
    }

    public Observable<Long> insertCollect(final UrlCollect collect) {
        return toObservable(Observable.create(new ObservableOnSubscribe<Long>() {

            @Override
            public void subscribe(ObservableEmitter<Long> subscriber) throws Exception {
                long rasId = mUrlCollectDao.insert(collect);
                subscriber.onNext(rasId);
                subscriber.onComplete();
            }
        }));
    }

    public Observable<Long> insertOrReplaceHistory(final ReadHistory history) {
        return toObservable(Observable.create(new ObservableOnSubscribe<Long>() {

            @Override
            public void subscribe(ObservableEmitter<Long> subscriber) throws Exception {
                String url = history.getUrl();
                QueryBuilder<ReadHistory> query = mReadHistoryDao.queryBuilder();
                List<ReadHistory> list = query.where(ReadHistoryDao.Properties.Url.eq(url)).list();
                boolean isNull = ListUtils.getSize(list) <= 0;
                long rasId = 0;
                if (isNull) {
                    rasId = mReadHistoryDao.insert(history);
                } else {
                    ReadHistory readHistory = list.get(0);
                    readHistory.setDate(new Date());
                    mReadHistoryDao.update(readHistory);
                }
                subscriber.onNext(rasId);
                subscriber.onComplete();
            }
        }));
    }

    public Observable<List<UrlCollect>> findUrlCollect(final String url) {
        return toObservable(Observable.create(new ObservableOnSubscribe<List<UrlCollect>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UrlCollect>> subscriber) throws Exception {
                QueryBuilder<UrlCollect> query = mUrlCollectDao.queryBuilder();
                List<UrlCollect> list = query.where(UrlCollectDao.Properties.Url.eq(url)).list();
                subscriber.onNext(list);
                subscriber.onComplete();
            }
        }));
    }
}
