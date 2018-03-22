package com.gank.gankly.ui.pure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.gallery.GalleryActivity;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 清纯妹子
 * Create by LingYan on 2016-05-17
 */
public class PureFragment extends LazyFragment implements ItemClick, PureContract.View {
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefresh;

    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    private PureAdapter mAdapter;

    private ArrayList<GiftBean> mImageCountList = new ArrayList<>();
    private ProgressDialog mDialog;
    private PureContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    public static PureFragment getInstance() {
        return new PureFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();

        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new PurePresenter(getContext(), this);
        initRefresh();
    }

    @Override
    protected void initLazy() {
        initRefresh();
    }

    private void initRefresh() {
        multipleStatusView.showLoading();
        mPresenter.refreshPure();
    }

    private void initRecycler() {
        mAdapter = new PureAdapter(context);
        swipeRefresh.setAdapter(mAdapter);

        swipeRefresh.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));

        swipeRefresh.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefreshListener() {

            @Override
            public void onRefresh() {
                mPresenter.refreshPure();
            }

            @Override
            public void onLoadMore() {
                mPresenter.appendPure();
            }
        });
    }


    private void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(context);
        }

        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(context.getString(R.string.loading_meizi_images));
        mDialog.setIndeterminate(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setOnCancelListener(dialog -> mPresenter.unSubscribe());

        if (!mDialog.isShowing()) {
            mDialog.show();
        }

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void onClick(int position, Object object) {
        showDialog();
        final GiftBean giftBean = (GiftBean) object;
        Observable.just(giftBean)
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(giftBean1 -> mPresenter.refreshImages(giftBean1.getUrl()));
    }

    public List<GiftBean> getList() {
        return mImageCountList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(false);
        }
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void showContent() {
        multipleStatusView.showContent();
    }

    @Override
    public void showEmpty() {
        multipleStatusView.showEmpty();
    }

    @Override
    public void showDisNetWork() {
        multipleStatusView.showDisNetwork();
    }

    @Override
    public void showError() {
        multipleStatusView.showError();
    }

    private void showLoading() {
        if (multipleStatusView != null) {
            multipleStatusView.showLoading();
        }
    }

    @Override
    public void refillData(List<GiftBean> list) {
        mAdapter.refillItems(list);
    }

    @Override
    public void appendData(List<GiftBean> list) {
        mAdapter.appedItems(list);
    }

    @Override
    public void openGalleryActivity(ArrayList<GiftBean> list) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, GalleryActivity.class);
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_GIFT);
        intent.putExtra(GalleryActivity.EXTRA_LIST, list);
        intent.putExtras(bundle);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(context,
                R.anim.alpha_in, R.anim.alpha_out);
        context.startActivity(intent, compat.toBundle());
    }

    @Override
    public void disLoadingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
