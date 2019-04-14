package com.gank.gankly.widget;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.socks.library.KLog;

/**
 * Bye Bye Burger Navigation Bar Behavior
 * <p>
 * Created by wing on 11/5/16.
 */

public class ByeBurgerBottomBehavior extends ByeBurgerBehavior {

    private TranslateAnimateHelper mAnimateHelper;

    public ByeBurgerBottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                                  int dx, int dy, int[] consumed) {
        KLog.d("child:" + child + ",target:" + target + ",consumed:" + consumed);
        if (isFirstMove) {
            isFirstMove = false;
            mAnimateHelper = TranslateAnimateHelper.get(child);
            mAnimateHelper.setStartY(child.getY());
            mAnimateHelper.setMode(TranslateAnimateHelper.MODE_BOTTOM);
        }
        if (Math.abs(dy) > mTouchSlop) {
            if (dy < 0) {

                if (mAnimateHelper.getState() == TranslateAnimateHelper.STATE_HIDE) {
                    mAnimateHelper.show();
                }
            } else if (dy > 0) {
                if (mAnimateHelper.getState() == TranslateAnimateHelper.STATE_SHOW) {
                    mAnimateHelper.hide();
                }
            }
        }
    }
}
