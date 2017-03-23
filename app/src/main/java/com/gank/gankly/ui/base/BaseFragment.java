package com.gank.gankly.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gank.gankly.App;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Create by LingYan on 2016-04-05
 * Email:137387869@qq.com
 */
public abstract class BaseFragment extends Fragment {
    protected Unbinder unBinder;
    private View mView;
    private App mApp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mApp = new App();
        unBinder = ButterKnife.bind(this, view);
        initValues();
        initViews();
        bindListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeThemes();
    }

    protected abstract int getLayoutId();

    protected abstract void initValues();

    protected abstract void initViews();

    protected abstract void bindListener();

    public void changeThemes() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((App) getActivity().getApplication()).getRefWatcher().watch(this);

        if (unBinder != null) {
            unBinder.unbind();
            unBinder = null;
        }
    }
}
