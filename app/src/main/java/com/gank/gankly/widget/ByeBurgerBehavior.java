package com.gank.gankly.widget;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Base Behavior
 * Created by wing on 11/8/16.
 */

public class ByeBurgerBehavior extends CoordinatorLayout.Behavior<View> {

    protected final int mTouchSlop;
    protected boolean isFirstMove = true;

    public ByeBurgerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    // on Scroll Started
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {

        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }
}
