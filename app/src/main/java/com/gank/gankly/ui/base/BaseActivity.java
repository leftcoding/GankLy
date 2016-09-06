package com.gank.gankly.ui.base;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.presenter.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Create by LingYan on 2016-04-05
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    private long mLastTime;
    private Fragment mContent = null;
    protected P mPresenter;
    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        initPresenter();
        mUnbinder = ButterKnife.bind(this);
        initValues();
        initViews();
        bindListener();
    }

    public void add(Fragment fragment) {
        addFragment(fragment, null, "", R.id.main_frame_layout, false);
    }

    public void replace(Fragment fragment) {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        mFragmentTransaction.replace(R.id.main_frame_layout, fragment).commit();
    }

    public void addHideFragment(Fragment from, Fragment to) {
        addHideFragment(from, to, R.id.main_frame_layout, null, "", false);
    }

    public void addHideFragment(Fragment from, Fragment to, int contentAreaId,
                                Bundle bundle, String tag, boolean isAnimation) {
        if (isOpenMore()) {
            return;
        }
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (mContent != to) {
            mContent = to;
//            if (isAnimation) {
//                mFragmentTransaction.setCustomAnimations(R.anim.anim_enter,
//                        R.anim.anim_exit, R.anim.back_enter, R.anim.back_exit);
//            }
            if (bundle != null) {
                to.setArguments(bundle);
            }

            if (!TextUtils.isEmpty(tag)) {
                mFragmentTransaction.addToBackStack(tag);
            }
            if (!to.isAdded()) {
                mFragmentTransaction.hide(from).add(contentAreaId, to)
                        .commitAllowingStateLoss();
            } else {
                mFragmentTransaction.hide(from).show(to)
                        .commitAllowingStateLoss();
            }
        }
    }

    private void addFragment(Fragment fragment, Bundle bundle, String tag,
                             int contentId, boolean isAnimation) {
        if (isOpenMore() || fragment == null) {
            return;
        }
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
//        if (isAnimation) {
//            mFragmentTransaction.setCustomAnimations(R.anim.in_from_right,
//                    R.anim.out_to_left, R.anim.in_from_left,
//                    R.anim.out_to_right);
//        }
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        mFragmentTransaction.add(contentId, fragment);

        if (!TextUtils.isEmpty(tag)) {
            mFragmentTransaction.addToBackStack(tag);
        }
        mFragmentTransaction.commitAllowingStateLoss();
    }

    protected abstract int getContentId();

    protected abstract void initViews();

    protected abstract void bindListener();

    protected abstract void initValues();

    protected void initPresenter() {
    }

    protected void initTheme() {
        if (App.isNight()) {
            setTheme(R.style.AppTheme_Night);
        } else {
            setTheme(R.style.AppTheme_Day);
        }
    }

    public void popBackStack() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void gotoActivity(Class<? extends Activity> cls, boolean isFinish) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }

    private boolean isOpenMore() {
        if (System.currentTimeMillis() - mLastTime <= 100) {
            mLastTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }
}
