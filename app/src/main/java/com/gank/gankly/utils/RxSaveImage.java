package com.gank.gankly.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.config.Constants;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-04-20
 */
public class RxSaveImage {
    public static Observable<Uri> saveImageAndGetPathObservable(final Context context, final String url) {
        return Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(ObservableEmitter<Uri> subscriber) throws Exception {
                Bitmap bitmap = null;
                try {
                    GlideUrl glideUrl;
                    if (url.contains("meizitu") || url.contains("mzitu")) {
                        glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                                .addHeader("Referer", "http://www.mzitu.com/mm/")
                                .build());
                    } else {
                        glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                                .build());
                    }
                    bitmap = Glide.with(context)
                            .asBitmap()
                            .load(glideUrl)
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .skipMemoryCache(true)
                            )
//                            .atMost()
                            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    KLog.e(e);
                    CrashUtils.crashReport(e);
                    subscriber.onError(e);
                }
                if (bitmap != null) {
                    subscriber.onNext(saveImage(context, bitmap, String.valueOf(url.hashCode())));
                } else {
                    subscriber.onError(new Exception("bitmap can't be null"));
                    CrashUtils.crashReport(new Exception("bitmap can't be null"));
                }
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Uri saveImage(Context context, Bitmap bm, String name) {
        File appDir = new File(Environment.getExternalStorageDirectory(), Constants.IMAGE_PATH);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, name + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            KLog.e(e);
            CrashUtils.crashReport(e);
        }
        Uri uri = Uri.fromFile(file);
        // 通知图库更新
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scannerIntent);
        return uri;
    }
}
