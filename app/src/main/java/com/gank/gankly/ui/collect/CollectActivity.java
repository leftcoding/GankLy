package com.gank.gankly.ui.collect;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.listener.ItemLongClick;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.web.WebActivity;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.widget.DeleteDialog;
import com.gank.gankly.widget.LoadingLayoutView;
import com.gank.gankly.widget.RecycleViewDivider;

import java.util.List;

import butterknife.Bind;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Create by LingYan on 2016-4-25
 */
public class CollectActivity extends BaseActivity implements DeleteDialog.DialogListener, SwipeRefreshLayout.OnRefreshListener, ItemLongClick {
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.collect_main)
    View mMain;
    @Bind(R.id.loading_view)
    LoadingLayoutView mLoadingLayoutView;

    private static final int PAGE_LIMIT = 10;

    private UrlCollectDao mUrlCollectDao;
    private CollectAdapter mCollectAdapter;
    private int mLostPosition;
    private int mPage = 0;
    private boolean isLoadMore = true;
    private int mLongClick;
    private UrlCollect mUrlCollect;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onNavigationClick() {
        mUrlCollectDao.deleteByKey(mUrlCollect.getId());
        mCollectAdapter.deleteItem(mLongClick);
    }

    @Override
    public void onCancelClick() {
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_collcet;
    }

    @Override
    protected void initValues() {
//        mToolbar.setTitle(R.string.navigation_collect);
        setTitle(R.string.navigation_collect);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initViews() {
        mCollectAdapter = new CollectAdapter(this);
        mCollectAdapter.setItemLongClick(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, R.drawable.shape_item_divider));
        mRecyclerView.setAdapter(mCollectAdapter);
        mRecyclerView.setBackgroundColor(App.getAppColor(R.color.white));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));

        updateDate();
    }

    @Override
    protected void bindListener() {
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
    public void onRefresh() {
        updateDate();
    }

    private void showListView() {
        mLoadingLayoutView.setVisibility(View.GONE);
        mMain.setVisibility(View.VISIBLE);
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
                isLoadMore = false;
                Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_SHORT).show();
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
        showListView();
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLongClick(int position, Object object) {
        mLongClick = position;
        UrlCollect urlCollect = (UrlCollect) object;
        mUrlCollect = urlCollect;
        Bundle bundle = new Bundle();
        bundle.putString("content", urlCollect.getComment());
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setListener(this);
        deleteDialog.setArguments(bundle);
        deleteDialog.show(getSupportFragmentManager(), "delete");
    }

    @Override
    public void onClick(int position, Object object) {
        UrlCollect urlCollect = (UrlCollect) object;
        Bundle bundle = new Bundle();
        bundle.putString("title", urlCollect.getComment());
        bundle.putString("url", urlCollect.getUrl());
        WebActivity.startWebActivity(CollectActivity.this, bundle);
    }
}
