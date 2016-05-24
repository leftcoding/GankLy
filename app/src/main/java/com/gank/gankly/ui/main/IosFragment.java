package com.gank.gankly.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.presenter.IosPresenter;
import com.gank.gankly.ui.view.IIosView;
import com.gank.gankly.ui.web.WebActivity;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-4-26
 */
public class IosFragment extends LazyFragment<IosPresenter> implements SwipeRefreshLayout.OnRefreshListener, RecyclerOnClick, IIosView<ResultsBean> {
    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainActivity mActivity;
    private GankAdapter mRecyclerAdapter;

    private int mPage = 1;
    private int mLastPosition;
    private boolean isLoadMore = true;
    private IosPresenter mPresenter;

    public IosFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IosPresenter(mActivity, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {
        mRecyclerAdapter = new GankAdapter(mActivity);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        initRecycler();
    }

    @Override
    protected void bindLister() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastPosition + 1 == mRecyclerAdapter.getItemCount() && !mSwipeRefreshLayout.isRefreshing()) {
                    if (isLoadMore) {
                        toRefresh();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meizi;
    }

    @Override
    protected void initDate() {
        onDownRefresh();
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }

    private void onDownRefresh() {
        mPage = 1;
        toRefresh();
    }

    private void toRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.fetchDate(mPage);
    }

    public static IosFragment newInstance() {
        IosFragment fragment = new IosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        onDownRefresh();
    }

    @Override
    public void onClick(View view, ResultsBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString("title", bean.getDesc());
        bundle.putString("url", bean.getUrl());
        bundle.putString("type", Constants.IOS);
        bundle.putString("author", bean.getWho());
        WebActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void refillDate(List<ResultsBean> list) {
        mRecyclerAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<ResultsBean> list) {
        mRecyclerAdapter.updateItems(list);
    }

    @Override
    public void hasNoMoreDate() {
        super.hasNoMoreDate();
        isLoadMore = false;
        Snackbar.make(mRecyclerView, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        mPage = mPage + 1;
    }

    @Override
    public void onError(Throwable e) {
        KLog.e(e);
        super.onError(e);
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_server_error, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDownRefresh();
                    }
                }).show();
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

    @Override
    public void clear() {
        super.clear();
        mRecyclerAdapter.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
