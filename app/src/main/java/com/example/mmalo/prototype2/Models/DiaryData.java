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
    private int fvCount;
    private int drCount;

    /**
     * Instantiates a new Diary data.
     */
    public DiaryData() {
    }

    /**
     * Instantiates a new Diary data.
     *
     * @param pd      the pd
     * @param com     the com
     * @param sp      the sp
     * @param ts      the ts
     * @param theMeal the the meal
     * @param fp      the fp
     * @param fv      the fv
     * @param dr      the dr
     */
    public DiaryData(byte[] pd, String com, byte[] sp, Timestamp ts, String theMeal, String fp, int fv, int dr) {
        this.setPhotoData(pd);
        this.setComment(com);
        this.setTimestamp(ts);
        this.setMeal(theMeal);
        this.setFilepath(fp);
        this.setFvCount(fv);
        this.setDrCount(dr);
    }

    /**
     * Sets photo data.
     *
     * @param pData the p data
     */
    public void setPhotoData(byte[] pData) {
        this.photoData = pData;
    }

    /**
     * Sets comment.
     *
     * @param comData the com data
     */
    public void setComment(String comData) {
        this.comment = comData;
    }

    /**
     * Sets spoken data.
     *
     * @param sData the s data
     */
    public void setSpokenData(byte[] sData) {
        this.spokenData = sData;
    }

    /**
     * Sets timestamp.
     *
     * @param time the time
     */
    public void setTimestamp(Timestamp time) {
        this.timestamp = time;
    }

    /**
     * Sets meal.
     *
     * @param mealType the meal type
     */
    public void setMeal(String mealType) {
        this.meal = mealType;
    }

    /**
     * Sets filepath.
     *
     * @param file the file
     */
    public void setFilepath(String file) {
        this.filepath = file;
    }

    /**
     * Sets fv count.
     *
     * @param fv the fv
     */
    public void setFvCount(int fv) {
        this.fvCount = fv;
    }

    /**
     * Sets dr count.
     *
     * @param dr the dr
     */
    public void setDrCount(int dr) {
        this.drCount = dr;
    }


    /**
     * Get photo data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getPhotoData() {
        return this.photoData;
    }

    /**
     * Gets comment.
     *
     * @return the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Get spoken data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getSpokenData() {
        return this.spokenData;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    /**
     * Gets meal.
     *
     * @return the meal
     */
    public String getMeal() {
        return this.meal;
    }

    /**
     * Gets filepath.
     *
     * @return the filepath
     */
    public String getFilepath() {
        return this.filepath;
    }

    /**
     * Gets fv count.
     *
     * @return the fv count
     */
    public int getFvCount() {
        return this.fvCount;
    }

    /**
     * Gets dr count.
     *
     * @return the dr count
     */
    public int getDrCount() {
        return this.drCount;
    }


    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public int describeContents() {
        return 0;
    }

    /**
     * The constant CREATOR.
     */
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
