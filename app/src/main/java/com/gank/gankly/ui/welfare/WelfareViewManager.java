package com.gank.gankly.ui.welfare;

import android.ly.business.domain.Gank;
import android.view.View;

import com.gank.gankly.ui.base.adapter.BaseViewManager;
import com.gank.gankly.ui.base.adapter.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2018-09-26
 */
public class WelfareViewManager extends BaseViewManager {
    private List<Gank> list = new ArrayList<>();
    private ItemClickListener itemClickListener;

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
            imageViewModel.setListener(itemClickListener);
            viewModels.add(imageViewModel);
        }
        return viewModels;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItem(View view, int position);
    }
}
