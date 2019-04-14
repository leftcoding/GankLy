package com.gank.gankly.ui.discovered.video;

import android.content.Context;
import android.content.res.Resources;
import android.ly.business.domain.Gank;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.listener.MeiziOnClick;
import com.gank.gankly.ui.MainActivity;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.WebVideoViewActivity;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;

/**
 * 休息视频
 * Create by LingYan on 2016-04-25
 */
public class VideoFragment extends LazyFragment implements MeiziOnClick, SwipeRefreshLayout.OnRefreshListener,
        VideoContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private VideoContract.Presenter mPresenter;
    private MainActivity mActivity;
    private VideoAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setRefreshing(true);
        //        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mMultipleStatusView.setListener(v -> onLoading());

        mAdapter = new VideoAdapter(mActivity);
        mAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setAdapter(mAdapter);

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefreshListener() {
            @Override
            public void onRefresh() {
//                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
//                mPresenter.fetchMore();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mPresenter = new VideoPresenter(GankDataSource.getInstance(), this);
    }

    @Override
    public void onLazyActivityCreate() {

    }

    private void onLoading() {
//        mPresenter.fetchNew();
    }

    protected void callBackRefreshUi() {
        Resources.Theme theme = mActivity.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.resourceId;
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, typedValue, true);
        int textColor = typedValue.data;
        theme.resolveAttribute(R.attr.themeBackground, typedValue, true);
        int mainColor = typedValue.data;
        mRecyclerView.setBackgroundColor(mainColor);

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            View view = childView.findViewById(R.id.goods_rl_title);
            TextView title = (TextView) childView.findViewById(R.id.title);
            view.setBackgroundResource(background);
            title.setTextColor(textColor);
        }
        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        List<Gank> list = mAdapter.getResults();
        bundle.putString(WebVideoViewActivity.TITLE, list.get(position).desc);
        bundle.putString(WebVideoViewActivity.URL, list.get(position).url);
        WebVideoViewActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void onRefresh() {
        onLoading();
    }

    @Override
    public void refillData(List<Gank> list) {
        mAdapter.refillItems(list);
    }

    @Override
    public void appendData(List<Gank> list) {
        mAdapter.appendItems(list);
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
        Snackbar.make(mRecyclerView, R.string.tip_no_more_load, Snackbar.LENGTH_LONG).show();
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

    private void showLoading() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showLoading();
        }
    }

    @Override
    public void shortToast(String string) {

    }
}
