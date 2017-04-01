package com.gank.gankly.ui.main.meizi;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.rxjava.theme.ThemeEvent;
import com.gank.gankly.config.Constants;
import com.gank.gankly.ui.base.BaseFragment;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.MainActivity;
import com.gank.gankly.ui.main.meizi.dailymeizi.DailyMeiziFragment;
import com.gank.gankly.ui.main.meizi.pure.PureFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 美しい妹
 * Create by LingYan on 2016-07-01
 * Email:137387869@qq.com
 */
public class GirlsFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.girl_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.girl_view_pager)
    ViewPager mViewPager;

    private MainActivity mActivity;
    private List<String> mTitles;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girls;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindListener() {
        RxBus_.getInstance().toObservable(ThemeEvent.class)
                .subscribe(themeEvent -> {
                    refreshUi();
                });
    }

    @Override
    protected void initValues() {
        List<LazyFragment> mList = new ArrayList<>();
        mList.add(new PureFragment());
        mList.add(new DailyMeiziFragment());

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
        mTabLayout.setSelectedTabIndicatorColor(App.getAppColor(R.color.white));
    }

    private void refreshUi() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mActivity.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int background = typedValue.data;
        mTabLayout.setBackgroundColor(background);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}