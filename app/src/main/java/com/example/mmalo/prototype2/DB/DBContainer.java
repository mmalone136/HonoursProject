package com.example.mmalo.prototype2.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Models.DataHolder;
import com.example.mmalo.prototype2.Models.DiaryData;
import com.example.mmalo.prototype2.OptionsActivity;
import com.example.mmalo.prototype2.R;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

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

    public long insertEntry(DiaryData dd, Context cont,int theFV, int theDR) {
        DBHelper dbh = new DBHelper(cont);
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put("comment_data", dd.getComment());
        vals.put("audio_data", dd.getSpokenData());
        vals.put("time_stamp", String.valueOf(dd.getTimestamp()));
        vals.put("meal", dd.getMeal());
        vals.put("filepath", dd.getFilepath());
        vals.put("fv_count", theFV);
        vals.put("drink_count", theDR);

        long rowID = db.insert("diary_entries", null, vals);
        return rowID;
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

    //Update called from SummaryActivity
    public boolean updateCountsDB(Context cont, Date theDate, ContentValues cv, String [] updateArgs) {
        try {
            dbh = new DBHelper(cont);
            db = dbh.getWritableDatabase();

            //String sql = "UPDATE counts SET fv_count = fv_count + ?, drink_count = drink_count + ? WHERE time_stamp = ?";
            //String[] args = {String.valueOf(fvCount), String.valueOf(drinkCount), String.valueOf(theDate)};

            long a = db.update("counts",cv,"time_stamp = ?", updateArgs);

            if (a == 0) {
                cv.put("time_stamp", theDate.toString());
                long rowID = db.insert("counts", null, cv);
                db.close();
            }

            db.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void updateCountsDB2(Context cont, int fvCount, int drinkCount, String mealChoice, Timestamp ts) {
        try {
            dbh = new DBHelper(cont);
            db = dbh.getWritableDatabase();
            Date theDate = new Date(ts.getTime());

            int f = fvCount + DataHolder.todaysFV;
            int d = drinkCount + DataHolder.todaysDrinks;

            ContentValues cv = new ContentValues();
            cv.put("fv_count", f);
            cv.put("drink_count", d);

            switch(mealChoice)
            {
                case"Breakfast":
                    cv.put("hadBreakfast", true);
                    break;
                case"Lunch":
                    cv.put("hadLunch", true);
                    break;
                case"Dinner":
                    cv.put("hadDinner", true);
                    break;
            }

            String[] updateArgs = {theDate.toString()};

            String sql = "UPDATE counts SET fv_count = fv_count + ?, drink_count = drink_count + ? WHERE time_stamp = ?";
            String[] args = {String.valueOf(fvCount), String.valueOf(drinkCount), String.valueOf(theDate)};

            long a = db.update("counts",cv,"time_stamp = ?", updateArgs);

            if (a == 0) {
                cv.put("time_stamp", theDate.toString());
                long rowID = db.insert("counts", null, cv);
                System.out.print("");
            }
            DataHolder.todaysFV = f;
            DataHolder.todaysDrinks = d;
            db.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
        }
    }

    public void updateComment(Context cont, ContentValues cv, String [] updateArgs) {
        try {
            dbh = new DBHelper(cont);
            db = dbh.getWritableDatabase();

            db.update("diary_entries", cv, "time_stamp = ?", updateArgs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public ArrayList<DiaryData> readEntriesForDate(Context cont, String date) {
        DBHelper dbh = new DBHelper(cont);
        SQLiteDatabase db = dbh.getReadableDatabase();

        String[] projection = {
                "entry_ID", "filepath", "comment_data", "time_stamp", "meal","fv_count","drink_count"
        };
        String select = "time_stamp Like ?";
        String[] selArgs = {"%" + date + "%"};
        Cursor cursor = db.query("diary_entries", projection, select, selArgs, null, null, null);

        ArrayList<DiaryData> entries = new ArrayList<DiaryData>();

        if(cursor.moveToFirst()) {
            do {
                try {
                    DiaryData curr;
                    //byte[] pic = cursor.getBlob(cursor.getColumnIndexOrThrow("photo_data"));
                    String comm = cursor.getString(cursor.getColumnIndexOrThrow("comment_data"));
                    String file = cursor.getString(cursor.getColumnIndexOrThrow("filepath"));
                    String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
                    String theMeal = cursor.getString(cursor.getColumnIndexOrThrow("meal"));
                    int fvCount = cursor.getInt(cursor.getColumnIndexOrThrow("fv_count"));
                    int drCount = cursor.getInt(cursor.getColumnIndexOrThrow("drink_count"));

                    Timestamp theTime = Timestamp.valueOf(tID);
                    curr = new DiaryData(null, comm, null, theTime, theMeal, file,fvCount,drCount);

                    entries.add(curr);
                } catch (Exception e) {
                    e.printStackTrace();

                    return null;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return entries;
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

    public void insertEntryTemp(DiaryData dd, Context cont) {
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
}
