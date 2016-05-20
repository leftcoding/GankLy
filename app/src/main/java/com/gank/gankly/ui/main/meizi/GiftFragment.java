package com.gank.gankly.ui.main.meizi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.bean.GiftResult;
import com.gank.gankly.config.ViewsModel;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.ui.browse.BrowseActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.view.IBaseView;
import com.gank.gankly.utils.RxUtils;
import com.gank.gankly.widget.LoadingLayoutView;
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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-05-17
 */
public class GiftFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ItemClick {
    private static GiftFragment sGiftFragment;

    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.loading_view)
    LoadingLayoutView mLoadingLayoutView;

    private GiftAdapter mAdapter;
    private MainActivity mActivity;

    private static final int timeout = 50 * 1000;
    private static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; Desktop) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.9.0.25";
    private String url = "http://www.mzitu.com/mm/page/";

    private int mCurPage = 1;
    private int mPages = 1;
    private int mLastPosition = 0;
    private int mDetailsPageCount = 1;
    private List<GiftBean> mImageCountList;
    private ProgressDialog mDialog;
    private int progress;
    private int mClickPosition;
    private IBaseView.ViewStatus mViewStatus = IBaseView.ViewStatus.LOADING;
    private RxUtils mRxUtils = new RxUtils();
    private boolean isUnSubscribe;
    private Subscription s;

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

        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setHomeAsUpIndicator(R.drawable.ic_home_navigation);
            bar.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.openDrawer();
            }
        });
        onDownRefresh();
    }

    @Override
    protected void initViews() {
        mImageCountList = new ArrayList<>();
        mAdapter = new GiftAdapter(mActivity);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        initRecycler();
    }

    @Override
    protected void bindLister() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collcet;
    }

    private void onDownRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mCurPage = 1;
        fetchPageCount();
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
                    fetchPageCount();
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


    private void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mActivity);
        }
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMessage(App.getAppString(R.string.loading));
        mDialog.setIndeterminate(false);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                KLog.d("onCancel");
                progress = 0;
                mRxUtils.unsubscribe();
            }
        });
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    KLog.d("KEYCODE_BACK");
                }
                return false;
            }
        });
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    private void disDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void setMax(int max) {
        if (mDialog != null) {
            mDialog.setMax(max);
        }
    }

    private void setProgress(int progress) {
        if (mDialog != null) {
            mDialog.setProgress(progress);
        }
    }

    private void fetchPageCount() {
        Observable<GiftResult> observable = Observable.create(new Observable.OnSubscribe<GiftResult>() {
            @Override
            public void call(Subscriber<? super GiftResult> subscriber) {
                try {
                    if (mCurPage > mPages) {
                        return;
                    }
                    String _url = url + mCurPage;
                    Document doc = Jsoup.connect(_url)
                            .userAgent(DESKTOP_USERAGENT)
                            .timeout(timeout)
                            .get();
                    int num = getBigPageNum(doc);
                    subscriber.onNext(new GiftResult(num, getPageCount(doc)));
                } catch (IOException e) {
                    KLog.e(e);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(3);

        observable.subscribe(new Subscriber<GiftResult>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
                if (mCurPage <= mPages) {
                    mCurPage = mCurPage + 1;
                }
                if (IBaseView.ViewStatus.SHOW != mViewStatus) {
                    mViewStatus = IBaseView.ViewStatus.SHOW;
                    showView();
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
                }
            }
        });
    }
    //我对你的爱就像
    private void fetchImagePage(final String url) {
        s = Observable.create(new Observable.OnSubscribe<GiftResult>() {
            @Override
            public void call(Subscriber<? super GiftResult> subscriber) {
                subscriber.add(new Subscription() {
                    @Override
                    public void unsubscribe() {
                        KLog.d("unsubscribe");
                        isUnSubscribe = true;
                    }

                    @Override
                    public boolean isUnsubscribed() {
                        return false;
                    }
                });

                if (s != null && !s.isUnsubscribed()) {

                }

                try {
                    if (mCurPage > mPages) {
                        return;
                    }
                    Document doc = Jsoup.connect(url)
                            .userAgent(DESKTOP_USERAGENT)
                            .timeout(timeout)
                            .get();
                    subscriber.onNext(new GiftResult(0, getImageCount(url, doc)));
                } catch (Exception e) {
                    KLog.e(e.toString() + e);
                }
                subscriber.onCompleted();
            }


        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GiftResult>() {
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
                                setMax(giftResult.getList().size());
                                fetchImages(giftResult.getList());
                            }
                        }
                    }
                });
        mRxUtils.manage(s);
    }

    private int getBigPageNum(Document doc) {
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

    private List<GiftBean> getPageCount(Document doc) {
        List<GiftBean> list = null;
        if (doc == null) {
            return null;
        }
        Elements hrefs = doc.select("#pins > li > a");
        Elements img = doc.select("#pins a img");
        Elements times = doc.select(".time");
        Elements views = doc.select(".view");

        int countSize = hrefs.size();
        int imgSize = img.size();
        int size = countSize > imgSize ? imgSize : countSize;

        if (size > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String imgUrl = img.get(i).attr("data-original");
                String title = img.get(i).attr("alt");
                String url = hrefs.get(i).attr("href");
                String time = times.get(i).text();
                String view = views.get(i).text();
                if (!TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(url)) {
                    list.add(new GiftBean(imgUrl, url, time, view, title));
                }
            }
        }
        return list;
    }

    private List<GiftBean> getImageCount(String url, Document doc) {
        KLog.d("getImageCount" + ",isUnSubscribe:" + isUnSubscribe);
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
            for (int i = 1; i <= mDetailsPageCount; i++) {
                String _url = url + "/" + i;
                list.add(new GiftBean(_url));
            }
        }

        return list;
    }

    private void fetchImages(List<GiftBean> list) {
        Observable.from(list).flatMap(new Func1<GiftBean, Observable<List<GiftBean>>>() {
            @Override
            public Observable<List<GiftBean>> call(GiftBean giftBean) {
                final String url = giftBean.getImgUrl();
                setProgress(progress++);
                return Observable.create(new Observable.OnSubscribe<List<GiftBean>>() {
                    @Override
                    public void call(Subscriber<? super List<GiftBean>> subscriber) {
                        try {
                            if (mCurPage > mPages) {
                                return;
                            }
                            Document doc = Jsoup.connect(url)
                                    .userAgent(DESKTOP_USERAGENT)
                                    .timeout(timeout)
                                    .get();
                            subscriber.onNext(getImageCountList(doc));
                        } catch (IOException e) {
                            KLog.e(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GiftBean>>() {
                    @Override
                    public void onCompleted() {
                        disDialog();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(mActivity, BrowseActivity.class);
                        bundle.putString(ViewsModel.Gift, ViewsModel.Gift);
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<GiftBean> giftResult) {
                        if (giftResult != null && giftResult.size() > 0) {
                            mImageCountList.addAll(giftResult);
                        }
                    }
                });
    }

    private List<GiftBean> getImageCountList(Document doc) {
        List<GiftBean> giftBeen = null;
        if (doc != null) {
            giftBeen = new ArrayList<>();
            Elements links = doc.select(".main-image img[src$=.jpg]");
            String img = links.get(0).attr("src");
            giftBeen.add(new GiftBean(img));
        }
        return giftBeen;
    }


    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private void showView() {
        mLoadingLayoutView.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        onDownRefresh();
    }

    @Override
    public void onClick(int position, Object object) {
        if (mClickPosition != position) {
            mClickPosition = position;
            mImageCountList.clear();
            progress = 0;
        }
        GiftBean giftBean = (GiftBean) object;
        showDialog();
        fetchImagePage(giftBean.getUrl());
    }

    public List<GiftBean> getList() {
        return mImageCountList;
    }
}
