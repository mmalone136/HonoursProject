package com.example.mmalo.prototype2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.Models.Day;
import com.example.mmalo.prototype2.Models.DiaryData;
import com.example.mmalo.prototype2.Models.Week;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by mmalo on 17/01/2017.
 */

public class SumOptions extends AppCompatActivity {

    public ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
    public ArrayList<String> uniqueDates = new ArrayList<String>();
    String[] weekData;
    Date weekStart;
    Date weekEnd;
    int step;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_options);
        step = 0;
        updateView(step);
    }


    public void getCurrentWeek(int step) {
        //TODO: Refactor
        Calendar c = GregorianCalendar.getInstance();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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

    public String[] getListForWeek(ArrayList<String> CurrentWeek) {

        String[] weekData = {step + " Monday - No Entries" , step + " Tuesday - No Entries", step + " Wednesday - No Entries", step + " Thursday - No Entries",
                step + " Friday - No Entries", step + " Saturday - No Entries", step + " Sunday - No Entries"};

        for (String curr : CurrentWeek) {
            //Check day of week for corresponding date
            //Add to appropriate array position


            Date currDate = Date.valueOf(curr);
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(currDate);
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            SimpleDateFormat format = new SimpleDateFormat("EEEE");
            String dayString = format.format(cal.getTime());

            int day = cal.get(Calendar.DAY_OF_WEEK);
            System.out.print("");
            if (day > 1) {
                weekData[day - 2] = curr + " | " + dayString;
            } else if (day == 1) {
                //Sunday previously at day 1, move to position 6 for end of week
                weekData[6] = curr + " | " + dayString;
            }
        }

        return weekData;
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
        weekData = getListForWeek(CurrentWeek);
        //Set adapter
        setListAdapter();
    }

    public void setListAdapter() {
        ListView datelist = (ListView) findViewById(R.id.listDates);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weekData);
        datelist.setAdapter(adapter);
        datelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String curr = uniqueDates.get(position);
                String curr = weekData[position];
                if (!curr.equals("")) {
                    DateviewActivity.date = curr.substring(0, 10);
                    Intent i = new Intent(getBaseContext(), DateviewActivity.class);
                    //i.putExtra("diary_data", curr);
                    startActivity(i);
                }
            }
        });
    }

    public void updateWeek(View v) {
        String tag = v.getTag().toString();

        if (tag.equals("+") && step<1) {
            step++;
            updateView(step);
        } else if (tag.equals("-")) {
            step--;
            updateView(step);
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
