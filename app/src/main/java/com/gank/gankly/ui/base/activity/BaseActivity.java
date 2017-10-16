package com.gank.gankly.ui.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.App;
import com.gank.gankly.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Create by LingYan on 2016-04-05
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final int contentAreaId = R.id.main_frame_layout;
    private long mLastTime;
    protected Unbinder mUnBinder;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        mUnBinder = ButterKnife.bind(this);
        initValues();
        initViews();
        bindListener();
        changeThemes();
        mFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
    }

    public void changeThemes() {

    }

    public void addMainFragment(Fragment fragment) {
        addFragment(fragment, null, "");
    }

    public void addHideFragment(Fragment from, Fragment to) {
        addHideFragment(from, to, contentAreaId, null, "", false);
    }

    public void addHideFragment(Fragment from, Fragment to, String Tag, int contentAreaId) {
        addHideFragment(from, to, contentAreaId, null, Tag, true);
    }

    public void addAnimFragment(Fragment from, Fragment to, boolean isAnim) {
        addHideFragment(from, to, contentAreaId, null, "", isAnim);
    }

    public void addAnimFragment(Fragment from, Fragment to, String tag, boolean isAnim) {
        addHideFragment(from, to, contentAreaId, null, tag, isAnim);
    }

    public void transitionHideFragment(Fragment from, Fragment to, int contentAreaId,
                                       Bundle bundle, String tag, Pair<View, String> transitionViews) {
        if (isOpenMore()) {
            return;
        }

        from = checkNull(from);
        to = checkNull(to);
        transitionViews = checkNull(transitionViews);

        Transition imageTransition = TransitionInflater.from(this).inflateTransition(R.transition.image_transfom);
        mFragmentTransaction.addSharedElement(transitionViews.first, transitionViews.second);
        to.setSharedElementEnterTransition(imageTransition);
        to.setSharedElementReturnTransition(imageTransition);

        if (bundle != null) {
            to.setArguments(bundle);
        }

        if (!TextUtils.isEmpty(tag)) {
            mFragmentTransaction.addToBackStack(tag);
        }

        mFragmentTransaction
                .hide(from)
                .add(contentAreaId, to, tag)
                .commitAllowingStateLoss();
    }

    public void addHideFragment(Fragment from, Fragment to, int contentAreaId,
                                Bundle bundle, String tag, boolean isAnim) {
        if (isOpenMore()) {
            return;
        }
        if (from != null && !from.equals(to) && to != null) {
            FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
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

    private Fragment checkNull(Fragment o) {
        if (o == null) {
            throw new NullPointerException("fragment is null");
        }
        return o;
    }

    private Pair<View, String> checkNull(Pair<View, String> o) {
        if (o == null) {
            throw new NullPointerException("Pair is null");
        }
        return o;
    }

    public void addToBackFragment(Fragment fragment, @Nullable String tag) {
        addFragment(fragment, null, tag);
    }

    private void addToBackFragment(Fragment fragment, Bundle bundle, @Nullable String tag) {
        addFragment(fragment, bundle, tag);
    }

    private void addFragment(Fragment fragment, Bundle bundle, String tag) {
        if (fragment == null) {
            throw new NullPointerException("fragment can't be null");
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
            mFragmentTransaction.add(contentAreaId, fragment);
        }
        mFragmentTransaction.commitAllowingStateLoss();
    }

    protected abstract int getContentId();

    protected abstract void initValues();

    protected abstract void initViews();

    protected abstract void bindListener();

    protected void initPresenter() {}

    protected void initTheme() {
        if (App.isNight()) {
            setTheme(R.style.AppTheme_Night);
        } else {
            setTheme(R.style.AppTheme_light);
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
        ((App) this.getApplication()).getRefWatcher().watch(this);
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            finishAfterTransition();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}
