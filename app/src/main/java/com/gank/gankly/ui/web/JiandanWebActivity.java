package com.gank.gankly.ui.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.gank.gankly.utils.CrashUtils;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.utils.RxUtils;
import com.gank.gankly.utils.ShareUtils;
import com.gank.gankly.utils.ToastUtils;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-5-10
 */
public class JiandanWebActivity extends BaseActivity {
    public static final int FROM_COLLECT = 1;
    public static final int FROM_JIANDAN = 2;

    private static final int timeout = 50 * 1000;
    private static final String USERAGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 7 Build/JSS15Q) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2307.2 Safari/537.36";

    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.web_progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.web_main)
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
                onBackPressed();
            }
        });
    }

    @Override
    protected void initValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString("url");
            mTitle = bundle.getString("title");
            mType = bundle.getString("type", Constants.JIANDAN);
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

        parseLoadUrlData(mUrl);
    }

    private void parseLoadUrlData(final String url) {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Document doc = Jsoup.connect(url)
                            .userAgent(USERAGENT)
                            .timeout(timeout)
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .get();
                    String _url = null;
                    if (doc != null) {
                        doc = removeDivs(doc);
                        _url = doc.html();
                    }
                    subscriber.onNext(_url);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    KLog.e(e);
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                KLog.d("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CrashUtils.crashReport(e);
            }

            @Override
            public void onNext(String s) {
                mWebView.loadDataWithBaseURL("http://i.jandan.net", s, "text/html", "utf-8", null);
            }
        });
    }

    private Document removeDivs(Document doc) {
        List<String> list = getRemoveDivs();
        for (int i = 0; i < list.size(); i++) {
            doc.select(list.get(i)).remove();
        }

        doc = removeScripts(doc);
        return doc;
    }

    private List<String> getRemoveDivs() {
        List<String> list = new ArrayList<>();
        list.add("#headerwrapper"); //id
        list.add("#footer");
        list.add("#commentform");
        list.add(".comment-hide");//class
        list.add(".share-links");
        list.add(".star-rating");
        list.add(".s_related");
        return list;
    }

    private Document removeScripts(Document doc) {
        Elements sc = doc.select("script");
        for (int i = 0; i < sc.size(); i++) {
            String scText = sc.get(i).toString();
            if (!TextUtils.isEmpty(scText) && scText.contains("decodeURIComponent") &&
                    scText.contains("s_related")) {
                sc.get(i).remove();
            }
        }
        return doc;
    }

    public static void startWebActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, JiandanWebActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.welfare_menu, menu);
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
        if (intent.resolveActivity(JiandanWebActivity.this.getPackageManager()) != null) {
            JiandanWebActivity.this.startActivity(intent);
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
            KLog.d("url:" + url);
            if (!TextUtils.isEmpty(url)) {
                mWebView.loadUrl(url);
            }
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
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

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return true;
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
