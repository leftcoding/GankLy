package com.gank.gankly.ui;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.TypedValue;
import android.view.KeyEvent;

import com.gank.gankly.R;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.rxjava.theme.ThemeEvent;
import com.gank.gankly.ui.base.activity.BaseActivity;
import com.gank.gankly.ui.discovered.DiscoveredFragment;
import com.gank.gankly.ui.girls.GirlsFragment;
import com.gank.gankly.ui.main.IndexFragment;
import com.gank.gankly.ui.mine.MineFragment;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.ToastUtils;
import com.gank.gankly.utils.permission.PermissionUtils;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Create by LingYan on 2016-6-13
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isRestore = savedInstanceState.getBoolean("isRestore");
            mIndex = savedInstanceState.getInt("index");
        }
        super.onCreate(savedInstanceState);

        PermissionUtils.requestAllPermissions(this);

        if (mFragmentList == null) {
            mFragmentList = getFragmentList();
        }
        mBottomBar.setDefaultTabPosition(0);

        changeBottomBar();

        RxBus_.getInstance().toObservable(ThemeEvent.class)
                .subscribe(themeEvent -> changeBottomBar());

        mBottomBar.setOnTabSelectListener(tabId -> {
            mIndex = getFragmentIndex(tabId);
            openFragment(mIndex);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private List<Fragment> getFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new IndexFragment());
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
            if (mIndex != 0) {
                mBottomBar.selectTabAtPosition(0);
                return false;
            } else if ((System.currentTimeMillis() - mKeyDownTime) > 2000) {
                mKeyDownTime = System.currentTimeMillis();
                ToastUtils.shortBottom(getBaseContext(), R.string.app_again_out);
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isRestore", true);
        outState.putInt("index", mIndex);
        super.onSaveInstanceState(outState);
    }
}
