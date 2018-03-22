package com.gank.gankly.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.gank.gankly.R;

/**
 * Create by LingYan on 2016-04-22
 */
public class ShareUtils {
    private static ShareUtils mShareUtils;

    public static ShareUtils getInstance() {
        if (mShareUtils == null) {
            mShareUtils = new ShareUtils();
        }
        return mShareUtils;
    }

    //分享文字
    public void shareText(Context context, String share) {
        shareText(context, null, share);
    }

    public void shareText(Context context, String title, String share) {
        if (TextUtils.isEmpty(share)) {
            ToastUtils.showToast(context, R.string.tip_share_empty_text);
            return;
        }
        share = title + "! " + share + " [Gankly]";
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, share);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to)));
    }

    public static void shareSingleImage(Context context, Uri result) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享图片");
        intent.putExtra(Intent.EXTRA_TEXT, "Look what I found!");
        intent.putExtra(Intent.EXTRA_STREAM, result);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享图片"));
    }
}
