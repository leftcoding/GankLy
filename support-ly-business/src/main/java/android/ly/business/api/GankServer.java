package android.ly.business.api;

import android.content.Context;
import android.ly.business.domain.Entity;
import android.ly.business.domain.ListResult;
import android.ly.business.domain.PageEntity;

import com.leftcoding.network.rxjava.RxApiManager;
import com.leftcoding.network.rxjava.RxConsumer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * Create by LingYan on 2017-09-30
 */

public class GankServer {
    private volatile static GankServer gankServer;
    private GankApi gankApi;
    private Context context;

    private GankServer(Context context) {
        gankApi = InitGankServer.init(context)
                .newRetrofit()
                .create(GankApi.class);
    }

    public static GankServer get(Context context) {
        if (gankServer == null) {
            synchronized (GankServer.class) {
                if (gankServer == null) {
                    gankServer = new GankServer(context);
                }
            }
        }
        return gankServer;
    }

    public void androids(final String requestTag, final int page, final int limit, final RxConsumer<PageEntity<Entity>> consumer) {
        convert(gankApi.androids(page, limit), requestTag, consumer);
    }

    public Observable<Response<PageEntity<Entity>>> ios(int page, int limit) {
        return gankApi.ios(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<ListResult<Entity>>> allGoods(int limit, int page) {
        return gankApi.allGoods(limit, page);
    }

    public Observable<Response<ListResult<Entity>>> images(int limit, int page) {
        return gankApi.images(limit, page);
    }

    public Observable<Response<ListResult<Entity>>> videos(int limit, int page) {
        return gankApi.videos(limit, page);
    }

    private <T> void convert(Observable<Response<T>> observable, String tag, final RxConsumer<T> consumer) {
        Disposable disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<T>>() {
                    @Override
                    public void accept(Response<T> response) throws Exception {
                        if (consumer != null) {
                            if (response == null) {
                                consumer.onError(new Throwable("response is null"));
                            } else {
                                if (response.isSuccessful()) {
                                    consumer.next(response.body());
                                } else {
                                    consumer.onError(new Throwable(response.toString()));
                                }
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (consumer != null) {
                            consumer.onError(throwable);
                        }
                    }
                });
        addRxManager(tag, disposable);
    }

    private void addRxManager(String tag, Disposable disposable) {
        RxApiManager.get().add(tag, disposable);
    }
}
