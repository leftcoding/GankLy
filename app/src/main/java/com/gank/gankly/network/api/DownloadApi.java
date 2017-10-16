package com.gank.gankly.network.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gank.gankly.bean.CheckVersion;
import com.gank.gankly.network.DownloadProgressInterceptor;
import com.gank.gankly.network.DownloadProgressListener;
import com.gank.gankly.network.service.DownloadService;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Create by LingYan on 2016-05-30
 */
public class DownloadApi {
    private static final int DEFAULT_OUT_TIME = 30;
//    private static final String BASE_URL = "http://gank.leftyan.com/";
    private static final String BASE_URL = "https://coding.net/u/leftcoding/p/Gank/git/raw/master/";
    private DownloadService mDownloadService;

    public DownloadApi(DownloadProgressListener listener) {
        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);
        init(interceptor);
    }

    private void init(Interceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(true);
        builder.connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS); //手动创建一个OkHttpClient并设置超时时间
        builder.addNetworkInterceptor(new StethoInterceptor()); //chrome test databases
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }

        RxJava2CallAdapterFactory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxJavaCallAdapterFactory)//RxJava2
                .baseUrl(BASE_URL)
                .build();

        mDownloadService = retrofit.create(DownloadService.class);
    }

    public void checkVersion(Observer<CheckVersion> subscriber) {
        mDownloadService.checkVersion()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void downloadApk(Consumer<InputStream> next, Observer subscriber) {
        mDownloadService.downloadApk()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(ResponseBody::byteStream)
                .observeOn(Schedulers.io())
                .doOnNext(next)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}