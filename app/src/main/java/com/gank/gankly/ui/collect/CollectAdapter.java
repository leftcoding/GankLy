package com.gank.gankly.ui.collect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.data.entity.UrlCollect;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-11
 */
public class CollectAdapter extends BaseAdapter {
    private List<UrlCollect> mList;
    private LayoutInflater mLayoutInflater;

    public CollectAdapter(Context context, List<UrlCollect> list) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView != null) {
            holderView = (HolderView) convertView.getTag();
        } else {
            convertView = mLayoutInflater.inflate(R.layout.adapter_collect, parent, false);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        }

        UrlCollect urlCollect = mList.get(position);
        holderView.title.setText(urlCollect.getComment());
        return convertView;
    }

    static class HolderView {
        @Bind(R.id.collect_txt_title)
        TextView title;

        public HolderView(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
