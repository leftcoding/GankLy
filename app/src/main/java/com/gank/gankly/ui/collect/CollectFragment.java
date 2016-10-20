package com.gank.gankly.ui.collect;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import com.gank.gankly.R;
import com.gank.gankly.bean.RxCollect;
import com.gank.gankly.data.entity.UrlCollect;
import com.gank.gankly.listener.ItemLongClick;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
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
public class CollectFragment extends BaseSwipeRefreshFragment implements
        DeleteDialog.DialogListener, OnRefreshListener, ItemLongClick, CollectContract.View {

    @BindView(R.id.meizi_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.meizi_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.loading_view)
    MultipleStatusView mMultipleStatusView;

    private SettingActivity mActivity;
    private CollectPresenter mPresenter;
    private CollectAdapter mCollectAdapter;

    private int mLostPosition;
    private int mPage = 0;
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
    public void onNavigationClick() {
        onDelete(mLongClick);
    }

    private void onDelete(int item) {
        mCollectAdapter.deleteItem(item);
//        mPresenter.deleteByKey(mDeleteId, mCollectAdapter.getItemCount());
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {
        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mCollectAdapter = new CollectAdapter(mActivity);
        mCollectAdapter.setItemLongClick(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, R.drawable.shape_item_divider));
        mRecyclerView.setAdapter(mCollectAdapter);
//        mRecyclerView.setBackgroundColor(App.getAppColor(R.color.white));
        mRecyclerView.setItemAnimator(new FadeInLeftAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.getItemAnimator().setAddDuration(500);
        mRecyclerView.getItemAnimator().setRemoveDuration(500);

        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
        mMultipleStatusView.showLoading();
        onRefresh();

//        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    protected void bindLister() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLostPosition + 1 == mCollectAdapter.getItemCount()
                        && !mSwipeRefreshLayout.isRefreshing()) {
                    if (hasMore) {
                        onDownRefresh();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                mLostPosition = manager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void onRefresh() {
        mPresenter.fetchNew();
    }

    private void onDownRefresh() {
//        mPresenter.fetchCollect(mPage, RefreshStatus.UP);
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
        super.showEmpty();
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showDisNetwork() {

    }

    @Override
    public void showContent() {
        mMultipleStatusView.showContent();
    }

    @Override
    public void showRefresh() {
        super.showRefresh();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        super.hideRefresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(CollectContract.Presenter presenter) {

    }
}
