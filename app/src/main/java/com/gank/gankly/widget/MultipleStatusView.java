package com.gank.gankly.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gank.gankly.R;
import com.gank.gankly.utils.CrashUtils;
import com.socks.library.KLog;

/**
 * 包括Loading Empty Error DisNetWork界面
 * Create by LingYan on 2016-06-15
 */
public class MultipleStatusView extends RelativeLayout {
    public static final int STATUS_CONTENT = 0x01;
    public static final int STATUS_ERROR = 0x02;
    public static final int STATUS_LOADING = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;
    public static final int STATUS_EMPTY = 0x05;

    private int mContentViewId;
    private int mErrorViewId;
    private int mLoadingViewId;
    private int mNoNetworkViewId;
    private int mEmptyViewId;

    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mNoNetworkView;
    private View mEmptyView;

    private LayoutInflater mInflater;
    private Context mContext;
    private int mViewStatus;
    private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private OnMultipleClick onMultiple;

    public interface OnMultipleClick {
        void retry(View v);
    }

    public MultipleStatusView(Context context) {
        this(context, null);
    }

    public MultipleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0);
        try {
            mErrorViewId = array.getResourceId(R.styleable.MultipleStatusView_error_view, R.layout.layout_multiple_error);
            mLoadingViewId = array.getResourceId(R.styleable.MultipleStatusView_loading_view, R.layout.layout_multiple_loading);
            mEmptyViewId = array.getResourceId(R.styleable.MultipleStatusView_empty_view, R.layout.layout_multiple_empty);
            mNoNetworkViewId = array.getResourceId(R.styleable.MultipleStatusView_no_network_view, R.layout.layout_multiple_disnetwork);
            mContentViewId = array.getResourceId(R.styleable.MultipleStatusView_content_view, -1);
        } catch (Exception e) {
            KLog.e(e);
            CrashUtils.crashReport(e);
        }
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setClickable(true);
        mInflater = LayoutInflater.from(mContext);
        showContent();
    }

    public void setListener(OnMultipleClick listener) {
        this.onMultiple = listener;
    }

    private void tryClick(View v) {
        if (onMultiple != null) {
            onMultiple.retry(v);
        }
    }

    public void showContent() {
        mViewStatus = STATUS_CONTENT;
        if (mContentView == null) {
            if (mContentViewId != -1) {
                mContentView = mInflater.inflate(mContentViewId, null);
                addView(mContentView, 0, mLayoutParams);
            } else {
                mContentView = getChildAt(0);
            }
        }
        showView(mViewStatus);
    }

    public void showLoading() {
        mViewStatus = STATUS_LOADING;
        if (mLoadingView == null) {
            mLoadingView = mInflater.inflate(mLoadingViewId, null);
            addView(mLoadingView, 0, mLayoutParams);
        }
        showView(mViewStatus);
    }

    public void showEmpty() {
        mViewStatus = STATUS_EMPTY;
        if (mEmptyView == null) {
            mEmptyView = mInflater.inflate(mEmptyViewId, null);
            addView(mEmptyView, 0, mLayoutParams);
        }
        showView(mViewStatus);
    }

    public void showDisNetwork() {
        mViewStatus = STATUS_NO_NETWORK;
        if (mNoNetworkView == null) {
            mNoNetworkView = mInflater.inflate(mNoNetworkViewId, null);
            mNoNetworkView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    tryClick(v);
                }
            });
            addView(mNoNetworkView, 0, mLayoutParams);
        }
        showView(mViewStatus);
    }

    public void showError() {
        mViewStatus = STATUS_ERROR;
        if (mErrorView == null) {
            mErrorView = mInflater.inflate(mErrorViewId, null);
            mErrorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    tryClick(v);
                }
            });
            addView(mErrorView, 0, mLayoutParams);
        }
        showView(mViewStatus);
    }

    private void showView(int viewStatus) {
        if (mContentView != null) {
            mContentView.setVisibility(viewStatus == STATUS_CONTENT ? View.VISIBLE : View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(viewStatus == STATUS_LOADING ? View.VISIBLE : View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(viewStatus == STATUS_EMPTY ? View.VISIBLE : View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(viewStatus == STATUS_ERROR ? View.VISIBLE : View.GONE);
        }
        if (mNoNetworkView != null) {
            mNoNetworkView.setVisibility(viewStatus == STATUS_NO_NETWORK ? View.VISIBLE : View.GONE);
        }
    }

    public int getViewStatus() {
        return mViewStatus;
    }

}
