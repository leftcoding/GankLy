package android.ly.business.domain;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Gank extends BaseEntity implements Serializable, ContentEqual {
    @SerializedName("_id")
    public String id;
    public String createdAt;
    public String desc;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public boolean used;
    public String who;

    @Override
    public boolean isContentEqual(ContentEqual contentEqual) {
        if (contentEqual instanceof Gank) {
            Gank item = (Gank) contentEqual;
            return TextUtils.equals(id, item.id)
                    && TextUtils.equals(createdAt, item.createdAt)
                    && TextUtils.equals(desc, item.desc)
                    && TextUtils.equals(publishedAt, item.publishedAt)
                    && TextUtils.equals(source, item.source)
                    && TextUtils.equals(type, item.type)
                    && TextUtils.equals(url, item.url)
                    && TextUtils.equals(who, item.who)
                    && used == item.used;
        }
        return false;
    }
}