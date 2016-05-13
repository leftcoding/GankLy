package com.gank.gankly.ui.main.video;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.bean.GankResult;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.network.GankRetrofit;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.web.WebVideoViewActivity;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-04-25
 */
public class VideoFragment extends BaseFragment implements MeiziOnClick, SwipeRefreshLayout.OnRefreshListener {
    private int mLimit = 20;
    private int mPage;
    private static VideoFragment sVideoFragment;

    @Bind(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private MainActivity mActivity;
    private VideoRecyclerAdapter mVideoRecyclerAdapter;
    private int mLastPosition;


    public static VideoFragment getInstance() {
        if (sVideoFragment == null) {
            sVideoFragment = new VideoFragment();
        }
        return sVideoFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    protected void initValues() {
        KLog.d("initValues");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onDownRefresh();
            }
        }, 100);
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.navigation_video);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar barLayout = mActivity.getSupportActionBar();
        if (barLayout != null) {
            barLayout.setHomeAsUpIndicator(R.drawable.ic_home_navigation);
            barLayout.setDisplayHomeAsUpEnabled(true);
        }

        mVideoRecyclerAdapter = new VideoRecyclerAdapter(mActivity);
        mVideoRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mVideoRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (mLastPosition + 1 == mVideoRecyclerAdapter.getItemCount())
                        && !mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(true);
                    fetchVideo();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
        mSwipeRefresh.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void bindLister() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.openDrawer();
            }
        });
    }

    private void onDownRefresh() {
        KLog.d("onDownRefresh");
        mPage = 1;
        fetchVideo();
    }

    private void fetchVideo() {
        final Observable<GankResult> video = GankRetrofit.getInstance()
                .getGankService().fetchVideo(mLimit, mPage);
        Observable<GankResult> image = GankRetrofit.getInstance()
                .getGankService().fetchBenefitsGoods(mLimit, mPage);

        Observable.zip(video, image, new Func2<GankResult, GankResult, GankResult>() {
            @Override
            public GankResult call(GankResult gankResult, GankResult gankResult2) {
                addImages(gankResult2);
                return gankResult;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResult>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefresh.setRefreshing(false);
                        mPage = mPage + 1;
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onNext(GankResult gankResult) {
                        if (!gankResult.isEmpty()) {
                            if (mPage == 1) {
                                mVideoRecyclerAdapter.clear();
                            }
                            mVideoRecyclerAdapter.updateItems(gankResult.getResults());
                        }

                        if (gankResult.getSize() < mLimit) {
                            Snackbar.make(mCoordinatorLayout, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void addImages(GankResult gankResult) {
        if (gankResult != null) {
            int page = MeiziArrayList.getInstance().getPage();
            if (page == 0 || page < mPage) {
                MeiziArrayList.getInstance().addBeanAndPage(gankResult.getResults(), mPage);
            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        List<ResultsBean> list = mVideoRecyclerAdapter.getResults();
        bundle.putString("title", list.get(position).getDesc());
        bundle.putString("url", list.get(position).getUrl());
        WebVideoViewActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        fetchVideo();
    }
}
