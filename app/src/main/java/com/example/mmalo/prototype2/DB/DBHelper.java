package com.example.mmalo.prototype2.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mmalo on 14/01/2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    //https://developer.android.com/training/basics/data-storage/databases.html
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DiaryData.db";
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


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS diary_entries";

    public DBHelper(Context cont)
    {
        super(cont,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sdb)
    {
        sdb.execSQL(SQL_CREATE_ENTRIES);
        sdb.execSQL(SQL_CREATE_COUNTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}
