package com.gank.gankly.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.StateSet;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.RxBus.RxBus_;
import com.gank.gankly.RxBus.Theme.ThemeEvent;
import com.gank.gankly.ui.base.BaseActivity;
import com.gank.gankly.utils.AppUtils;
import com.gank.gankly.utils.ToastUtils;

import butterknife.BindView;

/**
 * Kotlin
 * Create by LingYan on 2016-6-13
 * Email:137387869@qq.com
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    //    @BindView(R.id.main_navigation)
//    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private long mKeyDownTime;
    private Fragment mCurFragment;

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        changeNavigationView();

        RxBus_.getInstance().toObservable(ThemeEvent.class)
                .subscribe(themeEvent -> {
                    changeNavigationView();
                });
    }

    @Override
    protected void bindListener() {
//        mNavigationView.getMenu().getItem(0).setChecked(true);
//        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initValues() {
        mCurFragment = MainFragment.getInstance();
        addMainFragment(mCurFragment);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();

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
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
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

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
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

    private void changeNavigationView() {
        int textColorJava = getThemeAttrColor(MainActivity.this, R.attr.navigationBackground);
//        mNavigationView.setBackgroundColor(textColorJava);
//        mNavigationView.setItemTextColor(createColorStateList(MainActivity.this));
//        mNavigationView.setItemIconTintList(getSwitchThumbColorStateList());
    }

    @ColorInt
    private static int getThemeAttrColor(Context context, @AttrRes int colorAttr) {
        TypedArray array = context.obtainStyledAttributes(null, new int[]{colorAttr});
        try {
            return array.getColor(0, 0);
        } finally {
            array.recycle();
        }
    }

    private ColorStateList getSwitchThumbColorStateList() {
        int mSelectColor;
        int unSelectColor;

        if (App.isNight()) {
            mSelectColor = R.color.switch_thumb_disabled_dark;
            unSelectColor = R.color.navigation_item_icon;
        } else {
            mSelectColor = R.color.colorAccent;
            unSelectColor = R.color.gray;
        }

        final int[][] states = new int[3][];
        final int[] colors = new int[3];

        // Disabled state
        states[0] = new int[]{-android.R.attr.state_enabled};
        colors[0] = (Color.DKGRAY);

        // Checked state
        states[1] = new int[]{android.R.attr.state_checked};

        colors[1] = App.getAppColor(mSelectColor);

        // Unchecked enabled state state
        states[2] = new int[0];

        colors[2] = App.getAppColor(unSelectColor);

        return new ColorStateList(states, colors);
    }

    private static ColorStateList createColorStateList(Context context) {
        return new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled}, // Disabled state.
                        StateSet.WILD_CARD,                       // Enabled state.
                },
                new int[]{
                        getThemeAttrColor(context, R.attr.textPrimaryColor),  // Disabled state.
                        getThemeAttrColor(context, R.attr.textPrimaryColor), // Enabled state.
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        Fragment fragmentTo = null;
        switch (menuItem.getItemId()) {
//            case R.id.navigation_home:
//                fragmentTo = MainFragment.getInstance();
//                break;
//            case R.id.navigation_collect:
//                fragmentTo = new CollectFragment();
//                break;
//            case R.id.navigation_video:
//                fragmentTo = new VideoFragment();
//                break;
//            case R.id.navigation_about:
//                fragmentTo = new AboutFragment();
//                break;
//            case R.id.navigation_gift:
//                fragmentTo = new GirlsFragment();
//                break;
//            case R.id.navigation_settings:
//                fragmentTo = SettingFragment.getInstance();
//                break;
//            case R.id.navigation_jiandan:
//                menuItem.setChecked(false);
//                Intent intent = new Intent(MainActivity.this, JiandanActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                return false; // no checked
//                        break;
            default:
                break;
        }
        if (fragmentTo != null) {
            switchFragment(fragmentTo);
        }
        return true;
    }

    private void switchFragment(Fragment fragment) {
        if (!fragment.getClass().getName().equals(mCurFragment.getClass().getName())) {
            addHideFragment(mCurFragment, fragment);
            mCurFragment = fragment;
        }
    }
}
