package com.example.mmalo.prototype2.Models;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mmalo.prototype2.DateviewActivity;
import com.example.mmalo.prototype2.R;

/**
 * Created by mmalo on 02/03/2017.
 */

public class DateViewData {

    ListAdapter adapter;
    String[] weekData;
    String[] weekDates;

    public void setListAdapter(Context cont, ListView dateList) {
        //ListView dateList = (ListView) findViewById(R.id.listDates);
        //adapter = new ArrayAdapter<ListAdapter>(cont, android.R.layout.simple_list_item_1, weekData);


      //  dateList.setAdapter(adapter);

//        dateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //String curr = uniqueDates.get(position);
//                String curr = weekDates[position];
//                String alsoCurr = weekData[position];
//                String entryCheck = alsoCurr.substring(alsoCurr.length()-10);
//
//                //No Entries
//                if (!entryCheck.equals("NO ENTRIES")) {
//                    DateviewActivity.date = curr;
//                    Intent i = new Intent(getBaseContext(), DateviewActivity.class);
//                    //i.putExtra("diary_data", curr);
//                    startActivity(i);
//                }
//            }
//        });



}}
