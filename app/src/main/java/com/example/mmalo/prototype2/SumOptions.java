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
 * Created by mmalo on 17/01/2017.
 */

public class SumOptions extends AppCompatActivity {

    public ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
    public ArrayList<String> uniqueDates = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_options);

        initVals();
    }


    public void initVals(){

        ListView datelist = (ListView) findViewById(R.id.listDates);
        uniqueDates = readUniqDates();
        entries = readAllEntries();

        ArrayList<Timestamp> ts = new ArrayList<Timestamp>();
        ArrayList<String> dt = new ArrayList<String>();
        for (DiaryData d : entries) {
            Timestamp curr = d.getTimestamp();
            Date seven = new Date(curr.getTime());
            ts.add(curr);

            if(!dt.contains(seven.toString())){
                dt.add(seven.toString());

            }

        }


        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, uniqueDates);

        datelist.setAdapter(adapter);


        datelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //activity.dateselected = dates.get(position);

                String curr = uniqueDates.get(position);
                DateviewActivity.date = curr;
                Intent i = new Intent(getBaseContext(), DateviewActivity.class);
                //i.putExtra("diary_data", curr);
                startActivity(i);
            }
        });


    }

    public ArrayList<DiaryData> readAllEntries() {
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        String[] projection = {
                "entry_ID", "photo_data", "comment_data", "time_stamp", "meal"
        };

        //ArgOrder => Table,Columns, Columns From Where, Values from where, togroup, tofilter groups, sortorder
        Cursor cursor = db.query("diary_entries", projection, null, null, null, null, null);

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

                    Timestamp theTime = Timestamp.valueOf(tID);
                    curr = new DiaryData(null, comm, null, theTime, theMeal, file);

                    entries.add(curr);
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    e.printStackTrace();

                }


            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();

        return entries;
    }






    public ArrayList<String> readUniqDates() {
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        String[] projection = {
                "time_stamp"
        };
        String sort = "time_stamp ASC";

        //ArgOrder => Table,Columns, Columns From Where, Values from where, togroup, tofilter groups, sortorder
        Cursor cursor = db.query("diary_entries", projection, null, null, null, null, sort);
        //db.close();
        System.out.print("");
        ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
        //ArrayList<String> comments = new ArrayList<String>();
        //ArrayList<String> times = new ArrayList<String>();
        int i = 0;

        ArrayList<Timestamp> times = new ArrayList<Timestamp>();
        ArrayList<String> dates = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {

                try {
                    //DiaryData curr;
                    //byte[] pic = cursor.getBlob(cursor.getColumnIndexOrThrow("photo_data"));
                   // String comm = cursor.getString(cursor.getColumnIndexOrThrow("comment_data"));

                    String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
                    //String theMeal = cursor.getString(cursor.getColumnIndexOrThrow("meal"));

                    Timestamp theTime = Timestamp.valueOf(tID);
                   // curr = new DiaryData(pic, comm, null, theTime, theMeal);
                    times.add(theTime);




                        Date seven = new Date(theTime.getTime());


                        if(!dates.contains(seven.toString())){
                            dates.add(seven.toString());

                        }


                   // entries.add(curr);
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    e.printStackTrace();

                }


            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();

        return dates;
    }

}
