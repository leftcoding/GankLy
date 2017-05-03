package com.gank.gankly.utils.gilde;

import android.content.Context;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

/**
 * Create by LingYan on 2017-04-27
 * Email:137387869@qq.com
 */

public class GlideUtils {

    public static DrawableTypeRequest<String> wifiRequest(Context context, boolean isOnlyOnWiFi) {
        DrawableTypeRequest<String> request;
        if (isOnlyOnWiFi) {
            request = Glide.with(context).using(new NetworkDisablingLoader<String>()).from(String.class);
        } else {
            request = Glide.with(context).fromString();
        }
        return request;
    }
}
