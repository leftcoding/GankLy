package com.gank.gankly.ui.main.meizi.welfare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.gallery.GalleryActivity;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.utils.CircularAnimUtils;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.utils.theme.ThemeColor;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;

/**
 * 干货福利妹子
 * Create by LingYan on 2016-5-12
 * Email:137387869@qq.com
 */
public class WelfareFragment extends LazyFragment implements MeiziOnClick, WelfareContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private WelfareAdapter mWelfareAdapter;
    private HomeActivity mActivity;
    private RecyclerView mRecyclerView;

    private WelfareContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    public WelfareFragment() {
    }

    public static WelfareFragment newInstance() {
        WelfareFragment fragment = new WelfareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initViews() {
        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mWelfareAdapter = new WelfareAdapter(mActivity);
        mWelfareAdapter.setMeiZiOnClick(this);
        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                fetchNew();
            }

            @Override
            public void onLoadMore() {
                fetchMore();
            }
        });
        mSwipeRefreshLayout.setAdapter(mWelfareAdapter);
    }

    @Override
    protected void bindListener() {
        mMultipleStatusView.setListener(v -> {
            mMultipleStatusView.showLoading();
            fetchNew();
        });
    }

    @Override
    protected void initData() {
        mMultipleStatusView.showLoading();
        fetchNew();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new WelfarePresenter(GankDataSource.getInstance(), this);
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .show();
    }

    @Override
    public void showRefreshError(String errorStr) {
        Snackbar.make(mSwipeRefreshLayout, errorStr, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .setAction(R.string.retry, v -> fetchMore()).show();
    }

    private void fetchNew() {
        mPresenter.fetchNew();
    }

    private void fetchMore() {
        mPresenter.fetchMore();
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showContent() {
        mMultipleStatusView.showContent();
    }

    @Override
    public void showDisNetWork() {
        mMultipleStatusView.showDisNetwork();
    }

    @Override
    public void showEmpty() {
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showError() {
        mMultipleStatusView.showError();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(GalleryActivity.EXTRA_POSITION, position);
        Intent intent = new Intent(mActivity, GalleryActivity.class);
        intent.putExtra(GalleryActivity.TAG, bundle);
        CircularAnimUtils.startActivity(mActivity, intent, view, R.color.color_2f);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (HomeActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void callBackRefreshUi() {
        ThemeColor themeColor = new ThemeColor(this);
        int resource = themeColor.getResourceId(R.attr.themeBackground);
        mRecyclerView.setBackgroundResource(resource);

        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    public void refillData(List<ResultsBean> list) {
        mWelfareAdapter.refillItems(list);
    }

    @Override
    public void appendData(List<ResultsBean> list) {
        mWelfareAdapter.appendItems(list);
    }
}
