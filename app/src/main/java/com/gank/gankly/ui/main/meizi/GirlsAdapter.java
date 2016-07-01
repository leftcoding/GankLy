package com.gank.gankly.ui.main.meizi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.gank.gankly.ui.base.LazyFragment;
import com.socks.library.KLog;

import java.util.List;

/**
 * Create by LingYan on 2016-07-01
 */
public class GirlsAdapter extends FragmentStatePagerAdapter {
    private List<LazyFragment> mFragments;
    private List<String> titles;

    public GirlsAdapter(FragmentManager fm, List<LazyFragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        KLog.d("position:" + position);
        switch (position) {
            case 0:
                return GiftFragment.getInstance();
            case 1:
                return DailyMeiziFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
