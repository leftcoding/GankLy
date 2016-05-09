package com.gank.gankly.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gank.gankly.R;

/**
 * Create by LingYan on 2016-05-06
 */
public class LoadingLayoutView extends LinearLayout implements OnClickListener {
    public static final int LOADING = 0;
    public static final int ERROR = -1;
    public static final int EMPTY = 2;
    public static final int GONE = 3;

    Button btnRetry;
    LinearLayout viewLoading;
    LinearLayout viewError;
    LinearLayout viewEmpty;

    private Context mContext;
    //    private LoadingClick mLoadingClick;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;

//    public interface LoadingClick {
//        void loadingClick(View view);
//    }

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
        btnRetry = (Button) rootView.findViewById(R.id.loading_btn_retry);
        viewLoading = (LinearLayout) rootView.findViewById(R.id.loading_ll_loading);
        viewError = (LinearLayout) rootView.findViewById(R.id.loading_rl_error);
        viewEmpty = (LinearLayout) rootView.findViewById(R.id.loading_rl_empty);

        btnRetry.setOnClickListener(this);
        initStatus(ERROR);
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
            case GONE:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loading_btn_retry:
                initStatus(LOADING);
                mOnRefreshListener.onRefresh();
                break;
        }
    }

    public void setStatus(int status) {
        initStatus(status);
    }

//    public void setLoading(LoadingClick loading) {
//        mLoadingClick = loading;
//    }

    public void setLoading(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }
}
