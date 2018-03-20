package com.leftcoding.network.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by LingYan on 2017-09-29
 */
public class BaseServerHelper {
    private String mServerUrl;

    private NetworkControl mNetworkControl;

    private Retrofit.Builder mRetrofitBuilder;

    private OkHttpClient mOkHttpClient;

    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    private BaseServerHelper() {
        this(NetworkControl.get(), new Retrofit.Builder());
    }

    private BaseServerHelper(NetworkControl networkControl, Retrofit.Builder builder) {
        mNetworkControl = networkControl;
        mRetrofitBuilder = builder;
    }

    public BaseServerHelper addInterceptor(Interceptor interceptor) {
        mNetworkControl.addInterceptor(interceptor);
        return this;
    }

    public BaseServerHelper addNetworkInterceptor(Interceptor interceptor) {
        mNetworkControl.addNetworkInterceptor(interceptor);
        return this;
    }

    public BaseServerHelper addConverterFactory(Converter.Factory factory) {
        mRetrofitBuilder.addConverterFactory(factory);
        return this;
    }

    public BaseServerHelper addCallAdapterFactory(CallAdapter.Factory factory) {
        mRetrofitBuilder.addCallAdapterFactory(factory);
        return this;
    }

    public BaseServerHelper baseUrl(String url) {
        mServerUrl = url;
        return this;
    }

    public BaseServerHelper client(OkHttpClient client) {
        mOkHttpClient = client;
        return this;
    }

    private OkHttpClient getClient() {
        return mOkHttpClient != null ? mOkHttpClient : mNetworkControl.build();
    }

    public Retrofit newRetrofit() {
        return mRetrofitBuilder
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mServerUrl)
                .build();
    }

    private static final class GankServerHelperHolder {
        private static final BaseServerHelper INSTANCE = new BaseServerHelper();
    }

    public static synchronized BaseServerHelper get() {
        return GankServerHelperHolder.INSTANCE;
    }
}
