package android.ly.business.api;

import android.content.Context;

import com.leftcoding.network.base.BaseServerHelper;

import okhttp3.Interceptor;
import retrofit2.Retrofit;

/**
 * Create by LingYan on 2018-03-20
 */

public class InitGankServer {
    private volatile static InitGankServer initGankServer;
    private Context context;
    private BaseServerHelper baseServerHelper;
    private String baseUrl;

    private InitGankServer(Context context) {
        this.context = context.getApplicationContext() == null ? context : context.getApplicationContext();
        baseServerHelper = BaseServerHelper.get();
    }

    public static InitGankServer init(Context context) {
        if (initGankServer == null) {
            synchronized (InitGankServer.class) {
                if (initGankServer == null) {
                    initGankServer = new InitGankServer(context);
                }
            }
        }
        return initGankServer;
    }

    public InitGankServer baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public InitGankServer addNetworkInterceptor(Interceptor interceptor) {
        baseServerHelper.addNetworkInterceptor(interceptor);
        return this;
    }

    Retrofit newRetrofit() {
        return baseServerHelper.baseUrl(baseUrl)
                .newRetrofit();
    }
}
