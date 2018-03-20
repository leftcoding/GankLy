package com.gank.gankly.ui.ios;

import android.content.Context;
import android.lectcoding.ui.adapter.BaseAdapter;
import android.lectcoding.ui.adapter.BasicItem;
import android.lectcoding.ui.adapter.Item;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.butterknife.ButterKnifeHolder;
import com.gank.gankly.listener.ItemCallBack;
import com.gank.gankly.utils.DateUtils;
import com.leftcoding.network.domain.ResultsBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-04-25
 */
class IosAdapter extends BaseAdapter<ButterKnifeHolder> {
    private final List<ResultsBean> resultsBeans = new ArrayList<>();
    private final List<Item> itemList = new ArrayList<>();

    private Context context;

    private ItemCallBack itemCallBack;

    IosAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);

        registerAdapterDataObserver(mObserver);
    }

    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            itemList.clear();
            if (!resultsBeans.isEmpty()) {
                for (ResultsBean resultsBean : resultsBeans) {
                    itemList.add(new TextItem(resultsBean));
                }
            }
        }
    };

    @Override
    public ButterKnifeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ButterKnifeHolder defaultHolder;
        switch (viewType) {
            case TextHolder.LAYOUT:
                defaultHolder = new TextHolder(parent, itemCallBack);
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
            case TextHolder.LAYOUT:
                ((TextHolder) holder).bindItem((TextItem) item);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
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

    void fillItems(List<ResultsBean> results) {
        resultsBeans.clear();
        appendItems(results);
    }

    public void appendItems(List<ResultsBean> results) {
        resultsBeans.addAll(results);
        this.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void destroy() {
        unregisterAdapterDataObserver(mObserver);
    }

    public void setOnItemClickListener(ItemCallBack itemCallBack) {
        this.itemCallBack = itemCallBack;
    }

    static class TextHolder extends ButterKnifeHolder<TextItem> {
        static final int LAYOUT = R.layout.adapter_ios;
        @BindView(R.id.author_name)
        TextView authorName;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.title)
        TextView title;

        ItemCallBack itemCallBack;

        TextHolder(ViewGroup parent, ItemCallBack itemCallBack) {
            super(parent, LAYOUT);
            this.itemCallBack = itemCallBack;
        }

        @Override
        public void bindItem(TextItem item) {
            final ResultsBean resultsBean = item.getResultsBean();

            time.setText(item.getTime());
            title.setText(resultsBean.desc);
            authorName.setText(resultsBean.getWho());

            itemView.setOnClickListener(v -> {
                if (itemCallBack != null) {
                    itemCallBack.onClick(v, getAdapterPosition(), resultsBean);
                }
            });
        }
    }

    class TextItem extends BasicItem {
        private ResultsBean resultsBean;

        TextItem(ResultsBean resultsBean) {
            this.resultsBean = resultsBean;
        }

        ResultsBean getResultsBean() {
            return resultsBean;
        }

        public String getTime() {
            Date date = DateUtils.formatToDate(resultsBean.publishedAt);
            return DateUtils.formatString(date, DateUtils.MM_DD);
        }

        @Override
        public int getViewType() {
            return TextHolder.LAYOUT;
        }
    }
}
