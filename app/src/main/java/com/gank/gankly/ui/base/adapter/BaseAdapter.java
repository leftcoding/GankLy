package com.gank.gankly.ui.base.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2018-09-25
 */

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final int DEFAULT_SPAN_COUNT = 1;
    private List<ViewModel> viewModels = new ArrayList<>();

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewModel<VH> viewModel = getViewModel(viewType);
        return viewModel.createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        ViewModel<VH> viewModel = getViewModelForPosition(position);
        viewModel.bindView(holder);
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewModels.get(position).getViewType();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setViewManager(ViewManager viewManager) {
        viewManager.setAdapter(this);
    }

    void setViewModels(List<ViewModel> viewModels) {
        this.viewModels = viewModels;
    }

    private ViewModel<VH> getViewModelForPosition(int position) {
        return viewModels.get(position);
    }

    private ViewModel<VH> getViewModel(int viewType) {
        for (ViewModel viewModel : viewModels) {
            if (viewModel.getViewType() == viewType) {
                return viewModel;
            }
        }
        throw new RuntimeException("can't found viewType");
    }

    public final GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            try {
                return viewModels.get(position).getSpanSize(DEFAULT_SPAN_COUNT, position);
            } catch (Exception e) {
                return DEFAULT_SPAN_COUNT;
            }
        }
    };
}
