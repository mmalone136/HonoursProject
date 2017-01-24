package com.example.mmalo.prototype2;

import android.content.Intent;
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

import com.example.mmalo.prototype2.Models.DiaryData;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        initValues();
    }


    public void initValues() {

        ArrayList<String> stringsofthings = new ArrayList<String>();
        stringsofthings.add(theentry.getMeal());
        stringsofthings.add(String.valueOf(theentry.getTimestamp()));
        stringsofthings.add(theentry.getComment());

        ListView dataList = (ListView) findViewById(R.id.listViewOfDatas);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringsofthings);

        currPhoto = theentry.getPhotoData();
        Bitmap bmp = BitmapFactory.decodeByteArray(currPhoto,0,currPhoto.length);
       ImageView summaryPhoto = (ImageView) findViewById(R.id.imagePhoto);
        summaryPhoto.setImageBitmap(bmp);



        dataList.setAdapter(adapter);
    }


    public void nextOption(View v) {


    }
}
