package com.gank.gankly.ui.baisi;

import android.content.Context;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gank.gankly.R;
import com.gank.gankly.bean.BuDeJieVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-11
 */
public class BaiSiVideoAdapter extends RecyclerView.Adapter<BaiSiVideoAdapter.BaiSiHolderView> {
    private List<BuDeJieVideo.ListBean> mList;
    private final Context mContext;
    private onPlayClick playclick;
    private Map<String, Integer> heights = new ArrayMap<>();

    public BaiSiVideoAdapter(Context context) {
        setHasStableIds(true);
        this.mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public BaiSiHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.adapter_listview_layout, null);
        BaiSiHolderView holder = new BaiSiHolderView(view);
        view.setTag(holder);
        return new BaiSiHolderView(view);
    }

    @Override
    public void onBindViewHolder(BaiSiHolderView holder, int position) {
        BuDeJieVideo.ListBean bean = mList.get(position);
        String videoUrl = bean.getVideo().getDownload().get(0);
        holder.update(position, videoUrl);
        holder.name.setText(bean.getU().getName());
        holder.title.setText(bean.getText());
        holder.mTime.setText(bean.getPasstime());

        holder.mTitle = bean.getText();
        holder.mShareUrl = bean.getShare_url();

        int height = bean.getVideo().getHeight();
        int width = bean.getVideo().getWidth();
        holder.height = height;
        holder.width = width;
        if (!heights.containsKey(videoUrl)) {
            if (Math.max(height, width) == 0) {
                height = 1066;
                width = 1066;
            } else {
                height = height * 1080 / width;
                height = height > 800 ? 800 : height;
            }

            heights.put(videoUrl, height);
        } else {
            height = heights.get(videoUrl);
        }

        ViewGroup.LayoutParams layoutParams = holder.mPlay.getLayoutParams();
        layoutParams.width = 1080;
        layoutParams.height = height;
        holder.mPlay.setLayoutParams(layoutParams);


        Glide.with(mContext)
                .load(bean.getU().getHeader().get(0))
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(holder.imgPortarit);

        Glide.with(mContext)
                .load(bean.getVideo().getThumbnail().get(0))
                .apply(new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(holder.imgThumb);
    }


    @Override
    public void onViewRecycled(BaiSiHolderView holder) {
        super.onViewRecycled(holder);
        Glide.get(mContext).clearMemory();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class BaiSiHolderView extends RecyclerView.ViewHolder {
        @BindView(R.id.baisi_txt_name)
        TextView name;
        @BindView(R.id.baisi_video_title)
        TextView title;
        @BindView(R.id.baisi_img_portrait)
        ImageView imgPortarit;
        @BindView(R.id.baisi_txt_time)
        TextView mTime;
        @BindView(R.id.baisi_adapter_img_thumb)
        ImageView imgThumb;
        @BindView(R.id.adapter_super_video_layout)
        RelativeLayout mPlay;

        int height;
        int width;
        String mTitle;
        String mShareUrl;

        public BaiSiHolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(final int position, final String url) {
            mPlay.setOnClickListener(v -> {
                if (playclick != null) {
                    playclick.onPlayclick(position, imgThumb, url, height, width, mTitle, mShareUrl);
                }
            });
        }
    }

    public void updateItems(List<BuDeJieVideo.ListBean> list) {
        int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
        addItems(list);
    }

    public void addItems(List<BuDeJieVideo.ListBean> list) {
        int size = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(size, list.size());
    }

    public void setPlayClick(onPlayClick playclick) {
        this.playclick = playclick;
    }

    public interface onPlayClick {
        void onPlayclick(int position, View image, String url, int height, int width, String title, String shareUrl);
    }

    //防止多个viewType，出现2个item不同问题
    @Override
    public long getItemId(int position) {
        return position;
    }
}
