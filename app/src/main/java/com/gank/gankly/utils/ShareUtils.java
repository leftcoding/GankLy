package com.gank.gankly.utils;

import android.text.TextUtils;

import com.gank.gankly.App;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

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

    public void shareQQ(String title, String titleUrl, String text, String imgUrl) {
        imgUrl = "http://7xs032.com1.z0.glb.clouddn.com/ic_launcher.png";
        share(title, titleUrl, text, imgUrl, QQ.NAME, null);
    }

    public void shareWeChat(String title, String titleUrl, String text, String imgUrl) {
        share(title, titleUrl, text, imgUrl, Wechat.NAME, null);
    }

    public void shareWeChatMoments(String title, String titleUrl, String text, String imgUrl) {
        share(title, titleUrl, text, imgUrl, WechatMoments.NAME, null);
    }

    public void shareWeiBo(String title, String titleUrl, String text, String imgUrl) {
        share(title, titleUrl, text, imgUrl, SinaWeibo.NAME, null);
    }

    private void share(String title, String titleUrl, String text, String imgUrl,
                       String platformName, PlatformActionListener platformActionListener) {

        if (TextUtils.isEmpty(platformName)) {
            throw new RuntimeException("Platform name not can be null");
        }

        if (!AppUtils.isInstalledApk(App.getContext(), "com.tencent.mm")) {
            ToastUtils.showToast("微信未安装，请先安装");
        }

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(titleUrl);
        sp.setText(text);
        sp.setImageUrl(imgUrl);
        Platform platform = ShareSDK.getPlatform(platformName);

        if (platformActionListener != null) {
            platform.setPlatformActionListener(platformActionListener);
        }
        platform.share(sp);  // 执行图文分享
    }
}
