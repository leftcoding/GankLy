package com.gank.gankly.ui.main.discovered;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.RxBus.ChangeThemeEvent.ThemeEvent;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.ui.base.BaseSwipeRefreshFragment;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.jiandan.JiandanFragment;
import com.gank.gankly.ui.main.HomeActivity;
import com.gank.gankly.ui.main.video.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * 美しい妹
 * Create by LingYan on 2016-07-01
 * Email:137387869@qq.com
 */
public class DiscoveredFragment extends BaseSwipeRefreshFragment implements ViewPager.OnPageChangeListener {
    private static final String TYPE_VIDEO = "视频";
    private static final String TYPE_JIANDAN = "新鲜事";

    @BindView(R.id.discovered_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.discovered_view_pager)
    ViewPager mViewPager;

    private HomeActivity mActivity;
    private List<String> mTitles;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovered;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindListener() {
        RxBus.getInstance().toSubscription(ThemeEvent.class, new Action1<ThemeEvent>() {
                    @Override
                    public void call(ThemeEvent event) {
                        refreshUi();
                    }
                }
        );
    }

    @Override
    protected void initValues() {
        List<LazyFragment> mList = new ArrayList<>();
        mList.add(new VideoFragment());
        mList.add(new JiandanFragment());

        mTitles = new ArrayList<>();
        mTitles.add(TYPE_VIDEO);
        mTitles.add(TYPE_JIANDAN);

        DiscoveredAdapter mPagerAdapter = new DiscoveredAdapter(mActivity.getSupportFragmentManager(), mList,
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
        mActivity = (HomeActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initPresenter() {

    }
}