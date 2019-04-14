package com.gank.gankly.ui.pure;

import android.content.Context;
import android.lectcoding.ui.adapter.ObserverAdapter;
import android.lectcoding.ui.logcat.Logcat;
import android.ly.business.domain.Gift;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.gank.gankly.R;
import com.gank.gankly.butterknife.BindViewHolder;
import com.gank.gankly.butterknife.ItemModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-04-25
 */
public class PureAdapter extends ObserverAdapter<PureAdapter.PureViewHolder> {
    private final List<Gift> gifts = new ArrayList<>();
    private ItemClickCallback callback;
    private Context mContext;
    private final List<ItemModel> items = new ArrayList<>();

    PureAdapter(Context context) {
        setHasStableIds(true);
        mContext = context;
    }

    private final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
        }
    };

    @Override
    public PureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PureViewHolder holder;
        switch (viewType) {
            case GiftViewHolder.LAYOUT:
                holder = new GiftViewHolder(parent);
                break;
            default:
                holder = null;
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(PureViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case GiftViewHolder.LAYOUT:
                ((GiftViewHolder) holder).bindHolder((GiftViewItem) items.get(position), callback);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (!items.isEmpty()) {
            return items.get(position).getViewType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onViewRecycled(PureViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(mContext).clearMemory();
    }

    void refillItems(List<Gift> getResults) {
        gifts.clear();
        items.clear();
        appendItems(getResults);
    }

    public void appendItems(List<Gift> getResults) {
        int startIndex = gifts.size();
        Logcat.d("");
        gifts.addAll(getResults);
        changeItem(startIndex);
    }

    private void changeItem(int startIndex) {
        if (!gifts.isEmpty()) {
            for (int i = startIndex, size = gifts.size(); i < size; i++) {
                final Gift gift = gifts.get(i);
                if (gift != null) {
                    items.add(new GiftViewItem(gift));
                }
            }
        }
    }

    public void clear() {
        if (gifts != null) {
            gifts.clear();
        }
    }

    public void setOnItemClickListener(ItemClickCallback itemClickCallback) {
        this.callback = itemClickCallback;
    }

    @Override
    public void destroy() {
        super.destroy();
        items.clear();
        gifts.clear();
    }

    @Override
    protected RecyclerView.AdapterDataObserver getObserver() {
        return observer;
    }

    static class GiftViewHolder extends PureViewHolder<GiftViewItem> {
        static final int LAYOUT = R.layout.adapter_gift;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.goods_background)
        ImageView goodsBackground;

        @BindView(R.id.author)
        TextView author;

        GiftViewHolder(ViewGroup parent) {
            super(parent, LAYOUT);
        }

        @Override
        void bindHolder(GiftViewItem item, ItemClickCallback callback) {
            super.bindHolder(item, callback);
            final Gift gift = item.gift;
            final Context context = itemView.getContext();
            title.setText(gift.title);
            author.setText(gift.time);

            final GlideUrl glideUrl = new GlideUrl(gift.imgUrl, new LazyHeaders.Builder()
                    .addHeader("referer", "http://www.mzitu.com/mm/")
                    .build());

            Glide.with(context)
                    .asBitmap()
                    .load(glideUrl)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(goodsBackground);

            itemView.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onItemClick(gift);
                }
            });
        }
    }

    static class GiftViewItem extends ItemModel {
        final Gift gift;

        GiftViewItem(Gift gift) {
            this.gift = gift;
        }

        @Override
        public int getViewType() {
            return GiftViewHolder.LAYOUT;
        }
    }

    abstract static class PureViewHolder<II extends ItemModel> extends BindViewHolder<II> {

        PureViewHolder(ViewGroup parent, int layoutRes) {
            super(parent, layoutRes);
        }

        @Override
        public void bindHolder(II item) {

        }

        void bindHolder(II item, ItemClickCallback callback) {

        }
    }

    interface ItemClickCallback {
        void onItemClick(Gift gift);
    }
}
