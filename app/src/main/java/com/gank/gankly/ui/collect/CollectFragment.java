package com.gank.gankly.ui.collect;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.config.ViewStatus;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.listener.ItemLongClick;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.presenter.CollectPresenter;
import com.gank.gankly.ui.view.ICollectView;
import com.gank.gankly.ui.web.WebActivity;
import com.gank.gankly.widget.DeleteDialog;
import com.gank.gankly.widget.LoadingLayoutView;
import com.gank.gankly.widget.RecycleViewDivider;

import java.util.List;

import butterknife.Bind;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

/**
 * Create by LingYan on 2016-4-25
 */
public class CollectFragment extends BaseSwipeRefreshFragment<CollectPresenter> implements DeleteDialog.DialogListener,
        OnRefreshListener, ItemLongClick, ICollectView<UrlCollect> {
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.loading_view)
    LoadingLayoutView mLoadingLayoutView;

    private MainActivity mActivity;
    private CollectAdapter mCollectAdapter;
    private UrlCollect mUrlCollect;

    private int mLostPosition;
    private int mPage = 0;
    private int mLongClick;
    private ViewStatus mCurStatus;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onNavigationClick() {
        mCollectAdapter.deleteItem(mLongClick);
        mPresenter.deleteByKey(mUrlCollect.getId());
    }

    public static CollectFragment newInstance() {
        Bundle args = new Bundle();
        CollectFragment fragment = new CollectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initValues() {
        mCurStatus = ViewStatus.LOADING;
        mActivity.setTitle(R.string.navigation_collect);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setHomeAsUpIndicator(R.drawable.ic_home_navigation);
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.openDrawer();
            }
        });
    }

    @Override
    protected void initViews() {
        mCollectAdapter = new CollectAdapter(mActivity);
        mCollectAdapter.setItemLongClick(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, R.drawable.shape_item_divider));
        mRecyclerView.setAdapter(mCollectAdapter);
        mRecyclerView.setBackgroundColor(App.getAppColor(R.color.white));
        mRecyclerView.setItemAnimator(new FadeInLeftAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.getItemAnimator().setAddDuration(500);
        mRecyclerView.getItemAnimator().setRemoveDuration(500);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));

        onRefresh();
    }

    @Override
    protected void bindLister() {
        mLoadingLayoutView.setLoading(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLostPosition + 1 == mCollectAdapter.getItemCount()
                        && !mSwipeRefreshLayout.isRefreshing()) {
                    if (mPresenter.isLoadMore()) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        mPresenter.fetchDate(mPage);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLostPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collcet;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CollectPresenter(mActivity, this);
    }


    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPage = 0;
        mPresenter.fetchDate(mPage);
    }

    @Override
    public void onLongClick(int position, Object object) {
        UrlCollect urlCollect = (UrlCollect) object;
        mLongClick = position;
        mUrlCollect = urlCollect;
        Bundle bundle = new Bundle();
        bundle.putString("content", urlCollect.getComment());
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setListener(this);
        deleteDialog.setArguments(bundle);
        deleteDialog.show(mActivity.getSupportFragmentManager(), "delete");
    }

    @Override
    public void onClick(int position, Object object) {
        UrlCollect urlCollect = (UrlCollect) object;
        Bundle bundle = new Bundle();
        bundle.putString("title", urlCollect.getComment());
        bundle.putString("url", urlCollect.getUrl());
        WebActivity.startWebActivity(mActivity, bundle);
    }


    @Override
    public void refillDate(List<UrlCollect> list) {
        mCollectAdapter.clear();
        mCollectAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<UrlCollect> list) {
        mCollectAdapter.addItems(list);
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        mCurStatus = ViewStatus.EMPTY;
        mLoadingLayoutView.setVisibility(View.VISIBLE);
        mLoadingLayoutView.setStatus(LoadingLayoutView.EMPTY);
    }

    @Override
    public void showView() {
        mCurStatus = ViewStatus.SHOW;
        mLoadingLayoutView.setVisibility(View.GONE);
    }


    @Override
    public void fetchFinish() {
        mPage = mPage + 1;
    }

    @Override
    public void hideRefresh() {
        super.hideRefresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean isListEmpty() {
        return mCollectAdapter.getItemCount() == 0;
    }

    @Override
    public ViewStatus getCurViewStatus() {
        return mCurStatus;
    }
}
