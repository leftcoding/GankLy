//package com.gank.gankly.utils;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Environment;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.Target;
//import com.socks.library.KLog;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.concurrent.ExecutionException;
//
//import rx.Observable;
//import rx.Subscriber;
//import rx.functions.Func1;
//import rx.schedulers.Schedulers;
//
///**
// * Create by LingYan on 2016-04-20
// */
//public class RxSaveImage {
//    static Bitmap mBitmap;
//
//    public static Observable<Uri> saveImage(final Context context, final String url) {
//        return Observable.create(new Observable.OnSubscribe<File>() {
//            @Override
//            public void call(Subscriber<? super File> subscriber) {
//                KLog.d("call");
//                File file = null;
//                try {
//                    file = Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
//                } catch (InterruptedException | ExecutionException e) {
//                    KLog.d("e:" + e);
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                }
//                subscriber.onNext(file);
//                subscriber.onCompleted();
//            }
//        }).flatMap(new Func1<File, Observable<Uri>>() {
//            @Override
//            public Observable<Uri> call(File file) {
//                File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
//                if (!appDir.exists()) {
//                    appDir.mkdir();
//                }
//                String fileName = title.replace('/', '-') + ".jpg";
//                File file = new File(appDir, fileName);
//                try {
//                    FileOutputStream fos = new FileOutputStream(file);
//                    assert bitmap != null;
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.flush();
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                Uri uri = Uri.fromFile(file);
//                // 通知图库更新
//                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
//                context.sendBroadcast(scannerIntent);
//                return Observable.just(uri);
//            }
//        }).subscribeOn(Schedulers.io()
//
//        );
//    }
//}
