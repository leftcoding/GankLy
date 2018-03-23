package com.gank.gankly.ui.cure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.ly.business.domain.Gift;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.gank.gankly.R;

import android.ly.business.domain.DailyMeizi;

import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.gallery.GalleryActivity;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 妹子 - 清纯
 * Create by LingYan on 2016-07-01
 */
public class CureFragment extends LazyFragment implements CureContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefresh;

    private CureAdapter mCureAdapter;
    private CurePresenter curePresenter;

    private ProgressDialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCureAdapter = new CureAdapter();
        swipeRefresh.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        swipeRefresh.setAdapter(mCureAdapter);

        swipeRefresh.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefreshListener() {
            @Override
            public void onRefresh() {
                onRefresh();
            }

            @Override
            public void onLoadMore() {
                curePresenter.fetchMore();
            }
        });

        mCureAdapter.setOnItemClickListener(cureCallback);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        curePresenter = new CurePresenter(context, this);
    }

    private final CureAdapter.ItemCallback cureCallback = new CureAdapter.ItemCallback() {
        @Override
        public void onItemClick(final DailyMeizi dailyMeizi) {
            final String url = dailyMeizi.url;
            if (!TextUtils.isEmpty(url)) {
                showLoadingDialog();
                curePresenter.girlsImages(url);
            }
        }
    };

    @Override
    protected void initLazy() {
        onRefresh();
    }

    void onRefresh() {
        if (curePresenter != null) {
            curePresenter.fetchNew();
        }
    }

    private void showLoadingDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(context);
        }
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(context.getString(R.string.loading_meizi_images));
        mDialog.setIndeterminate(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setOnCancelListener(dialog -> curePresenter.unSubscribe());
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void openBrowseActivity(@NonNull ArrayList<Gift> list) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, GalleryActivity.class);
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_DAILY);
        intent.putExtra(GalleryActivity.EXTRA_LIST, list);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void showContent() {
        mMultipleStatusView.showContent();
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
    public void refillData(List<DailyMeizi> list) {
        mCureAdapter.refillItem(list);
    }

    @Override
    public void appendItem(List<DailyMeizi> list) {
        mCureAdapter.appendItem(list);
    }

    @Override
    public void setMaxProgress(int value) {
        if (mDialog != null) {
            mDialog.setMax(value);
        }
    }

    @Override
    public void disProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
