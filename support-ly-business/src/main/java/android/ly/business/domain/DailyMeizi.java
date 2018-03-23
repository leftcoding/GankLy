package android.ly.business.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by LingYan on 2016-07-05
 */
public class DailyMeizi implements Parcelable {
    /**
     * 月份
     */
    public String month;

    /**
     * 日期
     */
    public String day;

    /**
     * 请求地址
     */
    public String url;

    /**
     * 标题
     */
    public String title;

    public DailyMeizi(String url, String title) {
        this.url = url;
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.month);
        dest.writeString(this.day);
        dest.writeString(this.url);
        dest.writeString(this.title);
    }

    protected DailyMeizi(Parcel in) {
        this.month = in.readString();
        this.day = in.readString();
        this.url = in.readString();
        this.title = in.readString();
    }

    public static final Creator<DailyMeizi> CREATOR = new Creator<DailyMeizi>() {
        @Override
        public DailyMeizi createFromParcel(Parcel source) {
            return new DailyMeizi(source);
        }

        @Override
        public DailyMeizi[] newArray(int size) {
            return new DailyMeizi[size];
        }
    };
}
