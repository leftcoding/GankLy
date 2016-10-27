package com.gank.gankly.mvp.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Create by LingYan on 2016-04-05
 * Email:137387869@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final int CONTENT_ID = R.id.main_frame_layout;

    private long mLastTime;
    private Fragment mContent = null;
    protected Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        initPresenter();
        mUnBinder = ButterKnife.bind(this);
        initValues();
        initViews();
        bindListener();
        changeThemes();
    }

    public void changeThemes() {

    }

    public void addMainFragment(Fragment fragment) {
        addFragment(fragment, null, "", CONTENT_ID);
    }

    public void replace(Fragment fragment) {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        mFragmentTransaction.replace(CONTENT_ID, fragment).commit();
    }

    public void addHideFragment(Fragment from, Fragment to) {
        addHideFragment(from, to, CONTENT_ID, null, "", false);
    }

    public void addAnimFragment(Fragment from, Fragment to, boolean isAnim) {
        addHideFragment(from, to, CONTENT_ID, null, "", isAnim);
    }

    public void addAnimFragment(Fragment from, Fragment to, String tag, boolean isAnim) {
        addHideFragment(from, to, CONTENT_ID, null, tag, isAnim);
    }

    public void addHideFragment(Fragment from, Fragment to, int contentAreaId,
                                Bundle bundle, String tag, boolean isAnim) {
        if (isOpenMore()) {
            return;
        }
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (mContent != to) {
            mContent = to;
            if (bundle != null) {
                to.setArguments(bundle);
            }

            if (isAnim) {
                mFragmentTransaction.setCustomAnimations(R.anim.alpha_in, R.anim.alpha_out);
            }

            if (!TextUtils.isEmpty(tag)) {
                mFragmentTransaction.addToBackStack(tag);
            }

            if (!to.isAdded()) {
                mFragmentTransaction
                        .hide(from)
                        .add(contentAreaId, to, tag)
                        .commitAllowingStateLoss();
            } else {
                mFragmentTransaction
                        .hide(from)
                        .show(to)
                        .commitAllowingStateLoss();
            }
        }
    }

    public void addToBackFragment(Fragment fragment, @Nullable String tag) {
        addFragment(fragment, null, tag, CONTENT_ID);
    }

    private void addToBackFragment(Fragment fragment, Bundle bundle, @Nullable String tag) {
        addFragment(fragment, bundle, tag, CONTENT_ID);
    }


    private void addFragment(Fragment fragment, Bundle bundle, String tag, @Nullable int contentId) {
        if (fragment == null) {
            throw new RuntimeException(new NullPointerException("fragment can't be null"));
        }

        if (isOpenMore()) {
            return;
        }
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        if (!TextUtils.isEmpty(tag)) {
            mFragmentTransaction.addToBackStack(tag);
        }

        if (!fragment.isAdded()) {
            mFragmentTransaction.add(contentId, fragment);
        }
        mFragmentTransaction.commitAllowingStateLoss();
    }

    protected abstract void initPresenter();

    protected abstract int getContentId();

    protected abstract void initValues();

    protected abstract void initViews();

    protected abstract void bindListener();

    protected void initTheme() {
        if (App.isNight()) {
            setTheme(R.style.AppTheme_Night);
        } else {
            setTheme(R.style.AppTheme_Day);
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

    public void showSnackbar(View view, int rexText, int resColor) {
        Snackbar snackbar = Snackbar
                .make(view, rexText, Snackbar.LENGTH_LONG);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(resColor);
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
