package com.gank.gankly.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gank.gankly.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-05-06
 */
public class LoadingLayoutView extends LinearLayout {
    public static final int LOADING = 0;
    public static final int ERROR = -1;
    public static final int EMPTY = 2;

    @Bind(R.id.loading_btn_retry)
    Button btnRetry;
    @Bind(R.id.loading_ll_loading)
    View viewLoading;
    @Bind(R.id.loading_rl_error)
    View viewError;
    @Bind(R.id.loading_rl_empty)
    View viewEmpty;

    private Context mContext;

    public LoadingLayoutView(Context context) {
        super(context);
        if (!isInEditMode()) {
            initView(context);
            this.mContext = context;
        }
    }

    public LoadingLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initView(context);
            this.mContext = context;
        }
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_loading, this, false);
        ButterKnife.bind(view);
        initStatus(LOADING);
    }

    private void initStatus(int status) {

    }

}
