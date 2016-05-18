package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-05-17
 */
public class GiftFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener, ItemClick {
    private static GiftFragment sGiftFragment;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private GiftAdapter mAdapter;
    private MainActivity mActivity;

    private boolean isLoadMore = true;
    private static final int timeout = 50 * 1000;

    private static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; Desktop) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.9.0.25";
    private String url = "http://www.mzitu.com/mm/page/";

    private int mCurPage = 1;
    private int mPages = 1;
    private int mLastPosition = 0;
    private int mDetailsPageCount = 1;
    private int mCurDetailsPage = 1;
    private boolean isForLoading = false;

    public GiftFragment() {
    }

    public static GiftFragment getInstance() {
        if (sGiftFragment == null) {
            sGiftFragment = new GiftFragment();
        }
        return sGiftFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void parseArguments() {

    }

    @Override
    protected void initValues() {
        mActivity.setTitle(R.string.navigation_gift);
        mActivity.setSupportActionBar(mToolbar);
        ProgressBar progressBar = new ProgressBar(mActivity, null,
                android.R.attr.progressBarStyleSmallInverse);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(60, 60);
        params.gravity = Gravity.END;
        params.setMargins(0, 0, 20, 0);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowCustomEnabled(true);
            bar.setCustomView(progressBar, params);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
    }

    @Override
    protected void initViews() {
        mAdapter = new GiftAdapter(mActivity);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        initRecycler();
    }

    @Override
    protected void bindLister() {
        fetchImages();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initDate() {
        onDownRefresh();
    }

    private void onDownRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mCurPage = 1;
        fetchPageNum();
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (mLastPosition + 1 == mAdapter.getItemCount())
                        && !mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    fetchPageNum();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }

    private void fetchImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://www.mzitu.com/54285/1";
                Document doc = null;
                try {
                    doc = Jsoup.connect(url)
                            .userAgent(DESKTOP_USERAGENT)
                            .timeout(timeout)
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String title = doc.title();

                Elements links = doc.select(".main-image img[src$=.jpg]");
                KLog.d("links:" + links.size());
                for (int i = 0; i < links.size(); i++) {
                    String img = links.get(i).attr("src");
                    KLog.d("img:" + img);
                }

                links = doc.select(".pagenavi a[href]");
                KLog.d("pagenavi，size:" + links.size());
                for (int i = 0; i < links.size(); i++) {
                    KLog.d("pagenavi:" + links.get(i).text() + "," + links.get(i).attr("href"));

                }
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String url = "http://www.mzitu.com/mm/page/1";
//                try {
//                    Document doc = Jsoup.connect(url)
//                            .userAgent(DESKTOP_USERAGENT)
//                            .timeout(timeout)
//                            .get();
//
//                    Elements links = doc.select("#pins > li > a");
//                    KLog.d("page num:" + links.size());
//                    for (int i = 0; i < links.size(); i++) {
//                        String href = links.get(i).attr("href");
//                        KLog.d("href:" + href);
//                    }
//                    links = doc.select("#pins a img");
//                    for (int i = 0; i < links.size(); i++) {
//                        String img = links.get(i).attr("data-original");
//                        KLog.d("img:" + img);
//                    }
//
//                    try {
//                        links = doc.select(".time");
//                        KLog.d("time:" + links.get(0).text());
//
//                        links = doc.select(".view");
//                        KLog.d("time:" + links.get(0).text());
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    links = doc.select(".pagenavi a[href]");
//                    KLog.d("pagenavi，size:" + links.size());
//                    for (int i = 0; i < links.size(); i++) {
//                        KLog.d("pagenavi:" + isNumeric(links.get(i).text()) + "," + links.get(i).attr("href"));
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    private void fetchPageNum() {
        Observable<GiftResult> observable = Observable.create(new Observable.OnSubscribe<GiftResult>() {
            @Override
            public void call(Subscriber<? super GiftResult> subscriber) {
                try {
                    try {
                        if (mCurPage > mPages) {
                            return;
                        }
                        String _url = url + mCurPage;
                        KLog.d("_url:" + _url);
                        Document doc = Jsoup.connect(_url)
                                .userAgent(DESKTOP_USERAGENT)
                                .timeout(timeout)
                                .get();
                        int num = getPageNum(doc);
                        subscriber.onNext(new GiftResult(num, getCountPage(doc)));
                    } catch (IOException e) {
                        KLog.e(e);
                    }
                } catch (Exception e) {
                    KLog.e(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Subscriber<GiftResult>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(GiftResult giftResult) {
                if (giftResult != null) {
                    if (giftResult.getSize() > 0) {
                        if (mCurPage == 1) {
                            mAdapter.clear();
                        }
                        mAdapter.updateItems(giftResult.getList());
                    }
                    mPages = giftResult.getNum();
                    KLog.d("mPages:" + mPages);
                }
            }
        });
    }

    private void fetchCount(final String url) {
        Observable<GiftResult> observable = Observable.create(new Observable.OnSubscribe<GiftResult>() {
            @Override
            public void call(Subscriber<? super GiftResult> subscriber) {
                try {
                    try {
                        if (mCurPage > mPages) {
                            return;
                        }
                        KLog.d("url:" + url);
                        Document doc = Jsoup.connect(url)
                                .userAgent(DESKTOP_USERAGENT)
                                .timeout(timeout)
                                .get();
                        subscriber.onNext(new GiftResult(0, getCount(doc)));
                    } catch (IOException e) {
                        KLog.e(e);
                    }
                } catch (Exception e) {
                    KLog.e(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Subscriber<GiftResult>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
                KLog.d("mCurPage:" + mCurPage + ",mPages:" + mPages);
                if (mCurPage <= mPages) {
                    mCurPage = mCurPage + 1;
                }
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(GiftResult giftResult) {
                if (giftResult != null) {
                    if (giftResult.getSize() > 0) {
                        if (mCurPage == 1) {
                            mAdapter.clear();
                        }
                        mAdapter.updateItems(giftResult.getList());
                    }
                    mPages = giftResult.getNum();
                    KLog.d("mPages:" + mPages);
                }
            }
        });
    }

    private int getPageNum(Document doc) {
        int p = 0;
        if (doc != null) {
            Elements count = doc.select(".pagenavi a[href]");
            int size = count.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    String num = count.get(i).text();
                    if (isNumeric(num)) {
                        try {
                            return Integer.parseInt(num);
                        } catch (IllegalFormatException e) {
                            KLog.e(e);
                        }
                    }
                }
            }
        }
        return p;
    }

    private List<GiftBean> getCountPage(Document doc) {
        List<GiftBean> list = null;
        if (doc == null) {
            return null;
        }
        Elements count = doc.select("#pins > li > a");
        Elements img = doc.select("#pins a img");
        Elements times = doc.select(".time");
        Elements views = doc.select(".view");

        int countSize = count.size();
        int imgSize = img.size();
        int size = countSize > imgSize ? imgSize : countSize;
        KLog.d("links:" + size);

        if (size > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String imgUrl = img.get(i).attr("data-original");
                String title = img.get(i).attr("alt");
                String url = count.get(i).attr("href");
                String time = times.get(i).text();
                String view = views.get(i).text();
                if (!TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(url)) {
                    list.add(new GiftBean(imgUrl, url, time, view, title));
                }
            }
        }
        return list;
    }

    private List<GiftBean> getCount(Document doc) {
        List<GiftBean> list;
        if (doc == null) {
            return null;
        }
        list = new ArrayList<>();
        Elements pages = doc.select(".pagenavi a[href]");
        int size = pages.size();
        for (int i = size - 1; i > 0; i--) {
            String page = pages.get(i).text();
            if (isNumeric(page)) {
                mDetailsPageCount = Integer.parseInt(page);
                break;
            }
        }

        if (mDetailsPageCount > 0) {
            for (int i = mCurDetailsPage; mCurDetailsPage < mDetailsPageCount; mCurDetailsPage++) {
                Elements links = doc.select(".main-image img[src$=.jpg]");
                list.add(new GiftBean(links.get(i).attr("src")));

            }
        }
        return list;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    @Override
    public void onRefresh() {
        onDownRefresh();
    }

    @Override
    public void onClick(int position, Object object) {
        GiftBean giftBean = (GiftBean) object;
        fetchCount(giftBean.getUrl());
    }

    public List<GiftBean> getList() {
        return mAdapter.getResults();
    }
}
