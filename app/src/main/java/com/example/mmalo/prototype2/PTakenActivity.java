package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.text.method.DateTimeKeyListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.DB.DBContainer;
import com.example.mmalo.prototype2.DB.DBHelper;
import com.example.mmalo.prototype2.Models.DataHolder;
import com.example.mmalo.prototype2.Models.DiaryData;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mmalo on 02/11/2016.
 */
public class PTakenActivity extends AppCompatActivity {

    /**
     * The Photo data.
     */
    public static byte[] photoData;
    /**
     * The constant thePic.
     */
    public static Bitmap thePic;
    /**
     * The Comment data.
     */
    String commentData;
    /**
     * The Meal choice.
     */
    String mealChoice;
    /**
     * The constant timetaken.
     */
    public static Timestamp timetaken;
    /**
     * The constant filename.
     */
    public static String filename;
    /**
     * The Db cont.
     */
    public DBContainer dbCont;
    /**
     * The First click.
     */
    boolean firstClick;
    /**
     * The Fv.
     */
    int fv;
    /**
     * The Dr.
     */
    int dr;
    /**
     * The Curr count.
     */
    String currCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create activity and initialise necessary variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptaken);
        initValues();
        dbCont = new DBContainer();
        DataHolder.readData(this);
    }

    /**
     * Read all entries array list.
     *
     * @return the array list
     */
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

        while (cursor.moveToNext()) {
            DiaryData curr;

            String comm = cursor.getString(cursor.getColumnIndexOrThrow("comment_data"));

            String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
            String theMeal = cursor.getString(cursor.getColumnIndexOrThrow("meal"));
            String file = cursor.getString(cursor.getColumnIndexOrThrow("filepath"));
            int fvCount = cursor.getInt(cursor.getColumnIndexOrThrow("fv_count"));
            int drCount = cursor.getInt(cursor.getColumnIndexOrThrow("drink_count"));

            Timestamp theTime = Timestamp.valueOf(tID);
            curr = new DiaryData(null, comm, null, theTime, theMeal, file, fvCount, drCount);

            entries.add(curr);
        }
        cursor.close();

        return entries;
    }


    @Override
    public void onBackPressed() {
        //Override back press button to stop errors or unexpected navigation happening
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }


    /**
     * Init values.
     */
    public void initValues() {
        //Set up necessary variables
        int apiLevel = Build.VERSION.SDK_INT;
        Bitmap bitmap;
        thePic = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);

        //Set bitmap based on api level (Different APIs behaved differently on testing)
        if (apiLevel ==19 ||apiLevel ==21) {
            bitmap = thePic;
        }else {
            Matrix rotationMat = new Matrix();
            rotationMat.postRotate(270);
            bitmap = Bitmap.createBitmap(thePic, 0, 0, thePic.getWidth(), thePic.getHeight(), rotationMat, true);
        }

        //Set image view on activity to bitmap variable
        ImageView taken = (ImageView) findViewById(R.id.imageTaken);
        taken.setImageBitmap(bitmap);

        //Calculate Height of screen to set picture to half of screen size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int theHeight;

        if (height < 1200) {

        } else {
            //Calculate height and set
            theHeight = height / 2;
            ViewGroup.LayoutParams params = taken.getLayoutParams();
            params.height = theHeight;
            taken.requestLayout();
        }

        //Initialise values
        fv = 0;
        dr = 0;
        firstClick = true;
    }


    /**
     * Clear text box if first click.
     *
     * @param v the v
     */
    public void clearTextBox(View v) {
        if (v.getId() == R.id.textComments) ;
        {
            //Check if first click
            if (firstClick) {
                //Clear box of text
                EditText e = (EditText) v;
                e.setText("");
                firstClick = false;
            }
        }
    }

    /**
     * Meal button.
     *
     * @param v the v
     */
    public void mealButton(View v) {
        //Get which button was pressed by tag
        mealChoice = v.getTag().toString();

        //Hide label, and layouts needed
        TextView prompt = (TextView) findViewById(R.id.textPrompts);
        prompt.setVisibility(View.INVISIBLE);
        LinearLayout lays = (LinearLayout) findViewById(R.id.LinLayLayouts);
        lays.setVisibility(View.INVISIBLE);

        //Show text box and layout of buttons for next sections
        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);
        LinearLayout nexts = (LinearLayout) findViewById(R.id.LinLayNexts);
        nexts.setVisibility(View.VISIBLE);
    }

    /**
     * Add to counts.
     *
     * @param v the v
     */
    public void addToCounts(View v) {
        //Check which count to add to
        currCount = v.getTag().toString();

        //Get references to buttons for counts and show them
        Button buttFV = (Button) findViewById(R.id.buttonAdd);
        Button buttDR = (Button) findViewById(R.id.buttonMinus);
        Button buttConf = (Button) findViewById(R.id.buttonCountConfirm);
        buttFV.setVisibility(View.VISIBLE);
        buttDR.setVisibility(View.VISIBLE);
        buttConf.setVisibility(View.VISIBLE);

        //Hide the edittext box
        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.INVISIBLE);


        //Show labels for adding to counts
        TextView tv = (TextView) findViewById(R.id.textViewCount);
        tv.setVisibility(View.VISIBLE);
        TextView tv2 = (TextView) findViewById(R.id.portionPrompt);
        tv2.setVisibility(View.VISIBLE);

        //Hide image taken
        ImageView taken = (ImageView) findViewById(R.id.imageTaken);
        taken.setVisibility(View.INVISIBLE);

        //Check and update the current count
        if (currCount.equals("Drink")) {
            tv.setText(String.valueOf(dr));
        } else {
            tv.setText(String.valueOf(fv));
        }
    }

    /**
     * Save count.
     *
     * @param v the v
     */
    public void saveCount(View v) {
        //Get reference to count adding buttons, hides them
        Button buttFV = (Button) findViewById(R.id.buttonAdd);
        Button buttDR = (Button) findViewById(R.id.buttonMinus);
        Button buttConf = (Button) findViewById(R.id.buttonCountConfirm);
        buttFV.setVisibility(View.INVISIBLE);
        buttDR.setVisibility(View.INVISIBLE);
        buttConf.setVisibility(View.INVISIBLE);

        //Hides the text labels for adding to counts
        TextView tv = (TextView) findViewById(R.id.textViewCount);
        tv.setVisibility(View.INVISIBLE);
        TextView tv2 = (TextView) findViewById(R.id.portionPrompt);
        tv2.setVisibility(View.INVISIBLE);
        tv.setText("0");

        //Show taken picture and edittext box
        ImageView taken = (ImageView) findViewById(R.id.imageTaken);
        taken.setVisibility(View.VISIBLE);
        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);
    }

    /**
     * Inc count.
     *
     * @param v the v
     */
    public void incCount(View v) {
        //Get textview for count
        TextView tv = (TextView) findViewById(R.id.textViewCount);

        //Get current count value and increment if in range
        int curr = Integer.valueOf(tv.getText().toString());
        if (curr < 8) {
            curr++;
        }
        tv.setText(String.valueOf(curr));

        //Update local holder variables
        updateLocals(curr);
    }


    /**
     * Dec count.
     *
     * @param v the v
     */
    public void decCount(View v) {
        //Get textview for count
        TextView tv = (TextView) findViewById(R.id.textViewCount);

        //Get current count value and decrement if in range
        int curr = Integer.valueOf(tv.getText().toString());
        if (curr > 0) {
            curr--;
        }
        tv.setText(String.valueOf(curr));

        //Update local holder variables
        updateLocals(curr);

    }

    /**
     * Update locals.
     *
     * @param val the val
     */
    public void updateLocals(int val) {
        //Check which locaal to update and set value
        if (currCount.equals("Drink")) {
            dr = val;
        } else {
            fv = val;
        }
    }

    /**
     * Save image to file int.
     *
     * @param datas the datas
     * @return the int
     */
    public int saveImageToFile(byte[] datas) {
        try {
            //Create output stream, based on filename variable
            FileOutputStream fos;
            fos = openFileOutput(filename, Context.MODE_PRIVATE);

            //Write byte array to file
            fos.write(datas);

            //Close and return 7 as success value
            fos.close();
            return 7;
        } catch (Exception ex) {
            //Error occurred, return 0 as error
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Food option selected, hide "Food" or "Drink" options
     *
     * @param v the v
     */
    public void doFood(View v) {
        LinearLayout fd = (LinearLayout) findViewById(R.id.LinLayFD);
        fd.setVisibility(View.INVISIBLE);
    }

    /**
     * Drink option selected, hide other options
     *
     * @param v the v
     */
    public void doDrink(View v) {

        //TODO: Refactor this, getting layouts instead of individual views
        //TODO: Combine with mealButton()


        //TODO: The things mentioned in the todos above


        mealChoice = v.getTag().toString();

        LinearLayout fd = (LinearLayout) findViewById(R.id.LinLayFD);
        fd.setVisibility(View.INVISIBLE);

        LinearLayout lays = (LinearLayout) findViewById(R.id.LinLayLayouts);
        lays.setVisibility(View.INVISIBLE);

        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);


        LinearLayout nexts = (LinearLayout) findViewById(R.id.LinLayNexts);
        nexts.setVisibility(View.VISIBLE);


        TextView prompt = (TextView) findViewById(R.id.textPrompts);
        prompt.setVisibility(View.INVISIBLE);

    }

    /**
     * Submit form.
     *
     * @param v the v
     */
    public void submitForm(View v) {
        //Show EditText box
        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);
        commentData = comments.getText().toString();

        //Check if comment has been editted
        if (commentData.equals("Tap here to type a comment")) {
            commentData = "";
        }

        //Get filename
        String fp = filename;

        //Create new DiaryData entry object
        DiaryData entry = new DiaryData(null, commentData, null, timetaken, mealChoice, fp, fv, dr);

        //Call to save function
        int success = saveImageToFile(photoData);

        Toast t;

        //Check if save was successful
        if (success == 7) {
            //Save entry to DataBase
            long l = dbCont.insertEntry(entry, getApplicationContext(), fv, dr);
            if (l != -1) {
                //First insert successful
                //Display and insert to counts table
                t = Toast.makeText(this, "Entry Submitted Successfully", Toast.LENGTH_SHORT);
                dbCont.updateCountsDB2(this, fv, dr, mealChoice, timetaken);
            } else {
                //Display error toast
                t = Toast.makeText(this, "ERROR Submitting Entry", Toast.LENGTH_SHORT);
            }
        } else {
            //Display error toast
            t = Toast.makeText(this, "ERROR Submitting Entry", Toast.LENGTH_SHORT);
        }

        //Housekeeping and Garbage cleaning - to avoid OutOfMemory Errors
        thePic.recycle();
        photoData = null;
        t.show();

        //Call to check if daily targets have been completed
        boolean targetCheck = DataHolder.checkCompleted(this);
        Intent i;

        if (targetCheck) {
            //If true and haven't already shown, set flag to true
            WeekviewActivity.showNotif = true;
            i = new Intent(getBaseContext(), WeekviewActivity.class);
        } else {
            //If false, Don't show, go back to options screen
            i = new Intent(getBaseContext(), OptionsActivity.class);
        }
        this.startActivity(i);
    }


    /**
     * Cancel form.
     *
     * @param v the v
     */
    public void cancelForm(View v) {

        Toast t = Toast.makeText(this, "Entry Cancelled", Toast.LENGTH_LONG);
        t.show();
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);


    }


}
