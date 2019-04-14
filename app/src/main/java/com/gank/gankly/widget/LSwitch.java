package com.gank.gankly.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.widget.SwitchCompat;
import android.util.AttributeSet;

import com.gank.gankly.R;

/**
 * Create by LingYan on 2016-09-01
 */
public class LSwitch extends SwitchCompat {
    private int mColor = 1;

    public LSwitch(Context context) {
        super(context);
        init(context);
    }

    public LSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        DrawableCompat.setTintList(getThumbDrawable(), getSwitchThumbColorStateList());
        DrawableCompat.setTintList(getTrackDrawable(), getSwitchTrackColorStateList());
    }

    public void changeTheme() {
        DrawableCompat.setTintList(getThumbDrawable(), getSwitchThumbColorStateList());
        DrawableCompat.setTintList(getTrackDrawable(), getSwitchTrackColorStateList());
    }

    private ColorStateList getSwitchThumbColorStateList() {
        final int[][] states = new int[3][];
        final int[] colors = new int[3];

        // Disabled state
        states[0] = new int[]{-android.R.attr.state_enabled};
        colors[0] = (Color.DKGRAY);

        // Checked state
        states[1] = new int[]{android.R.attr.state_checked};
//        if (AppConfig.isNight()) {
//            mColor = R.color.switch_thumb_disabled_dark;
//        } else {
        mColor = R.color.colorAccent;
//        }

        colors[1] = getContext().getResources().getColor(mColor);

        // Unchecked enabled state state
        states[2] = new int[0];
        colors[2] = (Color.WHITE);

        return new ColorStateList(states, colors);
    }

    private ColorStateList getSwitchTrackColorStateList() {
        final int[][] states = new int[3][];
        final int[] colors = new int[3];

        // Disabled state
        states[0] = new int[]{-android.R.attr.state_enabled};
        colors[0] = Color.GRAY;

        // Checked state
        states[1] = new int[]{android.R.attr.state_checked};
        colors[1] = Color.argb(0x4D, // 30% alpha
                Color.red(ContextCompat.getColor(getContext(), mColor)),
                Color.green(ContextCompat.getColor(getContext(),mColor)),
                Color.blue(ContextCompat.getColor(getContext(),mColor)));

        // Unchecked enabled state state
        states[2] = new int[0];
        colors[2] = (Color.GRAY);

        return new ColorStateList(states, colors);
    }
}
