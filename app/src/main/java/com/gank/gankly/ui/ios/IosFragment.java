package com.gank.gankly.ui.ios;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.utils.theme.RecyclerViewColor;
import com.gank.gankly.utils.theme.ThemeColor;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.leftcoding.http.bean.ResultsBean;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;

/**
 * ios
 * Create by LingYan on 2016-4-26
 */
public class IosFragment extends LazyFragment implements IosContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private MainActivity mActivity;
    private IosAdapter mAdapter;
    private IosContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;

    private final AtomicBoolean first = new AtomicBoolean(true);

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
        mAdapter = new IosAdapter(this);

        mSwipeRefreshLayout.setAdapter(mAdapter);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnScrollListener(mSwipeRefRecyclerViewListener);

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mRecyclerView.setHasFixedSize(true);

        mAdapter.setOnItemClickListener(mRecyclerOnClick);
        mMultipleStatusView.setListener(v -> initRefreshIos());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new IosPresenter(getContext(), this);
    }

    @Override
    protected void initLazy() {
        initRefreshIos();
    }

    private LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener mSwipeRefRecyclerViewListener = new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
        @Override
        public void onRefresh() {
            initRefreshIos();
        }

        @Override
        public void onLoadMore() {
            loadMoreIos();
        }
    };

    private RecyclerOnClick mRecyclerOnClick = new RecyclerOnClick() {

        @Override
        public void onClick(View view, int position, ResultsBean bean) {
            Bundle bundle = new Bundle();
            bundle.putString(WebActivity.TITLE, bean.desc);
            bundle.putString(WebActivity.URL, bean.url);
            bundle.putString(WebActivity.AUTHOR, bean.who);
            bundle.putString(WebActivity.TYPE, Constants.IOS);
            WebActivity.startWebActivity(mActivity, bundle);
        }
    };

    @Override
    public void showError() {
        mMultipleStatusView.showError();
    }

    @Override
    public void showEmpty() {
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showProgress() {
        if (first.get()) {
            if (mMultipleStatusView != null) {
                mMultipleStatusView.showLoading();
            }
        }

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void hasNoMoreDate() {
        showSnackBar(mContext.getString(R.string.loading_all_over));
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
    public void refreshIosSuccess(List<ResultsBean> list) {
        if (mAdapter != null) {
            mAdapter.fillItems(list);
        }
    }

    @Override
    public void refreshIosFailure(String msg) {
        showSnackBar(msg);
    }

    private void showSnackBar(String msg) {
        if (mSwipeRefreshLayout != null) {
            Snackbar.make(mSwipeRefreshLayout, msg, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, v -> loadMoreIos());
        }
    }

    @Override
    public void appendIosSuccess(List<ResultsBean> list) {
        if (mAdapter != null) {
            mAdapter.appendItems(list);
        }
    }

    @Override
    public void appendIosFailure(String msg) {
        showSnackBar(msg);
    }

    public static IosFragment newInstance() {
        IosFragment fragment = new IosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initRefreshIos() {
        if (mPresenter != null) {
            mPresenter.refreshIos();
        }
    }

    private void loadMoreIos() {
        if (mPresenter != null) {
            mPresenter.appendIos();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    protected void callBackRefreshUi() {
        ThemeColor themeColor = new ThemeColor(this);
        RecyclerViewColor mRecycler = new RecyclerViewColor(mRecyclerView);
        mRecycler.setItemColor(R.id.title, R.attr.baseAdapterItemTextColor);
        mRecycler.setItemColor(R.id.time, R.attr.textSecondaryColor);
//        mRecycler.setItemBackgroundColor(R.id.ll, R.attr.lyItemSelectBackground);

        themeColor.setBackgroundResource(R.attr.themeBackground, mRecyclerView);
        themeColor.swipeRefresh(mSwipeRefreshLayout);
        themeColor.recyclerViewColor(mRecycler);
        themeColor.start();
    }
}
