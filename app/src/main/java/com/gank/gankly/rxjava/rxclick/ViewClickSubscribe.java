package com.gank.gankly.rxjava.rxclick;

import android.view.View;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

/**
 * Created by Lingyan on 2017/4/1 0001.
 */

public final class ViewClickSubscribe implements ObservableOnSubscribe<String> {
    private View mView;

    public ViewClickSubscribe(View view) {
        if (view == null) {
            throw new NullPointerException("view not can be null");
        }
        mView = view;
    }

    @Override
    public void subscribe(ObservableEmitter subscribe) throws Exception {
        View.OnClickListener v = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subscribe.isDisposed()) {
                    subscribe.onNext("");
                }
            }
        };
        mView.setOnClickListener(v);

        subscribe.setCancellable(new Cancellable() {
            @Override
            public void cancel() throws Exception {
                mView.setOnClickListener(null);
            }
        });
    }
}
