package com.gank.gankly.ui.baisi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.bean.BuDeJieVideo;
import com.gank.gankly.bean.GallerySize;
import com.gank.gankly.mvp.source.remote.BuDeJieDataSource;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.SpaceItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-11-30
 */

public class BaiSiVideoFragment extends LazyFragment implements BaiSiVideoContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private BaiSiVideoAdapter mBaiSiVideoAdapter;
    private BaiSiVideoContract.Presenter mPresenter;
    private BaiSiActivity mActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaiSiActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new BaiSiVideoPresenter(BuDeJieDataSource.getInstance(), this);
        mPresenter.fetchNew();
    }

    @Override
    public void onLazyActivityCreate() {

    }

    private void initViews() {
        mBaiSiVideoAdapter = new BaiSiVideoAdapter(mActivity);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setAdapter(mBaiSiVideoAdapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_space);
        mSwipeRefreshLayout.getRecyclerView().addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefreshListener() {
            @Override
            public void onLoadMore() {
                showProgress();
                mPresenter.fetchMore();
            }

            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }
        });

        mBaiSiVideoAdapter.setPlayClick((position, image, url, height, width, title, shareUrl) -> {
            int[] location = new int[2];
            image.getLocationInWindow(location);
            startActivity(new GallerySize(height, width, url, location[1], title, shareUrl));
        });

    }

    private void startActivity(GallerySize gs) {
        RxBus_.getInstance().postSticky(gs);
        Intent intent = new Intent(mActivity, BaiSiVideoPreViewActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(0, 0);
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
    public void hasNoMoreDate() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showDisNetWork() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void refillData(List<BuDeJieVideo.ListBean> list) {
        mBaiSiVideoAdapter.updateItems(list);
    }

    @Override
    public void appendData(List<BuDeJieVideo.ListBean> list) {
        mBaiSiVideoAdapter.addItems(list);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void shortToast(String string) {

    }
}
