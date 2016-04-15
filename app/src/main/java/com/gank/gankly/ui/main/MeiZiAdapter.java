package com.gank.gankly.ui.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.bean.ResultsBean;
import com.gank.gankly.widget.RatioImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-06
 */
public class MeiZiAdapter extends BaseAdapter {
    private List<ResultsBean> mResults;
    private Activity mContext;
    private LayoutInflater inflater;

    public MeiZiAdapter(Activity context, List<ResultsBean> results) {
        mResults = results;
        inflater = LayoutInflater.from(context);
        mContext = context;
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
            convertView = inflater.inflate(R.layout.adapter_meizi, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ResultsBean bean = mResults.get(position);
        holder.txtDesc.setText(bean.getDesc());
        Glide.with(mContext)
                .load(bean.getUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .into(holder.imgMeizi);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.meizi_txt_time)
        TextView txtDesc;

        @Bind(R.id.meizi_img_picture)
        RatioImageView imgMeizi;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
