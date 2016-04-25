package com.gank.gankly.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.gank.gankly.R;

import butterknife.ButterKnife;


/**
 * Create by LingYan on 2016-04-05
 */
public abstract class BaseActivity extends AppCompatActivity {
    private long mLastTime;
    private Fragment mContent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        ButterKnife.bind(this);
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
            if (!to.isAdded()) { // 先判断是否被add过
                mFragmentTransaction.hide(from).add(contentAreaId, to)
                        .commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                mFragmentTransaction.hide(from).show(to)
                        .commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
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
    }
}
