package com.gank.gankly.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.gank.gankly.butterknife.BindViewHolder;
import com.gank.gankly.butterknife.ItemModel;
import com.gank.gankly.ui.base.DiffAdapter;
import com.gank.gankly.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-04-25
 */
class IosAdapter extends DiffAdapter<BindViewHolder> {
    private List<ItemModel> itemModels = new ArrayList<>();
    private List<Gank> curGank;

    private Context context;
    private ItemCallback itemCallBack;

    IosAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindViewHolder defaultHolder;
        switch (viewType) {
            case ViewType.NORMAL:
                defaultHolder = new NormalViewHolder(parent, itemCallBack);
                break;
            default:
                defaultHolder = null;
                break;
        }
        return defaultHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BindViewHolder holder, int position) {
        final ItemModel viewItem = itemModels.get(position);
        switch (viewItem.getViewType()) {
            case ViewType.NORMAL:
                ((NormalViewHolder) holder).bindHolder((TextViewModel) viewItem);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getViewType();
    }

    @Override
    public void onViewRecycled(@NonNull BindViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(context).clearMemory();
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    void setItems(@NonNull List<Gank> list) {
        curGank = list;
    }

    void clearItems() {
        itemModels.clear();
        curGank = null;
    }

    /**
     * 在主线程进行列表差异结果比较，不适合大型数据量
     */
    public void update() {
        final List<ItemModel> newModels = new ArrayList<>(itemModels);
        if (!ListUtils.isEmpty(curGank)) {
            for (Gank gank : curGank) {
                newModels.add(new TextViewModel(gank));
            }
        }

        updateAdapter(itemModels, newModels);
    }

    @Override
    public void destroy() {
        clearItems();
        itemCallBack = null;
    }

    public void setOnItemClickListener(ItemCallback itemCallBack) {
        this.itemCallBack = itemCallBack;
    }
}
