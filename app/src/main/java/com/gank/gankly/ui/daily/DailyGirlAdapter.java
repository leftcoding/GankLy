package com.gank.gankly.ui.daily;

import android.lectcoding.ui.adapter.BaseAdapter;
import android.ly.business.domain.Girl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.butterknife.BindViewHolder;
import com.gank.gankly.butterknife.ItemModel;
import com.gank.gankly.listener.ItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-07-05
 */
public class DailyGirlAdapter extends BaseAdapter<DailyGirlAdapter.NormalViewHolder> {
    private ItemClick mMeiZiOnClick;
    private List<Girl> mGirlList = new ArrayList<>();

    DailyGirlAdapter() {
        setHasStableIds(true);
    }

    @Override
    public DailyGirlAdapter.NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DailyGirlHolder(parent);
    }

    @Override
    public void onBindViewHolder(NormalViewHolder holder, int position) {

    }

    public void setOnItemClickListener(ItemClick onItemClickListener) {
        mMeiZiOnClick = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mGirlList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void refillItem(List<Girl> girlList) {
        int size = mGirlList.size();
        mGirlList.clear();
        notifyItemRangeRemoved(0, size);
        appendItem(girlList);
    }

    public void appendItem(List<Girl> girlList) {
        mGirlList.addAll(girlList);
        notifyItemRangeInserted(getItemCount(), girlList.size());
    }

    @Override
    public void destroy() {

    }

    public static class DailyGirlHolder extends NormalViewHolder<NormalItem> implements View.OnClickListener {
        static final int LAYOUT = R.layout.adapter_daily_girl;

        @BindView(R.id.title)
        TextView txtTitle;

        Girl girl;

        public DailyGirlHolder(ViewGroup parent) {
            super(parent, LAYOUT);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public void bindHolder(NormalItem item) {

        }
    }

    static class NormalItem extends ItemModel {

        @Override
        public int getViewType() {
            return 0;
        }
    }

    abstract static class NormalViewHolder<TT extends ItemModel> extends BindViewHolder<TT> {

        public NormalViewHolder(ViewGroup parent, int layoutRes) {
            super(parent, layoutRes);
        }

        void bindHolder(TT item, ItemCallback callback) {

        }
    }

    interface ItemCallback {
        void onItemClick(Girl girl);
    }
}
