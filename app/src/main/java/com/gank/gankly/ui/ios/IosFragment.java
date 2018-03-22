package com.gank.gankly.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;

/**
 * ios
 * Create by LingYan on 2016-4-26
 */
public class IosFragment extends LazyFragment implements IosContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefresh;

    private IosAdapter iosAdapter;
    private IosContract.Presenter iosPresenter;

    private final AtomicBoolean isFirst = new AtomicBoolean(true);

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iosAdapter = new IosAdapter(getBaseContext());

        swipeRefresh.setAdapter(iosAdapter);
        swipeRefresh.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        swipeRefresh.setOnScrollListener(onSwipeRefreshListener);

        RecyclerView recyclerView = swipeRefresh.getRecyclerView();
        recyclerView.setHasFixedSize(true);

        iosAdapter.setOnItemClickListener(mItemCallBack);
        multipleStatusView.setListener(v -> refreshIos());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iosPresenter = new IosPresenter(getBaseContext(), this);
    }

    @Override
    protected void initLazy() {
        refreshIos();
    }

    private LySwipeRefreshLayout.OnSwipeRefreshListener onSwipeRefreshListener = new LySwipeRefreshLayout.OnSwipeRefreshListener() {
        @Override
        public void onRefresh() {
            refreshIos();
        }

        @Override
        public void onLoadMore() {
            loadMoreIos();
        }
    };

    private final IosAdapter.ItemCallback mItemCallBack = (view, gank) -> {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, gank.desc);
        bundle.putString(WebActivity.URL, gank.url);
        bundle.putString(WebActivity.AUTHOR, gank.who);
        bundle.putString(WebActivity.TYPE, Constants.IOS);
        WebActivity.startWebActivity(getBaseContext(), bundle);
    };

    @Override
    public void showError() {
        if (multipleStatusView != null) {
            multipleStatusView.showError();
        }
    }

    @Override
    public void showEmpty() {
        if (multipleStatusView != null) {
            multipleStatusView.showEmpty();
        }
    }

    @Override
    public void showProgress() {
        if (isFirst.get()) {
            if (multipleStatusView != null) {
                if (swipeRefresh != null && !swipeRefresh.isRefreshing()) {
                    multipleStatusView.showLoading();
                }
            }
        }

        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }

        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void hasNoMoreDate() {
        shortToast(getBaseContext().getString(R.string.loading_all_over));
    }

    @Override
    public void showContent() {
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
    }

    @Override
    public void showDisNetWork() {
        if (multipleStatusView != null) {
            multipleStatusView.showDisNetwork();
        }
    }

    public static IosFragment newInstance() {
        IosFragment fragment = new IosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void refreshIos() {
        if (iosPresenter != null) {
            iosPresenter.refreshIos();
        }
    }

    private void loadMoreIos() {
        if (iosPresenter != null) {
            iosPresenter.appendIos();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (iosPresenter != null) {
            iosPresenter.unSubscribe();
        }
    }

    @Override
    public void refreshSuccess(List<Gank> list) {
        showContent();

        if (isFirst.compareAndSet(true, false)) {
            if (list == null || list.isEmpty()) {
                showEmpty();
                return;
            }
        }

        if (iosAdapter != null) {
            iosAdapter.fillItems(list);
            iosAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshFailure(String msg) {
        shortToast(msg);
    }

    @Override
    public void appendSuccess(List<Gank> list) {
        if (iosAdapter != null) {
            iosAdapter.appendItems(list);
            iosAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void appendFailure(String msg) {
        shortToast(msg);
    }
}
