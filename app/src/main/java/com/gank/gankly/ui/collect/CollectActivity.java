package com.gank.gankly.ui.collect;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.widget.DeleteDialog;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import butterknife.Bind;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Create by LingYan on 2016-4-25
 */
public class CollectActivity extends BaseActivity implements DeleteDialog.DialogListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.collect_loading)
    View mLoadingView;
    @Bind(R.id.collect_main)
    View mMain;

    private UrlCollectDao mUrlCollectDao;
    private CollectAdapter mCollectAdapter;
    private List<UrlCollect> mList;
    private int mLostPosition;

    private int mPage = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    //    @OnItemClick(R.id.recycler_view)
    void onItemClick(int position) {
//        UrlCollect urlCollect = mList.get(position);
//        Bundle bundle = new Bundle();
//        bundle.putString("title", urlCollect.getComment());
//        bundle.putString("url", urlCollect.getUrl());
//        WebActivity.startWebActivity(CollectActivity.this, bundle);
    }

    //    @OnItemLongClick(R.id.recycler_view)
//    boolean onItemLongClick(int position) {
//        mLongClick = position;
//        Bundle bundle = new Bundle();
//        bundle.putString("title", "是否确认删除？");
//        bundle.putString("content", mList.get(position).getComment());
//        DeleteDialog deleteDialog = new DeleteDialog();
//        deleteDialog.setListener(this);
//        deleteDialog.setArguments(bundle);
//        deleteDialog.show(getSupportFragmentManager(), "delete");
//        return true;
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onNavigationClick() {
//        UrlCollect urlCollect = mList.get(mLongClick);
//        mUrlCollectDao.deleteByKey(urlCollect.getId());
//        mList.remove(mLongClick);
//        mCollectAdapter.notifyDataSetChanged();
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
        mToolbar.setTitle(R.string.navigation_collect);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true); //显示返回箭头
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mCollectAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));

        updateDate();
    }

    @Override
    protected void bindListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLostPosition + 1 == mCollectAdapter.getItemCount()
                        && !mSwipeRefreshLayout.isRefreshing()) {
                    updateDate();
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
        mLoadingView.setVisibility(View.GONE);
        mMain.setVisibility(View.VISIBLE);
    }

    public void updateDate() {
        mList = queryData();
        if (!ListUtils.isListEmpty(mList)) {
            showListView();
            mCollectAdapter.updateItems(mList);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private List<UrlCollect> queryData() {
        mPage = mPage + 1;
        mUrlCollectDao = App.getDaoSession().getUrlCollectDao();
        QueryBuilder<UrlCollect> queryBuilder = mUrlCollectDao.queryBuilder();
        queryBuilder.orderDesc(UrlCollectDao.Properties.Date);
        queryBuilder.limit(10).offset(1);
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
}
