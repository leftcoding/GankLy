package com.leftcoding.http.api;

import android.content.Context;

import com.leftcoding.http.BuildConfig;
import com.leftcoding.http.R;
import com.leftcoding.http.base.GankServerHelper;
import com.leftcoding.http.bean.ListResult;
import com.leftcoding.http.bean.PageResult;
import com.leftcoding.http.bean.ResultsBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Create by LingYan on 2017-09-30
 */

public class GankServerManager {
    private final String serverError;
    private GankApi mApi;

    private GankServerManager(Context context) {
        mApi = GankServerHelper.get()
//                .addConverterFactory(MyGsonConverterFactory.create())
                .baseUrl(BuildConfig.GANK_SERVER_ULR)
                .newRetrofit()
                .create(GankApi.class);

        serverError = context.getResources().getString(R.string.network_request_error);
    }

    public static synchronized GankServerManager with(Context context) {
        return new GankServerManager(context);
    }

    public Observable<PageResult<ResultsBean>> androids(final int page, final int limit) {
        return mApi.androids(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(pageResultFunction);
    }

    public Observable<Response<PageResult<ResultsBean>>> ios(int page, int limit) {
        return mApi.ios(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<ListResult<ResultsBean>>> allGoods(int limit, int page) {
        return mApi.allGoods(limit, page);
    }

    public Observable<Response<ListResult<ResultsBean>>> images(int limit, int page) {
        return mApi.images(limit, page);
    }

    public Observable<Response<ListResult<ResultsBean>>> videos(int limit, int page) {
        return mApi.videos(limit, page);
    }

    private Function<Response<PageResult<ResultsBean>>, ObservableSource<PageResult<ResultsBean>>> pageResultFunction = new Function<Response<PageResult<ResultsBean>>, ObservableSource<PageResult<ResultsBean>>>() {
        @Override
        public ObservableSource<com.leftcoding.http.bean.PageResult<ResultsBean>> apply
                (Response<PageResult<ResultsBean>> response) throws Exception {
            if (response != null) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        return Observable.just(response.body());
                    }
                }
            }
            return Observable.error(new Throwable(serverError));
        }
    };
}
