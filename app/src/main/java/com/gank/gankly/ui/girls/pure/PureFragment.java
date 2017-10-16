package com.gank.gankly.ui.girls.pure;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.bean.GiftBean;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.rxjava.theme.ThemeEvent;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.gallery.GalleryActivity;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.utils.DisplayUtils;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 清纯妹子
 * Create by LingYan on 2016-05-17
 * Email:137387869@qq.com
 */
public class PureFragment extends LazyFragment implements ItemClick, PureContract.View {
    @BindView(R.id.meizi_swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;

    private RecyclerView mRecyclerView;
    private PureAdapter mAdapter;
    private MainActivity mActivity;
    private Disposable mDisposable;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();

        mAdapter.setOnItemClickListener(this);

        mDisposable = RxBus_.getInstance().toObservable(ThemeEvent.class)
                .subscribe(themeEvent -> changeUi());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new PurePresenter(getContext(), this);
    }

    @Override
    protected void initLazy() {
        initRefresh();
    }

    private void initRefresh() {
        mMultipleStatusView.showLoading();
        mPresenter.refreshPure();
    }

    private void changeUi() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.data;
        TypedValue textValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, textValue, true);
        int textColor = textValue.data;
        theme.resolveAttribute(R.attr.themeBackground, textValue, true);
        int recyclerColor = textValue.data;
        mRecyclerView.setBackgroundColor(recyclerColor);

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            TextView title = (TextView) childView.findViewById(R.id.goods_txt_title);
            title.setTextColor(textColor);
            View rlView = childView.findViewById(R.id.goods_rl_title);
            rlView.setBackgroundColor(background);
        }

        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    private void initRecycler() {
        mAdapter = new PureAdapter(mActivity);
        mSwipeRefreshLayout.setAdapter(mAdapter);

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));

        float leftPadding = DisplayUtils.dp2px(8);// because StaggeredGridLayoutManager left margin
        mRecyclerView.setPadding((int) leftPadding, 0, 0, 0);
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {

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
            mDialog = new ProgressDialog(mActivity);
        }

        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(App.getAppString(R.string.loading_meizi_images));
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
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
        mMultipleStatusView.showContent();
    }

    @Override
    public void showEmpty() {
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showDisNetWork() {
        mMultipleStatusView.showDisNetwork();
    }

    @Override
    public void showError() {
        mMultipleStatusView.showError();
    }

    @Override
    public void showLoading() {
        mMultipleStatusView.showLoading();
    }

    @Override
    public void showRefreshError(String errorStr) {

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
        Intent intent = new Intent(mActivity, GalleryActivity.class);
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_GIFT);
        intent.putExtra(GalleryActivity.EXTRA_LIST, list);
        intent.putExtras(bundle);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                R.anim.alpha_in, R.anim.alpha_out);
        mActivity.startActivity(intent, compat.toBundle());
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
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
