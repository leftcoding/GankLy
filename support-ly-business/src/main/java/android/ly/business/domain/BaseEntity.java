package android.ly.business.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseEntity implements Parcelable{
    /**
     * 错误码
     */
    public boolean error;

    /**
     * 提示语
     */
    public String msg;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.error ? (byte) 1 : (byte) 0);
        dest.writeString(this.msg);
    }

    public BaseEntity() {
    }

    protected BaseEntity(Parcel in) {
        this.error = in.readByte() != 0;
        this.msg = in.readString();
    }

    public static final Creator<BaseEntity> CREATOR = new Creator<BaseEntity>() {
        @Override
        public BaseEntity createFromParcel(Parcel source) {
            return new BaseEntity(source);
        }

        @Override
        public BaseEntity[] newArray(int size) {
            return new BaseEntity[size];
        }
    };
}