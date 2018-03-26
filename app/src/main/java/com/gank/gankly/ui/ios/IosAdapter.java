package com.gank.gankly.ui.ios;

import android.content.Context;
import android.lectcoding.ui.adapter.BaseAdapter;
import android.lectcoding.ui.adapter.BasicViewItem;
import android.lectcoding.ui.adapter.ViewItem;
import android.ly.business.domain.Gank;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.butterknife.BindViewHolder;
import com.gank.gankly.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-04-25
 */
class IosAdapter extends BaseAdapter<BindViewHolder> {
    private final List<Gank> resultsBeans = new ArrayList<>();
    private final List<ViewItem> viewItemList = new ArrayList<>();

    private Context context;

    private ItemCallback itemCallBack;

    IosAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);

        registerAdapterDataObserver(mObserver);
    }

    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            viewItemList.clear();
            if (!resultsBeans.isEmpty()) {
                for (Gank resultsBean : resultsBeans) {
                    viewItemList.add(new TextViewItem(resultsBean));
                }
            }
        }
    };

    @Override
    public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BindViewHolder defaultHolder;
        switch (viewType) {
            case NormalViewHolder.LAYOUT:
                defaultHolder = new NormalViewHolder(parent, itemCallBack);
                break;
            default:
                defaultHolder = null;
                break;
        }
        return defaultHolder;
    }

    @Override
    public void onBindViewHolder(BindViewHolder holder, int position) {
        final ViewItem viewItem = viewItemList.get(position);
        switch (holder.getItemViewType()) {
            case NormalViewHolder.LAYOUT:
                ((NormalViewHolder) holder).bindHolder((TextViewItem) viewItem);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return viewItemList.get(position).getViewType();
    }

    @Override
    public void onViewRecycled(BindViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(context).clearMemory();
    }

    @Override
    public int getItemCount() {
        return resultsBeans.size();
    }

    void fillItems(List<Gank> results) {
        resultsBeans.clear();
        appendItems(results);
    }

    public void appendItems(List<Gank> results) {
        resultsBeans.addAll(results);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void destroy() {
        unregisterAdapterDataObserver(mObserver);
    }

    public void setOnItemClickListener(ItemCallback itemCallBack) {
        this.itemCallBack = itemCallBack;
    }

    static class NormalViewHolder extends BindViewHolder<TextViewItem> {
        static final int LAYOUT = R.layout.adapter_ios;

        @BindView(R.id.author_name)
        TextView authorName;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.title)
        TextView title;

        ItemCallback itemCallBack;

        NormalViewHolder(ViewGroup parent, ItemCallback itemCallBack) {
            super(parent, LAYOUT);
            this.itemCallBack = itemCallBack;
        }

        @Override
        public void bindHolder(TextViewItem item) {
            final Gank resultsBean = item.getResultsBean();

            time.setText(item.getTime());
            title.setText(resultsBean.desc);
            authorName.setText(resultsBean.who);

            itemView.setOnClickListener(v -> {
                if (itemCallBack != null) {
                    itemCallBack.onItemClick(v, resultsBean);
                }
            });
        }
    }

    static class TextViewItem extends BasicViewItem {
        private Gank resultsBean;

        TextViewItem(Gank resultsBean) {
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

    public interface ItemCallback {
        void onItemClick(View view, Gank gank);
    }
}
