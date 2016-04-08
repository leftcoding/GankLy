package com.gank.gankly.network;

import com.gank.gankly.bean.GankResult;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-04-06
 */
public class GankRetrofit {
    private static final String BASE_URL = "http://gank.io/api/data/";
    private static final int DEFAULT_OUT_TIME = 12;

    public static GankRetrofit instance;
    public GankApi gankApi;

    private GankRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS); //手动创建一个OkHttpClient并设置超时时间
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Cache-Control", "public, max-age=" + 60 * 60 * 4)
                        .build();

                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        gankApi = retrofit.create(GankApi.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final GankRetrofit INSTANCE = new GankRetrofit();
    }

    //获取单例
    public static GankRetrofit getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public GankApi getGankService() {
        return gankApi;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public void fetchAndroid(int limit, int page, Subscriber<GankResult> subscriber) {
        Observable<GankResult> observable = gankApi.fetchAndroidGoods(limit, page);
        toSubscribe(observable, subscriber);
    }

    public void fetchIos(int limit, int page, Subscriber<GankResult> subscriber) {
        Observable<GankResult> observable = gankApi.fetchIosGoods(limit, page);
        toSubscribe(observable, subscriber);
    }

    public void fetchAll(int limit, int page, Subscriber<GankResult> subscriber) {
        Observable<GankResult> observable = gankApi.fetchAllGoods(limit, page);
        toSubscribe(observable, subscriber);
    }

    public void fetchWelfare(int limit, int page, Subscriber<GankResult> subscriber) {
        Observable<GankResult> observable = gankApi.fetchBenefitsGoods(limit, page);
        toSubscribe(observable, subscriber);
    }


}
