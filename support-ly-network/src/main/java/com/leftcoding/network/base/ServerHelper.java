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
public class ServerHelper {
    private String url;

    private NetworkControl networkControl;

    private Retrofit.Builder retrofitBuilder;

    private OkHttpClient okHttpClient;

    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    private ServerHelper() {
        this(NetworkControl.get(), new Retrofit.Builder());
    }

    private ServerHelper(NetworkControl networkControl, Retrofit.Builder builder) {
        this.networkControl = networkControl;
        retrofitBuilder = builder;
    }

    public ServerHelper addInterceptor(Interceptor interceptor) {
        networkControl.addInterceptor(interceptor);
        return this;
    }

    public ServerHelper addNetworkInterceptor(Interceptor interceptor) {
        networkControl.addNetworkInterceptor(interceptor);
        return this;
    }

    public ServerHelper addConverterFactory(Converter.Factory factory) {
        retrofitBuilder.addConverterFactory(factory);
        return this;
    }

    public ServerHelper addCallAdapterFactory(CallAdapter.Factory factory) {
        retrofitBuilder.addCallAdapterFactory(factory);
        return this;
    }

    public ServerHelper baseUrl(String url) {
        this.url = url;
        return this;
    }

    public ServerHelper client(OkHttpClient client) {
        okHttpClient = client;
        return this;
    }

    private OkHttpClient getClient() {
        return okHttpClient != null ? okHttpClient : networkControl.build();
    }

    public Retrofit newRetrofit() {
        return retrofitBuilder
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    private static final class ServerHelperHolder {
        private static final ServerHelper INSTANCE = new ServerHelper();
    }

    public static synchronized ServerHelper get() {
        return ServerHelperHolder.INSTANCE;
    }
}
