package com.gank.gankly.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gank.gankly.R;


/**
 * Create by LingYan on 2016-06-06
 */
public class ItemSwitchView extends RelativeLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Context mContext;
    private TextView txtName;
    private View viItem;
    private LSwitch mSwitch;
    private boolean isCheck;
    private OnSwitch mOnSwitch;

    public interface OnSwitch {
        void onSwitch(boolean isCheck);
    }

    public ItemSwitchView(Context context) {
        super(context);
        initView(context);
    }

    public ItemSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ItemSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_switch, this, true);
        txtName = (TextView) view.findViewById(R.id.item_switch_txt_name);
        mSwitch = (LSwitch) view.findViewById(R.id.item_switch_auto_check);
        viItem = view.findViewById(R.id.setting_rl_auto_check);
    }

    public void setTextName(String name) {
        txtName.setText(name);
    }

    public void setSwitchChecked(boolean isCheck) {
        this.isCheck = isCheck;
        mSwitch.setChecked(isCheck);
    }

    public void setSwitchListener(OnSwitch onSwitch) {
        this.mOnSwitch = onSwitch;
    }

    public void onViewClick(boolean isCheck) {
        if (mOnSwitch != null) {
            mOnSwitch.onSwitch(isCheck);
        }
    }

    public LSwitch getSwitch() {
        return mSwitch;
    }

    public TextView getTextView() {
        return txtName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_rl_auto_check:
                isCheck = !isCheck;
                mSwitch.setChecked(isCheck);
                onViewClick(isCheck);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        viItem.setOnClickListener(this);
        mSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.isCheck = isChecked;
        mSwitch.setChecked(isChecked);
        onViewClick(isChecked);
    }
}
