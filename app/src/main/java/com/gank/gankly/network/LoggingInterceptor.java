package com.gank.gankly.network;

import com.socks.library.KLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 打印Retrofit 请求日志
 * Create by LingYan on 2016年4月13日
 */
<<<<<<< HEAD
public class LoggingInterceptor implements Interceptor {
=======
class LoggingInterceptor implements Interceptor {
>>>>>>> ba456322f78a58bea1ad2febbfee870809b766d1
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        KLog.d("OkHttp", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
<<<<<<< HEAD
        KLog.d("LoggingInterceptor", String.format("Received response for %s in %.1fms%n%s",
=======
        KLog.d("OkHttp", String.format("Received response for %s in %.1fms%n%s",
>>>>>>> ba456322f78a58bea1ad2febbfee870809b766d1
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}