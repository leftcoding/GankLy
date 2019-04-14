package com.gank.gankly.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gank.gankly.R;
import com.gank.gankly.ui.base.activity.BaseActivity;
import com.gank.gankly.ui.MainActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Create by LingYan on 2016-06-01
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
        start();
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_splash;
    }

    private void start() {
        Observable.timer(50, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> startAnim());
    }

    private void startAnim() {
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mView, "scaleX", 1f, 1.5f);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mView, "scaleY", 1f, 1.5f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200).playTogether(objectAnimatorX, objectAnimatorY);
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
