package com.gank.gankly.ui.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-06
 */
public class GankDetailsAdapter extends BaseAdapter {
    private List<ResultsBean> mResults;
    private LayoutInflater inflater;

    public GankDetailsAdapter(Activity context, List<ResultsBean> results) {
        mResults = results;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mResults != null) {
            return mResults.size();
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
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.adapter_welfare, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ResultsBean bean = mResults.get(position);
        holder.txtDesc.setText(bean.getDesc());
        holder.txtTime.setText(bean.getPublishedAt());
        holder.txtWho.setText(bean.getWho());
        return convertView;

    }

    static class ViewHolder {
        @Bind(R.id.goods_txt_title)
        TextView txtDesc;
        @Bind(R.id.goods_txt_author_time)
        TextView txtTime;
        @Bind(R.id.goods_txt_author)
        TextView txtWho;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
