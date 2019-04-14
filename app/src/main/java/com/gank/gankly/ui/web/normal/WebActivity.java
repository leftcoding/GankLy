package com.gank.gankly.ui.web.normal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.source.LocalDataSource;
import com.gank.gankly.ui.base.activity.BaseActivity;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.CircularAnimUtils;
import com.gank.gankly.utils.ShareUtils;
import com.gank.gankly.utils.ToastUtils;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.InputStream;
import java.util.Date;

import butterknife.BindView;

/**
 * 普通webView
 * Create by LingYan on 2016-5-10
 */
public class WebActivity extends BaseActivity implements WebContract.View {
    public static final int FROM_MAIN = 0;
    public static final int FROM_COLLECT = 1;

    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String TYPE = "type";
    public static final String AUTHOR = "author";
    public static final String FROM_TYPE = "from_type";

    @BindView(R.id.web_view)
    FrameLayout mWebParent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.web_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.web_main)
    View mView;

    WebView mWebView;

    private String mUrl;
    private String mTitle;
    private String mCollectType;
    private String mAuthor;
    private boolean isCollect;
    private boolean isInitCollect;
    private CollectStates mStates = CollectStates.NORMAL;
    private int mFromType;
    private WebContract.Presenter mPresenter;
    private MenuItem mMenuItem;

    private ValueCallback<Uri> uploadFile;

    @Override
    public void shortToast(String string) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showContent() {

    }

    enum CollectStates {
        NORMAL, COLLECT, UN_COLLECT
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        parseBundle();
        super.onCreate(savedInstanceState);
        mPresenter = new WebPresenter(LocalDataSource.getInstance(), this);

        mWebView = new WebView(getApplicationContext(), null);

        mWebParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        WebSettings settings = mWebView.getSettings();
        mWebView.requestFocusFromTouch(); //支持获取手势焦点，输入用户名、密码或其他
        settings.setJavaScriptEnabled(true);  //支持js
        settings.setDomStorageEnabled(true); //
        settings.setSupportZoom(true); //设置支持缩放
        settings.setBuiltInZoomControls(true); //
        settings.setDisplayZoomControls(false);//是否显示缩放控件
        settings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSupportMultipleWindows(false);
        settings.setAppCacheEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        settings.setAppCachePath(this.getDir("appcache", 0).getPath());
        settings.setDatabasePath(this.getDir("databases", 0).getPath());
        settings.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.loadUrl(mUrl);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();

        setTitle(mTitle);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setHomeAsUpIndicator(R.drawable.ic_toolbar_close);
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> CircularAnimUtils.actionVisible_(false, WebActivity.this, v, mView, 0, 618));

        isInitCollect = true;
        if (!TextUtils.isEmpty(mUrl)) {
            if (mPresenter != null) {
                mPresenter.findCollectUrl(mUrl);
                mPresenter.insetHistoryUrl(new ReadHistory(null, mUrl, mTitle, new Date(), mCollectType));
            }
        }
    }

    private void parseBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString(URL);
            mTitle = bundle.getString(TITLE);
            mCollectType = bundle.getString(TYPE, Constants.ALL);
            mAuthor = bundle.getString(AUTHOR);
            mFromType = bundle.getInt(FROM_TYPE);
        }
    }

    public static void startWebActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, WebActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenuItem = menu.findItem(R.id.welfare_collect);
        switchCollectIcon(isCollect);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.welfare_collect:
                mStates = CollectStates.NORMAL;
                int resColor;
                int resText;
                if (!isCollect) {
                    if (!isInitCollect) {
                        mStates = CollectStates.COLLECT;
                    }
                    resText = R.string.collect_success;
                    resColor = R.color.collect_snackbar_text_color;
                } else {
                    if (isInitCollect) {
                        mStates = CollectStates.UN_COLLECT;
                    }
                    resText = R.string.collect_cancel;
                    resColor = R.color.white;
                }

                isCollect = !isCollect;
                mPresenter.collectAction(isCollect);
                showSnackbar(mView, resText, getResources().getColor(resColor));
                switchCollectIcon(isCollect);
                return true;
            case R.id.welfare_share:
                ShareUtils.getInstance().shareText(this, mWebView.getTitle(), mWebView.getUrl());
                return true;
            case R.id.welfare_copy_url:
                AppUtils.copyText(this, mWebView.getUrl());
                ToastUtils.showToast(getBaseContext(), R.string.tip_copy_success);
                return true;
            case R.id.welfare_refresh:
                mWebView.reload();
                return true;
            case R.id.welfare_browser:
                openBrowser(mWebView.getUrl());
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void switchCollectIcon(boolean isCollect) {
        if (isCollect) {
            mMenuItem.setIcon(R.drawable.navigation_collect_prs);
        } else {
            mMenuItem.setIcon(R.drawable.navigation_collect_nor);
        }
    }

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        if (intent.resolveActivity(WebActivity.this.getPackageManager()) != null) {
            WebActivity.this.startActivity(intent);
        } else {
            ToastUtils.showToast(getBaseContext(), R.string.web_open_failed);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                finish();
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public com.tencent.smtt.export.external.interfaces.WebResourceResponse shouldInterceptRequest
                (WebView view,
                 com.tencent.smtt.export.external.interfaces.WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//             这些视频需要hack CSS才能达到全屏播放的效果
            if (url.contains("www.vmovier.com")) {
                injectCSS("vmovier.css");
            } else if (url.contains("video.weibo.com")) {
                injectCSS("weibo.css");
            } else if (url.contains("m.miaopai.com")) {
                injectCSS("miaopai.css");
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
            return super.onShowFileChooser(arg0, arg1, arg2);
        }

        @Override
        public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String captureType) {
            WebActivity.this.uploadFile = uploadFile;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "test"), 0);
        }


        @Override
        public boolean onJsAlert(WebView arg0, String arg1, String arg2, com.tencent.smtt.export.external.interfaces.JsResult
                arg3) {
            return super.onJsAlert(null, "www.baidu.com", "aa", arg3);
        }

        /**
         * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
         */
        @Override
        public void onReceivedTitle(WebView arg0, final String arg1) {
            super.onReceivedTitle(arg0, arg1);
        }
    }

    // Inject CSS method: read style.css from assets folder
    // Append stylesheet to document head
    private void injectCSS(String filename) {
        try {
            InputStream inputStream = this.getAssets().open(filename);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            mWebView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            ViewParent viewParent = mWebView.getParent();
            if (viewParent != null) {
                ((ViewGroup) viewParent).removeView(mWebView);
            }
            mWebView.stopLoading();
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearView();
            mWebView.clearFormData();
            mWebView.clearHistory();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy(); // All you have to do is destroy() the WebView before Activity finishes
    }

    @Override
    public void onCollect() {
        switchCollectIcon(true);
    }

    @Override
    public void onCancelCollect() {
        switchCollectIcon(false);
    }

    @Override
    public UrlCollect getCollect() {
        return new UrlCollect(null, mUrl, mTitle, new Date(), mCollectType, mAuthor);
    }

    @Override
    public void setCollectIcon(boolean isCollect) {
        invalidateOptionsMenu();//更新Menu
        this.isCollect = isCollect;
    }
}
