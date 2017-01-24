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

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by mmalo on 17/01/2017.
 */

public class SumOptions extends AppCompatActivity {

    public ArrayList<DiaryData> entries = new ArrayList<DiaryData>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_options);

        ListView datelist = (ListView) findViewById(R.id.listDates);

        entries = readAllEntries();

        ArrayList<Timestamp> ts = new ArrayList<Timestamp>();
        for (DiaryData d : entries) {
            ts.add(d.getTimestamp());

        }


        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ts);

        datelist.setAdapter(adapter);


        datelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    byte[] pic = null;//cursor.getBlob(cursor.getColumnIndexOrThrow("photo_data"));
                    String comm = cursor.getString(cursor.getColumnIndexOrThrow("comment_data"));

                    String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
                    String theMeal = cursor.getString(cursor.getColumnIndexOrThrow("meal"));

                    Timestamp theTime = Timestamp.valueOf(tID);
                    curr = new DiaryData(pic, comm, null, theTime, theMeal);

                    entries.add(curr);
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    e.printStackTrace();

                }


            } while (cursor.moveToNext());

        }


        cursor.close();

        return entries;
    }


}
