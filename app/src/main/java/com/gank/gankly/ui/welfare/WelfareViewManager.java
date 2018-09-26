package com.gank.gankly.ui.welfare;

import android.ly.business.domain.Gank;

import com.gank.gankly.ui.base.adapter.BaseViewManager;
import com.gank.gankly.ui.base.adapter.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2018-09-26
 */
public class WelfareViewManager extends BaseViewManager {
    private List<Gank> list = new ArrayList<>();

    public WelfareViewManager() {

    }

    public void setData(int page, List<Gank> _list) {
        if (page == 1) {
            list.clear();
        }
        list.addAll(_list);
    }

    @Override
    public List<ViewModel> getModels() {
        List<ViewModel> viewModels = new ArrayList<>();
        for (Gank gank : list) {
            ImageViewModel imageViewModel = new ImageViewModel();
            imageViewModel.setData(gank);
            viewModels.add(imageViewModel);
        }
        return viewModels;
    }
}
