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
        //Create activity and set up necessary variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateview);
        DataHolder.readData(this);
        dbCont = new DBContainer();
        countData = new ContentValues();

        //Get list of entries for current passed in date
        init = dbCont.readEntriesForDate(this,date);

        //Check no error occurred on reading data - init should never be empty
        if(init == null){
            //Show error message - go back a page
            Toast toast = Toast.makeText(this,"An error occured while reading data for: " + date,Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        }

        //Set up more initialisation  of references and variables
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
        //Set heading at top of screen to be current day and date
        SimpleDateFormat headForm = new SimpleDateFormat("EEEE\t\tdd/MM/yyyy");
        String heading = headForm.format(entries.get(0).getTimestamp());
        TextView head = (TextView) findViewById(R.id.tvHeader);
        head.setText(heading);
    }

    /**
     * Initisation of values and variables to allow activity to work.
     *
     * @param sortFlag the sort flag
     */
    public void initVals(Boolean sortFlag){

        //Check which sorting method to use
        if(sortFlag) {
            //Sorted by time
            entries = init;
        }else{
            //Sorted by meal type
            entries = sorted;
        }
        setButtonText(sortFlag);

        SimpleDateFormat sdForm = new SimpleDateFormat("HH:mm"); //:ss\t -\t dd/MM/yyyy");

        //Create list of string data to store entry data in displayable format
        ArrayList<String[]> listData = new ArrayList<>();
        for (DiaryData d : entries) {
            //Read timestamp from DiaryData object and format
            Timestamp dTimestamp = d.getTimestamp();
            String dateString = sdForm.format(dTimestamp);

            //Get meal from DiaryData object
            String meal = d.getMeal();

            //Create array to store the strings and add to overall list
            String [] curr = new String[2];
            curr[0] = meal;
            curr[1] = dateString;
            listData.add(curr);
        }
        //Call to setup listview adapter
        setListAdapter(listData);
    }

    /**
     * Flip sort - use the other sort method as was currently being used.
     *
     * @param v the v
     */
    public void flipSort(View v){
        flag = !flag;
        initVals(flag);
    }

    /**
     * Set sort button text based on which sorting method has been used
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
            //Get listview reference
            ListView entryList = (ListView) findViewById(R.id.listEntries);

            //Setup listview with custom adapter type
            entryList.setAdapter(new CustomAdapter(this, listData, 2));

            //Setup onClick listener for the item onClick
            entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Pass data to next activity based on current listview position
                    DiaryData curr = entries.get(position);
                    SummaryActivity.counts = countData;
                    SummaryActivity.theentry = curr;

                    //Create and start net activity - Summary
                    Intent i = new Intent(getBaseContext(), SummaryActivity.class);
                    startActivity(i);
                }
            });
        }catch(Exception e){
            //It has done an error setting ListView
            e.printStackTrace();
        }

    }

    /**
     * Split initial array list (sorted by time) into each type to allow display by type.
     *
     * @return the array list
     */
    public ArrayList<DiaryData> splitLists(){

        //Create holder lists for each meal type
        ArrayList<DiaryData> breakfast = new ArrayList<DiaryData>();
        ArrayList<DiaryData> lunch = new ArrayList<DiaryData>();
        ArrayList<DiaryData> dinner = new ArrayList<DiaryData>();
        ArrayList<DiaryData> snacks = new ArrayList<DiaryData>();
        ArrayList<DiaryData> drinks = new ArrayList<DiaryData>();

        for(DiaryData dd : init)
        {
            //Check each entry found and add to specific list for its meal type
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
        //Concatenate lists so they are sorted in meal type order
        ArrayList<DiaryData> sortedList = new ArrayList<DiaryData>();
        sortedList.addAll(breakfast);
        sortedList.addAll(lunch);
        sortedList.addAll(dinner);
        sortedList.addAll(snacks);
        sortedList.addAll(drinks);

        //Check size of each list
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
        //Add to count data for each list type
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
