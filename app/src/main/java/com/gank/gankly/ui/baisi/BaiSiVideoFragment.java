package com.gank.gankly.ui.baisi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.RxBus.RxBus_;
import com.gank.gankly.bean.BuDeJieVideo;
import com.gank.gankly.mvp.source.remote.BaiSiDataSource;
import com.gank.gankly.ui.baisi.image.GallerySize;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.utils.SpaceItemDecoration;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaiSiActivity) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    protected void initData() {
        KLog.d("initData");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new BaiSiVideoPresenter(BaiSiDataSource.getInstance(), this);
    }

    @Override
    protected void callBackRefreshUi() {

    }

    @Override
    protected void initValues() {
        mPresenter.fetchNew();
    }

    @Override
    protected void initViews() {
        mBaiSiVideoAdapter = new BaiSiVideoAdapter(mActivity);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setAdapter(mBaiSiVideoAdapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_space);
        mSwipeRefreshLayout.getRecyclerView().addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onLoadMore() {
                showRefresh();
                mPresenter.fetchMore();
            }

            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }
        });

        mBaiSiVideoAdapter.setPlayClick(new BaiSiVideoAdapter.onPlayClick() {
            @Override
            public void onPlayclick(int position, View image, String url, int height, int width, String title, String shareUrl) {
                int[] location = new int[2];
                image.getLocationInWindow(location);
                KLog.d("height:" + location[0] + ":" + location[1] + "," + image.getMeasuredHeight() + ":" + image.getMeasuredWidth());
                startActivity(new GallerySize(height, width, url, location[1], title, shareUrl));

            }
        });

    }

    private void startActivity(GallerySize gs) {
        RxBus_.getDefault().postSticky(gs);
        Intent intent = new Intent(mActivity, BaiSiVideoPreViewActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(0, 0);
    }


    @Override
    protected void bindListener() {

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
    public void hasNoMoreDate() {

    }

    @Override
    public void showRefreshError(String errorStr) {

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
}
