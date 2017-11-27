package com.leftcoding.http.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Create by LingYan on 2017-09-29
 */
public class GankServerHelper {
    private String mServerUrl;

    private HttpBuilderControl mHttpBuilderControl;

    private Retrofit.Builder mRetrofitBuilder;

    private OkHttpClient mOkHttpClient;

    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    protected GankServerHelper() {
        this(HttpBuilderControl.get(), new Retrofit.Builder());
    }

    private GankServerHelper(HttpBuilderControl httpBuilderControl, Retrofit.Builder builder) {
        mHttpBuilderControl = httpBuilderControl;
        mRetrofitBuilder = builder;
    }

    public GankServerHelper addInterceptor(Interceptor interceptor) {
        mHttpBuilderControl.addInterceptor(interceptor);
        return this;
    }

    public GankServerHelper addNetworkInterceptor(Interceptor interceptor) {
        mHttpBuilderControl.addNetworkInterceptor(interceptor);
        return this;
    }

    public GankServerHelper addConverterFactory(Converter.Factory factory) {
        mRetrofitBuilder.addConverterFactory(factory);
        return this;
    }

    public GankServerHelper addCallAdapterFactory(CallAdapter.Factory factory) {
        mRetrofitBuilder.addCallAdapterFactory(factory);
        return this;
    }

    public GankServerHelper baseUrl(String url) {
        mServerUrl = url;
        return this;
    }

    public GankServerHelper client(OkHttpClient client) {
        mOkHttpClient = client;
        return this;
    }

    private OkHttpClient getClient() {
        return mOkHttpClient != null ? mOkHttpClient : mHttpBuilderControl.build();
    }

    public Retrofit newRetrofit() {
        return mRetrofitBuilder
                .client(getClient())
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mServerUrl)
                .build();
    }

    private static final class GankServerHelperHolder {
        private static final GankServerHelper INSTANCE = new GankServerHelper();
    }

    public static synchronized GankServerHelper get() {
        return GankServerHelperHolder.INSTANCE;
    }
}
