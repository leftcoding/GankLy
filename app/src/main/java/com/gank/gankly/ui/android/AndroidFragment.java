package com.gank.gankly.ui.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.utils.CircularAnimUtils;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.utils.theme.RecyclerViewColor;
import com.gank.gankly.utils.theme.ThemeColor;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.leftcoding.http.bean.ResultsBean;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-4-26
 */
public class AndroidFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerOnClick, AndroidContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;

    private MainActivity mActivity;
    private AndroidAdapter mAdapter;
    private AndroidContract.Presenter mPresenter;

    private AtomicBoolean firstLoad = new AtomicBoolean(true);

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAdapter();
        initRecycler();

        mAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mMultipleStatusView.setListener(v -> {
            showLoading();
            mPresenter.refreshAndroid();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new AndroidPresenter(getContext(), this);
        mPresenter.refreshAndroid();
    }

    @Override
    protected void initLazy() {
    }

    private void initRecycler() {
        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshAndroid();
            }

            @Override
            public void onLoadMore() {
                mPresenter.appendAndroid();
            }
        });
    }

    private void initAdapter() {
        mAdapter = new AndroidAdapter(mActivity);
        mSwipeRefreshLayout.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.refreshAndroid();
    }

    @Override
    public void onClick(View view, ResultsBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, bean.desc);
        bundle.putString(WebActivity.URL, bean.url);
        bundle.putString(WebActivity.TYPE, Constants.ANDROID);
        bundle.putString(WebActivity.AUTHOR, bean.who);
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.putExtras(bundle);
        CircularAnimUtils.startActivity(mActivity, intent, view, R.color.white_half);
    }

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
        if (firstLoad.get()) {
            showLoading();
            return;
        }

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if (firstLoad.compareAndSet(true, false)) {
            showContent();
            return;
        }

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mSwipeRefreshLayout, R.string.loading_all_over, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showContent() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showContent();
        }
    }

    @Override
    public void showEmpty() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showEmpty();
        }
    }

    @Override
    public void showDisNetWork() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showDisNetwork();
        }
    }

    @Override
    public void showError() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showError();
        }
    }

    @Override
    public void showLoading() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showLoading();
        }
    }

    @Override
    public void showShortToast(String str) {
        str = TextUtils.isEmpty(str) ? getContext().getString(R.string.loading_error) : str;
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

    private ColorStateList getSwitchThumbColorStateList() {
        int mSelectColor;
        int unSelectColor;

        if (App.isNight()) {
            mSelectColor = R.color.switch_thumb_disabled_dark;
            unSelectColor = R.color.navigation_item_icon;
        } else {
            mSelectColor = R.color.colorAccent;
            unSelectColor = R.color.gray;
        }

        final int[][] states = new int[3][];
        final int[] colors = new int[3];

        // Disabled state
        states[0] = new int[]{-android.R.attr.state_enabled};
        colors[0] = (Color.DKGRAY);

        // Checked state
        states[1] = new int[]{android.R.attr.state_checked};

        colors[1] = App.getAppColor(mSelectColor);

        // Unchecked enabled state state
        states[2] = new int[0];

        colors[2] = App.getAppColor(unSelectColor);

        return new ColorStateList(states, colors);
    }

    protected void callBackRefreshUi() {
        ThemeColor themeColor = new ThemeColor(this);
        RecyclerViewColor mRecycler = new RecyclerViewColor(mRecyclerView);
        mRecycler.setItemColor(R.id.title, R.attr.baseAdapterItemTextColor);
        mRecycler.setItemColor(R.id.time, R.attr.textSecondaryColor);
        mRecycler.setItemBackgroundColor(R.id.android_rl, R.attr.lyItemSelectBackground);

        themeColor.setBackgroundResource(R.attr.themeBackground, mRecyclerView);
        themeColor.swipeRefresh(mSwipeRefreshLayout);
        themeColor.recyclerViewColor(mRecycler);
        themeColor.start();
    }
}
