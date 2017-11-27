package com.gank.gankly.ui.baisi.image;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.bean.BuDeJieBean;
import com.gank.gankly.mvp.source.remote.BuDeJieDataSource;
import com.gank.gankly.ui.baisi.BaiSiActivity;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.widget.SpaceItemDecoration;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-12-05
 * Email:137387869@qq.com
 */

public class BaiSiImageFragment extends LazyFragment implements BaiSiImageContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private BaiSiImageContract.Presenter mPresenter;
    private BaiSiImageAdapter mAdapter;
    private BaiSiActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaiSiActivity) context;
    }

    @Override
    protected void initLazy() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.fetchNew();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_space);
        mSwipeRefreshLayout.getRecyclerView().addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        mAdapter = new BaiSiImageAdapter(mActivity);
        mAdapter.setPlayClick(gallerySize -> mActivity.getSupportFragmentManager().beginTransaction()
                .addToBackStack("BaiSiGalleryFragment")
                .add(R.id.setting_frame_layout, BaiSiGalleryFragment.newInstance(gallerySize))
                .commitAllowingStateLoss());
        mSwipeRefreshLayout.setAdapter(mAdapter);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
                mPresenter.fetchMore();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new BaiSiImagePresenter(BuDeJieDataSource.getInstance(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
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
    public void refillData(List<BuDeJieBean.ListBean> list) {
        mAdapter.refillItems(list);
    }

    @Override
    public void appendData(List<BuDeJieBean.ListBean> list) {
        mAdapter.appendItems(list);
    }
}
