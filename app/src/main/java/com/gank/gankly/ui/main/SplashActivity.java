package com.gank.gankly.ui.main;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Create by LingYan on 2016-06-01
 * Email:137387869@qq.com
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.splash_main)
    View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
    }

    @Override
    protected void initValues() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startAnim();
                    }
                });
    }

    private void startAnim() {
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mView, "scaleX", 1f, 1.5f);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mView, "scaleY", 1f, 1.5f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000).playTogether(objectAnimatorX, objectAnimatorY);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
