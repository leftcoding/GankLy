package com.gank.gankly.ui.gallery;

import android.ly.business.domain.Gift;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {
    private List<Gift> gifts;

    GalleryPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
    }

    @Override
    public int getCount() {
        return gifts == null ? 0 : gifts.size();
    }

    @Override
    public Fragment getItem(int position) {
        return GalleryFragment.newInstance(gifts.get(position).imgUrl);
    }
}