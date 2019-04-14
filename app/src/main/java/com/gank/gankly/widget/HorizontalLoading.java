package com.gank.gankly.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Create by LingYan on 2017-04-27
 */

public class HorizontalLoading extends View {
    private static final String mPaintColor = "#cacaca";

    private Paint mPaint;
    private Paint mPaint1;
    private Paint mPaint0;

    //当前路径的长度
    private int mCurrentPath;
    private int lastPath;
    private int firstPath = -1;

    private int h;
    private AnimatorSet animatorSet;
    private int radius = 10;
    private int minWidth = radius * 4 * 2 + 10;
    private int minHeight = radius + 10;

    public HorizontalLoading(Context context) {
        this(context, null);
    }

    public HorizontalLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor(mPaintColor));
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint1 = new Paint();
        mPaint1.setColor(Color.parseColor(mPaintColor));
        mPaint1.setStrokeWidth(10);
        mPaint1.setAntiAlias(true);
        mPaint1.setAlpha(0);
        mPaint1.setStyle(Paint.Style.FILL);

        mPaint0 = new Paint();
        mPaint0.setColor(Color.parseColor(mPaintColor));
        mPaint0.setStrokeWidth(10);
        mPaint0.setAntiAlias(true);
        mPaint0.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int resultWidth;
        int resultHeight;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            resultWidth = widthSize;
        } else {
            resultWidth = Math.max(minWidth, widthSize);
            if (widthMode == MeasureSpec.AT_MOST) {
                resultWidth = minWidth;
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            resultHeight = heightSize;
        } else {
            resultHeight = Math.max(minHeight, heightSize);
        }

        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.h = h;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(10, h / 2);
        if (lastPath > radius * 3) {
            canvas.drawCircle(firstPath, 0, radius, mPaint1);
        }
        canvas.drawCircle(mCurrentPath, 0, radius, mPaint0);
        canvas.drawCircle(radius * 2 + mCurrentPath, 0, radius, mPaint0);
        if (lastPath < radius * 2) {
            canvas.drawCircle(radius * 4 + lastPath, 0, radius, mPaint);
        }

        invalidate();
    }

    public void onStart() {
        endAnimator();
        startAnimator();
    }

    public void endAnimator() {
        if (animatorSet != null && animatorSet.isStarted()) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
    }

    private void startAnimator() {
        final ValueAnimator firstAnim = ValueAnimator.ofInt(-radius * 4, 0);
        firstAnim.setRepeatCount(ValueAnimator.INFINITE);
        firstAnim.setRepeatMode(ValueAnimator.RESTART);
        firstAnim.setInterpolator(new LinearInterpolator());
        firstAnim.setDuration(800);
        firstAnim.addUpdateListener(animation -> firstPath = (int) animation.getAnimatedValue());

        final ValueAnimator lastAnim = ValueAnimator.ofInt(0, radius * 5);
        lastAnim.setRepeatCount(ValueAnimator.INFINITE);
        lastAnim.setRepeatMode(ValueAnimator.RESTART);
        lastAnim.setInterpolator(new LinearInterpolator());
        lastAnim.setDuration(800);
        lastAnim.addUpdateListener(animation -> lastPath = (int) animation.getAnimatedValue());

        final ValueAnimator currentAnim = ValueAnimator.ofInt(0, radius * 2);
        currentAnim.setRepeatCount(ValueAnimator.INFINITE);
        currentAnim.setRepeatMode(ValueAnimator.RESTART);
        currentAnim.setInterpolator(new LinearInterpolator());
        currentAnim.setDuration(800);
        currentAnim.addUpdateListener(animation -> mCurrentPath = (int) animation.getAnimatedValue());

        ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 0);
        alphaAnim.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnim.setRepeatMode(ValueAnimator.RESTART);
        alphaAnim.setInterpolator(new LinearInterpolator());
        alphaAnim.setDuration(800);
        alphaAnim.addUpdateListener(animation -> {
            int x = (int) animation.getAnimatedValue();
            mPaint.setAlpha(x);
            mPaint1.setAlpha(255 - x);
        });

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(lastAnim, alphaAnim, currentAnim, firstAnim);
        animatorSet.start();
    }
}
