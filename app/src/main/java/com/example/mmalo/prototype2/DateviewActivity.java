package com.example.mmalo.prototype2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by mmalo on 30/01/2017.
 */

public class DateviewActivity extends AppCompatActivity {

    public ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
    public ArrayList<DiaryData> init = new ArrayList<DiaryData>();
    public ArrayList<DiaryData> sorted = new ArrayList<DiaryData>();
    public static String date;
    public boolean flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateview);

        init = readEntriesForDate(date);
        flag = true;
        initVals(flag);
    }


    public void initVals(Boolean sortFlag){
        if(sortFlag) {
            entries = init;
        }else{
            sorted = splitLists();
            entries = sorted;
        }

        ArrayList<String> listData = new ArrayList<String>();
        for (DiaryData d : entries) {
            //TODO: Reformat this for output
            Timestamp dTimestamp = d.getTimestamp();

            String curr = d.getMeal() + " - " + dTimestamp.toString();
            listData.add(curr);
        }
        System.out.println("");

        setListAdapter(listData);
    }

    public void flipSort(View v){
        flag = !flag;
        initVals(flag);
    }

    public void setListAdapter(ArrayList<String> listData){
        try {

            ListView entryList = (ListView) findViewById(R.id.listEntries);

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);

            entryList.setAdapter(adapter);


            entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //activity.dateselected = dates.get(position);

                    DiaryData curr = entries.get(position);
                    SummaryActivity.theentry = curr;
                    Intent i = new Intent(getBaseContext(), SummaryActivity.class);
                    //i.putExtra("diary_data", curr);
                    startActivity(i);
                }
            });
        }catch(Exception e){

            e.printStackTrace();
            e.printStackTrace();
        }

    }

    public ArrayList<DiaryData> splitLists(){
        ArrayList<DiaryData> breakfast = new ArrayList<DiaryData>();
        ArrayList<DiaryData> lunch = new ArrayList<DiaryData>();
        ArrayList<DiaryData> dinner = new ArrayList<DiaryData>();
        ArrayList<DiaryData> snacks = new ArrayList<DiaryData>();
        ArrayList<DiaryData> drinks = new ArrayList<DiaryData>();

        for(DiaryData dd : entries)
        {
            switch(dd.getMeal())
            {
                case("Breakfast"):
                    breakfast.add(dd);
                break;
                case("Lunch"):
                    lunch.add(dd);
                    break;
                case("Dinner"):
                    dinner.add(dd);
                    break;
                case("Snack"):
                    snacks.add(dd);
                    break;
                case("Drink"):
                    drinks.add(dd);
                    break;
            }
        }
        ArrayList<DiaryData> sortedList = new ArrayList<DiaryData>();
        sortedList.addAll(breakfast);
        sortedList.addAll(lunch);
        sortedList.addAll(dinner);
        sortedList.addAll(snacks);
        sortedList.addAll(drinks);

        return sortedList;
    }


    public void goBack(View v){
        onBackPressed();
    }


    public ArrayList<DiaryData> readEntriesForDate(String date) {
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        String[] projection = {
                "entry_ID", "filepath", "comment_data", "time_stamp", "meal","fv_count","drink_count"
                // "entry_ID", "photo_data", "comment_data", "time_stamp", "meal"
        };
        String select = "time_stamp Like ?";
        String[] selArgs = {"%" + date + "%"};
        //ArgOrder => Table,Columns, Columns From Where, Values from where, togroup, tofilter groups, sortorder
        Cursor cursor = db.query("diary_entries", projection, select, selArgs, null, null, null);

        System.out.print("");
        ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
        ArrayList<String> comments = new ArrayList<String>();
        ArrayList<String> times = new ArrayList<String>();
        int i = 0;

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
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return entries;
    }
}
