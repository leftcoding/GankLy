package com.gank.gankly.rxjava.rxclick;

import android.support.annotation.NonNull;
import android.view.View;

import io.reactivex.Observable;

/**
 * Created by Lingyan on 2017/4/1 0001.
 * Email:137387869@qq.com
 */

public class Rxbunding {
    public static Observable<String> click(@NonNull View view) {
        return Observable.create(new ViewClickSubscribe(view));
    }

    public static Observable<String> onDelay() {
        return null;
    }
}
