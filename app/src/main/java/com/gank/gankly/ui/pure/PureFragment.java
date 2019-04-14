package com.gank.gankly.ui.pure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.ly.business.domain.Gift;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.LazyFragment;
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
 * 妹子 - 清纯
 * Create by LingYan on 2016-05-17
 */
public class PureFragment extends LazyFragment implements PureContract.View {
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefresh;

    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    private PureAdapter pureAdapter;
    private PureContract.Presenter purePresenter;

    private ProgressDialog progressDialog;

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

        pureAdapter.setOnItemClickListener(pureCallback);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        purePresenter = new PurePresenter(getContext(), this);
        initRefresh();
    }

    private final PureAdapter.ItemClickCallback pureCallback = new PureAdapter.ItemClickCallback() {
        @Override
        public void onItemClick(final Gift gift) {
            showDialog();
            Observable.just(gift)
                    .throttleFirst(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(giftBean1 -> purePresenter.refreshImages(giftBean1.url));
        }
    };

    private void initRefresh() {
        multipleStatusView.showLoading();
        purePresenter.refreshPure();
    }

    private void initRecycler() {
        pureAdapter = new PureAdapter(getContext());
        swipeRefresh.setAdapter(pureAdapter);

        swipeRefresh.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));

        swipeRefresh.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefreshListener() {

            @Override
            public void onRefresh() {
                purePresenter.refreshPure();
            }

            @Override
            public void onLoadMore() {
                purePresenter.appendPure();
            }
        });
    }


    private void showDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getContext().getString(R.string.loading_meizi_images));
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setOnCancelListener(dialog -> purePresenter.destroy());

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
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
    public void refillData(List<Gift> list) {
        if (pureAdapter != null) {
            pureAdapter.refillItems(list);
            pureAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void appendData(List<Gift> list) {
        if (pureAdapter != null) {
            pureAdapter.appendItems(list);
            pureAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void openGalleryActivity(ArrayList<Gift> list) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getContext(), GalleryActivity.class);
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_GIFT);
        intent.putExtra(GalleryActivity.EXTRA_LIST, list);
        intent.putExtras(bundle);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(getContext(),
                R.anim.alpha_in, R.anim.alpha_out);
        getContext().startActivity(intent, compat.toBundle());
    }

    @Override
    public void disLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pureAdapter != null) {
            pureAdapter.destroy();
        }
    }

    @Override
    public void onLazyActivityCreate() {

    }

    @Override
    public void shortToast(String string) {

    }
}
