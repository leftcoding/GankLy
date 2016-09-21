package com.gank.gankly.ui.main;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.gank.gankly.R;
import com.gank.gankly.RxBus.ChangeThemeEvent.ThemeEvent;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.ui.main.meizi.GirlsFragment;
import com.gank.gankly.ui.main.mine.MineFragment;
import com.gank.gankly.ui.main.video.VideoFragment;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.ToastUtils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Create by LingYan on 2016-6-13
 * Email:137387869@qq.com
 */
public class HomeActivity extends BaseActivity {
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    private long mKeyDownTime;
    private Fragment mCurFragment;

    Integer[] mBottomBarTabs = {R.id.tab_home, R.id.tab_image, R.id.tab_more, R.id.tab_video};

    @Override
    protected int getContentId() {
        return R.layout.fragment_main_bottom_navigation;
    }

    @Override
    protected void initViews() {
        changeBottomBar();

        RxBus.getInstance().toSubscription(ThemeEvent.class, new Action1<ThemeEvent>() {
            @Override
            public void call(ThemeEvent themeEvent) {
                changeBottomBar();
            }
        });
    }

    @Override
    protected void bindListener() {
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment fragmentTo = null;
                String tag = "";
                switch (tabId) {
                    case R.id.tab_home:
                        fragmentTo = MainFragment.getInstance();
                        tag = "tab_home";
                        break;
                    case R.id.tab_video:
                        fragmentTo = VideoFragment.getInstance();
                        tag = "tab_video";
                        break;
                    case R.id.tab_image:
                        fragmentTo = GirlsFragment.getInstance();
                        tag = "tab_image";
                        break;
                    case R.id.tab_more:
                        fragmentTo = MineFragment.getInstance();
                        tag = "tab_more";
                        break;
                }

                if (mCurFragment == null) {
                    addMainFragment(fragmentTo);
                } else {
                    if (!mCurFragment.getClass().getName().equals(fragmentTo.getClass().getName())) {
                        addAnimFragment(mCurFragment, fragmentTo, tag, true);
                    }
                }
                mCurFragment = fragmentTo;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();

    }

    public void changeBottomBar() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int color = typedValue.resourceId;

        Observable.from(mBottomBarTabs).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mBottomBar.getTabWithId(integer).setBackgroundResource(color);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mKeyDownTime) > 2000) {
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
