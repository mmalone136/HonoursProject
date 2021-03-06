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
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    /**
     * The Db cont.
     */
    public DBContainer dbCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create activity and initialise necessary variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        dbCont = new DBContainer();
        dbCont.createTables(this);
        DataHolder.readData(this);
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
        //Select item in list, do this
        switch (item.getItemId()) {
            case R.id.ImgRefs:
                showImageRefs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showImageRefs(){
        LinearLayout optionsMenu  = (LinearLayout) findViewById(R.id.LinLayOptionsMenu);
        optionsMenu.setVisibility(View.INVISIBLE);

        ImageView refs  = (ImageView) findViewById(R.id.ivImageRefs);
        refs.setVisibility(View.VISIBLE);
    }

    public void hideImageRefs(View v){
        LinearLayout optionsMenu  = (LinearLayout) findViewById(R.id.LinLayOptionsMenu);
        optionsMenu.setVisibility(View.VISIBLE);

        ImageView refs  = (ImageView) findViewById(R.id.ivImageRefs);
        refs.setVisibility(View.INVISIBLE);
    }



//--------------Functions for changing forms


    /**
     * Open photo preview.
     *
     * @param v the v
     */
    public void openPhotoPreview(View v) {
        //Check API level and choose correct camera activity for API level
        int apiLevel = Build.VERSION.SDK_INT;
        Intent i;
        if (apiLevel < 21) {
                i = new Intent(this, CameraActivity.class);
        } else {
            i = new Intent(this, Camera2Activity.class);
        }
        this.startActivity(i);
    }

    /**
     * Starts and opens "Healthy Messages" Section
     *
     * @param v the v
     */
    public void viewGuide(View v) {
        Intent i = new Intent(getBaseContext(), GuideActivity.class);
        this.startActivity(i);
    }

    /**
     * Starts and opens "Weekview" of the diary
     *
     * @param v the v
     */
    public void viewDates(View v) {
        WeekviewActivity.showNotif = false;
        Intent i = new Intent(getBaseContext(), WeekviewActivity.class);
        this.startActivity(i);
    }

    /**
     * Loads and opens tutorial.
     *
     * @param v the v
     */
    public void loadTutorial(View v) {
        Intent i = new Intent(getBaseContext(), TutorialActivity.class);
        this.startActivity(i);
    }


    /**
     * Open targets section.
     *
     * @param v the v
     */
    public void loadTargets(View v) {
        Intent i = new Intent(getBaseContext(), TargetsActivity.class);
        this.startActivity(i);
    }


//--------------Functions for creating dummy data => Temporary functions


    /**
     * Do db things array list.
     *
     * @return the array list
     */
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
