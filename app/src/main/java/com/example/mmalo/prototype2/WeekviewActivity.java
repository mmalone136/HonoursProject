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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by mmalo on 17/01/2017.
 */
public class WeekviewActivity extends AppCompatActivity {

    /**
     * The Entries.
     */
    public ArrayList<DiaryData> entries = new ArrayList<DiaryData>();
    /**
     * The Unique dates.
     */
    public ArrayList<String> uniqueDates = new ArrayList<String>();
    /**
     * The constant showNotif.
     */
    public static boolean showNotif;
    /**
     * The Week data.
     */
    String[] weekData;
    /**
     * The Week dates.
     */
    String[] weekDates;
    /**
     * The More week data.
     */
    ArrayList<String[]> moreWeekData;
    /**
     * The Week start.
     */
    Date weekStart;
    /**
     * The Week end.
     */
    Date weekEnd;
    /**
     * The Step.
     */
    int step;
    /**
     * The Step limit.
     */
    int stepLimit;
    /**
     * The Today position.
     */
    int todayPosition;
    /**
     * The Star bools.
     */
    boolean[] starBools;
    /**
     * The Check arrows.
     */
    boolean checkArrows;
    /**
     * The Date list.
     */
    ListView dateList;
    /**
     * The Week plus.
     */
    ImageButton weekPlus, /**
     * The Week back.
     */
    weekBack;
    /**
     * The Db cont.
     */
    DBContainer dbCont;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekview);
        DataHolder.readData(this);
        boolean[] temp = {false, false, false, false, false, false, false};
        starBools = temp;
        checkArrows=false;
        dbCont = new DBContainer();
        dateList = (ListView) findViewById(R.id.listDates);
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day==0||day==1)
        {
            todayPosition= 5+day;
        }else{
            todayPosition= day-2;
        }

        step = 0;
        weekPlus= (ImageButton) findViewById(R.id.rightWeek);
        weekBack= (ImageButton) findViewById(R.id.leftWeek);

        updateView(step);
        stepLimit = calculateEarliest();

        if(showNotif)
        {
            showNotif();
        }
    }

    /**
     * Show notif.
     */
    public void showNotif(){
        ImageView congrats = (ImageView) findViewById(R.id.congratsImage);
        congrats.setVisibility(View.VISIBLE);
        congrats.bringToFront();

        congrats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
            }
        });
        WeekviewActivity.showNotif = false;
    }

    /**
     * Gets current week.
     *
     * @param step the step
     */
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

    /**
     * Read unique dates array list.
     *
     * @return the array list
     */
    public ArrayList<String> readUniqueDates() {

        ArrayList<String> dates = new ArrayList<>();
        dates = dbCont.readUniqueDates(this);

        return dates;
    }

    /**
     * Read dates between array list.
     *
     * @param start the start
     * @param end   the end
     * @return the array list
     */
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

    /**
     * Gets list for week.
     *
     * @param CurrentWeek the current week
     * @return the list for week
     */
    public ArrayList<String[]> getListForWeek(ArrayList<String> CurrentWeek) {

        ArrayList<String[]> dataLists = new ArrayList<>();

        String[] weekData = {"# - No Entries", "# - No Entries", "# - No Entries", "# - No Entries",
                "# - No Entries", "# - No Entries", "# - No Entries"};

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

            if (step == 0 && day == todayPosition) {
                temp = "\tTODAY";
            }

            if (day > 1) {
                weekData[day - 2] = dayString + "\t\t\t" + dateString + temp;
            } else if (day == 1) {
                //Sunday previously at day 1, move to position 6 for end of week
                weekData[6] = dayString + "\t\t\t" + dateString + temp;
            }
        }

        for (int i = 0; i < weekData.length; i++) {
            String curr = weekData[i];
            String[] currList;

            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(weekStart);

            java.util.Date temp;
            Date current;

            cal.add(Calendar.DATE, i);
            temp = cal.getTime();
            current = new Date(temp.getTime());

            if(i<=todayPosition) {
                boolean currDayCounts = getTheBools(current);
                starBools[i] = currDayCounts;
            }


            java.util.Date currDate = cal.getTime();
            String dayString = formatter.format(currDate);
            String dateString = formDates.format(currDate);
            if (curr.charAt(0) == '#') {
                weekDates[i] = String.valueOf(current);
                weekData[i] = dayString + "\t\t\t" + dateString + "\t\tNO ENTRIES";
                String[] newOne = {dayString, dateString, "NO ENTRIES"};
                currList = newOne;

            } else {
                weekDates[i] = String.valueOf(current);
                String[] newOne = {dayString, dateString, ""};
                currList = newOne;
            }
            dataLists.add(currList);
        }

        return dataLists;
    }

    /**
     * Calculate earliest int.
     *
     * @return the int
     */
    public int calculateEarliest() {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(weekStart);
        int week = c.get(Calendar.WEEK_OF_YEAR);

        int difference;
        if (uniqueDates.size() > 0) {
            Date earliest = Date.valueOf(uniqueDates.get(0));
            c.setTime(earliest);
            int earlier = c.get(Calendar.WEEK_OF_YEAR);
            difference = -(week - earlier);
        } else {
            difference = 0;
        }

        return difference;
    }

    /**
     * Update view.
     *
     * @param step the step
     */
    public void updateView(int step) {

        //Read unique dates if not done
        if (uniqueDates.size() == 0) {
            uniqueDates = readUniqueDates();
        }

        if (checkArrows) {
            setArrowVis(step);
        }else{
            checkArrows = true;
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


    /**
     * Set arrow vis.
     *
     * @param step the step
     */
    public void setArrowVis(int step){
        if(step==stepLimit)
        {
            weekBack.setEnabled(false);
            weekBack.setImageResource(R.drawable.leftarrowgrey);
        }else{
            weekBack.setEnabled(true);
            weekBack.setImageResource(R.drawable.leftarrow);
        }

        if(step==0)
        {
            weekPlus.setEnabled(false);
            weekPlus.setImageResource(R.drawable.rightarrowgrey);

        }else{
            weekPlus.setEnabled(true);
            weekPlus.setImageResource(R.drawable.rightarrow);
        }
    }

    /**
     * Sets list adapter.
     */
    public void setListAdapter() {


        //http://stackoverflow.com/questions/19615766/android-custom-layout-for-listview
        //http://stackoverflow.com/questions/15832335/android-custom-row-item-for-listview

        //THIS ONE?
        //http://stackoverflow.com/questions/15832335/android-custom-row-item-for-listview
        //dateList = (ListView) findViewById(R.id.listDates);
        //ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weekData);
        //ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.imagebutton_layout,R.id.tvChild,weekData);

        //dateList.setAdapter(adapter);


        int toPass;
        if (step==0){
            toPass = todayPosition;
        }else{
            toPass = 77;
        }

        TextView thing = (TextView) findViewById(R.id.textOfThings);

        dateList.setAdapter(new CustomAdapter(this, moreWeekData, 1, starBools,toPass, thing));

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
                    startActivity(i);
                }
            }
        });
    }


    /**
     * Gets the bools.
     *
     * @param curr the curr
     * @return the the bools
     */
    public boolean getTheBools(Date curr) {
        String dateStr = String.valueOf(curr);
        int[] countValues = dbCont.readCountData(this, curr);
        boolean completed;
        int fv = countValues[0];
        int dr = countValues[1];
        int hasB = countValues[2];
        int hasL = countValues[3];
        int hasD = countValues[4];

        if (fv >= 5 && dr >= 8 && hasB > 0 && hasL > 0 && hasD > 0) {
            completed = true;
        } else {
            completed = false;
        }

        System.out.print("");

        return completed;
    }

    /**
     * Update week.
     *
     * @param v the v
     */
    public void updateWeek(View v) {
        String tag = v.getTag().toString();

        if (tag.equals("+") && step < 0) {
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

    /**
     * Back option.
     *
     * @param v the v
     */
    public void backOption(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }


}
