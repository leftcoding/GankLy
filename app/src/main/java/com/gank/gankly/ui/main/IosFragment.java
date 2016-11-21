package com.gank.gankly.ui.main;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.RxBus.ChangeThemeEvent.ThemeEvent;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.RecyclerOnClick;
import com.gank.gankly.presenter.IBaseRefreshPresenter;
import com.gank.gankly.presenter.impl.IosGoodsPresenterImpl;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.normal.WebActivity;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.view.IMeiziView;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.LySwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * ios
 * Create by LingYan on 2016-4-26
 * Email:137387869@qq.com
 */
public class IosFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerOnClick, IMeiziView<List<ResultsBean>> {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private HomeActivity mActivity;
    private GankAdapter mRecyclerAdapter;
    private IBaseRefreshPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    public IosFragment() {
    }

    public static IosFragment newInstance() {
        IosFragment fragment = new IosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IosGoodsPresenterImpl(mActivity, this);
    }

    @Override
    protected void initValues() {
        RxBus.getInstance().toSubscription(ThemeEvent.class, new Action1<ThemeEvent>() {
            @Override
            public void call(ThemeEvent event) {
                refreshUi();
            }
        });
    }

    @Override
    protected void initViews() {
        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mRecyclerAdapter = new GankAdapter(mActivity, GankAdapter.LAYOUT_IOS);
        mSwipeRefreshLayout.setAdapter(mRecyclerAdapter);

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void bindListener() {
        mMultipleStatusView.setListener(new MultipleStatusView.OnMultipleClick() {
            @Override
            public void retry(View v) {
                initFetchDate();
            }
        });
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
    protected void initData() {
        showLoading();
        initFetchDate();
    }

    private void initFetchDate() {
        mPresenter.fetchNew();
    }

    @Override
    public void refreshUi() {
        Resources.Theme theme = mActivity.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.data;
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, typedValue, true);
        int textColor = typedValue.data;
        theme.resolveAttribute(R.attr.textSecondaryColor, typedValue, true);
        int textSecondaryColor = typedValue.data;
        theme.resolveAttribute(R.attr.themeBackground, typedValue, true);
        int mainColor = typedValue.data;
        mRecyclerView.setBackgroundColor(mainColor);

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            childView.setBackgroundColor(background);
            TextView title = (TextView) childView.findViewById(R.id.goods_txt_title);
            title.setTextColor(textColor);
            TextView time = (TextView) childView.findViewById(R.id.goods_txt_time);
            time.setTextColor(textSecondaryColor);
        }

        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    public void onRefresh() {
        showRefresh();
        initFetchDate();
    }

    @Override
    public void refillDate(List<ResultsBean> list) {
        mRecyclerAdapter.updateItems(list);
    }

    @Override
    public void appendMoreDate(List<ResultsBean> list) {
        mRecyclerAdapter.appendMoreDate(list);
    }

    @Override
    public void showRefreshError(String errorStr) {
        Snackbar.make(mSwipeRefreshLayout, errorStr, Snackbar.LENGTH_LONG)
                .setActionTextColor(App.getAppColor(R.color.Blue))
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.fetchMore();
                    }
                }).show();
    }

    @Override
    public void hasNoMoreDate() {
        Snackbar.make(mRecyclerView, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void clear() {

    }

    @Override
    public void showLoading() {
        super.showLoading();
        mMultipleStatusView.showLoading();
    }

    @Override
    public void showError() {
        super.showError();
        mMultipleStatusView.showError();
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        mMultipleStatusView.showEmpty();
    }

    @Override
    public void showContent() {
        super.showContent();
        mMultipleStatusView.showContent();
    }

    @Override
    public void showDisNetWork() {
        super.showDisNetWork();
        mMultipleStatusView.showNoNetwork();
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onClick(View view, ResultsBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, bean.getDesc());
        bundle.putString(WebActivity.URL, bean.getUrl());
        bundle.putString(WebActivity.TYPE, Constants.IOS);
        bundle.putString(WebActivity.AUTHOR, bean.getWho());
        WebActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (HomeActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
