package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.mmalo.prototype2.Controllers.CameraController;
import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mmalo on 13/01/2017.
 */

public class OptionsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        //Toast t = Toast.makeText(this, "This is the correct", Toast.LENGTH_LONG);
        //t.show();
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
            db.execSQL("DELETE FROM diary_entries WHERE time_stamp LIKE '2017-01-31%'");
            System.out.print("");
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
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
