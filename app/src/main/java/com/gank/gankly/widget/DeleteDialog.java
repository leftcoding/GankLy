package com.gank.gankly.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.gank.gankly.R;

/**
 * 创建删除提示框
 * Create by LingYan on 2016-04-13
 */
public class DeleteDialog extends DialogFragment {
    public static final String TAG = "DeleteDialog";
    public static final String CONTENT = "content";
    public static final String ITEM = "item";

    private Context mContext;
    private DialogListener mListener;
    private DialogFragment mDialogFragment;
    private AlertDialog.Builder mBuilder;
    private String mContent;

    public interface DialogListener {
        void onNavigationClick();
    }

    public DeleteDialog() {
    }

    public void setListener(DialogListener dialogListener) {
        mListener = dialogListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogFragment = this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mContent = bundle.getString(CONTENT);
        }

        mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setTitle(R.string.tip_to_delete);
        mBuilder.setMessage(mContent);
        mBuilder.setNegativeButton(R.string.dialog_ok, (dialog, which) -> {
            mDialogFragment.dismiss();
            mListener.onNavigationClick();
        });
        mBuilder.setPositiveButton(R.string.dialog_cancel, (dialog, which) -> mDialogFragment.dismiss());
        return mBuilder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
