package com.gank.gankly.rxjava.rxclick;

import androidx.annotation.NonNull;
import android.view.View;

import io.reactivex.Observable;

/**
 * Created by Lingyan on 2017/4/1 0001.
 */

public class Rxbunding {
    public static Observable<String> click(@NonNull View view) {
        return Observable.create(new ViewClickSubscribe(view));
    }

    public static Observable<String> onDelay() {
        return null;
    }
}
