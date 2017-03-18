package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Controllers.CameraController;
import com.example.mmalo.prototype2.DB.DBContainer;
import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.Models.DataHolder;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by mmalo on 13/01/2017.
 */

public class OptionsActivity extends AppCompatActivity {

    public DBContainer dbCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        dbCont = new DBContainer();
        dbCont.createTables(this);
        //if (DataHolder.dataRead) {
            DataHolder.readData(this);
       // }

        //Button diaryButton = (Button) findViewById(R.id.buttonSummary);
        //LayoutInflater infalInflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        //diaryButton = infalInflater.inflate(R.layout.imagebutton_layout, null);

        //android:drawableLeft="@drawable/diary1"
        //android:gravity="left|center_vertical"
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
        switch (item.getItemId()) {
            case R.id.DelData:
                dbCont.dropTables(getApplicationContext());
                return true;
            case R.id.CreateData:
                dbCont.createTables(getApplicationContext());
                return true;
            case R.id.InsertData:
                ArrayList<DiaryData> seven = doDBThings();
                for (DiaryData d : seven) {
                    dbCont.insertEntryTemp(d, getApplicationContext());
                }
                return true;
            case R.id.DeleteMore:
                dbCont.deleteDate(getApplicationContext());
                return true;
            case R.id.RefreshTotals:
                //readCountData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//--------------Functions for changing forms


    //TakePhoto
    public void openPhotoPreview(View v) {
        CameraController cc = new CameraController();
        boolean hasCam = cc.checkCameraHardware(this);
        System.out.println(hasCam);
        Toast toast;
        toast = Toast.makeText(this, "Has Camera: " + hasCam, Toast.LENGTH_LONG);
        //toast.show();

        int apiLevel = Build.VERSION.SDK_INT;
        Intent i;
        if (apiLevel < 21) {
            i = new Intent(this, CameraActivity.class);
        } else {
            i = new Intent(this, Camera2Activity.class);
        }
        this.startActivity(i);
    }

    //ViewGuide
    public void viewGuide(View v) {
        Intent i = new Intent(getBaseContext(), GuideActivity.class);
        this.startActivity(i);
    }

    //ViewDiary
    public void viewDates(View v) {
        Intent i = new Intent(getBaseContext(), WeekviewActivity.class);
        this.startActivity(i);
    }

    public void loadTutorial(View v) {
        Intent i = new Intent(getBaseContext(), TutorialActivity.class);
        this.startActivity(i);
    }


    public void loadTargets(View v) {
        Intent i = new Intent(getBaseContext(), TargetsActivity.class);
        this.startActivity(i);
    }


//--------------Functions for creating dummy data => Temporary functions


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
        ts = java.sql.Timestamp.valueOf("2017-02-09 19:58:19.0");
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
        ts = java.sql.Timestamp.valueOf("2017-02-07 17:36:12.0");
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
        meal = "Dinner";
        ts = java.sql.Timestamp.valueOf("2017-02-03 17:55:12.0");
        dd.setPhotoData(photo);
        dd.setSpokenData(audio);
        dd.setComment(comment);
        dd.setTimestamp(ts);
        dd.setMeal(meal);
        DDList.add(dd);

        dd = new DiaryData();
        i++;
        comment = "This is the comment : " + i;
        ts = java.sql.Timestamp.valueOf("2017-01-31 08:49:22.0");
        meal = "Breakfast";
        dd.setPhotoData(photo);
        dd.setSpokenData(audio);
        dd.setComment(comment);
        dd.setTimestamp(ts);
        dd.setMeal(meal);
        DDList.add(dd);


        System.out.print("");
        return DDList;
    }

}
