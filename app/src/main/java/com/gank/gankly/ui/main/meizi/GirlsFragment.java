package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.RxBus.ChangeThemeEvent.ThemeEvent;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.config.Constants;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * 美しい妹
 * Create by LingYan on 2016-07-01
 * Email:137387869@qq.com
 */
public class GirlsFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.girl_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.girl_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.girl_view_pager)
    ViewPager mViewPager;

    private MainActivity mActivity;
    private List<String> mTitles;
    private static GirlsFragment sMainFragment;

    public static GirlsFragment getInstance() {
        if (sMainFragment == null) {
            sMainFragment = new GirlsFragment();
        }
        return sMainFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.navigation_gift);
        mActivity.setSupportActionBar(mToolbar);

        ActionBar ab = mActivity.getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_home_navigation);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void bindLister() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.openDrawer();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girls;
    }

    @Override
    protected void initValues() {
        List<LazyFragment> mList = new ArrayList<>();
        mList.add(GiftFragment.getInstance());
        mList.add(DailyMeiziFragment.getInstance());

        mTitles = new ArrayList<>();
        mTitles.add(Constants.QINGCHUN);
        mTitles.add(Constants.DAILY_GIRL);

        GirlsAdapter mPagerAdapter = new GirlsAdapter(mActivity.getSupportFragmentManager(), mList,
                mTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);

        initTabLayout();
    }

    private void initTabLayout() {
        for (int i = 0; i < mTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(i)));
        }

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.setBackgroundColor(App.getAppColor(R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorColor(App.getAppColor(R.color.white));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void changeTheme() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int background = typedValue.data;
        mTabLayout.setBackgroundColor(background);
        mToolbar.setBackgroundColor(background);

    }

    @Override
    public void onResume() {
        super.onResume();

        RxBus.getInstance().toSubscription(ThemeEvent.class, new Action1<ThemeEvent>() {
                    @Override
                    public void call(ThemeEvent event) {
                        changeTheme();
                    }
                }
        );
    }
}