package com.gank.gankly.ui.discovered.more;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.gank.gankly.ui.base.fragment.LazyFragment;

import java.util.List;

/**
 * Create by LingYan on 2016-07-01
 */
public class DiscoveredAdapter extends FragmentStatePagerAdapter {
    private List<LazyFragment> mFragments;
    private List<String> titles;

    public DiscoveredAdapter(FragmentManager fm, List<LazyFragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
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
