package com.gank.gankly.ui.main.video;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.RxBus.ChangeThemeEvent.ThemeEvent;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.impl.VideoPresenterImpl;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.base.LySwipeRefreshLayout;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.ui.web.WebVideoViewActivity;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.view.IMeiziView;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * 休息视频
 * Create by LingYan on 2016-04-25
 * Email:137387869@qq.com
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
    LySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private IBaseRefreshPresenter mPresenter;
    private HomeActivity mActivity;
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
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) context;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new VideoPresenterImpl(mActivity, this);
    }

    @Override
    protected void initValues() {
        onLoading();
    }

    private void onLoading() {
        mMultipleStatusView.showLoading();
        onDownRefresh();
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.navigation_video);

        setMultipleStatusView(mMultipleStatusView);
        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mMultipleStatusView.setListener(new MultipleStatusView.OnMultipleClick() {
            @Override
            public void retry(View v) {
                onLoading();
            }
        });

        mVideoRecyclerAdapter = new VideoAdapter(mActivity);
        mVideoRecyclerAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setAdapter(mVideoRecyclerAdapter);

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                onDownRefresh();
            }

            @Override
            public void onLoadMore() {
                mPresenter.fetchMore();
            }
        });
    }

    @Override
    protected void bindLister() {
        RxBus.getInstance().toSubscription(ThemeEvent.class, new Action1<ThemeEvent>() {
            @Override
            public void call(ThemeEvent themeEvent) {
                onRefreshUi();
            }
        });
    }

    private void onRefreshUi() {
        Resources.Theme theme = mActivity.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.data;
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, typedValue, true);
        int textColor = typedValue.data;
        theme.resolveAttribute(R.attr.themeBackground, typedValue, true);
        int mainColor = typedValue.data;
        mRecyclerView.setBackgroundColor(mainColor);
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        mToolbar.setBackgroundResource(typedValue.resourceId);

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            View view = childView.findViewById(R.id.goods_rl_title);
            view.setBackgroundColor(background);
            TextView title = (TextView) childView.findViewById(R.id.goods_txt_title);
            title.setTextColor(textColor);
        }

        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    private void onDownRefresh() {
        mPresenter.fetchNew();
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
