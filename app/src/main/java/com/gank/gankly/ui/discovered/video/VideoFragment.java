package com.gank.gankly.ui.discovered.video;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.ui.web.WebVideoViewActivity;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;

/**
 * 休息视频
 * Create by LingYan on 2016-04-25
 * Email:137387869@qq.com
 */
public class VideoFragment extends LazyFragment implements MeiziOnClick,
        SwipeRefreshLayout.OnRefreshListener, VideoContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private VideoContract.Presenter mPresenter;
    private HomeActivity mActivity;
    private VideoAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) context;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new VideoPresenter(GankDataSource.getInstance(), this);
    }

    @Override
    protected void initValues() {
    }

    private void onLoading() {
        mPresenter.fetchNew();
    }

    @Override
    protected void initViews() {
        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mMultipleStatusView.setListener(v -> onLoading());

        mAdapter = new VideoAdapter(mActivity);
        mAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setAdapter(mAdapter);

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
                mPresenter.fetchMore();
            }
        });
    }

    @Override
    protected void bindListener() {
    }

    @Override
    protected void callBackRefreshUi() {
        Resources.Theme theme = mActivity.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.resourceId;
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, typedValue, true);
        int textColor = typedValue.data;
        theme.resolveAttribute(R.attr.themeBackground, typedValue, true);
        int mainColor = typedValue.data;
        mRecyclerView.setBackgroundColor(mainColor);

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            View view = childView.findViewById(R.id.goods_rl_title);
            TextView title = (TextView) childView.findViewById(R.id.goods_txt_title);
            view.setBackgroundResource(background);
            title.setTextColor(textColor);
        }
        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        List<ResultsBean> list = mAdapter.getResults();
        bundle.putString(WebVideoViewActivity.TITLE, list.get(position).getDesc());
        bundle.putString(WebVideoViewActivity.URL, list.get(position).getUrl());
        WebVideoViewActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void onRefresh() {
        onLoading();
    }

    @Override
    protected void initData() {
        mPresenter.fetchNew();
    }

    @Override
    public void refillData(List<ResultsBean> list) {
        mAdapter.refillItems(list);
    }

    @Override
    public void appendData(List<ResultsBean> list) {
        mAdapter.appendItems(list);
    }

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mRecyclerView, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showContent() {
        mMultipleStatusView.showContent();
    }

    @Override
    public void showEmpty() {
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showDisNetWork() {
        mMultipleStatusView.showDisNetwork();
    }

    @Override
    public void showError() {
        mMultipleStatusView.showError();
    }

    @Override
    public void showLoading() {
        mMultipleStatusView.showLoading();
    }

    @Override
    public void showRefreshError(String errorStr) {
        Snackbar.make(mRecyclerView, errorStr, Snackbar.LENGTH_LONG).show();
    }
}
