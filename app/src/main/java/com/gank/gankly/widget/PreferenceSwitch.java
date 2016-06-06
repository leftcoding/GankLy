package com.gank.gankly.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.TwoStatePreference;
import android.util.AttributeSet;

import com.gank.gankly.R;

/**
 * Create by LingYan on 2016-06-06
 */
public class PreferenceSwitch extends TwoStatePreference {
    public PreferenceSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.preferenceSwitch, defStyleAttr, defStyleRes);
        boolean isCheck = a.getBoolean(R.styleable.preferenceSwitch_check, true);
        String textOn = a.getString(R.styleable.preferenceSwitch_text_on);
        String textOff = a.getString(R.styleable.preferenceSwitch_text_off);

        setTitle(textOn);
        setSummary(textOff);
        setChecked(true);
        a.recycle();
    }

    public PreferenceSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.SwitchPreference);
    }

    public PreferenceSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreferenceSwitch(Context context) {
        this(context, null);
    }
}
