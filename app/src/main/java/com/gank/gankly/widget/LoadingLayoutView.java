package com.gank.gankly.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gank.gankly.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by LingYan on 2016-05-06
 */
public class LoadingLayoutView extends LinearLayout {
    public static final int LOADING = 0;
    public static final int ERROR = -1;
    public static final int EMPTY = 2;

    @Bind(R.id.loading_ll_loading)
    View viewLoading;
    @Bind(R.id.loading_rl_error)
    View viewError;
    @Bind(R.id.loading_rl_empty)
    View viewEmpty;
    @Bind(R.id.loading_btn_retry)
    Button btnRetry;

    private Context mContext;

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;

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
        View rootView = LayoutInflater.from(context).inflate(R.layout.fragment_loading, this, true);
        ButterKnife.bind(this, rootView);
//        viewLoading = rootView.findViewById(R.id.loading_ll_loading);
//        btnRetry = (Button) rootView.findViewById(R.id.loading_btn_retry);
//        viewError = rootView.findViewById(R.id.loading_rl_error);
//        viewEmpty = rootView.findViewById(R.id.loading_rl_empty);

        initStatus(LOADING);
    }

    private void initStatus(int status) {
        viewEmpty.setVisibility(View.GONE);
        viewError.setVisibility(View.GONE);
        viewLoading.setVisibility(View.GONE);

        switch (status) {
            case LOADING:
                viewLoading.setVisibility(View.VISIBLE);
                break;
            case EMPTY:
                viewEmpty.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                viewError.setVisibility(View.VISIBLE);
                break;
        }
    }


    @OnClick(R.id.loading_btn_retry)
    void onRetry() {
        initStatus(LOADING);
        mOnRefreshListener.onRefresh();
    }

    public void setStatus(int status) {
        initStatus(status);
    }

    public void setLoading(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }
}
