package com.gank.gankly.ui.main.meizi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.config.ViewsModel;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.presenter.GiftPresenter;
import com.gank.gankly.ui.base.BaseSwipeRefreshLayout;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.browse.BrowseActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.view.IGiftView;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-05-17
 * Email:137387869@qq.com
 */
public class GiftFragment extends LazyFragment implements ItemClick, IGiftView {
    private static GiftFragment sGiftFragment;
    @BindView(R.id.meizi_swipe_refresh)
    BaseSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.loading_view)
    MultipleStatusView mMultipleStatusView;

    private GiftAdapter mAdapter;
    private MainActivity mActivity;

    private int mCurPage = 1;
    private List<GiftBean> mImageCountList = new ArrayList<>();
    private ProgressDialog mDialog;
    private GiftPresenter mPresenter;

    public GiftFragment() {
    }

    public static GiftFragment getInstance() {
        if (sGiftFragment == null) {
            sGiftFragment = new GiftFragment();
        }
        return sGiftFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new GiftPresenter(mActivity, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initValues() {
        initRefresh();
    }

    @Override
    protected void initViews() {
        initRecycler();
    }

    @Override
    protected void bindLister() {
    }

    @Override
    protected void initDate() {
        initRefresh();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    private void initRefresh() {
        mMultipleStatusView.showLoading();
        onFetchNew();
    }

    private void onFetchNew() {
        mCurPage = 1;
        mPresenter.fetchNew(mCurPage);
    }

    private void onFetchNext() {
        showRefresh();
        mPresenter.fetchNext(mCurPage);
    }

    private void initRecycler() {
        mSwipeRefreshLayout.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mSwipeRefreshLayout.setOnScrollListener(new BaseSwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                onFetchNew();
            }

            @Override
            public void onLoadMore() {
                onFetchNext();
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
        mAdapter = new GiftAdapter(mActivity);
        mAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setAdapter(mAdapter);
    }


    private void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mActivity);
        }
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMessage(App.getAppString(R.string.loading_meizi_images));
        mDialog.setIndeterminate(false);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setProgress(0);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mPresenter.unSubscribe();
            }
        });
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void showContent() {
        mMultipleStatusView.showContent();
    }

    @Override
    public void hideRefresh() {
        super.hideRefresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        super.showRefresh();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void clear() {
        mAdapter.clear();
    }

    @Override
    public void refillDate(List<GiftBean> list) {
        mAdapter.updateItems(list);
    }

    @Override
    public void setMax(int max) {
        if (mDialog != null) {
            mDialog.setMax(max);
        }
    }

    @Override
    public void setProgress(int progress) {
        if (mDialog != null) {
            mDialog.setProgress(progress);
        }
    }

    @Override
    public void disDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void setNextPage(int page) {
        this.mCurPage = page;
    }


    @Override
    public void refillImagesCount(List<GiftBean> giftResult) {
        mImageCountList.addAll(giftResult);
    }

    @Override
    public void gotoBrowseActivity() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mActivity, BrowseActivity.class);
        bundle.putString(ViewsModel.Gift, ViewsModel.Gift);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void onClick(int position, Object object) {
        showDialog();
        mImageCountList.clear();
        GiftBean giftBean = (GiftBean) object;
        mPresenter.fetchImagePageList(giftBean.getUrl());
    }

    public List<GiftBean> getList() {
        return mImageCountList;
    }
}
