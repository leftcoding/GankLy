package com.gank.gankly.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.gank.gankly.bean.CheckVersion;
import com.gank.gankly.network.DownloadProgressListener;
import com.gank.gankly.network.api.DownloadApi;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.FileUtils;
import com.gank.gankly.view.ILauncher;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by LingYan on 2016-06-01
 */
public class LauncherPresenter extends BasePresenter<ILauncher> {
    private DownloadApi mDownloadApi;
    private String apkName = "gankly.apk";
    private File mFile = new File(Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/GankLy/" + apkName);

    public LauncherPresenter(Activity mActivity, ILauncher view) {
        this(mActivity, view, null);
    }

    public LauncherPresenter(Activity mActivity, ILauncher view, DownloadProgressListener listener) {
        super(mActivity, view);
        mDownloadApi = new DownloadApi(listener);
    }

    public void checkVersion() {
        mIView.showDialog();
        mDownloadApi.checkVersion(new Observer<CheckVersion>() {
            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
                mIView.hiddenDialog();
            }

            @Override
            public void onComplete() {
                mIView.hiddenDialog();
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CheckVersion checkVersion) {
                int curVersion = AppUtils.getVersionCode(mActivity);
                if (checkVersion.getCode() > curVersion) {
                    mIView.callUpdate(checkVersion);
                } else {
                    mIView.noNewVersion();
                }
            }
        });
    }

    public void downloadApk() {
        mDownloadApi.downloadApk(new Consumer<InputStream>() {
            @Override
            public void accept(InputStream inputStream) throws Exception {
                try {
                    FileUtils.writeFile(inputStream, mFile);
                } catch (IOException e) {
                    KLog.e(e);
                    CrashUtils.crashReport(e);
                }
            }
        }, new Observer<InputStream>() {

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(InputStream inputStream) {
                downloadSuccess(mActivity, mFile);
            }
        });
    }

    public void downloadSuccess(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
