package com.gank.gankly.ui.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.ItemCallBack;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.utils.CircularAnimUtils;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.utils.theme.RecyclerViewColor;
import com.gank.gankly.utils.theme.ThemeColor;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.leftcoding.network.domain.ResultsBean;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-4-26
 */
public class AndroidFragment extends LazyFragment implements AndroidContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView mRecyclerView;

    private MainActivity mActivity;
    private AndroidAdapter mAdapter;
    private AndroidContract.Presenter mPresenter;

    private AtomicBoolean firstRefresh = new AtomicBoolean(true);

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();

        mAdapter.setOnItemClickListener(itemCallBack);
        multipleStatusView.setListener(onMultipleClick);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new AndroidPresenter(getBaseContext(), this);
        mPresenter.refreshAndroid();
    }

    @Override
    protected void initLazy() {

    }

    private void initRecycler() {
        mAdapter = new AndroidAdapter(getBaseContext());
        swipeRefreshLayout.setAdapter(mAdapter);
        mRecyclerView = swipeRefreshLayout.getRecyclerView();
        swipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnScrollListener(onRefreshListener);
    }

    private final ItemCallBack itemCallBack = (view, position, object) -> {
        if (object instanceof ResultsBean) {
            ResultsBean resultsBean = (ResultsBean) object;
            Bundle bundle = new Bundle();
            bundle.putString(WebActivity.TITLE, resultsBean.desc);
            bundle.putString(WebActivity.URL, resultsBean.url);
            bundle.putString(WebActivity.TYPE, Constants.ANDROID);
            bundle.putString(WebActivity.AUTHOR, resultsBean.who);
            Intent intent = new Intent(mActivity, WebActivity.class);
            intent.putExtras(bundle);
            CircularAnimUtils.startActivity(mActivity, intent, view, R.color.white_half);
        }
    };

    private final LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener onRefreshListener = new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
        @Override
        public void onRefresh() {
            mPresenter.refreshAndroid();
        }

        @Override
        public void onLoadMore() {
            mPresenter.appendAndroid();
        }
    };

    private final MultipleStatusView.OnMultipleClick onMultipleClick = new MultipleStatusView.OnMultipleClick() {
        @Override
        public void retry(View v) {
            showLoading();
            mPresenter.refreshAndroid();
        }
    };

    @Override
    public void refreshAndroidSuccess(List<ResultsBean> list) {
        mAdapter.fillItems(list);
    }

    @Override
    public void refreshAndroidFailure(String msg) {

    }

    @Override
    public void appendAndroidSuccess(List<ResultsBean> list) {
        mAdapter.appendItems(list);
    }

    @Override
    public void appendAndroidFailure(String msg) {

    }

    @Override
    public void showProgress() {
        if (firstRefresh.get()) {
            showLoading();
            return;
        }

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(swipeRefreshLayout, R.string.loading_all_over, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showContent() {
        if (firstRefresh.get()) {
            firstRefresh.set(false);
        }

        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
    }

    @Override
    public void showEmpty() {
        if (multipleStatusView != null) {
            multipleStatusView.showEmpty();
        }
    }

    @Override
    public void showDisNetWork() {
        if (multipleStatusView != null) {
            multipleStatusView.showDisNetwork();
        }
    }

    @Override
    public void showError() {
        if (multipleStatusView != null) {
            multipleStatusView.showError();
        }
    }

    private void showLoading() {
        if (multipleStatusView != null) {
            multipleStatusView.showLoading();
        }
    }

    @Override
    public void showShortToast(String str) {
        ToastUtils.showToast(str);
    }

    public static AndroidFragment newInstance() {
        AndroidFragment fragment = new AndroidFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }

        if (mPresenter != null) {
            mPresenter.unSubscribe();
            mPresenter = null;
        }
    }

    protected void callBackRefreshUi() {
        ThemeColor themeColor = new ThemeColor(this);
        RecyclerViewColor mRecycler = new RecyclerViewColor(mRecyclerView);
        mRecycler.setItemColor(R.id.title, R.attr.baseAdapterItemTextColor);
        mRecycler.setItemColor(R.id.time, R.attr.textSecondaryColor);
        mRecycler.setItemBackgroundColor(R.id.android_rl, R.attr.lyItemSelectBackground);

        themeColor.setBackgroundResource(R.attr.themeBackground, mRecyclerView);
        themeColor.swipeRefresh(swipeRefreshLayout);
        themeColor.recyclerViewColor(mRecycler);
        themeColor.start();
    }
}
