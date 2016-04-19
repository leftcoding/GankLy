package com.gank.gankly.ui.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.utils.ToastUtils;

import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

public class WebActivity extends BaseActivity {
    @Bind(R.id.web_view)
    WebView mWebView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.web_progress_bar)
    ProgressBar mProgressBar;

    private String mUrl;
    private String mTitle;
    private UrlCollectDao mUrlCollectDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initValues();
        initView();
        bindLister();
    }

    private void initValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString("url");
            mTitle = bundle.getString("title");
        }

        mUrlCollectDao = App.getDaoSession().getUrlCollectDao();
    }

    private void initView() {
        WebSettings settings = mWebView.getSettings();
        //支持获取手势焦点，输入用户名、密码或其他
        mWebView.requestFocusFromTouch();

        settings.setJavaScriptEnabled(true);  //支持js

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

    private void bindLister() {
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true); //显示返回箭头
        }

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.welfare_collect) {
                    addUrl();
                } else if (itemId == R.id.welfare_share) {
                    showShare(mTitle, mWebView.getUrl(), mTitle, null);
                }
                return false;
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addUrl() {
        UrlCollect urlCollect = new UrlCollect(null, mWebView.getUrl(), mTitle, new Date());
        mUrlCollectDao.insert(urlCollect);
        ToastUtils.showToast(R.string.collect_success);
    }

    public class MyWebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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
    }

    /**
     * 演示调用ShareSDK执行分享
     */
    public static void showShare(String title, String titleUrl, String text, String imgUrl) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle("Android干货分享");
        sp.setTitleUrl(titleUrl); // 标题的超链接
        sp.setText(text);
        sp.setImageUrl("http://www.uisheji.com/wp-content/uploads/2012/12/05/mza_3859585171796455226.175x175-75.png");
//        sp.setSite("发布分享的网站名称");
//        sp.setSiteUrl("发布分享网站的地址");
        Platform qzone = ShareSDK.getPlatform(QQ.NAME);
        // 设置分享事件回调
        qzone.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        // 执行图文分享
        qzone.share(sp);
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
        getMenuInflater().inflate(R.menu.menu_welfare, menu);
        return true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        ButterKnife.unbind(this);
    }
}
