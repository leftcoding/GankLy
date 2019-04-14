package android.ly.business.api;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.ly.business.domain.ListEntity;
import android.ly.business.domain.PageEntity;

import com.leftcoding.network.base.Server;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * Create by LingYan on 2017-09-30
 */

public class GankServer extends Server {
    private volatile static GankServer server;
    private GankApi api;

    private GankServer(Context context) {
        super(context);
        api = GankServerHelper.init(context)
                .newRetrofit()
                .create(GankApi.class);
    }

    public static GankServer with(Context context) {
        if (server == null) {
            synchronized (GankServer.class) {
                if (server == null) {
                    server = new GankServer(context);
                }
            }
        }
        return server;
    }

    public Observable<PageEntity<Gank>> androids(final int page, final int limit) {
        return api.androids(page, limit)
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
        return api.ios(page, limit)
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
        return api.allGoods(limit, page);
    }

    public void images(String tag, final int page, final int limit, ConsumerCall<PageEntity<Gank>> call) {
        Observable<PageEntity<Gank>> observable = api.images(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Response<PageEntity<Gank>>, PageEntity<Gank>>() {
                    @Override
                    public PageEntity<Gank> apply(Response<PageEntity<Gank>> response) throws Exception {
                        if (response == null || !response.isSuccessful()) {
                            return null;
                        }
                        return response.body();
                    }
                });

        addExec(tag, observable, call);
    }

    public Observable<Response<ListEntity<Gank>>> videos(int limit, int page) {
        return api.videos(limit, page);
    }
}
