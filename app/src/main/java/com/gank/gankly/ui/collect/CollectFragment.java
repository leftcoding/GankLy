package com.gank.gankly.ui.collect;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.gank.gankly.R;
import com.gank.gankly.bean.RxCollect;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.listener.ItemLongClick;
import com.gank.gankly.mvp.base.FetchFragment;
import com.gank.gankly.ui.base.LySwipeRefreshLayout;
import com.gank.gankly.ui.more.SettingActivity;
import com.gank.gankly.ui.web.WebActivity;
import com.gank.gankly.utils.RxUtils;
import com.gank.gankly.widget.DeleteDialog;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.RecycleViewDivider;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import rx.Subscriber;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

/**
 * 收藏
 * Create by LingYan on 2016-4-25
 * Email:137387869@qq.com
 */
public class CollectFragment extends FetchFragment implements
        DeleteDialog.DialogListener, OnRefreshListener, ItemLongClick, CollectContract.View {
    @BindView(R.id.swipe_multiple_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private SettingActivity mActivity;
    private CollectContract.Presenter mPresenter;
    private CollectAdapter mCollectAdapter;

    private int mLongClick;
    private int mClickPosition = -1;
    private boolean hasMore = true;
    private long mDeleteId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collcet;
    }

    public CollectFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (SettingActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        RxUtils.getInstance().unCollect(new Subscriber<RxCollect>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
            }

            @Override
            public void onNext(RxCollect rxCollect) {
                if (rxCollect.isCollect()) {
                    onDelete(mClickPosition);
                }
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CollectPresenter();
        mPresenter.setModel(new CollectModel(), this);
    }

    @Override
    protected void initValues() {
        mToolbar.setTitle(R.string.mine_my_collect);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar bar = mActivity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    @Override
    protected void initViews() {
        setSwipeRefreshLayout(mSwipeRefreshLayout);
        initAdapter();
        setRecyclerView();
        onRefresh();
    }

    private void setRecyclerView() {
        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setAdapter(mCollectAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, R.drawable.shape_item_divider));
        mRecyclerView.setItemAnimator(new FadeInLeftAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.getItemAnimator().setAddDuration(500);
        mRecyclerView.getItemAnimator().setRemoveDuration(500);
    }

    private void initAdapter() {
        mCollectAdapter = new CollectAdapter(mActivity);
        mCollectAdapter.setItemLongClick(this);
    }

    @Override
    protected void bindLister() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        mMultipleStatusView.showLoading();
        mPresenter.fetchNew();
    }

    @Override
    public void onLongClick(int position, Object object) {
        mLongClick = position;
        UrlCollect mUrlCollect = (UrlCollect) object;
        mDeleteId = mUrlCollect.getId();
        Bundle bundle = new Bundle();
        bundle.putString("content", mUrlCollect.getComment());
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setListener(this);
        deleteDialog.setArguments(bundle);
        deleteDialog.show(mActivity.getSupportFragmentManager(), "delete");
    }

    @Override
    public void onClick(int position, Object object) {
        mClickPosition = position;
        UrlCollect urlCollect = (UrlCollect) object;
        mDeleteId = urlCollect.getId();
        Bundle bundle = new Bundle();
        bundle.putString("title", urlCollect.getComment());
        bundle.putString("url", urlCollect.getUrl());
        bundle.putInt("from_type", WebActivity.FROM_COLLECT);
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
    public void hasNoMoreDate() {
        hasMore = false;
        Snackbar.make(mSwipeRefreshLayout, R.string.tip_no_more_load, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEmpty() {
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showDisNetwork() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showContent() {
        mMultipleStatusView.showContent();
    }

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNavigationClick() {
        onDelete(mLongClick);
    }

    private void onDelete(int item) {
        mCollectAdapter.deleteItem(item);
//        mPresenter.deleteByKey(mDeleteId, mCollectAdapter.getItemCount());
    }
}
