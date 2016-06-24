package com.gank.gankly.ui.main.meizi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.config.ViewsModel;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.presenter.GiftPresenter;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.base.BaseSwipeRefreshLayout;
import com.gank.gankly.ui.browse.BrowseActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.view.IGiftView;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-05-17
 */
public class GiftFragment extends BaseSwipeRefreshFragment implements ItemClick, IGiftView {
    private static GiftFragment sGiftFragment;

    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.meizi_swipe_refresh)
    BaseSwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.loading_view)
    MultipleStatusView mMultipleStatusView;

    private GiftAdapter mAdapter;
    private MainActivity mActivity;

    private int mCurPage = 1;
    private List<GiftBean> mImageCountList;
    private ProgressDialog mDialog;
    private int mClickPosition;
    private boolean isLoading;
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
        mToolbar.setTitle(R.string.navigation_gift);
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

        mMultipleStatusView.showLoading();
        onDownRefresh();
    }

    @Override
    protected void initViews() {
        mImageCountList = new ArrayList<>();
        initRecycler();
    }

    @Override
    protected void bindLister() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    private void onDownRefresh() {
        mCurPage = 1;
        toRefresh();
    }

    private void toRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.fetchPageCount(mCurPage);
    }

    private void initRecycler() {
        mSwipeRefreshLayout.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mSwipeRefreshLayout.setRefreshListener(new BaseSwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        onDownRefresh();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                        toRefresh();
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
    public void onCompleted() {
        super.onCompleted();
        mCurPage = mCurPage + 1;
        showContent();
        mSwipeRefreshLayout.finishLoad();
    }

    @Override
    public void clear() {
        super.clear();
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
        if (mClickPosition != position || isLoading) {
            mClickPosition = position;
            mImageCountList.clear();
            isLoading = false;
            mPresenter.initProgress();
        }
        showDialog();
        isLoading = true;
        GiftBean giftBean = (GiftBean) object;
        mPresenter.fetchImagePages(giftBean.getUrl());
    }

    public List<GiftBean> getList() {
        return mImageCountList;
    }
}
