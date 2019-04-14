package com.gank.gankly.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.recyclerview.OnFlexibleScrollListener;

import java.util.List;

import butterknife.BindView;

/**
 * ios
 * Create by LingYan on 2016-4-26
 */
public class IosFragment extends LazyFragment implements IosContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private IosAdapter iosAdapter;
    private IosContract.Presenter iosPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_welfare;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iosAdapter = new IosAdapter(getContext());
        iosAdapter.setOnItemClickListener(onItemClickListener);

        OnFlexibleScrollListener scrollListener = new OnFlexibleScrollListener();
        scrollListener.setOnScrollListener(onRecyclerViewListener);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(iosAdapter);

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        multipleStatusView.setListener(v -> refreshIos());
    }

    @Override
    public void onLazyActivityCreate() {
        iosPresenter = new IosPresenter(getContext(), this);
        refreshIos();
    }

    private final OnFlexibleScrollListener.OnRecyclerViewListener onRecyclerViewListener = new OnFlexibleScrollListener.OnRecyclerViewListener() {
        @Override
        public void onLoadMore() {
            loadMoreIos();
        }
    };

    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshIos();
        }
    };

    private final ItemCallback onItemClickListener = (view, gank) -> {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, gank.desc);
        bundle.putString(WebActivity.URL, gank.url);
        bundle.putString(WebActivity.AUTHOR, gank.who);
        bundle.putString(WebActivity.TYPE, Constants.IOS);
        WebActivity.startWebActivity(getContext(), bundle);
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
        shortToast(getString(R.string.loading_all_over));
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
            iosPresenter.destroy();
        }
        if (iosAdapter != null) {
            iosAdapter.destroy();
        }
    }

    @Override
    public void refreshSuccess(List<Gank> list) {
        showContent();

        if (list == null || list.isEmpty()) {
            showEmpty();
            return;
        }

        if (iosAdapter != null) {
            iosAdapter.clearItems();
            iosAdapter.setItems(list);
            iosAdapter.update();
        }
    }

    @Override
    public void refreshFailure(String msg) {
        shortToast(msg);
    }

    @Override
    public void appendSuccess(List<Gank> list) {
        if (iosAdapter != null) {
            iosAdapter.setItems(list);
            iosAdapter.update();
        }
    }

    @Override
    public void appendFailure(String msg) {
        shortToast(msg);
    }
}
