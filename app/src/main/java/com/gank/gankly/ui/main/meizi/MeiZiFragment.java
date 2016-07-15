package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.impl.MeiziPresenterImpl;
import com.gank.gankly.ui.base.BaseSwipeRefreshLayout;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.browse.BrowseActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.view.IMeiziView;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-5-12
 */
public class MeiZiFragment extends LazyFragment implements MeiziOnClick, SwipeRefreshLayout.OnRefreshListener,
        IMeiziView<List<ResultsBean>> {
    @Bind(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @Bind(R.id.swipe_refresh)
    BaseSwipeRefreshLayout mSwipeRefreshLayout;

    private MeiZiRecyclerAdapter mRecyclerAdapter;
    private MainActivity mActivity;

    private IBaseRefreshPresenter mPresenter;
    private int mPage = 1;

    public MeiZiFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
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
        mRecyclerAdapter = new MeiZiRecyclerAdapter(mActivity);
        mRecyclerAdapter.setMeiZiOnClick(this);
        initRecycler();
    }

    private void initRecycler() {
        mSwipeRefreshLayout.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mSwipeRefreshLayout.getRecyclerView().setHasFixedSize(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setAdapter(mRecyclerAdapter);
    }

    @Override
    protected void bindLister() {
        mMultipleStatusView.setListener(new MultipleStatusView.OnMultipleClick() {
            @Override
            public void retry(View v) {
                mMultipleStatusView.showLoading();
                mPresenter.fetchNew(mPage);
            }
        });
        mSwipeRefreshLayout.setOnScrollListener(new BaseSwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchNew(mPage);
            }

            @Override
            public void onLoadMore() {
                onNextRefresh();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_swiperefresh_multiple_status;
    }

    @Override
    protected void initDate() {
        onLoadingRefresh();
    }

    private void onLoadingRefresh() {
        mMultipleStatusView.showLoading();
        mPresenter.fetchNew(mPage);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MeiziPresenterImpl(mActivity, this);
    }

    private void onNextRefresh() {
        mPresenter.fetchMore(mPage);
    }

    public static MeiZiFragment newInstance() {
        MeiZiFragment fragment = new MeiZiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.fetchNew(mPage);
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        Intent intent = new Intent(mActivity, BrowseActivity.class);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void refillDate(List<ResultsBean> list) {
        mRecyclerAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<ResultsBean> list) {
        mRecyclerAdapter.addItems(list);
    }

    @Override
    public void setNextPage(int page) {
        mPage = page;
    }

    @Override
    public void hasNoMoreDate() {
        super.hasNoMoreDate();
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .show();
    }

    @Override
    public void showRefreshError(String errorStr) {
        Snackbar.make(mSwipeRefreshLayout, errorStr, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.fetchNew(mPage);
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
    public void showEmpty() {
        super.showEmpty();
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showError() {
        super.showError();
        mMultipleStatusView.showError();
    }

    @Override
    public void clear() {
        super.clear();
        mRecyclerAdapter.clear();
    }
}
