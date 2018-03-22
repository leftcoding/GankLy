package android.ly.business.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Gank extends BaseEntity implements Parcelable {
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
    public boolean isLoaded;
    public List<String> images;

    public Gank() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.createdAt);
        dest.writeString(this.desc);
        dest.writeString(this.publishedAt);
        dest.writeString(this.source);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeByte(this.used ? (byte) 1 : (byte) 0);
        dest.writeString(this.who);
        dest.writeByte(this.isLoaded ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.images);
    }

    protected Gank(Parcel in) {
        this.id = in.readString();
        this.createdAt = in.readString();
        this.desc = in.readString();
        this.publishedAt = in.readString();
        this.source = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.used = in.readByte() != 0;
        this.who = in.readString();
        this.isLoaded = in.readByte() != 0;
        this.images = in.createStringArrayList();
    }

    public static final Creator<Gank> CREATOR = new Creator<Gank>() {
        @Override
        public Gank createFromParcel(Parcel source) {
            return new Gank(source);
        }

        @Override
        public Gank[] newArray(int size) {
            return new Gank[size];
        }
    };

    public boolean isImagesEmpty() {
        return images == null || images.isEmpty();
    }
}