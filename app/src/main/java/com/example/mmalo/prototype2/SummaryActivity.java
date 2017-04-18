package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.DB.DBContainer;
import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.ExpListClasses.CustomAdapter;
import com.example.mmalo.prototype2.Models.DataHolder;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mmalo on 10/11/2016.
 */
public class SummaryActivity extends AppCompatActivity {

    /**
     * The Curr photo.
     */
    byte[] currPhoto;
    /**
     * The constant counts.
     */
    public static ContentValues counts;
    /**
     * The constant theentry.
     */
    public static DiaryData theentry;
    /**
     * The Data list.
     */
    ListView dataList;
    /**
     * The Comments.
     */
    EditText comments;
    /**
     * The Edits.
     */
    LinearLayout edits, /**
     * The Save lay.
     */
    saveLay, /**
     * The Review lay.
     */
    reviewLay, /**
     * The For d lay.
     */
    ForDLay, /**
     * The Bl lay.
     */
    blLay, /**
     * The Ds lay.
     */
    dsLay, /**
     * The Meals.
     */
    meals;
    //May be unneeded
    //Button editMeal, buttonFV, buttonDR;

    //Button review, save;
    /**
     * The Db cont.
     */
    public DBContainer dbCont;
    /**
     * The New meal.
     */
    String newMeal, /**
     * The Curr count.
     */
    currCount;
    /**
     * The Old meal.
     */
    String oldMeal;
    /**
     * The Init dr.
     */
    int initDR, /**
     * The Init fv.
     */
    initFV;
    /**
     * The Curr fv.
     */
    int currFV, /**
     * The Curr dr.
     */
    currDR;
    /**
     * The Dr.
     */
    int dr, /**
     * The Fv.
     */
    fv;
    /**
     * The Fv diff.
     */
    int fvDiff, /**
     * The Dr diff.
     */
    drDiff;
    /**
     * The Fv change.
     */
    boolean fvChange, /**
     * The Dr change.
     */
    drChange;
    /**
     * The First click.
     */
    boolean firstClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create activity and call initialisation functions
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        DataHolder.readData(this);
        initValues();
        System.gc();
    }

    /**
     * Init values.
     */
    public void initValues() {
        //Define references to Views needed by activity
        dbCont = new DBContainer();
        edits = (LinearLayout) findViewById(R.id.LinLayEdits);
        saveLay = (LinearLayout) findViewById(R.id.LinLayBottomOptions);
        reviewLay = (LinearLayout) findViewById(R.id.LinLayBottomOptions2);
        ForDLay = (LinearLayout) findViewById(R.id.LinLayForD);
        blLay = (LinearLayout) findViewById(R.id.LinLayBL1);
        dsLay = (LinearLayout) findViewById(R.id.LinLayDS1);
        meals = (LinearLayout) findViewById(R.id.LinLayMeals);

        //Set values for listview display
        setListView();

        newMeal = "";
        try {
            //Read photo byte data from file, create bitmap object
            currPhoto = readImageFromFile(theentry.getFilepath());
            Bitmap bmp = BitmapFactory.decodeByteArray(currPhoto, 0, currPhoto.length);

            //Reference image view and set bitmap to it
            ImageView summaryPhoto = (ImageView) findViewById(R.id.imagePhoto);
            summaryPhoto.setImageBitmap(bmp);

            //Set initial values for variables
            initDR = theentry.getDrCount();
            initFV = theentry.getFvCount();
            dr = initDR;
            fv = initFV;
            currFV = 0;
            currDR = 0;
            firstClick = true;
            fvChange = false;
            drChange = false;

            //Calculate screen height
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.heightPixels;
            int theHeight;

            //Check height of screen and set photo height
            if (height < 1200) {

            } else {
                //Set value of image to half of screen height
                theHeight = height / 2;
                ViewGroup.LayoutParams params = summaryPhoto.getLayoutParams();
                params.height = theHeight;
                summaryPhoto.requestLayout();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Back press.
     *
     * @param v the v
     */
    public void backPress(View v) {
        //Back pressed, go back to DateviewActivity
        System.gc();
        Intent i = new Intent(getBaseContext(), DateviewActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        //Override device back button, call garbage collection, and then finish activity
        System.gc();
        finish();
        return;
    }


    /**
     * Sets list view.
     */
    public void setListView() {
        //Set up needed variables
        ArrayList<String> listData = new ArrayList<String>();
        String[] moreListData = new String[3];
        String count1 = theentry.getFvCount() + "#" + theentry.getDrCount();

        //Get comment data and check if empty
        String comms = theentry.getComment();
        if (comms.equals("")) {
            comms = "No Comment";
        }

        //Add meal, count and comment to list for display
        listData.add(theentry.getMeal());
        listData.add(count1);
        listData.add(comms);

        //Add meal, count and comment to list for display
        moreListData[0] = theentry.getMeal();
        moreListData[1] = count1;
        moreListData[2] = comms;

        //Get reference to listview for display
        dataList = (ListView) findViewById(R.id.listViewOfDatas);

        try {
            //Set adapter with array data
            dataList.setAdapter(new CustomAdapter(this, moreListData, 3));
        } catch (Exception exp) {
            exp.printStackTrace();
            System.out.print(exp.getCause());
        }
    }

    /**
     * Edit data in entry.
     *
     * @param v the v
     */
    public void reviewData(View v) {

        //Hide listview and show comment box
        dataList.setVisibility(View.INVISIBLE);
        comments = (EditText) findViewById(R.id.textComms);
        comments.setVisibility(View.VISIBLE);

        //Get existing comment, set to message ("Tap..") if comment empty
        String comms = theentry.getComment();
        if (comms.equals("")) {
            comms = "Tap here to type a comment";
        }
        comments.setText(comms);

        //Hide other things - Review & Back button
        reviewLay.setVisibility(View.INVISIBLE);

        //Show layouts needed for editting entry
        saveLay.setVisibility(View.VISIBLE);
        edits.setVisibility(View.VISIBLE);
    }

    /**
     * Clear text box.
     *
     * @param v the v
     */
    public void clearTextBox(View v) {
        //Reference textbox
        EditText e = (EditText) v;
        if (e.getId() == R.id.textComms) ;
        {
            //Store current value of textbox
            String temp = e.getText().toString();

            //If comment is initial message and is the first click
            if (temp.equals("Tap here to type a comment") && firstClick) {
                //Blank textbox
                e.setText("");
                firstClick = false;
            }
        }
    }

    /**
     * Save count.
     *
     * @param v the v
     */
    public void saveCount(View v) {

        //Reference confirmation button, hide it
        Button buttConf = (Button) findViewById(R.id.buttonCountConfirm2);
        buttConf.setVisibility(View.INVISIBLE);

        //Reference plus and minus count buttons, hide that layout
        LinearLayout plusMinus = (LinearLayout) findViewById(R.id.LinLayInc);
        plusMinus.setVisibility(View.INVISIBLE);

        //Show comments, EditText box and save button layout
        comments.setVisibility(View.VISIBLE);
        edits.setVisibility(View.VISIBLE);
        saveLay.setVisibility(View.VISIBLE);

        //Reference TextViews and set them as invisible
        TextView tv = (TextView) findViewById(R.id.textViewCount2);
        tv.setVisibility(View.INVISIBLE);
        tv.setText("0");
        TextView tv2 = (TextView) findViewById(R.id.portionPrompt);
        tv2.setVisibility(View.INVISIBLE);

        //Show ImageView
        ImageView taken = (ImageView) findViewById(R.id.imagePhoto);
        taken.setVisibility(View.VISIBLE);
    }

    /**
     * Add to counts.
     *
     * @param v the v
     */
    public void addToCounts(View v) {
        //Show counter buttons, cancel button and text view
        //Get count to add to
        currCount = v.getTag().toString();

        //Reference Confirmation button and + & - button layouts and show them
        LinearLayout plusMinus = (LinearLayout) findViewById(R.id.LinLayInc);
        plusMinus.setVisibility(View.VISIBLE);
        Button buttConf = (Button) findViewById(R.id.buttonCountConfirm2);
        buttConf.setVisibility(View.VISIBLE);

        //Reference and Show TextViews
        TextView tv = (TextView) findViewById(R.id.textViewCount2);
        tv.setVisibility(View.VISIBLE);
        TextView tv2 = (TextView) findViewById(R.id.portionPrompt);
        tv2.setVisibility(View.VISIBLE);

        //Hide ImageView from screen
        ImageView taken = (ImageView) findViewById(R.id.imagePhoto);
        taken.setVisibility(View.INVISIBLE);

        //Hide comment, EditText box and save button layouts
        comments.setVisibility(View.INVISIBLE);
        edits.setVisibility(View.INVISIBLE);
        saveLay.setVisibility(View.INVISIBLE);

        //Check which count is in use and update TextView
        if (currCount.equals("Drink")) {
            tv.setText(String.valueOf(dr));
        } else {
            tv.setText(String.valueOf(fv));
        }
    }

    /**
     * Update local holder variables.
     *
     * @param val the val
     */
    public void updateLocals(int val) {
        if (currCount.equals("Drink")) {
            dr = val;
            drChange = true;
        } else {
            fv = val;
            fvChange = true;
        }
    }

    /**
     * Inc count.
     *
     * @param v the v
     */
    public void incCount(View v) {
        //Get TextView Reference and current count value
        TextView tv = (TextView) findViewById(R.id.textViewCount2);
        int curr = Integer.valueOf(tv.getText().toString());

        //If in range, increment count
        if (curr < 8) {
            curr++;
        }

        //Update TextView and local variables
        tv.setText(String.valueOf(curr));
        updateLocals(curr);
    }

    /**
     * Dec count.
     *
     * @param v the v
     */
    public void decCount(View v) {
        //Get TextView Reference and current count value
        TextView tv = (TextView) findViewById(R.id.textViewCount2);
        int curr = Integer.valueOf(tv.getText().toString());

        //If in range, decrement count
        if (curr > 0) {
            curr--;
        }

        //Update TextView and local variables
        tv.setText(String.valueOf(curr));
        updateLocals(curr);
    }

    /**
     * Show meal types.
     *
     * @param v the v
     */
    public void showMealTypes(View v) {
        ForDLay.setVisibility(View.INVISIBLE);
        meals.setVisibility(View.VISIBLE);
    }

    /**
     * Update meal tag.
     *
     * @param v the v
     */
    public void updateMealTag(View v) {
        String tag = v.getTag().toString();
        newMeal = tag;
        meals.setVisibility(View.INVISIBLE);
        edits.setVisibility(View.VISIBLE);
        saveLay.setVisibility(View.VISIBLE);
        comments.setVisibility(View.VISIBLE);
        ForDLay.setVisibility(View.INVISIBLE);
    }

    /**
     * Edit meal tag.
     *
     * @param v the v
     */
    public void editMealTag(View v) {

        ForDLay.setVisibility(View.VISIBLE);
        saveLay.setVisibility(View.INVISIBLE);
        edits.setVisibility(View.INVISIBLE);
        comments.setVisibility(View.INVISIBLE);
    }


    /**
     * Update meal comment.
     *
     * @param comment the comment
     */
    public void updateMealComment(String comment) {
        //Define ContentValue Pair holder
        ContentValues cv = new ContentValues();

        //Add each parameter to ContentValues object
        cv.put("comment_data", comment);
        cv.put("fv_count", fv);
        cv.put("drink_count", dr);
        if (!newMeal.equals("")) {
            cv.put("meal", newMeal);
        }

        //Set arguments for update and execute call to update
        String[] updateArgs = {theentry.getTimestamp().toString()};
        dbCont.updateComment(this, cv, updateArgs);
    }

    /**
     * Save review.
     *
     * @param v the v
     */
    public void saveReview(View v) {

        //Show and hide layouts
        dataList.setVisibility(View.VISIBLE);
        reviewLay.setVisibility(View.VISIBLE);
        saveLay.setVisibility(View.INVISIBLE);
        comments.setVisibility(View.INVISIBLE);

        //Get comment, set comment on entry object and text box
        String updated = comments.getText().toString();
        theentry.setComment(updated);
        comments.setText(updated);

        //Check if meal type has been changed
        oldMeal = "";
        if (!newMeal.equals("")) {

            oldMeal = theentry.getMeal();
            theentry.setMeal(newMeal);
        }

        //Calculate difference in counts
        fvDiff = fv - initFV;
        drDiff = dr - initDR;

        int fvToPass = initFV;
        int drToPass = initDR;

        //Calculate Fruit & Veg count value to pass
        fvToPass = fvDiff + DataHolder.todaysFV;
        if (fvChange) {

            theentry.setFvCount(fv);
        } else {
            fv = initFV;
        }

        //Calculate drink count value to pass
        drToPass = drDiff + DataHolder.todaysDrinks;
        if (drChange) {

            theentry.setDrCount(dr);
        } else {

            dr = initDR;
        }

        //Use fvDIff and drDiff to update daily counts
        if (fvDiff != 0 || drDiff != 0 || !newMeal.equals("")) {
            Date d = new Date(theentry.getTimestamp().getTime());
            updateCountsDB(fvToPass, drToPass, d);
        }
        edits.setVisibility(View.INVISIBLE);

        //Call to set up ListView data
        setListView();

        //Check if comment has been update and call to update function
        String comms = updated;
        if (comms.equals("Tap here to type a comment")) {
            updated = "";
        }
        updateMealComment(updated);

        //Call to check if targets have been completed
        checkTargetProgress();
    }

    /**
     * Check target progress.
     */
    public void checkTargetProgress() {
        //Check if day is current day
        String temp;
        SimpleDateFormat formDates = new SimpleDateFormat("yyyy-MM-dd");
        temp = formDates.format(theentry.getTimestamp());
        if (temp.equals(DataHolder.strCurrentDate)) {
            //Check if completed
            boolean targetCheck = DataHolder.checkCompleted(this);
            Intent i;

            if (targetCheck) {
                //Do notification
                WeekviewActivity.showNotif = true;
                i = new Intent(getBaseContext(), WeekviewActivity.class);
                this.startActivity(i);
            } else {
                //i = new Intent(getBaseContext(), OptionsActivity.class);
            }
        }
    }

    /**
     * Update counts db.
     *
     * @param fvCount    the fv count
     * @param drinkCount the drink count
     * @param theDate    the the date
     */
    public void updateCountsDB(int fvCount, int drinkCount, Date theDate) {
        //Init variables
        ContentValues cv = new ContentValues();
        int f = fvCount;
        int d = drinkCount;

        //Add to content values and set update arguments
        cv.put("fv_count", f);
        cv.put("drink_count", d);
        String[] updateArgs = {theDate.toString()};

        //Check if flags are to be set
        if (!newMeal.equals("") && !newMeal.equals("Snack") && !newMeal.equals("Drink")) {
            String toAdd = "had" + newMeal;
            cv.put(toAdd, true);
        }

        //Check if meal has changed
        if (!oldMeal.equals("") && !oldMeal.equals("Snack") && !oldMeal.equals("Drink")) {
            int oldCount = counts.getAsInteger((oldMeal + "Count"));
            if (!(oldCount > 1)) {
                String toAdd = "had" + oldMeal;
                cv.put(toAdd, false);
            }
        }

        //Call to update function
        boolean success = dbCont.updateCountsDB(this, theDate, cv, updateArgs);
        if (!success) {
            DataHolder.todaysFV = f;
            DataHolder.todaysDrinks = d;
        }
    }


    /**
     * Read image from file byte [ ].
     *
     * @param filename the filename
     * @return the byte [ ]
     */
    public byte[] readImageFromFile(String filename) {
        try {
            //Create input stream
            InputStream is = openFileInput(filename);

            //Convert Iput stream to byte array and return
            byte[] photoData = org.apache.commons.io.IOUtils.toByteArray(is);
            return photoData;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
