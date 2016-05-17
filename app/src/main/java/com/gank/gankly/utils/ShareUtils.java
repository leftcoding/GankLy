package com.gank.gankly.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.gank.gankly.R;
import com.socks.library.KLog;

import java.io.File;

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

//    //分享单张图片
//    public void shareSingleImage(Context context, Uri imageUri) {
//        if (imageUri == null) {
//            return;
//        }
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        shareIntent.setType("image/*");
//        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
//    }

    //分享文字
    public void shareText(Context context, String share) {
        shareText(context, null, share);
    }

    public void shareText(Context context, String title, String share) {
        if (TextUtils.isEmpty(share)) {
            ToastUtils.showToast(R.string.tip_share_empty_text);
            return;
        }
        share = title + share + " [Gankly]";
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, share);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    public class ShareTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public ShareTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String url = params[0]; // should be easy to extend to share multiple images at once
            try {
                return Glide
                        .with(context)
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                KLog.e("SHARE", "Sharing " + url + " failed", ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            Uri uri = Uri.fromFile(result);
            share(uri);
        }

        private void share(Uri result) {
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
