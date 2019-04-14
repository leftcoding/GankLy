package com.gank.gankly.ui.welfare;

import android.content.Intent;
import android.ly.business.domain.Gank;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.config.MeiziArrayList;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.base.adapter.ViewModelAdapter;
import com.gank.gankly.ui.gallery.GalleryActivity;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.recyclerview.OnFlexibleScrollListener;

import java.util.List;

import butterknife.BindView;

/**
 * 干货 - 妹子
 * Create by LingYan on 2016-5-12
 */
public class WelfareFragment extends LazyFragment implements WelfareContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private WelfareViewManager itemManager;
    private WelfareContract.Presenter presenter;

    private int curPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_welfare;
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
        ViewModelAdapter welfareAdapter = new ViewModelAdapter();
        itemManager = new WelfareViewManager();
        itemManager.setClickListener(itemClickListener);
        welfareAdapter.setViewManager(itemManager);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        OnFlexibleScrollListener scrollListener = new OnFlexibleScrollListener();
        scrollListener.setOnScrollListener(onRecyclerViewListener);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setAdapter(welfareAdapter);

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        multipleStatusView.setListener(onMultipleClick);
    }

    @Override
    public void onLazyActivityCreate() {
        presenter = new WelfarePresenter(getContext(), this);
        presenter.loadWelfare(curPage);
    }

    private final OnFlexibleScrollListener.OnRecyclerViewListener onRecyclerViewListener = new OnFlexibleScrollListener.OnRecyclerViewListener() {
        @Override
        public void onLoadMore() {
            loadWelfare();
        }
    };

    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            curPage = 1;
            loadWelfare();
        }
    };

    private final MultipleStatusView.OnMultipleClick onMultipleClick = new MultipleStatusView.OnMultipleClick() {
        @Override
        public void retry(View v) {
            multipleStatusView.showLoading();
        }
    };

    private final WelfareViewManager.ItemClickListener itemClickListener = new WelfareViewManager.ItemClickListener() {
        @Override
        public void onItem(View view, int position) {
            Intent intent = new Intent(getContext(), GalleryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(GalleryActivity.EXTRA_POSITION, position);
            bundle.putInt(GalleryActivity.TYPE, GalleryActivity.TYPE_INDEX);
            intent.putExtras(bundle);
//            startActivity(intent);

            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.transition_welfare_image));
            startActivity(intent, activityOptionsCompat.toBundle());
        }
    };

    private void loadWelfare() {
        if (presenter != null) {
            presenter.loadWelfare(curPage);
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showProgress() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    public void showContent() {
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
    }

    public void showEmpty() {
        if (multipleStatusView != null) {
            multipleStatusView.showEmpty();
        }
    }

    @Override
    public void loadWelfareSuccess(int page, List<Gank> list) {
        if (itemManager != null) {
            if (page == 1 && ListUtils.isEmpty(list)) {
                showEmpty();
                return;
            }

            showContent();
            curPage = page + 1;
            itemManager.setData(page, list);
            itemManager.notifyDataSetChanged();
            MeiziArrayList.getInstance().addImages(list, page);
        }
    }

    @Override
    public void loadWelfareFailure(String msg) {
        shortToast(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.destroy();
        }
    }
}
