package com.gank.gankly.ui.cure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.ly.business.domain.Gift;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.LazyFragment;
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
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefresh;

    private CureAdapter cureAdapter;
    private CureContract.Presenter curePresenter;

    private ProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cureAdapter = new CureAdapter();
        cureAdapter.setOnItemClickListener(cureCallback);
        swipeRefresh.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        swipeRefresh.setAdapter(cureAdapter);

        swipeRefresh.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefreshListener() {
            @Override
            public void onRefresh() {
                initCureRefresh();
            }

            @Override
            public void onLoadMore() {
                curePresenter.appendGirls();
            }
        });
    }

    private void initCureRefresh() {
        if (curePresenter != null) {
            curePresenter.refreshGirls();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        curePresenter = new CurePresenter(getContext(), this);
    }

    private final CureAdapter.ItemCallback cureCallback = new CureAdapter.ItemCallback() {
        @Override
        public void onItemClick(final String url) {
            if (!TextUtils.isEmpty(url)) {
                showLoadingDialog();
                curePresenter.loadImages(url);
            }
        }
    };

    private void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getContext().getString(R.string.loading_meizi_images));
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setOnCancelListener(dialog -> curePresenter.destroy());
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void openBrowseActivity(@NonNull ArrayList<Gift> list) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getContext(), GalleryActivity.class);
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_DAILY);
        intent.putExtra(GalleryActivity.EXTRA_LIST, list);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    @Override
    public void hideProgress() {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void showProgress() {
        if (swipeRefresh != null && !swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void showContent() {
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
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
    public void setMaxProgress(int value) {
        if (progressDialog != null) {
            progressDialog.setMax(value);
        }
    }

    @Override
    public void disProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cureAdapter != null) {
            cureAdapter.destroy();
        }
        if (curePresenter != null) {
            curePresenter.destroy();
        }
    }

    @Override
    public void onLazyActivityCreate() {
        initCureRefresh();
    }

    @Override
    public void refreshSuccess(List<Gift> list) {
        if (list != null) {
            if (cureAdapter != null) {
                cureAdapter.refillItem(list);
                cureAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void refreshFailure(String msg) {
        shortToast(msg);
    }

    @Override
    public void appendSuccess(List<Gift> list) {
        if (cureAdapter != null) {
            if (list != null) {
                cureAdapter.appendItem(list);
                cureAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void appendFailure(String msg) {
        shortToast(msg);
    }

    @Override
    public void shortToast(String string) {

    }
}
