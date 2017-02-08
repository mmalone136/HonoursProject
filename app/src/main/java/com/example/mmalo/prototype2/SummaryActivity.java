package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by mmalo on 10/11/2016.
 */

public class SummaryActivity extends AppCompatActivity {

    byte[] currPhoto;
    String currMeal;
    String currComment;
    Timestamp entryTime;
    DiaryData currEntry;
    public static DiaryData theentry;
    ListView dataList;
    Button review;
    Button save;
    EditText comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        initValues();
        System.gc();
    }


    public void initValues() {
        setListView();
        try {
            currPhoto = readImageFromFile(theentry.getFilepath());//theentry.getPhotoData();
            Bitmap bmp = BitmapFactory.decodeByteArray(currPhoto, 0, currPhoto.length);
            ImageView summaryPhoto = (ImageView) findViewById(R.id.imagePhoto);
            summaryPhoto.setImageBitmap(bmp);

        }catch(Exception ex)
        {
            ex.printStackTrace();

        }
    }

    public void backPress(View v){
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        System.gc();
        finish();
        return;
    }


    public void setListView(){

        ArrayList<String> stringsofthings = new ArrayList<String>();

        String counts = "Fruit & Veg: " + theentry.getFvCount() + " | Drinks: " + theentry.getDrCount();

        stringsofthings.add(theentry.getMeal());
        stringsofthings.add(String.valueOf(theentry.getTimestamp()));
        stringsofthings.add(theentry.getComment());
        stringsofthings.add(counts);

        dataList = (ListView) findViewById(R.id.listViewOfDatas);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringsofthings);

        dataList.setAdapter(adapter);
    }

    public void reviewData(View v) {
        //Hide List View, hide review button
        //Show Text box containing existing text, allow typing to edit, show save button

        dataList.setVisibility(View.INVISIBLE);

        comments = (EditText) findViewById(R.id.textComms);
        comments.setVisibility(View.VISIBLE);
        comments.setText(theentry.getComment());

        review = (Button) findViewById(R.id.buttonReview);
        review.setVisibility(View.INVISIBLE);

        save = (Button) findViewById(R.id.buttonSave);
        save.setVisibility(View.VISIBLE);
    }

    public void updateDatabase(String comment){
        try {
            DBHelper dbh = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbh.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("comment_data", comment);
            String[] updateArgs = {theentry.getTimestamp().toString()};
            db.update("diary_entries", cv, "time_stamp = ?", updateArgs);
        }catch(Exception ex){
            ex.printStackTrace();
            ex.printStackTrace();
        }


    }


    public void saveReview(View v){
        //Send updated comment to database
        review.setVisibility(View.VISIBLE);
        save.setVisibility(View.INVISIBLE);

        String updated = comments.getText().toString();
        dataList.setVisibility(View.VISIBLE);
        theentry.setComment(updated);
        comments.setText(updated);
        comments.setVisibility(View.INVISIBLE);

        setListView();
        updateDatabase(updated);
    }



    public byte[] readImageFromFile(String filename) {
        try {

            InputStream is = openFileInput(filename);

            byte[] photodata = org.apache.commons.io.IOUtils.toByteArray(is);

            System.out.println("");
            return photodata;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
