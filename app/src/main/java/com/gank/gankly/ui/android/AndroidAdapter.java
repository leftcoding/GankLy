package com.gank.gankly.ui.android;

import android.content.Context;
import android.lectcoding.ui.adapter.BaseAdapter;
import android.lectcoding.ui.adapter.BasicItem;
import android.lectcoding.ui.adapter.Item;
import android.ly.business.domain.Gank;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.butterknife.ButterKnifeHolder;
import com.gank.gankly.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;


/**
 * Create by LingYan on 2016-04-25
 */
class AndroidAdapter extends BaseAdapter<ButterKnifeHolder> {
    private ItemCallback itemCallback;
    private Context context;

    private final List<Gank> resultsBeans = new ArrayList<>();
    private final List<Item> itemList = new ArrayList<>();

    AndroidAdapter(@NonNull Context context) {
        this.context = context;
        setHasStableIds(true);
        registerAdapterDataObserver(dataObserver);
    }

    private final RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.clear();
            if (!resultsBeans.isEmpty()) {
                for (Gank resultsBean : resultsBeans) {
                    if (resultsBean != null) {
                        itemList.add(new TextItem(resultsBean));
                    }
                }
            }
        }
    };

    @Override
    public int getItemViewType(int position) {
        if (!itemList.isEmpty()) {
            return itemList.get(position).getViewType();
        }
        return RecyclerView.INVALID_TYPE;
    }

    @Override
    public ButterKnifeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ButterKnifeHolder defaultHolder;
        switch (viewType) {
            case NormalViewHolder.LAYOUT:
                defaultHolder = new NormalViewHolder(parent, itemCallback);
                break;
            default:
                defaultHolder = null;
                break;
        }
        return defaultHolder;
    }

    @Override
    public void onBindViewHolder(ButterKnifeHolder holder, int position) {
        final Item item = itemList.get(position);
        switch (holder.getItemViewType()) {
            case NormalViewHolder.LAYOUT:
                ((NormalViewHolder) holder).bindHolder((TextItem) item);
                break;
        }
    }

    @Override
    public void onViewRecycled(ButterKnifeHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(context).clearMemory();
    }

    @Override
    public int getItemCount() {
        return resultsBeans.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void fillItems(List<Gank> results) {
        resultsBeans.clear();
        appendItems(results);
    }

    public void appendItems(List<Gank> results) {
        resultsBeans.addAll(results);
    }

    public void setOnItemClickListener(ItemCallback itemCallBack) {
        this.itemCallback = itemCallBack;
    }

    @Override
    public void destroy() {
        if (itemList != null) {
            itemList.clear();
        }

        if (resultsBeans != null) {
            resultsBeans.clear();
        }

        unregisterAdapterDataObserver(dataObserver);
    }

    private static class TextItem extends BasicItem {
        private Gank resultsBean;

        TextItem(Gank resultsBean) {
            this.resultsBean = resultsBean;
        }

        Gank getResultsBean() {
            return resultsBean;
        }

        public String getTime() {
            Date date = DateUtils.formatToDate(resultsBean.publishedAt);
            return DateUtils.formatString(date, DateUtils.MM_DD);
        }

        @Override
        public int getViewType() {
            return NormalViewHolder.LAYOUT;
        }
    }

    static class NormalViewHolder extends ButterKnifeHolder<TextItem> {
        static final int LAYOUT = R.layout.adapter_android;

        @BindView(R.id.author_name)
        TextView authorName;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.title)
        TextView title;

        private Gank resultsBean;

        NormalViewHolder(ViewGroup parent, final ItemCallback itemCallBack) {
            super(parent, R.layout.adapter_android);
            itemView.setOnClickListener(v -> {
                if (itemCallBack != null && resultsBean != null) {
                    itemCallBack.onItemClick(v, resultsBean);
                }
            });
        }

        @Override
        public void bindHolder(TextItem item) {
            final Gank resultsBean = item.getResultsBean();
            this.resultsBean = resultsBean;

            time.setText(item.getTime());
            title.setText(resultsBean.desc);
            authorName.setText(resultsBean.who);
        }
    }

    public interface ItemCallback {
        void onItemClick(View view, Gank gank);
    }
}
