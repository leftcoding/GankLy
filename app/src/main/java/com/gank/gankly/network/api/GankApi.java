package com.gank.gankly.network.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.network.service.GankService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Retrofit请求封装
 * Create by LingYan on 2016-04-06
 */
public class GankApi {
    private static final String BASE_URL = "http://gank.io/api/data/";
    private static final int DEFAULT_OUT_TIME = 12;

    public GankService gankApi;

    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    private GankApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS); //手动创建一个OkHttpClient并设置超时时间
        builder.addNetworkInterceptor(new StethoInterceptor()); //chrome test databases
//        builder.interceptors().add(new LoggingInterceptor()); //打印请求log
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                Request original = chain.request();
//
//                Request request = original.newBuilder()
//                        .header("Content-Type", "application/json")
//                        .header("Cache-Control", "public, max-age=" + 60 * 60 * 4)
//                        .build();
//                KLog.d(chain.proceed(request).body().string());
//                return chain.proceed(request);
//            }
//        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        gankApi = retrofit.create(GankService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final GankApi INSTANCE = new GankApi();
    }

    //获取单例
    public static GankApi getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public GankService getGankService() {
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

    public void fetchVideo(int limit, int page, Subscriber<GankResult> subscriber) {
        Observable<GankResult> observable = gankApi.fetchVideo(limit, page);
        toSubscribe(observable, subscriber);
    }
}
