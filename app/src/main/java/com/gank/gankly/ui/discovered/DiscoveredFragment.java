package com.gank.gankly.ui.discovered;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.gank.gankly.R;
import com.gank.gankly.butterknife.ButterKnifeFragment;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.rxjava.theme.ThemeEvent;
import com.gank.gankly.ui.MainActivity;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.discovered.jiandan.JiandanFragment;
import com.gank.gankly.ui.discovered.more.DiscoveredAdapter;
import com.gank.gankly.ui.discovered.more.DiscoveredMoreFragment;
import com.gank.gankly.ui.discovered.teamBlog.TeamBlogFragment;
import com.gank.gankly.ui.discovered.technology.TechnologyFragment;
import com.gank.gankly.ui.discovered.video.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * 发现
 * Create by LingYan on 2016-07-01
 */
public class DiscoveredFragment extends ButterKnifeFragment implements ViewPager.OnPageChangeListener {
    private static final String TYPE_VIDEO = "视频";
    private static final String TYPE_JIANDAN = "新鲜事";
    private static final String TYPE_THCHNOLOGY = "科技资讯";
    private static final String TYPE_TEAM_BLOG = "团队博客";
    private static final String TYPE_MORE = "更多";

    @BindView(R.id.discovered_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.discovered_view_pager)
    ViewPager mViewPager;

    private MainActivity mActivity;
    private Disposable mDisposable;

    private List<String> mTitles;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovered;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDisposable = RxBus_.getInstance().toObservable(ThemeEvent.class)
                .subscribe(themeEvent -> {
                    refreshUi();
                });

        List<LazyFragment> mList = new ArrayList<>();
        mList.add(new VideoFragment());
        mList.add(new JiandanFragment());
        mList.add(new TechnologyFragment());
        mList.add(new TeamBlogFragment());
        mList.add(new DiscoveredMoreFragment());

        mTitles = new ArrayList<>();
        mTitles.add(TYPE_VIDEO);
        mTitles.add(TYPE_JIANDAN);
        mTitles.add(TYPE_THCHNOLOGY);
        mTitles.add(TYPE_TEAM_BLOG);
        mTitles.add(TYPE_MORE);

        DiscoveredAdapter mPagerAdapter = new DiscoveredAdapter(getChildFragmentManager(), mList,
                mTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.addOnPageChangeListener(this);

        initTabLayout();
    }

    private void initTabLayout() {
        for (int i = 0; i < mTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(i)));
        }

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.white));
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}