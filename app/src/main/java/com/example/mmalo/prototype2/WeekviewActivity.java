package com.example.mmalo.prototype2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.ExpListClasses.CustomAdapter;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by mmalo on 17/01/2017.
 */

public class WeekviewActivity extends AppCompatActivity {

    public ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
    public ArrayList<String> uniqueDates = new ArrayList<String>();
    String[] weekData;
    String[] weekDates;
    ArrayList<String[]> moreWeekData;
    Date weekStart;
    Date weekEnd;
    int step;
    int stepLimit;
    int todayPosition;
    ListView dateList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekview);

        dateList = (ListView) findViewById(R.id.listDates);
        Calendar calendar = Calendar.getInstance();
        todayPosition = calendar.get(Calendar.DAY_OF_WEEK);
        step = 0;
        updateView(step);
        stepLimit = calculateEarliest();

    }


    public void getCurrentWeek(int step) {
        //TODO: Refactor
        Calendar c = GregorianCalendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, (7 * step));
        c.setFirstDayOfWeek(Calendar.MONDAY);

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.clear(Calendar.HOUR);
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.SECOND);
        c.clear(Calendar.MILLISECOND);

        java.util.Date start = c.getTime();

        c.add(Calendar.DATE, 6);
        java.util.Date end = c.getTime();
        Date first = new Date(start.getTime());
        Date last = new Date(end.getTime());

        weekStart = first;
        weekEnd = last;
    }

    public ArrayList<String> readUniqueDates() {
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

        ArrayList<Timestamp> times = new ArrayList<Timestamp>();
        ArrayList<String> dates = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));

                    Timestamp theTime = Timestamp.valueOf(tID);
                    times.add(theTime);

                    Date seven = new Date(theTime.getTime());

                    SimpleDateFormat s = new SimpleDateFormat("EEEE");

                    String day = s.format(seven);

                    if (!dates.contains(seven.toString())) {
                        dates.add(seven.toString());//+ " | " + day
                    }
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

    public ArrayList<String> readDatesBetween(Date start, Date end) {
        ArrayList<String> toReturn = new ArrayList<String>();

        java.util.Date utilStart = new Date(start.getTime());
        java.util.Date utilEnd = new Date(end.getTime());

        //0 if the argument Date is equal to this Date;
        // a value less than 0 if this Date is before the Date argument;
        // value greater than 0 if this Date is after the Date argument.

        for (String curr : uniqueDates) {
            Date currDate = Date.valueOf(curr);
            int compStart = start.compareTo(currDate);
            int compEnd = end.compareTo(currDate);
            if (compStart <= 0 && compEnd >= 0) {
                toReturn.add(currDate.toString());
            }
        }
        return toReturn;
    }

    public ArrayList<String[]> getListForWeek(ArrayList<String> CurrentWeek) {

        ArrayList<String[]> dataLists = new ArrayList<>();

        String[] weekData = {"# - No Entries", "# - No Entries", "# - No Entries", "# - No Entries",
                "# - No Entries", "# - No Entries","# - No Entries"};

        weekDates = new String[7];

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        SimpleDateFormat formDates = new SimpleDateFormat("dd/MM/yyyy");

        for (String curr : CurrentWeek) {

            Date currDate = Date.valueOf(curr);
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(currDate);
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            String dayString = formatter.format(cal.getTime());
            String dateString = formDates.format(currDate);

            int day = cal.get(Calendar.DAY_OF_WEEK);
            System.out.print("");

            String temp = "";

            if(step==0 && day ==todayPosition){
                temp = "\tTODAY";
            }

            if (day > 1) {
                weekData[day - 2] = dayString + "\t\t\t" + dateString+temp;
            } else if (day == 1) {
                //Sunday previously at day 1, move to position 6 for end of week
                weekData[6] = dayString + "\t\t\t" + dateString+temp;
            }
        }

        for(int i = 0;i<weekData.length;i++){
            String curr = weekData[i];
            String [] currList;

            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(weekStart);

            java.util.Date temp;
            Date current;

            cal.add(Calendar.DATE, i);
            temp = cal.getTime();
            current = new Date(temp.getTime());

            java.util.Date currDate = cal.getTime();
            String dayString = formatter.format(currDate);
            String dateString = formDates.format(currDate);


            if(curr.charAt(0)=='#')
            {
                weekDates[i] = String.valueOf(current);
                weekData[i] = dayString + "\t\t\t" + dateString + "\t\tNO ENTRIES";
                String [] newOne = {dayString, dateString, "NO ENTRIES"};
                currList = newOne;

            }else{
                weekDates[i] = String.valueOf(current);
                String [] newOne = {dayString, dateString, ""};
                currList = newOne;
            }
            dataLists.add(currList);
        }

        return dataLists;
    }

    public int calculateEarliest() {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(weekStart);
        int week = c.get(Calendar.WEEK_OF_YEAR);

        int difference;
        if(uniqueDates.size()>0) {
            Date earliest = Date.valueOf(uniqueDates.get(0));
            c.setTime(earliest);
            int earlier = c.get(Calendar.WEEK_OF_YEAR);
            difference  = -(week - earlier);
        }
        else
        {
            difference = 0;
        }

        return difference;
    }

    public void updateView(int step) {

        //Read unique dates if not done
        if (uniqueDates.size() == 0) {
            uniqueDates = readUniqueDates();
        }
        //Set week start and end
        getCurrentWeek(step);
        //Read inbetween dates
        ArrayList<String> CurrentWeek = readDatesBetween(weekStart, weekEnd);
        //Set Week Data
        moreWeekData = getListForWeek(CurrentWeek);
        //Set adapter
        setListAdapter();
    }

    public void setListAdapter() {


        //http://stackoverflow.com/questions/19615766/android-custom-layout-for-listview
        //http://stackoverflow.com/questions/15832335/android-custom-row-item-for-listview

        //THIS ONE?
        //http://stackoverflow.com/questions/15832335/android-custom-row-item-for-listview
        //dateList = (ListView) findViewById(R.id.listDates);
        //ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weekData);
        //ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.imagebutton_layout,R.id.tvChild,weekData);

        //dateList.setAdapter(adapter);

        dateList.setAdapter(new CustomAdapter(this, moreWeekData));

        //dateList.setAdapter(new CustomAdapter(this, new String[] { "data1","data2" }));

        dateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String curr = uniqueDates.get(position);
                String curr = weekDates[position];
                String entryCheck = moreWeekData.get(position)[2];
                //No Entries
                if (!entryCheck.equals("NO ENTRIES")) {
                    DateviewActivity.date = curr;
                    Intent i = new Intent(getBaseContext(), DateviewActivity.class);
                    //i.putExtra("diary_data", curr);
                    startActivity(i);
                }
            }
        });
    }

    public void updateWeek(View v) {
        String tag = v.getTag().toString();

        if (tag.equals("+") && step < 1) {
            step++;
            updateView(step);
        } else if (tag.equals("-")) {
            if (step > stepLimit) {
                step--;
                updateView(step);
            }
        } else if (tag.equals("0")) {
            step = 0;
            updateView(step);
        }
    }

    @Override
    public void onBackPressed() {
        backOption(new View(getApplicationContext()));
    }

    public void backOption(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }


}
