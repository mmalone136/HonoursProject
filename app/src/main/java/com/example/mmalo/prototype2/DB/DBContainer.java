package com.example.mmalo.prototype2.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.example.mmalo.prototype2.Models.DiaryData;
import com.example.mmalo.prototype2.R;

import java.sql.Date;

/**
 * Created by mmalo on 27/02/2017.
 */

public class DBContainer {

    DBHelper dbh;
    SQLiteDatabase db;

    public DBContainer() {}

    public void dropTables(Context cont) {
        dbh = new DBHelper(cont);
        db = dbh.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS diary_entries");
        db.execSQL("DROP TABLE IF EXISTS counts");
        System.out.print("");
        db.close();
    }

    public void createTables(Context cont) {
        DBHelper dbh;
        SQLiteDatabase db;
        dbh = new DBHelper(cont);
        db = dbh.getWritableDatabase();

        String SQL_CREATE_ENTRIES =
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

        db.execSQL(SQL_CREATE_ENTRIES);

        String SQL_CREATE_COUNTS =
                "CREATE TABLE IF NOT EXISTS counts (" +
                        " entry_ID INTEGER PRIMARY KEY," +
                        " time_stamp TEXT," +
                        " fv_count INT," +
                        " drink_count INT," +
                        " hadBreakfast BOOLEAN," +
                        " hadLunch BOOLEAN," +
                        " hadDinner BOOLEAN)";

        db.execSQL(SQL_CREATE_COUNTS);

        System.out.print("");
        db.close();

    }

    public void insertEntry(DiaryData dd, Context cont) {
        DBHelper dbh = new DBHelper(cont);
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues vals = new ContentValues();
        //vals.put("entry_ID",1);
        vals.put("photo_data", dd.getPhotoData());
        vals.put("comment_data", dd.getComment());
        vals.put("audio_data", dd.getSpokenData());
        vals.put("time_stamp", String.valueOf(dd.getTimestamp()));
        vals.put("meal", dd.getMeal());

        long rowID = db.insert("diary_entries", null, vals);
        db.close();
    }

    public int[] readCountData(Context cont){
        dbh = new DBHelper(cont);
        db = dbh.getReadableDatabase();

        int[] returnFields = new int[5];

        java.util.Date theDate = new java.util.Date();
        Date today = new Date(theDate.getTime());

        String[] projection = {
                "entry_ID", "fv_count", "time_stamp", "drink_count","hadBreakfast", "hadLunch","hadDinner"
        };
        String select = "time_stamp Like ?";
        String[] selArgs = {"%" + today + "%"};
        //ArgOrder => Table,Columns, Columns From Where, Values from where, togroup, tofilter groups, sortorder
        Cursor cursor = db.query("counts", projection, select, selArgs, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                try {
                    String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
                    int fvCount = cursor.getInt(cursor.getColumnIndexOrThrow("fv_count"));
                    int drinkCount = cursor.getInt(cursor.getColumnIndexOrThrow("drink_count"));

                    int bfast = cursor.getInt(cursor.getColumnIndexOrThrow("hadBreakfast"));
                    int lunch = cursor.getInt(cursor.getColumnIndexOrThrow("hadLunch"));
                    int dinn = cursor.getInt(cursor.getColumnIndexOrThrow("hadDinner"));

                    returnFields[0] = fvCount;
                    returnFields[1] = drinkCount;
                    returnFields[2] = bfast;
                    returnFields[3] = lunch;
                    returnFields[4] = dinn;

                } catch (Exception e) {
                    e.printStackTrace();
                    e.printStackTrace();
                    return null;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnFields;

    }


    //---------- TEMP FUNCTIONS ------------
    public void deleteDate(Context cont) {
        try

        {
            dbh = new DBHelper(cont);
            db = dbh.getWritableDatabase();
            String date = "2017-01-31";
            db.execSQL("DELETE FROM diary_entries WHERE time_stamp LIKE '2017-02-01%'");
            System.out.print("");
            db.close();
        } catch (
                Exception ex
                )

        {
            ex.printStackTrace();
            ex.printStackTrace();
        }

    }

}
