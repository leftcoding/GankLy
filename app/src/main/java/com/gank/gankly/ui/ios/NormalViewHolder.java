package com.gank.gankly.ui.ios;

import android.ly.business.domain.Gank;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.butterknife.BindViewHolder;

import butterknife.BindView;

public class NormalViewHolder extends BindViewHolder<TextViewModel> {
    @BindView(R.id.author_name)
    TextView authorName;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.title)
    TextView title;

    private ItemCallback itemCallBack;

    NormalViewHolder(ViewGroup parent, ItemCallback callback) {
        super(parent, R.layout.adapter_ios);
        this.itemCallBack = callback;
    }

    @Override
    public void bindHolder(TextViewModel item) {
        final Gank gank = item.gank;

        time.setText(item.getTime());
        title.setText(gank.desc);
        authorName.setText(gank.who);

        itemView.setOnClickListener(v -> {
            if (itemCallBack != null) {
                itemCallBack.onItemClick(v, gank);
            }
        });
    }
}