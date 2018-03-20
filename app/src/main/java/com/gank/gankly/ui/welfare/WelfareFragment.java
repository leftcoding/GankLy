package com.gank.gankly.ui.welfare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gank.gankly.AppConfig;
import com.gank.gankly.R;
import com.gank.gankly.listener.ItemCallBack;
import com.gank.gankly.mvp.source.remote.GankDataSource;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.gallery.GalleryActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.utils.theme.ThemeColor;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.leftcoding.network.domain.ResultsBean;

import java.util.List;

import butterknife.BindView;

/**
 * 干货福利妹子
 * Create by LingYan on 2016-5-12
 */
public class WelfareFragment extends LazyFragment implements WelfareContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private WelfareAdapter mWelfareAdapter;
    private MainActivity mActivity;
    private RecyclerView mRecyclerView;

    private WelfareContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    public static WelfareFragment newInstance() {
        WelfareFragment fragment = new WelfareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWelfareAdapter = new WelfareAdapter(mActivity);
        mWelfareAdapter.setMeiZiOnClick(itemCallBack);
        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
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

        mMultipleStatusView.setListener(v -> {
            mMultipleStatusView.showLoading();
            fetchNew();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new WelfarePresenter(GankDataSource.getInstance(), this);
    }

    @Override
    protected void initLazy() {
        mMultipleStatusView.showLoading();
        fetchNew();
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_LONG)
                .setActionTextColor(AppConfig.getAppColor(R.color.Blue))
                .show();
    }

    private final ItemCallBack itemCallBack = new ItemCallBack() {
        @Override
        public void onClick(View view, int position, Object object) {
            Bundle bundle = new Bundle();
            bundle.putInt(GalleryActivity.EXTRA_POSITION, position);
            Intent intent = new Intent(mActivity, GalleryActivity.class);
            intent.putExtra(GalleryActivity.TYPE, 1);
            intent.putExtras(bundle);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity);
            mActivity.startActivity(intent, activityOptionsCompat.toBundle());
        }
    };

    private void fetchNew() {
        mPresenter.fetchNew();
    }

    private void fetchMore() {
        mPresenter.fetchMore();
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showProgress() {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    protected void callBackRefreshUi() {
        ThemeColor themeColor = new ThemeColor(this);
        int resource = themeColor.getResourceId(R.attr.themeBackground);
        mRecyclerView.setBackgroundResource(resource);

        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    public void refreshData(List<ResultsBean> list) {
        mWelfareAdapter.refillItems(list);
    }

    @Override
    public void appendData(List<ResultsBean> list) {
        mWelfareAdapter.appendItems(list);
    }

    @Override
    public void refershDataFailure(String msg) {
        showSnackbar(msg);
    }

    @Override
    public void appendWelfareFailure(String msg) {
        showSnackbar(msg);
    }

    private void showSnackbar(String msg) {
        if (mSwipeRefreshLayout != null) {
            Snackbar.make(mSwipeRefreshLayout, msg, Snackbar.LENGTH_LONG)
                    .setActionTextColor(AppConfig.getAppColor(R.color.Blue))
                    .setAction(R.string.retry, v -> fetchMore()).show();
        }
    }
}
