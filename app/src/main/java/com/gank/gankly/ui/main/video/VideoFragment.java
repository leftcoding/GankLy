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
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.ViewStatus;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.presenter.VideoPresenter;
import com.gank.gankly.ui.view.IVideoView;
import com.gank.gankly.ui.web.WebVideoViewActivity;
import com.gank.gankly.widget.LoadingLayoutView;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-04-25
 */
public class VideoFragment extends BaseSwipeRefreshFragment<VideoPresenter> implements MeiziOnClick, SwipeRefreshLayout.OnRefreshListener, IVideoView<ResultsBean> {
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
    @Bind(R.id.loading_layout)
    LoadingLayoutView mLoadingLayoutView;

    private VideoPresenter mPresenter;
    private MainActivity mActivity;
    private VideoAdapter mVideoRecyclerAdapter;
    private int mLastPosition;
    private ViewStatus mCurStatus = ViewStatus.LOADING;


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
    protected void initPresenter() {
        mPresenter = new VideoPresenter(mActivity, this);
    }

    @Override
    protected void initValues() {
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

        mVideoRecyclerAdapter = new VideoAdapter(mActivity);
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
                    mPresenter.setViewStatus(mCurStatus);
                    mPresenter.fetchDate(mPage, mLimit);
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
        mPage = 1;
        mPresenter.setViewStatus(mCurStatus);
        mPresenter.fetchDate(mPage, mLimit);
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
        onDownRefresh();
    }

    @Override
    public void refillDate(List list) {
        mVideoRecyclerAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<ResultsBean> list) {
        mVideoRecyclerAdapter.addItems(list);
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mCoordinatorLayout, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideRefresh() {
        super.hideRefresh();
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        super.showRefresh();
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        KLog.e(e);
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        mPage = mPage + 1;
    }

    @Override
    public void showView() {
        super.showView();
        mLoadingLayoutView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
