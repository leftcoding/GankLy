package com.gank.gankly.ui.main.meizi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.LazyFragment;

/**
 * Create by LingYan on 2016-07-01
 */
public class DailyMeiziFragment extends LazyFragment {
    public DailyMeiziFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initPresenter() {

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
        return R.layout.view_empty;
    }

    public static DailyMeiziFragment newInstance() {
        Bundle args = new Bundle();
        DailyMeiziFragment fragment = new DailyMeiziFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
