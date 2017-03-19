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
import android.view.View;
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
import java.util.ArrayList;

/**
 * Created by mmalo on 10/11/2016.
 */

public class SummaryActivity extends AppCompatActivity {

    byte[] currPhoto;
    public static ContentValues counts;
    public static DiaryData theentry;
    ListView dataList;
    EditText comments;
    LinearLayout edits, saveLay, reviewLay, ForDLay, blLay, dsLay, meals;
    //May be unneeded
    Button editMeal, buttonFV, buttonDR;

    Button review, save;


    public DBContainer dbCont;
    String newMeal, currCount;
    String oldMeal;
    int initDR, initFV;
    int currFV, currDR;
    int dr, fv;
    int fvDiff, drDiff;
    boolean fvChange, drChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        DataHolder.readData(this);
        initValues();
        System.gc();
    }

    public void initValues() {
        dbCont = new DBContainer();
        edits = (LinearLayout) findViewById(R.id.LinLayEdits);
        saveLay = (LinearLayout) findViewById(R.id.LinLayBottomOptions);
        reviewLay = (LinearLayout) findViewById(R.id.LinLayBottomOptions2);
        ForDLay = (LinearLayout) findViewById(R.id.LinLayForD);
        //blLay, dsLay;
        blLay = (LinearLayout) findViewById(R.id.LinLayBL1);
        dsLay = (LinearLayout) findViewById(R.id.LinLayDS1);
        meals = (LinearLayout) findViewById(R.id.LinLayMeals);

        setListView();
        newMeal = "";
        try {
            currPhoto = readImageFromFile(theentry.getFilepath());//theentry.getPhotoData();
            Bitmap bmp = BitmapFactory.decodeByteArray(currPhoto, 0, currPhoto.length);
            ImageView summaryPhoto = (ImageView) findViewById(R.id.imagePhoto);
            summaryPhoto.setImageBitmap(bmp);

            initDR = theentry.getDrCount();
            initFV = theentry.getFvCount();
            dr =initDR;
            fv = initFV;
            currFV = 0;
            currDR = 0;
            fvChange = false;
            drChange = false;

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void backPress(View v) {
        //onBackPressed();
        System.gc();
        Intent i = new Intent(getBaseContext(), DateviewActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        System.gc();
        finish();
        return;
    }

    public void setListView() {
        ArrayList<String> listData = new ArrayList<String>();
        String[] moreListData = new String[4];


        String count1 = "Fruit & Veg: " + theentry.getFvCount();
        String count2 = "Drinks: " + theentry.getDrCount();

        String comms = theentry.getComment();
        if (comms.equals("")) {
            comms = "No Comment";
        }

        listData.add(theentry.getMeal());
        //listData.add(String.valueOf(theentry.getTimestamp()));
        listData.add(comms);
        listData.add(count1);
        listData.add(count2);

        moreListData[0] = theentry.getMeal();
        moreListData[1] = comms;
        moreListData[2] = count1;
        moreListData[3] = count2;


        dataList = (ListView) findViewById(R.id.listViewOfDatas);
        //ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);

        try {
            dataList.setAdapter(new CustomAdapter(this, moreListData, 3));
        } catch (Exception exp) {
            exp.printStackTrace();
            System.out.print(exp.getCause());
            System.out.print(exp.getCause());

        }
    }

    public void reviewData(View v) {
        //Hide List View, hide review button
        //Show Text box containing existing text, allow typing to edit, show save button

        dataList.setVisibility(View.INVISIBLE);

        comments = (EditText) findViewById(R.id.textComms);
        comments.setVisibility(View.VISIBLE);

        String comms = theentry.getComment();
        if (comms.equals("")) {
            comms = "Tap here to type a comment";
        }

        comments.setText(comms);

        //review = (Button) findViewById(R.id.buttonReview);
        reviewLay.setVisibility(View.INVISIBLE);

        //save = (Button) findViewById(R.id.buttonSave);
        saveLay.setVisibility(View.VISIBLE);

        //LinearLayout edits = (LinearLayout) findViewById(R.id.LinLayEdits);
        edits.setVisibility(View.VISIBLE);

    }

    public void saveCount(View v) {
        Button buttFV = (Button) findViewById(R.id.buttonAdd2);
        Button buttDR = (Button) findViewById(R.id.buttonMinus2);
        Button buttConf = (Button) findViewById(R.id.buttonCountConfirm2);

        buttFV.setVisibility(View.INVISIBLE);
        buttDR.setVisibility(View.INVISIBLE);
        buttConf.setVisibility(View.INVISIBLE);

        comments.setVisibility(View.VISIBLE);
        edits.setVisibility(View.VISIBLE);

        TextView tv = (TextView) findViewById(R.id.textViewCount2);
        tv.setVisibility(View.INVISIBLE);
        tv.setText("0");
    }

    public void addToCounts(View v) {
        //Show counter buttons, cancel button and text view
        currCount = v.getTag().toString();

        Button buttFV = (Button) findViewById(R.id.buttonAdd2);
        Button buttDR = (Button) findViewById(R.id.buttonMinus2);
        Button buttConf = (Button) findViewById(R.id.buttonCountConfirm2);

        buttFV.setVisibility(View.VISIBLE);
        buttDR.setVisibility(View.VISIBLE);
        buttConf.setVisibility(View.VISIBLE);

        comments.setVisibility(View.INVISIBLE);
        edits.setVisibility(View.INVISIBLE);

        TextView tv = (TextView) findViewById(R.id.textViewCount2);
        tv.setVisibility(View.VISIBLE);

        if (currCount.equals("Drink")) {
            tv.setText(String.valueOf(dr));
            //tv.setText(String.valueOf(initDR + drDiff));
        } else {
            tv.setText(String.valueOf(fv));
           // tv.setText(String.valueOf(initFV + fvDiff));
        }
    }

    public void updateLocals(int val) {
        if (currCount.equals("Drink")) {
            dr = val;
            drChange = true;
        } else {
            fv = val;
            fvChange = true;
        }
    }

    public void incCount(View v) {
        TextView tv = (TextView) findViewById(R.id.textViewCount2);
        int curr = Integer.valueOf(tv.getText().toString());
        //int curr=0;
        //if (currCount.equals("Drink")) {
        //    if (currDR < 8) {
        //        currDR++;
         //   }
         //   curr=initDR+currDR;
        //}else {
        //    if (currFV < 8) {
        //        currFV++;
        //    }
        //    curr=initFV+currFV;
        //}


        if (curr < 8) {
                    curr++;
               }
        tv.setText(String.valueOf(curr));

        updateLocals(curr);
    }

    public void decCount(View v) {
        TextView tv = (TextView) findViewById(R.id.textViewCount2);
        int curr = Integer.valueOf(tv.getText().toString());
        if (curr > 0) {
            curr--;
        }
        tv.setText(String.valueOf(curr));
        updateLocals(curr);
    }

    public void showMealTypes(View v) {
        ForDLay.setVisibility(View.INVISIBLE);
        meals.setVisibility(View.VISIBLE);
    }

    public void updateMealTag(View v) {
        String tag = v.getTag().toString();
        newMeal = tag;
        meals.setVisibility(View.INVISIBLE);
        edits.setVisibility(View.VISIBLE);
        saveLay.setVisibility(View.VISIBLE);
        comments.setVisibility(View.VISIBLE);
    }

    public void editMealTag(View v) {
        //LinLayForD
        ForDLay.setVisibility(View.VISIBLE);

        edits.setVisibility(View.INVISIBLE);
        comments.setVisibility(View.INVISIBLE);
    }


    public void updateMealComment(String comment) {
        ContentValues cv = new ContentValues();
        cv.put("comment_data", comment);
        if (!newMeal.equals("")) {
            cv.put("meal", newMeal);
        }

        cv.put("fv_count", fv);
        cv.put("drink_count", dr);

        String[] updateArgs = {theentry.getTimestamp().toString()};
        dbCont.updateComment(this, cv, updateArgs);
    }


    public void saveReview(View v) {
        //Send updated comment to database
        reviewLay.setVisibility(View.VISIBLE);
        saveLay.setVisibility(View.INVISIBLE);


        String updated = comments.getText().toString();

        dataList.setVisibility(View.VISIBLE);
        theentry.setComment(updated);
        comments.setText(updated);
        comments.setVisibility(View.INVISIBLE);

        oldMeal = "";
        if (!newMeal.equals("")) {

            oldMeal = theentry.getMeal();
            theentry.setMeal(newMeal);
        }

        fvDiff = fv - initFV;
        drDiff = dr - initDR;

        int fvToPass = initFV;
        int drToPass = initDR;




        fvToPass = fvDiff + DataHolder.todaysFV;
        if (fvChange) {

            theentry.setFvCount(fv);
        } else {
            fv = initFV;
        }


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

        setListView();

        String comms = updated;
        if (comms.equals("Tap here to type a comment")) {
            updated = "";
        }

        updateMealComment(updated);

        checkTargetProgress();
    }



    public void checkTargetProgress(){

        boolean targetCheck = DataHolder.checkCompleted(this);

        Toast seven;
        if(targetCheck)
        {
            seven = Toast.makeText(this, "Well done, you've completed your daily goals!", Toast.LENGTH_LONG);
        }else
        {
            seven = Toast.makeText(this, "Not quite done", Toast.LENGTH_LONG);
        }

        seven.show();
    }


    public void updateCountsDB(int fvCount, int drinkCount, Date theDate) {
        ContentValues cv = new ContentValues();

        int f = fvCount;
        int d = drinkCount;

        cv.put("fv_count", f);
        cv.put("drink_count", d);
        String[] updateArgs = {theDate.toString()};


        if (!newMeal.equals("") && !newMeal.equals("Snack") && !newMeal.equals("Drink")) {
            String toAdd = "had" + newMeal;
            cv.put(toAdd, true);
        }

        if (!oldMeal.equals("") && !oldMeal.equals("Snack") && !oldMeal.equals("Drink")) {
            int oldCount = counts.getAsInteger((oldMeal + "Count"));
            if (!(oldCount > 1)) {
                String toAdd = "had" + oldMeal;
                cv.put(toAdd, false);
            }
        }

        boolean success = dbCont.updateCountsDB(this, theDate, cv, updateArgs);
        if (!success) {
            DataHolder.todaysFV = f;
            DataHolder.todaysDrinks = d;
        }
    }


    public byte[] readImageFromFile(String filename) {
        try {
            InputStream is = openFileInput(filename);

            byte[] photoData = org.apache.commons.io.IOUtils.toByteArray(is);

            System.out.println("");
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
