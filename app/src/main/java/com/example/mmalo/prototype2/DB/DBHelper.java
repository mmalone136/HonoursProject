package com.example.mmalo.prototype2.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mmalo on 14/01/2017.
 *
 * Class adapted from tutorial:
 * //https://developer.android.com/training/basics/data-storage/databases.html
 *
 *
 */

public class DBHelper extends SQLiteOpenHelper{

    //Setup container variables for Database version, name
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DiaryData.db";

    //Define necessary create statements
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS diary_entries (" +
                    " entry_ID INTEGER PRIMARY KEY," +
                    " photo_data BLOB ," +
                    " comment_data TEXT," +
                    " audio_data BLOB ," +
                    " time_stamp TEXT," +
                    " filepath TEXT," +
                    " meal TEXT," +
                    " fv_count INT," +
                    " drink_count INT)";

    private static final String SQL_CREATE_COUNTS =
            "CREATE TABLE IF NOT EXISTS counts (" +
                    " entry_ID INTEGER PRIMARY KEY," +
                    " time_stamp TEXT," +
                    " fv_count INT," +
                    " drink_count INT" +
                    " hadBreakfast BOOLEAN" +
                    " hadLunch BOOLEAN" +
                    " hadDinner BOOLEAN)";


    /**
     *
     * @param cont
     */
    public DBHelper(Context cont)
    {
        super(cont,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /**
     *
     * @param sdb
     */
    public void onCreate(SQLiteDatabase sdb)
    {
        //Execute create statements
        sdb.execSQL(SQL_CREATE_ENTRIES);
        sdb.execSQL(SQL_CREATE_COUNTS);
    }

    /**
     * From tutorial - may not be necessary
     *
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


}
