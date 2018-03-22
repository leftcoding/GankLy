package android.ly.business.api;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.ly.business.domain.ListEntity;
import android.ly.business.domain.PageEntity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
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

    public Observable<PageEntity<Gank>> androids(final int page, final int limit) {
        return gankApi.androids(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Response<PageEntity<Gank>>, PageEntity<Gank>>() {
                    @Override
                    public PageEntity<Gank> apply(Response<PageEntity<Gank>> pageEntityResponse) throws Exception {
                        if (pageEntityResponse == null) {
                            return null;
                        }
                        return pageEntityResponse.body();
                    }
                });
    }

    public Observable<PageEntity<Gank>> ios(int page, int limit) {
        return gankApi.ios(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Response<PageEntity<Gank>>, PageEntity<Gank>>() {
                    @Override
                    public PageEntity<Gank> apply(Response<PageEntity<Gank>> pageEntityResponse) throws Exception {
                        if (pageEntityResponse == null) {
                            return null;
                        }
                        return pageEntityResponse.body();
                    }
                });
    }

    public Observable<Response<ListEntity<Gank>>> allGoods(int limit, int page) {
        return gankApi.allGoods(limit, page);
    }

    public Observable<Response<ListEntity<Gank>>> images(int limit, int page) {
        return gankApi.images(limit, page);
    }

    public Observable<Response<ListEntity<Gank>>> videos(int limit, int page) {
        return gankApi.videos(limit, page);
    }
}
