package com.gank.gankly.ui.discovered.jiandan;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.bean.JianDanBean;
import com.gank.gankly.config.Constants;
import com.gank.gankly.listener.ItemClick;
import com.gank.gankly.ui.MainActivity;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.web.JiandanWebActivity;
import com.gank.gankly.utils.StyleUtils;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.gank.gankly.widget.MyDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * 新鲜事
 * Create by LingYan on 2016-11-18
 */

public class JiandanFragment extends LazyFragment implements JiandanContract.View, ItemClick {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private MainActivity mActivity;
    private JiandanAdapter mAdapter;
    private JiandanContract.Presenter mPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new JiandanAdapter();
        mAdapter.setListener(this);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setAdapter(mAdapter);
        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.getRecyclerView().setHasFixedSize(true);
        mSwipeRefreshLayout.getRecyclerView().addItemDecoration(new MyDecoration(mActivity, LinearLayoutManager.HORIZONTAL));
//        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
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
//        mPresenter = new JiandanPresenter(JiandanDataSource.getInstance(), this);
    }

    @Override
    public void onLazyActivityCreate() {

    }

    protected void callBackRefreshUi() {
        Resources.Theme theme = mActivity.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.data;
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, typedValue, true);
        int textColor = typedValue.data;
        theme.resolveAttribute(R.attr.textSecondaryColor, typedValue, true);
        int authorColor = typedValue.data;
        theme.resolveAttribute(R.attr.themeBackground, typedValue, true);
        int mainColor = typedValue.data;
        mRecyclerView.setBackgroundColor(mainColor);
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            View view = childView.findViewById(R.id.jiandan_ll);
            view.setBackgroundColor(background);
            TextView title = (TextView) childView.findViewById(R.id.jiandan_txt_title);
            TextView author = (TextView) childView.findViewById(R.id.jiandan_txt_author);
            title.setTextColor(textColor);
            author.setTextColor(authorColor);
        }

        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    public void refillData(List<JianDanBean> list) {
        mAdapter.updateItem(list);
    }

    @Override
    public void appendMoreDate(List<JianDanBean> list) {
        mAdapter.appendItem(list);
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
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
    public void onClick(int position, Object object) {
        JianDanBean bean = (JianDanBean) object;
        Bundle bundle = new Bundle();
        bundle.putString(JiandanWebActivity.TITLE, bean.getTitle());
        bundle.putString(JiandanWebActivity.URL, bean.getUrl());
        bundle.putString(JiandanWebActivity.TYPE, Constants.JIANDAN);
        bundle.putString(JiandanWebActivity.AUTHOR, bean.getType());
        JiandanWebActivity.startWebActivity(mActivity, bundle);
    }

    @Override
    public void shortToast(String string) {

    }
}
