package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.text.method.DateTimeKeyListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mmalo on 02/11/2016.
 */

public class PTakenActivity extends AppCompatActivity {

    public static byte[] photoData;
    public static Bitmap thePic;
    String commentData;
    String mealChoice;
    Timestamp timetaken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptaken);
        initValues();
        Toast t = Toast.makeText(this, "PTAKEN_ACT", Toast.LENGTH_LONG);
        t.show();
        //ArrayList<DiaryData> seven = doDBThings();
        //for (DiaryData d:seven) {
        //    insertEntry(d);
        //}
        //readAllEntries();
    }

    public ArrayList<DiaryData> doDBThings(){

        DBHelper dbh = new DBHelper(getApplicationContext());

        SQLiteDatabase db = dbh.getWritableDatabase();

        ArrayList<DiaryData> DDList = new ArrayList<DiaryData>();

        for(int i = 0; i<25; i++){
            DiaryData dd = new DiaryData();

            byte[] photo = new byte[7];
            byte[] audio = new byte[7];
            String comment = "This is the comment : " + i;
            Timestamp t = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            Timestamp ts = new java.sql.Timestamp(date.getTime());


            String meal;
            if(i<10)
            {
                meal= "Breakfast";
            }else if(i>=10 && i<20)
            {
                meal = "Lunch";
            }else{

                meal="Dinner";
            }

            dd.setComment(comment);
            dd.setPhotoData(photo);
            dd.setSpokenData(audio);
            dd.setTimestamp(ts);
            dd.setMeal(meal);

            DDList.add(dd);
        }



        System.out.print("");
        return DDList;
    }


    public long insertEntry(DiaryData dd){
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues vals = new ContentValues();
        //vals.put("entry_ID",1);
        vals.put("photo_data",dd.getPhotoData());
        vals.put("comment_data",dd.getComment());
        vals.put("audio_data",dd.getSpokenData());
        vals.put("time_stamp", String.valueOf(dd.getTimestamp()));
        vals.put("meal",dd.getMeal());

        long rowID = db.insert("diary_entries",null,vals);
        return rowID;
    }

    public ArrayList<DiaryData> readAllEntries(){
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        String[] projection = {
          "entry_ID","photo_data","comment_data","time_stamp","meal"
        };

        //String selection = "column_name = ?";
        //String [] selectionArgs = {"value"};
        //String sortOrder = "column_name DESC";

        //ArgOrder => Table,Columns, Columns From Where, Values from where, togroup, tofilter groups, sortorder
        Cursor cursor = db.query("diary_entries",projection,null,null,null,null,null);

        System.out.print("");
        ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
        ArrayList<String> comments = new ArrayList<String>();
        ArrayList<String> times = new ArrayList<String>();
        int i = 0;
        while(cursor.moveToNext()) {
            DiaryData curr;
            byte[] pic = cursor.getBlob(cursor.getColumnIndexOrThrow("photo_data"));
            String comm = cursor.getString(cursor.getColumnIndexOrThrow("comment_data"));
            //Timestamp tID = new Timestamp(cursor.getLong(cursor.getColumnIndexOrThrow("time_stamp")));
            String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
            String theMeal = cursor.getString(cursor.getColumnIndexOrThrow("meal"));

           // comments.add(itemId);
           // times.add(tID);
            //public DiaryData(byte[]pd, String com, byte[] sp,Timestamp ts, String theMeal ){
            Timestamp theTime = Timestamp.valueOf(tID);
            curr = new DiaryData(pic,comm,null,theTime,theMeal);

            entries.add(curr);
            i++;
        }
        cursor.close();

        return entries;
    }



    public void initValues()
    {
        java.util.Date date = new java.util.Date();
        Timestamp ts = new java.sql.Timestamp(date.getTime());
        timetaken = ts;

        ImageView taken = (ImageView) findViewById(R.id.imageTaken);
        taken.setImageBitmap(thePic);

        //https://developer.android.com/guide/topics/ui/controls/spinner.html
        //http://stackoverflow.com/questions/2784081/android-create-spinner-programmatically-from-array

        //Spinner spinner = (Spinner) findViewById(R.id.spinnerMeal);
        //String [] meals = {"Select Meal", "Breakfast", "Lunch", "Dinner", "Snack"};

      //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, meals);

        // Specify the layout to use when the list of choices appears
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
      //  spinner.setAdapter(adapter);
    }


    public void mealButton(View v)
    {
        //if tag == 1,2,3,4 set meal = corresponding value then do:

        mealChoice = v.getTag().toString();



        Button button = (Button) findViewById(R.id.buttonDin);
        button.setVisibility(View.INVISIBLE);
        Button button2 = (Button) findViewById(R.id.buttonSnack);
        button2.setVisibility(View.INVISIBLE);
        Button button3 = (Button) findViewById(R.id.buttonLunch);
        button3.setVisibility(View.INVISIBLE);
        Button button4 = (Button) findViewById(R.id.buttonBreak);
        button4.setVisibility(View.INVISIBLE);



        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);

        Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setVisibility(View.VISIBLE);


    }

    public void submitForm(View v)
    {
        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);
        commentData = comments.getText().toString();

        //commentData =
        DiaryData entry = new DiaryData(photoData,commentData,null,timetaken,mealChoice);
        long l = insertEntry(entry);

        Toast t = Toast.makeText(this, "Entry Submitted Successfully", Toast.LENGTH_LONG);
        t.show();
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);


    }


    public void cancelForm(View v)
    {

        Toast t = Toast.makeText(this, "Entry Cancelled", Toast.LENGTH_LONG);
        t.show();
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);


    }



}
