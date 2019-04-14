package com.gank.gankly.ui.girls;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.config.Constants;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.base.fragment.SupportFragment;
import com.gank.gankly.ui.cure.CureFragment;
import com.gank.gankly.ui.pure.PureFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * 美しい妹
 * Create by LingYan on 2016-07-01
 */
public class GirlsFragment extends SupportFragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.girl_tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.girl_view_pager)
    ViewPager mViewPager;

    private GirlsAdapter mPagerAdapter;
    private Disposable mDisposable;

    private List<String> mTitles;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girls;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<LazyFragment> mList = new ArrayList<>();
        mList.add(new PureFragment());
        mList.add(new CureFragment());

        mTitles = new ArrayList<>();
        mTitles.add(Constants.QINGCHUN);
        mTitles.add(Constants.CURE);

        mPagerAdapter = new GirlsAdapter(getChildFragmentManager(), mList, mTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);

        initTabLayout();
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
        mTabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.white));
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
}