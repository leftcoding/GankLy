package com.gank.gankly.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
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

    private final AtomicBoolean first = new AtomicBoolean(true);

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

    private final ItemCallBack mItemCallBack = (view, position, object) -> {
        if (object instanceof Gank) {
            Gank bean = (Gank) object;
            Bundle bundle = new Bundle();
            bundle.putString(WebActivity.TITLE, bean.desc);
            bundle.putString(WebActivity.URL, bean.url);
            bundle.putString(WebActivity.AUTHOR, bean.who);
            bundle.putString(WebActivity.TYPE, Constants.IOS);
            WebActivity.startWebActivity(getBaseContext(), bundle);
        }
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
        if (first.get()) {
            if (multipleStatusView != null) {
                multipleStatusView.showLoading();
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
        showSnackBar(getBaseContext().getString(R.string.loading_all_over));
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

    @Override
    public void refreshIosSuccess(List<Gank> list) {
        if (iosAdapter != null) {
            iosAdapter.fillItems(list);
        }
    }

    @Override
    public void refreshIosFailure(String msg) {
        showSnackBar(msg);
    }

    private void showSnackBar(String msg) {
        if (swipeRefresh != null) {
            Snackbar.make(swipeRefresh, msg, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, v -> loadMoreIos());
        }
    }

    @Override
    public void appendIosSuccess(List<Gank> list) {
        if (iosAdapter != null) {
            iosAdapter.appendItems(list);
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

//    protected void callBackRefreshUi() {
//        ThemeColor themeColor = new ThemeColor(this);
//        RecyclerViewColor mRecycler = new RecyclerViewColor(recyclerView);
//        mRecycler.setItemColor(R.id.title, R.attr.baseAdapterItemTextColor);
//        mRecycler.setItemColor(R.id.time, R.attr.textSecondaryColor);
//
//        themeColor.setBackgroundResource(R.attr.themeBackground, recyclerView);
//        themeColor.swipeRefresh(swipeRefresh);
//        themeColor.recyclerViewColor(mRecycler);
//        themeColor.start();
//    }
}
