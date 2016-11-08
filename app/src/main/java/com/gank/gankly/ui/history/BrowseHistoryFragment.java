package com.gank.gankly.ui.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.data.entity.ReadHistory;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.mvp.base.FetchFragment;
import com.gank.gankly.mvp.source.LocalDataSource;
import com.gank.gankly.ui.base.LySwipeRefreshLayout;
import com.gank.gankly.ui.more.MoreActivity;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.widget.LyRecyclerView;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.NoAlphaItemAnimator;

import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-10-31
 * Email:137387869@qq.com
 */

public class BrowseHistoryFragment extends FetchFragment implements BrowseHistoryContract.View, ItemClick {
    private BrowseHistoryContract.Presenter mPresenter;
    private BrowseHistoryAdapter mAdapter;

    @BindView(R.id.browse_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.loading_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.meizi_swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private MoreActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MoreActivity) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browse;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new BrowseHistoryPresenter(LocalDataSource.getInstance(), this);
    }

    @Override
    protected void initValues() {
        mPresenter.fetchNew();
        mAdapter = new BrowseHistoryAdapter();
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.mine_browse);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar barLayout = mActivity.getSupportActionBar();
        if (barLayout != null) {
            barLayout.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });

        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mSwipeRefreshLayout.setAdapter(mAdapter);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.getRecyclerView().setItemAnimator(new NoAlphaItemAnimator());
        ((SimpleItemAnimator)mSwipeRefreshLayout.getRecyclerView().getItemAnimator()).setSupportsChangeAnimations(false);
        mSwipeRefreshLayout.setILyRecycler(new LyRecyclerView.ILyRecycler() {
            @Override
            public void removeRecycle(int pos) {
                mAdapter.removeRecycle(pos);
                if (mAdapter.getItemCount() == 0) {
                    showEmpty();
                }
            }

            @Override
            public void onClick(int pos) {
                ReadHistory readHistory = mAdapter.getReadHistory(pos);
                openWebActivity(readHistory);
            }
        });
    }

    @Override
    protected void bindLister() {
        mAdapter.setOnItemClick(this);
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
    public void refillData(List<ReadHistory> history) {
        mAdapter.updateList(history);
    }

    @Override
    public void appendData(List<ReadHistory> history) {
        mAdapter.appendList(history);
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
        Snackbar.make(mCoordinatorLayout, R.string.loading_no_more, Snackbar.LENGTH_LONG).show();
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
        mMultipleStatusView.showNoNetwork();
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

    }

    @Override
    public void onClick(int position, Object object) {
        openWebActivity((ReadHistory) object);
    }

    private void openWebActivity(ReadHistory readHistory) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, readHistory.getComment());
        bundle.putString(WebActivity.URL, readHistory.getUrl());
        bundle.putString(WebActivity.TYPE, readHistory.getG_type());
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }
}
