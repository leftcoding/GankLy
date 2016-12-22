package com.gank.gankly.network.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gank.gankly.network.service.BaiSiService;
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
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
 */

public class BaiSiApi {
    private static final String BASE_URL = "http://s.budejie.com/";
    private static final int DEFAULT_OUT_TIME = 30;

    public BaiSiService mService;

    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    private BaiSiApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS); //手动创建一个OkHttpClient并设置超时时间
        builder.addNetworkInterceptor(new StethoInterceptor()); //chrome test databases
//        builder.interceptors().add(new LoggingInterceptor()); //打印请求log
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                okhttp3.Request original = chain.request();
//
//                okhttp3.Request request = original.newBuilder()
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

        mService = retrofit.create(BaiSiService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final BaiSiApi INSTANCE = new BaiSiApi();
    }

    //获取单例
    public static BaiSiApi getInstance() {
        return BaiSiApi.SingletonHolder.INSTANCE;
    }

    public BaiSiService getService() {
        return mService;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

}
