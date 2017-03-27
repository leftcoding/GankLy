package com.gank.gankly.ui.main.ios;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.main.android.AndroidAdapter;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.utils.theme.RecyclerViewColor;
import com.gank.gankly.utils.theme.ThemeColor;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;

/**
 * ios
 * Create by LingYan on 2016-4-26
 * Email:137387869@qq.com
 */
public class IosFragment extends LazyFragment implements RecyclerOnClick, IosContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private MainActivity mActivity;
    private AndroidAdapter mRecyclerAdapter;
    private IosContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    public static IosFragment newInstance() {
        IosFragment fragment = new IosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IosPresenter(GankDataSource.getInstance(), this);
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initViews() {
        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mRecyclerAdapter = new AndroidAdapter(mActivity, IosAdapter.LAYOUT_IOS);
        mSwipeRefreshLayout.setAdapter(mRecyclerAdapter);

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    protected void bindListener() {
        mMultipleStatusView.setListener(v -> initFetchDate());
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
    protected void initData() {
        showLoading();
        initFetchDate();
    }

    private void initFetchDate() {
        mPresenter.fetchNew();
    }

    @Override
    public void showLoading() {
        mMultipleStatusView.showLoading();
    }

    @Override
    public void showRefreshError(String errorStr) {
        Snackbar.make(mSwipeRefreshLayout, errorStr, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, v -> mPresenter.fetchMore());
    }

    @Override
    public void showError() {
        mMultipleStatusView.showError();
    }

    @Override
    public void showEmpty() {
        mMultipleStatusView.showEmpty();
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
    public void onClick(View view, ResultsBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, bean.getDesc());
        bundle.putString(WebActivity.URL, bean.getUrl());
        bundle.putString(WebActivity.TYPE, Constants.IOS);
        bundle.putString(WebActivity.AUTHOR, bean.getWho());
        WebActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void callBackRefreshUi() {
        ThemeColor themeColor = new ThemeColor(this);
        RecyclerViewColor mRecycler = new RecyclerViewColor(mRecyclerView);
        mRecycler.textViewColor(R.id.goods_txt_title, R.attr.baseAdapterItemTextColor);
        mRecycler.textViewColor(R.id.goods_txt_time, R.attr.textSecondaryColor);
        mRecycler.backGroundColor(R.id.ios_ll, R.attr.lyItemSelectBackground);

        themeColor.backgroundResource(R.attr.themeBackground, mRecyclerView);
        themeColor.swipeRefresh(mSwipeRefreshLayout);
        themeColor.recyclerViewColor(mRecycler);
        themeColor.start();
    }

    @Override
    public void refillDate(List<ResultsBean> list) {
        mRecyclerAdapter.refillItems(list);
    }

    @Override
    public void appendData(List<ResultsBean> list) {
        mRecyclerAdapter.appendItems(list);
    }
}
