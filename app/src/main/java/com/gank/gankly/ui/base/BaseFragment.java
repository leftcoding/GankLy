package com.gank.gankly.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Create by LingYan on 2016-04-05
 * Email:137387869@qq.com
 */
public abstract class BaseFragment extends Fragment {
    protected Unbinder unBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unBinder = ButterKnife.bind(this, view);
        initValues();
        initViews();
        bindLister();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeThemes();
    }
    protected abstract int getLayoutId();

    protected abstract void initValues();

    protected abstract void initViews();

    protected abstract void bindLister();

    public void changeThemes() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unBinder != null) {
            unBinder.unbind();
            unBinder = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
