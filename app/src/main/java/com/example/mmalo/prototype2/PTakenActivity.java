package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public static Timestamp timetaken;
    public static String filename;


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

    public ArrayList<DiaryData> doDBThings() {

        DBHelper dbh = new DBHelper(getApplicationContext());

        SQLiteDatabase db = dbh.getWritableDatabase();

        ArrayList<DiaryData> DDList = new ArrayList<DiaryData>();

        for (int i = 0; i < 25; i++) {
            DiaryData dd = new DiaryData();

            byte[] photo = new byte[7];
            byte[] audio = new byte[7];
            String comment = "This is the comment : " + i;
            Timestamp t = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            java.util.Date date = new java.util.Date();
            Timestamp ts = new java.sql.Timestamp(date.getTime());


            String meal;
            if (i < 10) {
                meal = "Breakfast";
            } else if (i >= 10 && i < 20) {
                meal = "Lunch";
            } else {

                meal = "Dinner";
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


    public long insertEntry(DiaryData dd) {
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues vals = new ContentValues();
        //vals.put("entry_ID",1);
        // vals.put("photo_data",dd.getPhotoData());
        vals.put("comment_data", dd.getComment());
        vals.put("audio_data", dd.getSpokenData());
        vals.put("time_stamp", String.valueOf(dd.getTimestamp()));
        vals.put("meal", dd.getMeal());
        vals.put("filepath", dd.getFilepath());
        long rowID = db.insert("diary_entries", null, vals);
        return rowID;
    }

    public ArrayList<DiaryData> readAllEntries() {
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        String[] projection = {
                "entry_ID", "photo_data", "comment_data", "time_stamp", "meal"
        };

        //String selection = "column_name = ?";
        //String [] selectionArgs = {"value"};
        //String sortOrder = "column_name DESC";

        //ArgOrder => Table,Columns, Columns From Where, Values from where, togroup, tofilter groups, sortorder
        Cursor cursor = db.query("diary_entries", projection, null, null, null, null, null);

        System.out.print("");
        ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
        ArrayList<String> comments = new ArrayList<String>();
        ArrayList<String> times = new ArrayList<String>();
        int i = 0;
        while (cursor.moveToNext()) {
            DiaryData curr;
            //byte[] pic = cursor.getBlob(cursor.getColumnIndexOrThrow("photo_data"));
            String comm = cursor.getString(cursor.getColumnIndexOrThrow("comment_data"));
            //Timestamp tID = new Timestamp(cursor.getLong(cursor.getColumnIndexOrThrow("time_stamp")));
            String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
            String theMeal = cursor.getString(cursor.getColumnIndexOrThrow("meal"));
            String file = cursor.getString(cursor.getColumnIndexOrThrow("filepath"));
            // comments.add(itemId);
            // times.add(tID);
            //public DiaryData(byte[]pd, String com, byte[] sp,Timestamp ts, String theMeal ){
            Timestamp theTime = Timestamp.valueOf(tID);
            curr = new DiaryData(null, comm, null, theTime, theMeal, file);

            entries.add(curr);
            i++;
        }
        cursor.close();

        return entries;
    }


    public void initValues() {
        thePic = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
        //afterTaken(bitmap,data);
        //dataToPass= data;

        ByteArrayOutputStream str = new ByteArrayOutputStream();
        thePic.compress(Bitmap.CompressFormat.JPEG, 100, str);
        Matrix rotationMat = new Matrix();
        //rotationMat.postRotate(90);
        rotationMat.postRotate(0);

        thePic = Bitmap.createBitmap(thePic, 0, 0, thePic.getWidth(), thePic.getHeight(), rotationMat, true);

        ImageView taken = (ImageView) findViewById(R.id.imageTaken);
        taken.setImageBitmap(thePic);

    }


    public void mealButton(View v) {
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

        Button buttonFV = (Button) findViewById(R.id.buttonFV);
        buttonFV.setVisibility(View.VISIBLE);
        Button buttonDR = (Button) findViewById(R.id.buttonDR);
        buttonDR.setVisibility(View.VISIBLE);

    }

    public void addToCounts(View v){
        //Show counter buttons, cancel button and text view


    }


    public void incCount(View v){
        TextView tv = (TextView) findViewById(R.id.textViewIncCount);
        int curr = Integer.valueOf(tv.getText().toString());
        curr++;
        tv.setText(String.valueOf(curr));

    }

    public void decCount(View v){
        TextView tv = (TextView) findViewById(R.id.textViewIncCount);
        int curr = Integer.valueOf(tv.getText().toString());
        if (curr>0) {
            curr--;
        }
        tv.setText(String.valueOf(curr));

    }



    public int saveImageToFile(byte[] datas) {
        FileOutputStream fos;
        //filename = theTime.toString();

        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(datas);
            fos.close();
            return 7;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public void doFood(View v){
        Button buttFood = (Button) findViewById(R.id.buttonFood);
        buttFood.setVisibility(View.INVISIBLE);

        Button buttDrink = (Button) findViewById(R.id.buttonDrink);
        buttDrink.setVisibility(View.INVISIBLE);

    }

    public void doDrink(View v){
        mealChoice = v.getTag().toString();

        Button buttFood = (Button) findViewById(R.id.buttonFood);
        buttFood.setVisibility(View.INVISIBLE);

        Button buttDrink = (Button) findViewById(R.id.buttonDrink);
        buttDrink.setVisibility(View.INVISIBLE);

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

        Button buttonFV = (Button) findViewById(R.id.buttonFV);
        buttonFV.setVisibility(View.VISIBLE);
        Button buttonDR = (Button) findViewById(R.id.buttonDR);
        buttonDR.setVisibility(View.VISIBLE);


    }

    public void submitForm(View v) {
        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);
        commentData = comments.getText().toString();
        String fp = filename;

        //commentData =
        DiaryData entry = new DiaryData(null, commentData, null, timetaken, mealChoice, fp);

        System.out.print("");
        System.out.print("");
        System.out.print("");
        Toast t;
        int success = saveImageToFile(photoData);

        if (success == 7) {
            long l = insertEntry(entry);
            if (l != -1) {
                t = Toast.makeText(this, "Entry Submitted Successfully", Toast.LENGTH_LONG);
            } else {
                t = Toast.makeText(this, "ERROR Submitting Entry", Toast.LENGTH_LONG);
            }
        } else {
            t = Toast.makeText(this, "ERROR Submitting Entry", Toast.LENGTH_LONG);
        }

        thePic.recycle();
        photoData = null;
        t.show();
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);


    }


    public void cancelForm(View v) {

        Toast t = Toast.makeText(this, "Entry Cancelled", Toast.LENGTH_LONG);
        t.show();
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);


    }


}
