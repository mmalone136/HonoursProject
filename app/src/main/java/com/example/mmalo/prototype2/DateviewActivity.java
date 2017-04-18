package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.DB.DBContainer;
import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.ExpListClasses.CustomAdapter;
import com.example.mmalo.prototype2.Models.DataHolder;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mmalo on 30/01/2017.
 */
public class DateviewActivity extends AppCompatActivity {

    /**
     * The Entries.
     */
    public ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
    /**
     * The Init.
     */
    public ArrayList<DiaryData> init = new ArrayList<DiaryData>();
    /**
     * The Sorted.
     */
    public ArrayList<DiaryData> sorted = new ArrayList<DiaryData>();
    /**
     * The Count data.
     */
    public ContentValues countData;
    /**
     * The constant date.
     */
    public static String date;
    /**
     * The Flag.
     */
    public boolean flag;
    /**
     * The Db cont.
     */
    public DBContainer dbCont;
    /**
     * The Sort button.
     */
    Button sortButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateview);
        DataHolder.readData(this);
        dbCont = new DBContainer();
        countData = new ContentValues();
        init = dbCont.readEntriesForDate(this,date);
        if(init == null){
            Toast toast = Toast.makeText(this,"An error occured while reading data for: " + date,Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        }
        sortButton = (Button) findViewById(R.id.buttonSortEntries);
        sorted = splitLists();
        flag = true;
        initVals(flag);
        setHeading();
    }

    /**
     * Set heading.
     */
    public void setHeading(){

        SimpleDateFormat headForm = new SimpleDateFormat("EEEE\t\tdd/MM/yyyy");
        String heading = headForm.format(entries.get(0).getTimestamp());
        TextView head = (TextView) findViewById(R.id.tvHeader);
        head.setText(heading);
    }

    /**
     * Init vals.
     *
     * @param sortFlag the sort flag
     */
    public void initVals(Boolean sortFlag){

        if(sortFlag) {
            entries = init;
        }else{
            entries = sorted;
        }
        setButtonText(sortFlag);
        SimpleDateFormat sdForm = new SimpleDateFormat("HH:mm"); //:ss\t -\t dd/MM/yyyy");

        ArrayList<String[]> listData = new ArrayList<>();
        for (DiaryData d : entries) {
            Timestamp dTimestamp = d.getTimestamp();


            String dateString = sdForm.format(dTimestamp);
            String meal = d.getMeal();
            String [] curr = new String[2];

            curr[0] = meal;
            curr[1] = dateString;
            listData.add(curr);
        }
        System.out.println("");

        setListAdapter(listData);
    }

    /**
     * Flip sort.
     *
     * @param v the v
     */
    public void flipSort(View v){
        flag = !flag;
        initVals(flag);
    }

    /**
     * Set button text.
     *
     * @param sortFlag the sort flag
     */
    public void setButtonText(boolean sortFlag){
        if(sortFlag) {
            sortButton.setText(R.string.sortMeal);
        }else{
            sortButton.setText(R.string.sortTime);
        }

    }

    /**
     * Set list adapter.
     *
     * @param listData the list data
     */
    public void setListAdapter(ArrayList<String[]> listData){
        try {

            ListView entryList = (ListView) findViewById(R.id.listEntries);

            //ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);

            //entryList.setAdapter(adapter);
            entryList.setAdapter(new CustomAdapter(this, listData, 2));

            entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //activity.dateselected = dates.get(position);

                    DiaryData curr = entries.get(position);
                    SummaryActivity.counts = countData;
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

    /**
     * Split lists array list.
     *
     * @return the array list
     */
    public ArrayList<DiaryData> splitLists(){
        ArrayList<DiaryData> breakfast = new ArrayList<DiaryData>();
        ArrayList<DiaryData> lunch = new ArrayList<DiaryData>();
        ArrayList<DiaryData> dinner = new ArrayList<DiaryData>();
        ArrayList<DiaryData> snacks = new ArrayList<DiaryData>();
        ArrayList<DiaryData> drinks = new ArrayList<DiaryData>();

        for(DiaryData dd : init)
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


        getCountsFromLists(breakfast, lunch,dinner);


        return sortedList;
    }

    /**
     * Get counts from lists.
     *
     * @param breakList the break list
     * @param lunchList the lunch list
     * @param dinnList  the dinn list
     */
    public void getCountsFromLists(ArrayList<DiaryData> breakList,ArrayList<DiaryData> lunchList,ArrayList<DiaryData> dinnList){
        countData.put("BreakfastCount",breakList.size());
        countData.put("LunchCount",lunchList.size());
        countData.put("DinnerCount",dinnList.size());
    }


    /**
     * Go back.
     *
     * @param v the v
     */
    public void goBack(View v){
        Intent i = new Intent(getBaseContext(), WeekviewActivity.class);
        this.startActivity(i);
    }

}
