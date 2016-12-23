package com.gank.gankly.network.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gank.gankly.bean.JiandanResult;
import com.gank.gankly.network.service.JiandanService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by LingYan on 2016-07-20
 * Email:137387869@qq.com
 */
public class JiandanApi {
    private static final String BASE_URL = "http://jandan.net/";
    private static final int DEFAULT_OUT_TIME = 30;

    public JiandanService mService;

    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    private JiandanApi() {
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
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mService = retrofit.create(JiandanService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final JiandanApi INSTANCE = new JiandanApi();
    }

    //获取单例
    public static JiandanApi getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void fetchJianDan(int page, Observer<JiandanResult> subscriber) {
        Observable<JiandanResult> observable = mService.fetchData("get_recent_posts", "url,date,tags,author,title,comment_count,custom_fields", "thumb_c,views", "1", String.valueOf(page));
    }
}
