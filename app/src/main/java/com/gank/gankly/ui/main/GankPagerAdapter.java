package com.gank.gankly.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.ui.main.meizi.MeiZiFragment;

import java.util.List;

public class GankPagerAdapter extends FragmentStatePagerAdapter {
    private List<LazyFragment> mFragments;
    private List<String> titles;

    public GankPagerAdapter(FragmentManager fm, List<LazyFragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return IosFragment.newInstance();
            case 2:
                return MeiZiFragment.newInstance();
            default:
                return AndroidFragment.newInstance();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
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