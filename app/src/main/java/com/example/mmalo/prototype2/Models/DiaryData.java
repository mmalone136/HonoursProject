package com.example.mmalo.prototype2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.PublicKey;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by mmalo on 15/01/2017.
 */

public class DiaryData implements Parcelable {
    private int mData;
    private byte[] photoData;
    private String comment;
    private byte[] spokenData;
    private Timestamp timestamp;
    private String meal;
    private String filepath;
    // private Date theDate;
    // private Time theTime;

    public DiaryData() {
    }

    public DiaryData(byte[] pd, String com, byte[] sp, Timestamp ts, String theMeal, String fp) {
        this.setPhotoData(pd);
        this.setComment(com);
        //this.setSpokenData(sp);
        this.setTimestamp(ts);
        this.setMeal(theMeal);
        this.setFilepath(fp);
    }

    public void setPhotoData(byte[] pData) {
        this.photoData = pData;
    }

    public void setComment(String comData) {
        this.comment = comData;
    }

    public void setSpokenData(byte[] sData) {
        this.spokenData = sData;
    }

    public void setTimestamp(Timestamp time) {
        this.timestamp = time;
    }

    public void setMeal(String mealType) {
        this.meal = mealType;
    }

    public void setFilepath(String file) {
        this.meal = file;
    }


    public byte[] getPhotoData() {
        return this.photoData;
    }

    public String getComment() {
        return this.comment;
    }

    public byte[] getSpokenData() {
        return this.spokenData;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public String getMeal() {
        return this.meal;
    }

    public String getFilepath() {
        return this.filepath;
    }


    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<DiaryData> CREATOR
            = new Parcelable.Creator<DiaryData>() {
        public DiaryData createFromParcel(Parcel in) {
            return new DiaryData();
        }

        public DiaryData[] newArray(int size) {
            return new DiaryData[size];
        }
    };
}
