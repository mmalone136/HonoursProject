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
        //Create activity and initialise variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekview);
        DataHolder.readData(this);
        boolean[] temp = {false, false, false, false, false, false, false};
        starBools = temp;
        checkArrows=false;
        dbCont = new DBContainer();

        //Get Reference for listview, create calendar instance
        dateList = (ListView) findViewById(R.id.listDates);
        Calendar calendar = Calendar.getInstance();

        //Get current day of week and map to position of array
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day==0||day==1)
        {
            todayPosition= 5+day;
        }else{
            todayPosition= day-2;
        }

        //Set step to zero and reference imagebuttons
        step = 0;
        weekPlus= (ImageButton) findViewById(R.id.rightWeek);
        weekBack= (ImageButton) findViewById(R.id.leftWeek);

        //Update view and calculate earliest week
        updateView(step);
        stepLimit = calculateEarliest();

        //Check if showing target completion image needs to show
        if(showNotif)
        {
            showNotif();
        }
    }

    /**
     * Show notification of target completion.
     */
    public void showNotif(){
        //Get reference to image view, show image and bring to front
        ImageView congrats = (ImageView) findViewById(R.id.congratsImage);
        congrats.setVisibility(View.VISIBLE);
        congrats.bringToFront();

        //Set onclick function for imageview
        congrats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide imageview on click
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
        //Create calendar object
        Calendar c = GregorianCalendar.getInstance();

        //Set calendar object to star week at monday, not the default sunday
        //Offset date by number of weeks
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, (7 * step));
        c.setFirstDayOfWeek(Calendar.MONDAY);

        //Clear time components off of object
        //Stops objects of same date and different time being different
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.clear(Calendar.HOUR);
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.SECOND);
        c.clear(Calendar.MILLISECOND);

        java.util.Date start = c.getTime();

        //Add 6 days to get to end of week
        c.add(Calendar.DATE, 6);
        java.util.Date end = c.getTime();
        Date first = new Date(start.getTime());
        Date last = new Date(end.getTime());

        //Set start and end objects for current week
        weekStart = first;
        weekEnd = last;
    }

    /**
     * Read unique dates array list.
     *
     * @return the array list
     */
    public ArrayList<String> readUniqueDates() {
        //Reads a list of dates with entries in them - no repeated dates in list
        ArrayList<String> dates = new ArrayList<>();
        dates = dbCont.readUniqueDates(this);
        return dates;
    }

    /**
     * Read dates between given start and end date - return array list of dates (String objects).
     *
     * @param start the start
     * @param end   the end
     * @return the array list
     */
    public ArrayList<String> readDatesBetween(Date start, Date end) {
        //Create list objects
        ArrayList<String> toReturn = new ArrayList<String>();

        //Loop for each date in list of unique dates
        for (String curr : uniqueDates) {

            //Get current date
            Date currDate = Date.valueOf(curr);

            //Compare current date with start and end to check if in range
            int compStart = start.compareTo(currDate);
            int compEnd = end.compareTo(currDate);

            // Comparison gives 0 if dates equal,
            // <0 if object is before parameter value,
            // >0 if object is after parameter value
            if (compStart <= 0 && compEnd >= 0) {
                //Add to list if if range
                toReturn.add(currDate.toString());
            }
        }
        //Return list
        return toReturn;
    }

    /**
     * Gets list for week.
     *
     * @param CurrentWeek the current week
     * @return the list for week
     */
    public ArrayList<String[]> getListForWeek(ArrayList<String> CurrentWeek) {

        //Create and initialise list and array objects
        ArrayList<String[]> dataLists = new ArrayList<>();
        String[] weekData = {"# - No Entries", "# - No Entries", "# - No Entries", "# - No Entries",
                "# - No Entries", "# - No Entries", "# - No Entries"};

        weekDates = new String[7];

        //Create objects to format date objects for string output.
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        SimpleDateFormat formDates = new SimpleDateFormat("dd/MM/yyyy");

        //Loop for each string in current week
        for (String curr : CurrentWeek) {

            //Get current date values for comparisons
            Date currDate = Date.valueOf(curr);
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(currDate);
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            //Format the day and date for current iteration
            String dayString = formatter.format(cal.getTime());
            String dateString = formDates.format(currDate);

            //Get the position of current day
            int day = cal.get(Calendar.DAY_OF_WEEK);

            String temp = "";

            //Check if current iteration is current day
            if (step == 0 && day == todayPosition) {
                temp = "\tTODAY";
            }

            //Reorder indices for output of array - so monday is first day
            if (day > 1) {
                weekData[day - 2] = dayString + "\t\t\t" + dateString + temp;
            } else if (day == 1) {
                //Sunday previously at day 1, move to position 6 for end of week
                weekData[6] = dayString + "\t\t\t" + dateString + temp;
            }
        }

        //Loop for entrie week
        for (int i = 0; i < weekData.length; i++) {
            //Get current value
            String curr = weekData[i];
            String[] currList;
            java.util.Date temp;
            Date current;

            //Create calendar, set to start of current week
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(weekStart);

            //Add index to calendar object to get to current iteration date
            cal.add(Calendar.DATE, i);
            temp = cal.getTime();
            current = new Date(temp.getTime());

            //If index is on or before current day
            if(i<=todayPosition) {
                //Check array flagging if targets have been completed or not
                boolean currDayCounts = getTheBools(current);
                starBools[i] = currDayCounts;
            }

            //Format current day and date to string objects
            java.util.Date currDate = cal.getTime();
            String dayString = formatter.format(currDate);
            String dateString = formDates.format(currDate);

            //Check if position has entries
            if (curr.charAt(0) == '#') {
                //No entries
                weekDates[i] = String.valueOf(current);
                weekData[i] = dayString + "\t\t\t" + dateString + "\t\tNO ENTRIES";

                //This formats strings to array to place in list view
                String[] newOne = {dayString, dateString, "NO ENTRIES"};
                currList = newOne;
            } else {
                //Has entries - format strings to array to pass to list view
                weekDates[i] = String.valueOf(current);
                String[] newOne = {dayString, dateString, ""};
                currList = newOne;
            }
            dataLists.add(currList);
        }
        return dataLists;
    }

    /**
     * Calculate earliest date which holds an entry
     * Means infinite scrolling back can't happen
     *
     * @return the int
     */
    public int calculateEarliest() {
        //Create calendar object and set to current week
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(weekStart);

        //Calculate current week of year
        int week = c.get(Calendar.WEEK_OF_YEAR);
        int difference;

        //Check unique dates list isnt empty
        if (uniqueDates.size() > 0) {

            //Dates are read in ascending order of date
            //So earliest date is first position in array
            Date earliest = Date.valueOf(uniqueDates.get(0));
            c.setTime(earliest);

            //Calculate difference between current week and earliest week
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

        //Check if arrows should be enables
        if (checkArrows) {
            //Set
            setArrowVis(step);
        }else{
            //First load - set check to true
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
        //Check value of step to determine week
        //if at earliest week, show greyed out left arrow
        if(step==stepLimit)
        {
            weekBack.setEnabled(false);
            weekBack.setImageResource(R.drawable.leftarrowgrey);
        }else{
            //Else, show left arrow as enabled
            weekBack.setEnabled(true);
            weekBack.setImageResource(R.drawable.leftarrow);
        }

        //If at current week, disable right arrow
        if(step==0)
        {
            weekPlus.setEnabled(false);
            weekPlus.setImageResource(R.drawable.rightarrowgrey);
        }else{
            //Else, enable right arrow
            weekPlus.setEnabled(true);
            weekPlus.setImageResource(R.drawable.rightarrow);
        }
    }

    /**
     * Sets list adapter.
     */
    public void setListAdapter() {

        int toPass;
        if (step==0){
            toPass = todayPosition;
        }else{
            toPass = 77;
        }

        TextView thing = (TextView) findViewById(R.id.textOfThings);

        dateList.setAdapter(new CustomAdapter(this, moreWeekData, 1, starBools,toPass, thing));

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
     * Gets the boolean array, to show if stars are grey or gold
     *
     * @param curr the curr
     * @return the the bools
     */
    public boolean getTheBools(Date curr) {
        //Read count values for current date - calls database container function
        int[] countValues = dbCont.readCountData(this, curr);
        boolean completed;

        //Pass returned values into holder variables
        int fv = countValues[0];
        int dr = countValues[1];
        int hasB = countValues[2];
        int hasL = countValues[3];
        int hasD = countValues[4];


        //Check if counts have been completed
        if (fv >= 5 && dr >= 8 && hasB > 0 && hasL > 0 && hasD > 0) {
            completed = true;
        } else {
            completed = false;
        }

        return completed;
    }

    /**
     * Update week being shown on screen
     *
     * @param v the v
     */
    public void updateWeek(View v) {
        //Check which button was pressed - by tag
        String tag = v.getTag().toString();

        //If forward button and not at limit (actual current week)
        //Increment and update to show next week
        if (tag.equals("+") && step < 0) {
            step++;
            updateView(step);
        }
        //If backwards button and not at limit
        //Decrement and update to show previous week
        else if (tag.equals("-")) {
            if (step > stepLimit) {
                step--;
                updateView(step);
            }
        }
        //Reset to current week pressed
        //Reset step to zero and update week
        else if (tag.equals("0")) {
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
