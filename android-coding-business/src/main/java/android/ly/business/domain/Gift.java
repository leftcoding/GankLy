package android.ly.business.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by LingYan on 2016-05-18
 */
public class Gift implements Parcelable {
    public String imgUrl;
    public String url;
    public String time;
    public String views;
    public String title;

    public Gift(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Gift(String imgUrl, String url, String time, String views, String title) {
        this.imgUrl = imgUrl;
        this.url = url;
        this.time = time;
        this.views = views;
        this.title = title;
    }

    protected Gift(Parcel in) {
        imgUrl = in.readString();
        url = in.readString();
        time = in.readString();
        views = in.readString();
        title = in.readString();
    }

    public static final Creator<Gift> CREATOR = new Creator<Gift>() {
        @Override
        public Gift createFromParcel(Parcel in) {
            return new Gift(in);
        }

        @Override
        public Gift[] newArray(int size) {
            return new Gift[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(url);
        dest.writeString(time);
        dest.writeString(views);
        dest.writeString(title);
    }
}
