package com.gank.gankly.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gank.gankly.R;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Create by LingYan on 2016-06-06
 */
public class ItemTextView extends RelativeLayout implements View.OnClickListener {
    @Bind(R.id.item_view_txt_name)
    TextView txtName;
    @Bind(R.id.item_view_txt_summary)
    TextView txtSummary;
    @Bind(R.id.item_rl_update_version)
    View mView;
    @BindString(R.string.setting_check_version)
    String mName;

    private UpdateListener mUpdateListener;
    private String mSummary;

    public interface UpdateListener {
        void onUpdate();
    }

    public ItemTextView(Context context) {
        this(context, null);
    }

    public ItemTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setUpdateListener(UpdateListener listener) {
        this.mUpdateListener = listener;
    }

    public ItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_text, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ItemSwitchView);
        mName = array.getString(R.styleable.ItemSwitchView_name);
        mSummary = array.getString(R.styleable.ItemSwitchView_summary);
        ButterKnife.bind(this, view);
        array.recycle();
    }

    public void setTextName(String res) {
        txtName.setText(res);
    }

    public void setTxtSummary(String res) {
        txtSummary.setText(res);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        txtName.setText(mName);
        txtSummary.setText(mSummary);
        mView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_rl_update_version:
                if (mUpdateListener != null) {
                    mUpdateListener.onUpdate();
                }
                break;

            default:
                break;
        }
    }
}
