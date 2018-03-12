package com.zishan.sardinemerchant.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wislie on 2017/12/7.
 */

public class RightBean implements Parcelable{
    private String tag;

    private Long customGroupId;

    public RightBean(Long customGroupId){
        this.customGroupId = customGroupId;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public Long getCustomGroupId() {
        return customGroupId;
    }

    public void setCustomGroupId(Long customGroupId) {
        this.customGroupId = customGroupId;
    }

    protected RightBean(Parcel in) {
        tag = in.readString();
        customGroupId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeLong(customGroupId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RightBean> CREATOR = new Creator<RightBean>() {
        @Override
        public RightBean createFromParcel(Parcel in) {
            return new RightBean(in);
        }

        @Override
        public RightBean[] newArray(int size) {
            return new RightBean[size];
        }
    };
}
