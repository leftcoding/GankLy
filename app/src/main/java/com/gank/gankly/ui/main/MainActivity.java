package com.gank.gankly.ui.main;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.RxBus.RxBus_;
import com.gank.gankly.RxBus.Theme.ThemeEvent;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.discovered.DiscoveredFragment;
import com.gank.gankly.ui.main.meizi.GirlsFragment;
import com.gank.gankly.ui.mine.MineFragment;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.utils.permission.PermissionUtils;
import com.roughike.bottombar.BottomBar;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 首页
 * Create by LingYan on 2016-6-13
 * Email:137387869@qq.com
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    private long mKeyDownTime;
    private Fragment mCurFragment;

    private Integer[] mBottomBarTabs = {R.id.tab_home, R.id.tab_image, R.id.tab_more, R.id.tab_news};
    private List<Fragment> mFragmentList;
    private int mIndex = 0;
    private boolean isRestore = false;

    @Override
    protected int getContentId() {
        return R.layout.fragment_main_bottom_navigation;
    }

    @Override
    protected void initViews() {
        PermissionUtils.requestAllPermissions(this);

        if (mFragmentList == null) {
            mFragmentList = getFragmentList();
        }
        mBottomBar.setDefaultTabPosition(0);

        changeBottomBar();

        RxBus_.getInstance().toObservable(ThemeEvent.class)
                .subscribe(themeEvent -> changeBottomBar());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void bindListener() {
        mBottomBar.setOnTabSelectListener(tabId -> {
            mIndex = getFragmentIndex(tabId);
            openFragment(mIndex);
        });
    }

    private int getFragmentIndex(int tabId) {
        int index;
        switch (tabId) {
            case R.id.tab_home:
                index = 0;
                break;
            case R.id.tab_news:
                index = 1;
                break;
            case R.id.tab_image:
                index = 2;
                break;
            case R.id.tab_more:
                index = 3;
                break;
            default:
                index = 0;
                break;
        }
        return index;
    }

    private void openFragment(int index) {
        Fragment fragmentTo = mFragmentList.get(index);
        if (mCurFragment == null) {
            if (!isRestore) {
                addMainFragment(fragmentTo);
            }
            mCurFragment = fragmentTo;
        } else {
            if (!mCurFragment.equals(fragmentTo)) {
                addAnimFragment(mCurFragment, fragmentTo, true);
                mCurFragment = fragmentTo;
            }
        }
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new DiscoveredFragment());
        fragments.add(new GirlsFragment());
        fragments.add(new MineFragment());
        return fragments;
    }


    public void changeBottomBar() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int color = typedValue.resourceId;

        Observable.fromArray(mBottomBarTabs)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> mBottomBar.getTabWithId(integer).setBackgroundResource(color));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIndex == 3) {
                mBottomBar.selectTabAtPosition(0);
                return false;
            } else if ((System.currentTimeMillis() - mKeyDownTime) > 2000) {
                mKeyDownTime = System.currentTimeMillis();
                ToastUtils.shortBottom(R.string.app_again_out);
                return false;
            } else {
                finish();
                AppUtils.killProcess();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus_.getInstance().removeAllStickyEvents();// 移除所有Sticky事件
    }

    @Override
    protected void onStart() {
        KLog.d("onStart");
        //防止底部导航栏会下移
        getWindow().
                getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        super.onStart();
    }

    @Override
    protected void onResume() {
        KLog.d("onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        KLog.d("onRestart");
        super.onRestart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        KLog.d("onCreate");
        if (savedInstanceState != null) {
            isRestore = savedInstanceState.getBoolean("isRestore");
            mIndex = savedInstanceState.getInt("index");
        }
        KLog.d("isRestore:" + isRestore + "，mIndex：" + mIndex);
        super.onCreate(savedInstanceState);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        KLog.d("onRestoreInstanceState");
//        if (savedInstanceState != null) {
//            isRestore = savedInstanceState.getBoolean("isRestore");
//        }
//        KLog.d("isRestore:" + isRestore);
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        KLog.d("onSaveInstanceState");
        outState.putBoolean("isRestore", true);
        outState.putInt("index", mIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        KLog.d("onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        KLog.d("onStop");
        super.onStop();
    }
}
