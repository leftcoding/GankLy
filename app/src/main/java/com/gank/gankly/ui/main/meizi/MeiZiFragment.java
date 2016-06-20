package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.browse.BrowseActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.presenter.IosPresenter;
import com.gank.gankly.view.IIosView;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-5-12
 */
public class MeiZiFragment extends LazyFragment<IosPresenter> implements SwipeRefreshLayout.OnRefreshListener, MeiziOnClick, IIosView<ResultsBean> {
    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.meizi_multiple_status_view)
    MultipleStatusView mMultipleStatusView;

    private MeiZiRecyclerAdapter mRecyclerAdapter;
    private MainActivity mActivity;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private IosPresenter mPresenter;

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
        mRecyclerView.setAdapter(mRecyclerAdapter);
        initRecycler();
    }

    private void initRecycler() {
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
    }

    @Override
    protected void bindLister() {
        mMultipleStatusView.setListener(new MultipleStatusView.OnMultipleClick() {
            @Override
            public void retry(View v) {
                mMultipleStatusView.showLoading();
                onDownRefresh();
            }
        });
        mRecyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            MeiZiFragment.this.onScrollStateChanged();
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                }
        );
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meizi;
    }

    @Override
    protected void initDate() {
        onDownRefresh();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IosPresenter(mActivity, this);
    }

    private void onDownRefresh() {
        mPresenter.fetchGirl();
    }

    private void onNextRefresh() {
        mPresenter.fetchNextGirl();
    }

    public static MeiZiFragment newInstance() {
        MeiZiFragment fragment = new MeiZiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void onScrollStateChanged() {
        int[] positions = new int[mStaggeredGridLayoutManager.getSpanCount()];
        mStaggeredGridLayoutManager.findLastVisibleItemPositions(positions);
        for (int position : positions) {
            if (position == mStaggeredGridLayoutManager.getItemCount() - 1
                    && !mSwipeRefreshLayout.isRefreshing()) {
                onNextRefresh();
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

    @Override
    public void refillDate(List<ResultsBean> list) {
        mRecyclerAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<ResultsBean> list) {
        mRecyclerAdapter.addItems(list);
    }

    @Override
    public void hasNoMoreDate() {
        super.hasNoMoreDate();
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .show();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
    }

    @Override
    public void onError(Throwable e, String errorString) {
        super.onError(e, errorString);
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
