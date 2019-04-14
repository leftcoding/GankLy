package com.gank.gankly.ui.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.activity.BaseActivity;
import com.gank.gankly.utils.ToastUtils;
import com.socks.library.KLog;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-04-26
 */
public class WebVideoViewActivity extends BaseActivity {
    public static final String TITLE = "Title";
    public static final String URL = "url";

    @BindView(R.id.web_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.web_video)
    X5WebView mWebView;

    private String mUrl;
    private String mHistory;
    private List<String> mStrings = new ArrayList<>();

    @Override
    protected int getContentId() {
        return R.layout.fragment_video_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString(URL);
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mWebView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);


        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        KLog.d("mUrl:" + mUrl);
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        } else {
            ToastUtils.showToast(getBaseContext(), R.string.tip_server_error);
        }


    }

    public static void startWebActivity(Context packageContext, Bundle bundle) {
        Intent i = new Intent(packageContext, WebVideoViewActivity.class);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        packageContext.startActivity(i);
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url)) {
                mWebView.loadUrl(url);
            }
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest
                (WebView view,
                 com.tencent.smtt.export.external.interfaces.WebResourceRequest request) {
            KLog.e("should", "request.getUrl().toString() is " + request.getUrl().toString());
            return super.shouldInterceptRequest(view, request);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mHistory = url;
            if (!mStrings.contains(url)) {
                mStrings.add(url);
            }
        }
    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsConfirm(WebView arg0, String arg1, String arg2, com.tencent.smtt.export.external.interfaces.JsResult
                arg3) {
            return super.onJsConfirm(arg0, arg1, arg2, arg3);
        }

        View myVideoView;
        View myNormalView;
        IX5WebChromeClient.CustomViewCallback callback;

        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            if (mProgressBar == null) {
                return;
            }
            mProgressBar.setProgress(newProgress);

            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            super.onProgressChanged(webView, newProgress);
        }

        /**
         * 全屏播放配置
         */
        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback
                customViewCallback) {
            FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
            ViewGroup viewGroup = (ViewGroup) normalView.getParent();
            viewGroup.removeView(normalView);
            viewGroup.addView(view);
            myVideoView = view;
            myNormalView = normalView;
            callback = customViewCallback;
        }

        @Override
        public void onHideCustomView() {
            if (callback != null) {
                callback.onCustomViewHidden();
                callback = null;
            }
            if (myVideoView != null) {
                ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                viewGroup.removeView(myVideoView);
                viewGroup.addView(myNormalView);
            }
        }

        @Override
        public boolean onShowFileChooser(WebView arg0,
                                         ValueCallback<Uri[]> arg1, WebChromeClient.FileChooserParams arg2) {
            // TODO Auto-generated method stub
            KLog.e("app", "onShowFileChooser");
            return super.onShowFileChooser(arg0, arg1, arg2);
        }

        @Override
        public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String captureType) {
//            JiandanWebActivity.this.uploadFile = uploadFile;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("*/*");
//            startActivityForResult(Intent.createChooser(i, "test"), 0);
        }


        @Override
        public boolean onJsAlert(WebView arg0, String arg1, String arg2, com.tencent.smtt.export.external.interfaces.JsResult
                arg3) {
            /**
             * 这里写入你自定义的window alert
             */
            // AlertDialog.Builder builder = new Builder(getContext());
            // builder.setTitle("X5内核");
            // builder.setPositiveButton("确定", new
            // DialogInterface.OnClickListener() {
            //
            // @Override
            // public void onClick(DialogInterface dialog, int which) {
            // // TODO Auto-generated method stub
            // dialog.dismiss();
            // }
            // });
            // builder.show();
            // arg3.confirm();
            // return true;
            KLog.i("yuanhaizhou", "setX5webview = null");
            return super.onJsAlert(null, "www.baidu.com", "aa", arg3);
        }

        /**
         * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
         */


        @Override
        public void onReceivedTitle(WebView arg0, final String arg1) {
            super.onReceivedTitle(arg0, arg1);
            KLog.d("yuanhaizhou", "webpage title is " + arg1);
        }
    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
