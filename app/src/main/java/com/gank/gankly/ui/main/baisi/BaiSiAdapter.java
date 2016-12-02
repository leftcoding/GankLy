package com.gank.gankly.ui.main.baisi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gank.gankly.R;
import com.gank.gankly.bean.BaiSiBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-04-11
 */
public class BaiSiAdapter extends RecyclerView.Adapter<BaiSiAdapter.BaiSiHolderView> {
    private List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> mList;
    private final Context mContext;
    private onPlayClick playclick;

    public BaiSiAdapter(Context context) {
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
        BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean = mList.get(position);
        holder.update(position, bean.getVideo_uri());
        holder.name.setText(bean.getName());
        holder.title.setText(bean.getText().trim().replace("\\n", ""));
        holder.mTime.setText(bean.getCreate_time());
        Glide.with(mContext).load(bean.getProfile_image()).into(holder.imgPortarit);
    }


    @Override
    public void onViewRecycled(BaiSiHolderView holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.imgPortarit);
        holder.imgPortarit.setImageBitmap(null);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class BaiSiHolderView extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_player_control)
        RelativeLayout rlayPlayerControl;
//        private RelativeLayout rlayPlayer;
        @BindView(R.id.baisi_txt_name)
        TextView name;
        @BindView(R.id.baisi_video_title)
        TextView title;
        @BindView(R.id.baisi_img_portrait)
        ImageView imgPortarit;
        @BindView(R.id.baisi_txt_time)
        TextView mTime;

        public BaiSiHolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            rlayPlayer = (RelativeLayout) itemView.findViewById(R.id.adapter_super_video_layout);
//            if (rlayPlayer != null) {
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlayPlayer.getLayoutParams();
//                layoutParams.height = (int) (getScreenWidth((Activity) mContext) * 0.5652f);//这值是网上抄来的，我设置了这个之后就没有全屏回来拉伸的效果，具体为什么我也不太清楚
//                rlayPlayer.setLayoutParams(layoutParams);
//            }
        }

        public void update(final int position, final String url) {
            //点击回调 播放视频
            rlayPlayerControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playclick != null) {
                        playclick.onPlayclick(position, rlayPlayerControl, url);
                    }
                }
            });
        }
    }

    public void updateItems(List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list) {
        int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
        addItems(list);
    }

    public void addItems(List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list) {
        int size = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(size, list.size());
    }

//    public static int getScreenWidth(Activity activity) {
//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenWidth = dm.widthPixels;
//        return screenWidth;
//    }

    public void setPlayClick(onPlayClick playclick) {
        this.playclick = playclick;
    }

    public interface onPlayClick {
        void onPlayclick(int position, RelativeLayout image, String url);
    }
}
