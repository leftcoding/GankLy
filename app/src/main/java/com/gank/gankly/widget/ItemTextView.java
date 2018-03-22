package com.gank.gankly.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gank.gankly.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于自定义文本Item
 * Create by LingYan on 2016-06-06
 */
public class ItemTextView extends RelativeLayout {
    @BindView(R.id.item_view_txt_name)
    TextView txtTitle;
    @BindView(R.id.item_view_txt_summary)
    TextView txtSummary;
    @BindView(R.id.item_rl_update_version)
    View mView;
    @BindView(R.id.item_view_txt_new_version)
    TextView txtVersion;

    private String mSummary;
    private String mTitle;
    private int mTitleSize;
    private int mSummarySize;
    private int mTitleColor;
    private int mSummaryColor;

    private Context context;

    public ItemTextView(Context context) {
        this(context, null);
    }

    public ItemTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_text_view, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ItemTextView);
        try {
            mTitle = array.getString(R.styleable.ItemTextView_textTitle);
            mSummary = array.getString(R.styleable.ItemTextView_textSummary);
            mTitleSize = array.getInteger(R.styleable.ItemTextView_textTitleSize, 14);
            mSummarySize = array.getInteger(R.styleable.ItemTextView_textSummarySize, 12);
            mTitleColor = array.getColor(R.styleable.ItemTextView_textTitleColor, context.getResources().getColor(R.color.text_default));
            mSummaryColor = array.getColor(R.styleable.ItemTextView_textSummaryColor, context.getResources().getColor(R.color.text_999999));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        ButterKnife.bind(this, view);
        array.recycle();
    }

    public void setTextName(int resId) {
        txtTitle.setText(resId);
    }

    public void setTextSummary(String res) {
        txtSummary.setText(res);
    }

    public void showVersion() {
        txtVersion.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        txtTitle.setText(mTitle);
        txtSummary.setText(mSummary);
        txtTitle.setTextColor(mTitleColor);
        txtSummary.setTextColor(mSummaryColor);
        txtTitle.setTextSize(mTitleSize);
        txtSummary.setTextSize(mSummarySize);
    }
}
