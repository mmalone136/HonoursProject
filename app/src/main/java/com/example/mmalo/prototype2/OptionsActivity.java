package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Controllers.CameraController;
import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mmalo on 13/01/2017.
 */

public class OptionsActivity extends AppCompatActivity {

    public static int todaysFV;
    public static int todaysDrinks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        //Toast t = Toast.makeText(this, "This is the correct", Toast.LENGTH_LONG);
        //t.show();
        readCountData();
    }

    public void deleteDBData(View v) {
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS diary_entries");
        System.out.print("");
        db.close();
    }

    public void deleteTiday(View v) {
        try {
            DBHelper dbh = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbh.getWritableDatabase();
            String date = "2017-01-31";
            db.execSQL("DELETE FROM diary_entries WHERE time_stamp LIKE '2017-02-01%'");
            System.out.print("");
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        DBHelper dbh;
        SQLiteDatabase db;
        switch (item.getItemId()) {
            case R.id.DelData:
                dbh = new DBHelper(getApplicationContext());
                db = dbh.getWritableDatabase();

                db.execSQL("DROP TABLE IF EXISTS diary_entries");
                db.execSQL("DROP TABLE IF EXISTS counts");
                System.out.print("");
                db.close();
                return true;
            case R.id.CreateData:
                dbh = new DBHelper(getApplicationContext());
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
                                " drink_count INT)";
                db.execSQL(SQL_CREATE_COUNTS);
                System.out.print("");
                db.close();
                return true;
            case R.id.InsertData:
                ArrayList<DiaryData> seven = doDBThings();
                for (DiaryData d : seven) {
                    insertEntry(d);
                }
                return true;
            case R.id.DeleteMore:
                try {
                    dbh = new DBHelper(getApplicationContext());
                    db = dbh.getWritableDatabase();
                    String date = "2017-01-31";
                    db.execSQL("DELETE FROM diary_entries WHERE time_stamp LIKE '2017-02-01%'");
                    System.out.print("");
                    db.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ex.printStackTrace();
                }
                return true;
            case R.id.RefreshTotals:
                readCountData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void createTable(View v) {
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getWritableDatabase();


        String SQL_CREATE_ENTRIES =
                "CREATE TABLE diary_entries (" +
                        " entry_ID INTEGER PRIMARY KEY," +
                        " photo_data BLOB ," +
                        " comment_data TEXT," +
                        " audio_data BLOB ," +
                        " time_stamp TEXT," +
                        " filepath TEXT," +
                        " meal TEXT)";

        db.execSQL(SQL_CREATE_ENTRIES);
        System.out.print("");
        db.close();
    }

    public void viewDates(View v) {
        Intent i = new Intent(getBaseContext(), SumOptions.class);
        this.startActivity(i);
    }

    public void doThingsNow(View v) {
        ArrayList<DiaryData> seven = doDBThings();
        for (DiaryData d : seven) {
            insertEntry(d);
        }


    }

    public void readCountData(){


//        "CREATE TABLE IF NOT EXISTS counts (" +
//                " entry_ID INTEGER PRIMARY KEY," +
//                " time_stamp TEXT," +
//                " fv_count INT," +
//                " drink_count INT)";


        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        java.util.Date theDate = new java.util.Date();
        Date today = new Date(theDate.getTime());

        String[] projection = {
                "entry_ID", "fv_count", "time_stamp", "drink_count"
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

                   // Timestamp theTime = Timestamp.valueOf(tID);
                   // Date seven = new Date(theTime.getTime());

                    todaysFV = fvCount;
                    todaysDrinks = drinkCount;

                } catch (Exception e) {
                    e.printStackTrace();
                    e.printStackTrace();

                    todaysFV = 0;
                    todaysDrinks = 0;

                }


            } while (cursor.moveToNext());


        }
        cursor.close();
        TextView fv = (TextView) findViewById(R.id.textViewFV);
        TextView dr = (TextView) findViewById(R.id.textViewDrinks);


        fv.setText("Fruit & Veg: " + todaysFV);
        dr.setText("Drinks: " + todaysDrinks);


    }

    public ArrayList<DiaryData> doDBThings() {

        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getWritableDatabase();

        ArrayList<DiaryData> DDList = new ArrayList<DiaryData>();

        DiaryData dd = new DiaryData();
        int i = 0;
        byte[] photo = new byte[7];
        byte[] audio = new byte[7];
        String comment = "This is the comment : " + i;
        java.util.Date date = new java.util.Date();
        Timestamp ts = new java.sql.Timestamp(date.getTime());


        ts = java.sql.Timestamp.valueOf("2017-01-17 10:10:10.0");
        String meal = "Breakfast";
        dd.setComment(comment);
        dd.setPhotoData(photo);
        dd.setSpokenData(audio);
        dd.setTimestamp(ts);
        dd.setMeal(meal);
        DDList.add(dd);

        dd = new DiaryData();
        i++;
        comment = "This is the comment : " + i;
        meal = "Lunch";
        ts = java.sql.Timestamp.valueOf("2017-01-17 12:10:10.0");
        dd.setPhotoData(photo);
        dd.setSpokenData(audio);
        dd.setComment(comment);
        dd.setTimestamp(ts);
        dd.setMeal(meal);
        DDList.add(dd);

        dd = new DiaryData();
        i++;
        comment = "This is the comment : " + i;
        meal = "Dinner";
        ts = java.sql.Timestamp.valueOf("2017-01-17 17:36:12.0");
        dd.setPhotoData(photo);
        dd.setSpokenData(audio);
        dd.setComment(comment);
        dd.setTimestamp(ts);
        dd.setMeal(meal);
        DDList.add(dd);

        dd = new DiaryData();
        i++;
        comment = "This is the comment : " + i;
        ts = java.sql.Timestamp.valueOf("2017-01-16 08:17:22.0");
        meal = "Breakfast";
        dd.setPhotoData(photo);
        dd.setSpokenData(audio);
        dd.setComment(comment);
        dd.setTimestamp(ts);
        dd.setMeal(meal);
        DDList.add(dd);

        dd = new DiaryData();
        i++;
        comment = "This is the comment : " + i;
        meal = "Lunch";
        ts = java.sql.Timestamp.valueOf("2017-01-16 12:59:10.0");
        dd.setPhotoData(photo);
        dd.setSpokenData(audio);
        dd.setComment(comment);
        dd.setTimestamp(ts);
        dd.setMeal(meal);
        DDList.add(dd);

        dd = new DiaryData();
        i++;
        comment = "This is the comment : " + i;
        meal = "Dinner";
        ts = java.sql.Timestamp.valueOf("2017-01-16 17:56:12.0");
        dd.setPhotoData(photo);
        dd.setSpokenData(audio);
        dd.setComment(comment);
        dd.setTimestamp(ts);
        dd.setMeal(meal);
        DDList.add(dd);


        dd = new DiaryData();
        i++;
        comment = "This is the comment : " + i;
        meal = "Snack";
        ts = java.sql.Timestamp.valueOf("2017-01-16 19:58:19.0");
        dd.setPhotoData(photo);
        dd.setSpokenData(audio);
        dd.setComment(comment);
        dd.setTimestamp(ts);
        dd.setMeal(meal);
        DDList.add(dd);


        System.out.print("");
        return DDList;
    }


    public void insertEntry(DiaryData dd) {
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues vals = new ContentValues();
        //vals.put("entry_ID",1);
        vals.put("photo_data", dd.getPhotoData());
        vals.put("comment_data", dd.getComment());
        vals.put("audio_data", dd.getSpokenData());
        vals.put("time_stamp", String.valueOf(dd.getTimestamp()));
        vals.put("meal", dd.getMeal());

        long rowID = db.insert("diary_entries", null, vals);
    }


    public void openPhotoPreview(View v) {
        System.out.println(v.getId());
        System.out.println("It did the thing");
        CameraController cc = new CameraController();
        boolean hasCam = cc.checkCameraHardware(this);
        System.out.println(hasCam);
        Toast toast;
        toast = Toast.makeText(this, "Has Camera: " + hasCam, Toast.LENGTH_LONG);
        //toast.show();
        // Camera c = cc.getCameraInstance();

        Intent i = new Intent(this, CameraActivity.class);
        this.startActivity(i);

        //Intent i = new Intent(this, CameraActivity.class);
        //startActivity(i);

    }
}
