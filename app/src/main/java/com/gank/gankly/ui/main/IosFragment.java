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
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.impl.IosGoodsPresenterImpl;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.WebActivity;
import com.gank.gankly.view.IIosView;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-4-26
 */
public class IosFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerOnClick, IIosView<List<ResultsBean>> {
    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.meizi_multiple_status_view)
    MultipleStatusView mMultipleStatusView;

    private MainActivity mActivity;
    private GankAdapter mRecyclerAdapter;
    private IBaseRefreshPresenter mPresenter;

    private int mLastPosition;

    public IosFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IosGoodsPresenterImpl(mActivity, this);
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
        mRecyclerAdapter = new GankAdapter(mActivity,GankAdapter.LAYOUT_IOS);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }

    @Override
    protected void bindLister() {
        mMultipleStatusView.setListener(new MultipleStatusView.OnMultipleClick() {
            @Override
            public void retry(View v) {
                initFetchDate();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastPosition + 1 == mRecyclerAdapter.getItemCount()
                        && !mSwipeRefreshLayout.isRefreshing()) {
                    mPresenter.fetchMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                mLastPosition = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meizi;
    }

    @Override
    protected void initDate() {
        initFetchDate();
    }

    private void initFetchDate() {
        mPresenter.fetchNew();
    }

    public static IosFragment newInstance() {
        IosFragment fragment = new IosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        showRefresh();
        initFetchDate();
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
        mRecyclerAdapter.appendMoreDate(list);
    }

    @Override
    public void showRefreshError(String errorStr) {
        Snackbar.make(mSwipeRefreshLayout, errorStr, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.fetchMore();
                    }
                }).show();
    }

    @Override
    public void hasNoMoreDate() {
        super.hasNoMoreDate();
        Snackbar.make(mRecyclerView, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        super.showError();
        mMultipleStatusView.showError();
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showContent() {
        super.showContent();
        mMultipleStatusView.showContent();
    }

    @Override
    public void showDisNetWork() {
        super.showDisNetWork();
        mMultipleStatusView.showNoNetwork();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
