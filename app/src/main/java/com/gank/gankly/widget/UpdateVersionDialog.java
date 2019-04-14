package com.gank.gankly.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.listener.DialogOnClick;

/**
 * Create by LingYan on 2016-09-09
 */
public class UpdateVersionDialog extends DialogFragment {
    public static final String UPDATE_CONTENT = "update_content";
    private Context mContext;
    private DialogOnClick mDialogOnClick;
    private String mContent;


    public UpdateVersionDialog() {
    }

    public void setDialogOnClick(DialogOnClick mDialogOnClick) {
        this.mDialogOnClick = mDialogOnClick;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mContent = bundle.getString(UPDATE_CONTENT, "");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_update_version, null);
        builder.setView(view);
        view.findViewById(R.id.update_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogOnClick != null) {
                    mDialogOnClick.cancel();
                }
            }
        });
        view.findViewById(R.id.update_btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogOnClick != null) {
                    mDialogOnClick.submit();
                }
            }
        });
        ((TextView) view.findViewById(R.id.update_txt_content)).setText(mContent);
        return builder.create();
    }
}
