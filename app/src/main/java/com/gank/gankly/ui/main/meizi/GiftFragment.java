package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.config.Constants;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.browse.BrowseActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Create by LingYan on 2016-05-17
 */
public class GiftFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener, MeiziOnClick {
    private static GiftFragment sGiftFragment;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MeiZiRecyclerAdapter mRecyclerAdapter;
    private MainActivity mActivity;

    private int mPage = 1;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private boolean isLoadMore = true;

    private static final int timeout = 5 * 1000;
    public static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; Desktop) AppleWebKit/534.13 (KHTML, like Gecko) UCBrowser/8.9.0.25";

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
            bar.setDisplayHomeAsUpEnabled(true);
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
        mRecyclerAdapter = new MeiZiRecyclerAdapter(mActivity);
        mRecyclerAdapter.setMeiZiOnClick(this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
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
        mPage = 1;
//        fetchDate();
    }

    private void initRecycler() {
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    GiftFragment.this.onScrollStateChanged();
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }

    private void fetchImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://www.mzitu.com/63571";
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
                KLog.d("pagenavi:" + links.size());
                for (int i = 0; i < links.size(); i++) {
                    KLog.d("pagenavi:" + links.get(i).text());
                }
//                KLog.d("pagenavi:" + links.get(links.size() - 2).text());
            }
        }).start();
    }


    private void fetchDate() {
        final int limit = Constants.MEIZI_LIMIT;
        GankRetrofit.getInstance().fetchWelfare(limit, mPage, new Subscriber<GankResult>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
                mPage = mPage + 1;
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                mSwipeRefreshLayout.setRefreshing(false);
                Snackbar.make(mSwipeRefreshLayout, R.string.tip_server_error, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNext(GankResult gankResult) {
                if (!gankResult.isEmpty()) {
                    if (mPage == 1) {
                        MeiziArrayList.getInstance().clear();
                    }
                    MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), mPage);
                }
                if (gankResult.getSize() < limit) {
                    isLoadMore = false;
                    Snackbar.make(mRecyclerView, R.string.loading_pic_no_more, Snackbar.LENGTH_LONG).show();
                }

                mRecyclerAdapter.updateItems(MeiziArrayList.getInstance().getArrayList());
            }
        });
    }

    private void onScrollStateChanged() {
        int[] positions = new int[mStaggeredGridLayoutManager.getSpanCount()];
        mStaggeredGridLayoutManager.findLastVisibleItemPositions(positions);
        for (int position : positions) {
            if (position == mStaggeredGridLayoutManager.getItemCount() - 1 && isLoadMore && !mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(true);
                fetchDate();
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        onDownRefresh();
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        Intent intent = new Intent(mActivity, BrowseActivity.class);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }
}
