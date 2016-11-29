package com.gank.gankly.ui.main;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.FileUtils;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.File;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-11-29
 * Email:137387869@qq.com
 */

public class PlayActivity extends BaseActivity {
    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";

    @BindView(R.id.video_player)
    StandardGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;

    private boolean isTransition;

    @Override
    protected int getContentId() {
        return R.layout.activity_player;
    }

    @Override
    protected void initValues() {
        String url = "http://baobab.wdjcdn.com/14564977406580.mp4";
        //需要路径的
        videoPlayer.setUp(url, true, new File(FileUtils.getPath()), "");
        //videoPlayer.setUp(url, true, "");

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);
        videoPlayer.setThumbImageView(imageView);

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        videoPlayer.getTitleTextView().setText("测试视频");

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);

        //设置全屏按键功能
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });

        videoPlayer.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));
        videoPlayer.setDialogVolumeProgressBar(getResources().getDrawable(R.drawable.video_new_volume_progress_bg));
        videoPlayer.setDialogProgressBar(getResources().getDrawable(R.drawable.video_new_progress));
        videoPlayer.setBottomShowProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_seekbar_progress),
                getResources().getDrawable(R.drawable.video_new_seekbar_thumb));
        videoPlayer.setDialogProgressColor(getResources().getColor(R.color.colorAccent), -11);

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //过渡动画
        initTransition();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }

    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(videoPlayer, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            videoPlayer.startPlayLogic();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    videoPlayer.startPlayLogic();
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }
}
