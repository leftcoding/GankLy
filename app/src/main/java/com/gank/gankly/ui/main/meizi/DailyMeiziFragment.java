package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gank.gankly.R;
import com.gank.gankly.bean.DailyMeiziBean;
import com.gank.gankly.presenter.RefreshPresenter;
import com.gank.gankly.presenter.impl.DailyMeiziPresenterImpl;
import com.gank.gankly.ui.base.BaseSwipeRefreshLayout;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.view.IDailyMeiziView;
import com.gank.gankly.widget.MultipleStatusView;

import java.util.List;

import butterknife.Bind;

/**
 * Create by LingYan on 2016-07-01
 */
public class DailyMeiziFragment extends LazyFragment<DailyMeiziPresenterImpl> implements IDailyMeiziView<DailyMeiziBean> {
    private RefreshPresenter mPresenter;
    private MainActivity mActivity;

    @Bind(R.id.loading_view)
    MultipleStatusView mMultipleStatusView;
    @Bind(R.id.meizi_swipe_refresh)
    BaseSwipeRefreshLayout mSwipeRefreshLayout;

    public DailyMeiziFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DailyMeiziPresenterImpl(mActivity, this);
        mPresenter.fetchNew();
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindLister() {

    }

    @Override
    protected void initDate() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    public static DailyMeiziFragment newInstance() {
        Bundle args = new Bundle();
        DailyMeiziFragment fragment = new DailyMeiziFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void refillDate(List list) {

    }
}
