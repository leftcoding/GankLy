package com.gank.gankly.network.api;

import com.gank.gankly.network.DownloadProgressInterceptor;
import com.gank.gankly.network.DownloadProgressListener;
import com.gank.gankly.network.service.DownloadService;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-05-30
 */
public class DownloadApi {
    private static final int DEFAULT_TIMEOUT = 12;
    private static final String BASE_URL = "http://gankly.leftyan.com/";
    private DownloadService mDownloadService;
    private DownloadProgressListener listener;

    public DownloadApi(DownloadProgressListener listener) {
        this.listener = listener;
        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(this.listener);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);
        client.retryOnConnectionFailure(true);
        client.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        client.addInterceptor(new LoggingInterceptor());
//        client.addInterceptor(new Interceptor() {
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
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mDownloadService = retrofit.create(DownloadService.class);
    }

    public void downloadApk(Action1<InputStream> next, Subscriber subscriber) {
        mDownloadService.downloadApk()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(next)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void checkVersion(Subscriber subscriber) {
        mDownloadService.checkVersion()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
