package com.gank.gankly.ui.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.CircularAnimUtil;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.utils.RxUtils;
import com.gank.gankly.utils.ShareUtils;
import com.gank.gankly.utils.ToastUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-5-10
 * Email:137387869@qq.com
 */
public class WebActivity extends BaseActivity {
    public static final int FROM_MAIN = 0;
    public static final int FROM_COLLECT = 1;

    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String TYPE = "type";
    public static final String AUTHOR = "author";

    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.web_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.web_main)
    View mView;

    private String mUrl;
    private String mTitle;
    private UrlCollectDao mUrlCollectDao;
    private String mType;
    private String mAuthor;
    private boolean isCollect;
    private boolean isInitCollect;
    private UrlCollect mUrlCollect;
    private CollectStates mStates = CollectStates.NORMAL;
    private int mFromType;

    enum CollectStates {
        NORMAL, COLLECT, UN_COLLECT
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initTheme() {
        super.initTheme();
        if (App.isNight()) {
            setTheme(R.style.AppTheme_Night_NoActionBar);
        } else {
            setTheme(R.style.AppTheme_Day_NoActionBar);
        }
    }

    @Override
    protected void initViews() {
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
        settings.supportMultipleWindows();  //多窗口
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void bindListener() {
        setTitle(mTitle);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setHomeAsUpIndicator(R.drawable.ic_toolbar_close);
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircularAnimUtil.actionVisible_(false, WebActivity.this, v, mView, 0, 618);
            }
        });
    }

    @Override
    protected void initValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString("url");
            mTitle = bundle.getString("title");
            mType = bundle.getString("type", Constants.ALL);
            mAuthor = bundle.getString("author");
            mFromType = bundle.getInt("from_type");
        }
        mUrlCollectDao = App.getDaoSession().getUrlCollectDao();
        List<UrlCollect> list = mUrlCollectDao.queryBuilder().where(UrlCollectDao.Properties.Url.eq(mUrl)).list();
        if (!ListUtils.isListEmpty(list)) {
            isInitCollect = true;
            isCollect = true;
            mUrlCollect = list.get(0);
        }
    }

    public static void startWebActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, WebActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        switchCollectIcon(menu.findItem(R.id.welfare_collect));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.welfare_collect:
                mStates = CollectStates.NORMAL;
                if (!isCollect) {
                    if (!isInitCollect) {
                        mStates = CollectStates.COLLECT;
                    }
                    Snackbar.make(mView, R.string.collect_success, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (isInitCollect) {
                        mStates = CollectStates.UN_COLLECT;
                    }
                    Snackbar.make(mView, R.string.collect_cancel, Snackbar.LENGTH_SHORT).show();
                }

                isCollect = !isCollect;
                switchCollectIcon(item);
                return true;
            case R.id.welfare_share:
                ShareUtils.getInstance().shareText(this, mWebView.getTitle(), mWebView.getUrl());
                return true;
            case R.id.welfare_copy_url:
                AppUtils.copyText(this, mWebView.getUrl());
                ToastUtils.showToast(R.string.tip_copy_success);
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

    private void switchCollectIcon(MenuItem item) {
        if (isCollect) {
            item.setIcon(R.drawable.navigation_collect_prs);
        } else {
            item.setIcon(R.drawable.navigation_collect_nor);
        }
    }

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        if (intent.resolveActivity(WebActivity.this.getPackageManager()) != null) {
            WebActivity.this.startActivity(intent);
        } else {
            ToastUtils.showToast(R.string.web_open_failed);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void collectUrl() {
        UrlCollect urlCollect = new UrlCollect(null, mUrl, mTitle, new Date(), mType, mAuthor);
        mUrlCollectDao.insert(urlCollect);
    }

    private void cancelCollect() {
        mUrlCollectDao.deleteByKey(mUrlCollect.getId());
    }

    public class MyWebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url)) {
                mWebView.loadUrl(url);
            }
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//            String url = request.getUrl().toString();
//            KLog.d("url:" + url);
//            return new WebResourceResponse("application/x-javascript", "utf-8", null);
//            return new WebResourceResponse("image/jpeg", "UTF-8", null);
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
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

    public class MyWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mProgressBar == null) {
                return;
            }
            mProgressBar.setProgress(newProgress);

            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return true;
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
        if (mStates == CollectStates.COLLECT) {
            collectUrl();
        } else if (mStates == CollectStates.UN_COLLECT) {
            cancelCollect();
            if (mFromType == FROM_COLLECT) {
                RxUtils.getInstance().OnUnCollect();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
    }
}
