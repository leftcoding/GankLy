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
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.listener.ItemLongClick;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.web.WebActivity;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.widget.DeleteDialog;
import com.gank.gankly.widget.LoadingLayoutView;
import com.gank.gankly.widget.RecycleViewDivider;

import java.util.List;

import butterknife.Bind;
import de.greenrobot.dao.query.QueryBuilder;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

/**
 * Create by LingYan on 2016-4-25
 */
public class CollectFragment_backups extends BaseFragment implements DeleteDialog.DialogListener, SwipeRefreshLayout.OnRefreshListener, ItemLongClick {
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.loading_view)
    LoadingLayoutView mLoadingLayoutView;

    private static final int PAGE_LIMIT = 10;
    private static CollectFragment_backups sCollectFragment;

    private MainActivity mActivity;
    private UrlCollectDao mUrlCollectDao;
    private CollectAdapter mCollectAdapter;
    private UrlCollect mUrlCollect;
    private int mLostPosition;
    private int mPage = 0;
    private int mLongClick;
    private boolean isLoadMore = true;

    public static CollectFragment_backups getInstance() {
        if (sCollectFragment == null) {
            sCollectFragment = new CollectFragment_backups();
        }
        return sCollectFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onNavigationClick() {
        mUrlCollectDao.deleteByKey(mUrlCollect.getId());
        mCollectAdapter.deleteItem(mLongClick);
    }

    @Override
    protected void initValues() {
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

        updateDate();
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
                    if (isLoadMore) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        updateDate();
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
    public void onRefresh() {
        mPage = 0;
        updateDate();
    }

    private void showListView() {
        mLoadingLayoutView.setVisibility(View.GONE);
    }

    public void updateDate() {
        List<UrlCollect> mList = queryData();
        if (!ListUtils.isListEmpty(mList)) {
            if (mPage == 0) {
                mCollectAdapter.clear();
            }
            mCollectAdapter.updateItems(mList);
            mPage = mPage + 1;
            int size = mList.size();
            if (size < PAGE_LIMIT) {
                noLoadMore();
            }
        } else {
            noLoadMore();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void noLoadMore() {
        isLoadMore = false;
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_SHORT).show();
    }

    private List<UrlCollect> queryData() {
        int offSet = mPage * PAGE_LIMIT;
        mUrlCollectDao = App.getDaoSession().getUrlCollectDao();
        QueryBuilder<UrlCollect> queryBuilder = mUrlCollectDao.queryBuilder();
        queryBuilder.orderDesc(UrlCollectDao.Properties.Date);
        queryBuilder.offset(offSet).limit(PAGE_LIMIT);
        return queryBuilder.list();
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
}
