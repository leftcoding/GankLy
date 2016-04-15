package com.gank.gankly.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.R;
import com.gank.gankly.ui.collect.CollectActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建删除提示框
 * Create by LingYan on 2016-04-13
 */
public class DeleteDialog extends DialogFragment {
    @Bind(R.id.dialog_delete_txt_title)
    TextView txtTitle;
    @Bind(R.id.dialog_delete_txt_content)
    TextView txtContent;

    private CollectActivity mCollectActivity;
    private DialogListener listener;
    private DialogFragment mDialogFragment;

    public interface DialogListener {
        void onNavigationClick();

        void onCancelClick();
    }

    public DeleteDialog() {
    }

    public void setListener(DialogListener dialogListener) {
        listener = dialogListener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCollectActivity = (CollectActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogFragment = this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(mCollectActivity);
        View view = inflater.inflate(R.layout.dialog_delete, null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(mCollectActivity);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("title");
            String content = bundle.getString("content");
            txtTitle.setText(title);
            txtContent.setText(content);
        }
    }

    @OnClick(R.id.dialog_delete_btn_ok)
    void onNavigation() {
        listener.onNavigationClick();
        mDialogFragment.dismiss();
    }

    @OnClick(R.id.dialog_delete_btn_cancel)
    void onCancel() {
        listener.onCancelClick();
        mDialogFragment.dismiss();
    }
}
