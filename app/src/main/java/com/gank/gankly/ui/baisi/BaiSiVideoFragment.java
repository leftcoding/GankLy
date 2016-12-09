package com.gank.gankly.ui.baisi;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gank.gankly.R;
import com.gank.gankly.bean.BaiSiBean;
import com.gank.gankly.mvp.source.remote.BaiSiDataSource;
import com.gank.gankly.ui.base.LazyFragment;
import com.gank.gankly.widget.LySwipeRefreshLayout;
import com.gank.gankly.widget.MultipleStatusView;
import com.socks.library.KLog;
import com.superplayer.library.SuperPlayer;
import com.superplayer.library.SuperPlayerManage;
import com.superplayer.library.mediaplayer.IjkVideoView;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-11-30
 * Email:137387869@qq.com
 */

public class BaiSiVideoFragment extends LazyFragment implements BaiSiContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private BaiSiAdapter mBaiSiAdapter;
    private BaiSiContract.Presenter mPresenter;
    private BaiSiActivity mActivity;

    private int postion = -1;
    private int lastPostion = -1;
    private SuperPlayer player;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaiSiActivity) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPresenter() {
        mPresenter = new BaiSiPresenter(BaiSiDataSource.getInstance(), this);
        player = SuperPlayerManage.getSuperManage().initialize(mActivity);
        player.setShowTopControl(false).setSupportGesture(false);
    }

    @Override
    protected void callBackRefreshUi() {

    }

    @Override
    protected void initValues() {
        mPresenter.fetchNew();
    }

    @Override
    protected void initViews() {
        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mBaiSiAdapter = new BaiSiAdapter(mActivity);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setAdapter(mBaiSiAdapter);
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnSwipeRefRecyclerViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
                showRefresh();
                mPresenter.fetchMore();
            }
        });

        mBaiSiAdapter.setPlayClick(new BaiSiAdapter.onPlayClick() {
            @Override
            public void onPlayclick(int position, RelativeLayout image, String url) {
                image.setVisibility(View.GONE);
                if (player.isPlaying() && lastPostion == position) {
                    return;
                }

                player.setScaleType(SuperPlayer.SCALETYPE_4_3);
                postion = position;
                if (player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
                    if (position != lastPostion) {
                        player.stopPlayVideo();
                        player.release();
                    }
                }

                if (lastPostion != -1) {
                    player.showView(R.id.adapter_player_control);
                }

                View view = mSwipeRefreshLayout.getRecyclerView().findViewHolderForAdapterPosition(position).itemView;
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.adapter_super_video);
                frameLayout.removeAllViews();
                player.showView(R.id.adapter_player_control);
                frameLayout.addView(player);
                player.play(url);
                lastPostion = position;
                Toast.makeText(mActivity, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 播放完设置还原播放界面
         */
        player.onComplete(new Runnable() {
            @Override
            public void run() {
                ViewGroup last = (ViewGroup) player.getParent();//找到videoitemview的父类，然后remove
                if (last != null && last.getChildCount() > 0) {
                    last.removeAllViews();
                    View itemView = (View) last.getParent();
                    if (itemView != null) {
                        itemView.findViewById(R.id.adapter_player_control).setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        /***
         * 监听列表的下拉滑动
         */
        mRecyclerView.addOnChildAttachStateChangeListener(new SuperRecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                int index = mRecyclerView.getChildAdapterPosition(view);
                KLog.d("Attached#index:" + index + ",postion:" + postion);
                View controlview = view.findViewById(R.id.adapter_player_control);
                if (controlview == null) {
                    return;
                }
                view.findViewById(R.id.adapter_player_control).setVisibility(View.VISIBLE);
                if (index == postion) {
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.adapter_super_video);
                    frameLayout.removeAllViews();
                    if (player != null &&
                            ((player.isPlaying()) || player.getVideoStatus() == IjkVideoView.STATE_PAUSED)) {
                        view.findViewById(R.id.adapter_player_control).setVisibility(View.GONE);
                    }
                    if (player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
                        if (player.getParent() != null)
                            ((ViewGroup) player.getParent()).removeAllViews();
                        frameLayout.addView(player);
                        return;
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int index = mSwipeRefreshLayout.getRecyclerView().getChildAdapterPosition(view);
                KLog.d("Detached#index:" + index + ",postion:" + postion);
                if ((index) == postion) {
                    if (player != null) {
                        player.showView(R.id.adapter_player_control);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                player.stop();
                                player.release();
                            }
                        }).start();
                    }
                }
            }
        });
    }

    @Override
    protected void bindListener() {

    }

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hasNoMoreDate() {

    }

    @Override
    public void showRefreshError(String errorStr) {

    }

    @Override
    public void refillData(List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list) {
        mBaiSiAdapter.updateItems(list);
    }

    @Override
    public void appendData(List<BaiSiBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list) {
        mBaiSiAdapter.addItems(list);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    public void onBack() {
        if (player != null && player.onBackPressed()) {
            return;
        }
    }
}
