package com.gank.gankly.ui.collect;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.data.entity.UrlCollectDao;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.web.WebActivity;
import com.gank.gankly.utils.ListUtils;
import com.gank.gankly.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import de.greenrobot.dao.query.QueryBuilder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainer;
import in.srain.cube.views.ptr.loadmore.LoadMoreHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreListViewContainer;

public class CollectActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    ListView mListView;
    @Bind(R.id.welfare_frame)
    PtrClassicFrameLayout mPtrFrameLayout;
    @Bind(R.id.welfare_load_more)
    LoadMoreListViewContainer mLoadMore;
    @Bind(R.id.collect_loading)
    View mLoadingView;
    @Bind(R.id.collect_main)
    View mMain;

    private UrlCollectDao mUrlCollectDao;
    private List<UrlCollect> mList;
    private CollectAdapter mCollectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collcet);
        ButterKnife.bind(this);
        initValues();
        initView();
        bindLister();
    }

    private void initValues() {
        mList = new ArrayList<>();
        mCollectAdapter = new CollectAdapter(this, mList);
        mListView.setAdapter(mCollectAdapter);

        List<UrlCollect> _list = queryData();
        if (!ListUtils.isListEmpty(_list)) {
            mList.addAll(_list);
            mCollectAdapter.notifyDataSetChanged();
        }
        showListView();
    }

    private void initView() {
        mToolbar.setTitle(R.string.navigation_settings);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true); //显示返回箭头
        }


    }

    private void bindLister() {
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });

        mLoadMore.useDefaultFooter();
        mLoadMore.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPtrFrameLayout.setEnabled(false);
        mLoadMore.loadMoreFinish(false, false);
    }

    private List<UrlCollect> queryData() {
        mUrlCollectDao = App.getDaoSession().getUrlCollectDao();
        QueryBuilder<UrlCollect> queryBuilder = mUrlCollectDao.queryBuilder();
        queryBuilder.orderDesc(UrlCollectDao.Properties.Date);
        return queryBuilder.list();
    }

    private void onDownRefresh() {

    }

    private void showLoading() {

    }

    private void showListView() {
        mLoadingView.setVisibility(View.GONE);
        mMain.setVisibility(View.VISIBLE);
    }


    @OnItemClick(R.id.recycler_view)
    void onItemClick(int position) {
        UrlCollect urlCollect = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("title", urlCollect.getComment());
        bundle.putString("url", urlCollect.getUrl());
        WebActivity.startWebActivity(CollectActivity.this, bundle);
    }

    @OnItemLongClick(R.id.recycler_view)
    boolean onItemLongClick(int position) {
        UrlCollect urlCollect = mList.get(position);
        mUrlCollectDao.deleteByKey(urlCollect.getId());
        mList.remove(position);
        mCollectAdapter.notifyDataSetChanged();
        ToastUtils.showToast("删除成功!");
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}
