package com.gank.gankly.ui.android;

import android.content.Context;
import android.content.Intent;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageConfig;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.ui.MainActivity;
import com.gank.gankly.ui.base.LazyFragment;
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

    private AtomicBoolean isFirst = new AtomicBoolean(true);
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
        initRecycler();

        androidAdapter.setOnItemClickListener(itemCallBack);
        multipleStatusView.setListener(onMultipleClick);
    }

    @Override
    public void onLazyActivityCreate() {
        androidPresenter = new AndroidPresenter(context, this);
        loadAndroid(PageConfig.starPage());
    }

    private void initRecycler() {
        androidAdapter = new AndroidAdapter(context);
        swipeRefreshLayout.setAdapter(androidAdapter);
        swipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnScrollListener(onRefreshListener);
    }

    private final AndroidAdapter.ItemCallback itemCallBack = new AndroidAdapter.ItemCallback() {
        @Override
        public void onItemClick(View view, Gank gank) {
            Bundle bundle = new Bundle();
            bundle.putString(WebActivity.TITLE, gank.desc);
            bundle.putString(WebActivity.URL, gank.url);
            bundle.putString(WebActivity.TYPE, Constants.ANDROID);
            bundle.putString(WebActivity.AUTHOR, gank.who);
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtras(bundle);
            CircularAnimUtils.startActivity((MainActivity) context, intent, view, R.color.white_half);
        }
    };

    private final LySwipeRefreshLayout.OnSwipeRefreshListener onRefreshListener = new LySwipeRefreshLayout.OnSwipeRefreshListener() {
        @Override
        public void onRefresh() {
            loadAndroid(PageConfig.starPage());
        }

        @Override
        public void onLoadMore() {
            if (pageConfig != null) {
                loadAndroid(pageConfig.getNextPage());
            }
        }
    };

    private final MultipleStatusView.OnMultipleClick onMultipleClick = v -> {
        showLoading();
        loadAndroid(PageConfig.starPage());
    };

    private void loadAndroid(int page) {
        androidPresenter.loadAndroid(page);
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

        if (androidAdapter != null) {
            androidAdapter.fillItems(list);
            androidAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshFailure(String msg) {
        shortToast(msg);
    }

    @Override
    public void appendSuccess(List<Gank> list) {
        if (androidAdapter != null) {
            androidAdapter.appendItems(list);
            androidAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void appendFailure(String msg) {
        shortToast(msg);
    }

    @Override
    public void showProgress() {
        if (isFirst.get()) {
            if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
                showLoading();
                return;
            }
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
        }

        if (androidPresenter != null) {
            androidPresenter.destroy();
        }
    }

    @Override
    public void shortToast(String string) {

    }
}
