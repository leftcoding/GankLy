package com.gank.gankly.ui.collect;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.bean.RxCollect;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.mvp.source.LocalDataSource;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.ui.base.fragment.SupportFragment;
import com.gank.gankly.ui.more.MoreActivity;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.widget.LyRecyclerView;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * 收藏
 * Create by LingYan on 2016-4-25
 */
public class CollectFragment extends SupportFragment implements CollectContract.View {
    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private MoreActivity mActivity;
    private CollectContract.Presenter mPresenter;
    private CollectAdapter mCollectAdapter;
    private Disposable mDisposable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collcet;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MoreActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.setTitle(R.string.mine_my_collect);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(v -> mActivity.finish());

        mDisposable = RxBus_.getInstance().toObservable(RxCollect.class)
                .subscribe(rxCollect -> {
                    if (rxCollect.isCollect()) {
                        onDelete();
                    }
                });

        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefreshListener() {
            @Override
            public void onRefresh() {
                showProgress();
                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
                mPresenter.fetchMore();
            }
        });

        initAdapter();
        setRecyclerView();
        initRefresh();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new CollectPresenter(LocalDataSource.getInstance(), this);
    }

    private void setRecyclerView() {
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setAdapter(mCollectAdapter);
        mSwipeRefreshLayout.setILyRecycler(new LyRecyclerView.ILyRecycler() {
            @Override
            public void removeRecycle(final int position) {
                final UrlCollect urlCollect = mCollectAdapter.getUrlCollect(position);
                long id = urlCollect.getId();
                mPresenter.cancelCollect(id);
                mCollectAdapter.deleteItem(position);
                Snackbar.make(mCoordinatorLayout, R.string.collect_revoke, Snackbar.LENGTH_LONG)
                        .setAction(R.string.revoke, v -> {
//                                mCollectAdapter.backAdapter();
//                                mPresenter.backCollect();
//                                if (mCollectAdapter.getItemCount() > 0) {
//                                    showContent();
//                                }
                            mPresenter.insertCollect(urlCollect);
                        })
                        .show();
                if (mCollectAdapter.getItemCount() == 0) {
                    showEmpty();
                }
            }

            @Override
            public void onClick(int position) {
                openWebActivity(position);
            }
        });

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, R.drawable.shape_item_divider));
//        mRecyclerView.setItemAnimator(new FadeInUpAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.getItemAnimator().setAddDuration(150);
        mRecyclerView.getItemAnimator().setRemoveDuration(150);
    }

    private void initAdapter() {
        mCollectAdapter = new CollectAdapter(mActivity);
    }

    private void initRefresh() {
        mMultipleStatusView.showLoading();
        mPresenter.fetchNew();
    }

    private void openWebActivity(int position) {
        UrlCollect urlCollect = mCollectAdapter.getUrlCollect(position);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, urlCollect.getComment());
        bundle.putString(WebActivity.URL, urlCollect.getUrl());
        bundle.putInt(WebActivity.FROM_TYPE, WebActivity.FROM_COLLECT);
        WebActivity.startWebActivity(mActivity, bundle);
    }


    @Override
    public void setAdapterList(List<UrlCollect> list) {
        mCollectAdapter.updateItems(list);
    }

    @Override
    public void appendAdapter(List<UrlCollect> list) {
        mCollectAdapter.addItems(list);
    }

    @Override
    public void onDelete() {
        mCollectAdapter.deleteItem(mSwipeRefreshLayout.getPosition());
        if (mCollectAdapter.getItemCount() == 0) {
            showEmpty();
        }
    }

    @Override
    public int getItemsCount() {
        return mCollectAdapter.getItemCount();
    }

    @Override
    public void revokeCollect() {
        mCollectAdapter.backAdapter();
        if (mCollectAdapter.getItemCount() > 0) {
            showContent();
        }
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEmpty() {
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showDisNetWork() {
        mMultipleStatusView.showDisNetwork();
    }

    @Override
    public void showError() {
        mMultipleStatusView.showError();
    }

    private void showLoading() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showLoading();
        }
    }

    @Override
    public void showContent() {
        mMultipleStatusView.showContent();
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
