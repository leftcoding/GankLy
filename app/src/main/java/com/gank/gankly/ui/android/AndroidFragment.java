package com.gank.gankly.ui.android;

import android.content.Context;
import android.content.Intent;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageConfig;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.ItemCallBack;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.utils.CircularAnimUtils;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

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

    private Context context;
    private AndroidAdapter androidAdapter;
    private AndroidContract.Presenter androidPresenter;

    private AtomicBoolean firstRefresh = new AtomicBoolean(true);
    private PageConfig pageConfig;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageConfig = new PageConfig();

        initRecycler();

        androidAdapter.setOnItemClickListener(itemCallBack);
        multipleStatusView.setListener(onMultipleClick);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        androidPresenter = new AndroidPresenter(getBaseContext(), this);
        initLoadAndroid();
    }

    @Override
    protected void initLazy() {

    }

    private void initRecycler() {
        androidAdapter = new AndroidAdapter(getBaseContext());
        swipeRefreshLayout.setAdapter(androidAdapter);
        swipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnScrollListener(onRefreshListener);
    }

    private final ItemCallBack itemCallBack = (view, position, object) -> {
        if (object instanceof Gank) {
            Gank resultsBean = (Gank) object;
            Bundle bundle = new Bundle();
            bundle.putString(WebActivity.TITLE, resultsBean.desc);
            bundle.putString(WebActivity.URL, resultsBean.url);
            bundle.putString(WebActivity.TYPE, Constants.ANDROID);
            bundle.putString(WebActivity.AUTHOR, resultsBean.who);
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtras(bundle);
            CircularAnimUtils.startActivity((MainActivity) context, intent, view, R.color.white_half);
        }
    };

    private final LySwipeRefreshLayout.OnSwipeRefreshListener onRefreshListener = new LySwipeRefreshLayout.OnSwipeRefreshListener() {
        @Override
        public void onRefresh() {
            initLoadAndroid();
        }

        @Override
        public void onLoadMore() {
            androidPresenter.appendAndroid(pageConfig.curPage);
        }
    };

    private final MultipleStatusView.OnMultipleClick onMultipleClick = v -> {
        showLoading();
        initLoadAndroid();
    };

    private void initLoadAndroid() {
        androidPresenter.refreshAndroid(pageConfig.initPage);
    }

    @Override
    public void refreshAndroidSuccess(List<Gank> list) {
        if (pageConfig != null) {
            pageConfig.curPage = pageConfig.initPage + 1;
        }
        if (androidAdapter != null) {
            androidAdapter.fillItems(list);
            androidAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshAndroidFailure(String msg) {
        showShortToast(msg);
    }

    @Override
    public void appendAndroidSuccess(List<Gank> list) {
        if (pageConfig != null) {
            pageConfig.curPage += 1;
        }
        if (androidAdapter != null) {
            androidAdapter.appendItems(list);
            androidAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void appendAndroidFailure(String msg) {
        showShortToast(msg);
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

    public static AndroidFragment newInstance() {
        AndroidFragment fragment = new AndroidFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (androidAdapter != null) {
            androidAdapter.destroy();
            androidAdapter = null;
        }

        if (androidPresenter != null) {
            androidPresenter.unSubscribe();
            androidPresenter = null;
        }
    }
}
