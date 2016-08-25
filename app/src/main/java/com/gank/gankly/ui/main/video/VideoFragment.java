package com.gank.gankly.ui.main.video;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.impl.VideoPresenterImpl;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.base.BaseSwipeRefreshLayout;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.web.WebVideoViewActivity;
import com.gank.gankly.view.IMeiziView;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-04-25
 */
public class VideoFragment extends BaseSwipeRefreshFragment implements MeiziOnClick,
        SwipeRefreshLayout.OnRefreshListener, IMeiziView<List<ResultsBean>> {
    private static VideoFragment sVideoFragment;

    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    BaseSwipeRefreshLayout mSwipeRefreshLayout;

    private IBaseRefreshPresenter mPresenter;
    private MainActivity mActivity;
    private VideoAdapter mVideoRecyclerAdapter;

    public static VideoFragment getInstance() {
        if (sVideoFragment == null) {
            synchronized (VideoFragment.class) {
                if (sVideoFragment == null) {
                    sVideoFragment = new VideoFragment();
                }
            }
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
        mPresenter = new VideoPresenterImpl(mActivity, this);
    }

    @Override
    protected void initValues() {
        onLoading();
    }

    private void onLoading(){
        mMultipleStatusView.showLoading();
        onDownRefresh();
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
        setMultipleStatusView(mMultipleStatusView);
        mMultipleStatusView.setListener(new MultipleStatusView.OnMultipleClick() {
            @Override
            public void retry(View v) {
                onLoading();
            }
        });
        mVideoRecyclerAdapter = new VideoAdapter(mActivity);
        mVideoRecyclerAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setAdapter(mVideoRecyclerAdapter);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnScrollListener(new BaseSwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                onDownRefresh();
            }

            @Override
            public void onLoadMore() {
                mPresenter.fetchMore();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark);
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
        mPresenter.fetchNew();
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
    public void refillDate(List<ResultsBean> list) {
        mVideoRecyclerAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<ResultsBean> list) {
        mVideoRecyclerAdapter.addItems(list);
    }

    @Override
    public void showRefreshError(String error) {
        Snackbar.make(mCoordinatorLayout, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mCoordinatorLayout, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideRefresh() {
        super.hideRefresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        super.showRefresh();
        mSwipeRefreshLayout.setRefreshing(true);
    }
}
