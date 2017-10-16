package com.gank.gankly.ui.girls;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.rxjava.theme.ThemeEvent;
import com.gank.gankly.ui.base.fragment.LazyFragment;
import com.gank.gankly.ui.base.fragment.SupportFragment;
import com.gank.gankly.ui.girls.cure.CureFragment;
import com.gank.gankly.ui.girls.pure.PureFragment;
import com.gank.gankly.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * 美しい妹
 * Create by LingYan on 2016-07-01
 * Email:137387869@qq.com
 */
public class GirlsFragment extends SupportFragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.girl_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.girl_view_pager)
    ViewPager mViewPager;

    private MainActivity mActivity;
    private GirlsAdapter mPagerAdapter;
    private Disposable mDisposable;

    private List<String> mTitles;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girls;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<LazyFragment> mList = new ArrayList<>();
        mList.add(new PureFragment());
//        mList.add(new DailyMeiziFragment());
        mList.add(new CureFragment());

        mTitles = new ArrayList<>();
        mTitles.add(Constants.QINGCHUN);
        mTitles.add(Constants.CURE);

        mPagerAdapter = new GirlsAdapter(mActivity.getSupportFragmentManager(), mList, mTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);

        initTabLayout();

        mDisposable = RxBus_.getInstance().toObservable(ThemeEvent.class)
                .subscribe(themeEvent -> {
                    refreshUi();
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
}