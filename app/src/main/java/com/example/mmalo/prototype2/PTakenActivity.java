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

    public static byte[] photoData;
    public static Bitmap thePic;
    String commentData;
    String mealChoice;
    public static Timestamp timetaken;
    public static String filename;
    public DBContainer dbCont;
    boolean firstClick;
    int fv;
    int dr;
    String currCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptaken);
        initValues();
        dbCont = new DBContainer();
        Toast t = Toast.makeText(this, "PTAKEN_ACT", Toast.LENGTH_LONG);
        //t.show();
        DataHolder.readData(this);
    }

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

        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }


    public void initValues() {

        int apiLevel = Build.VERSION.SDK_INT;
        Bitmap bitmap;
        thePic = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);

        if (apiLevel ==19 ||apiLevel ==21) {
            bitmap = thePic;
        }else {
            Matrix rotationMat = new Matrix();
            rotationMat.postRotate(270);
            bitmap = Bitmap.createBitmap(thePic, 0, 0, thePic.getWidth(), thePic.getHeight(), rotationMat, true);
        }

        ImageView taken = (ImageView) findViewById(R.id.imageTaken);
        taken.setImageBitmap(bitmap);

        //View par = (View) taken.getParent();
        //int height = par.getHeight();

        int takenHeight = taken.getHeight();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int theHeight;
        if (height < 1200) {

        } else {
            theHeight = height / 2;
            ViewGroup.LayoutParams params = taken.getLayoutParams();
            params.height = theHeight;
            taken.requestLayout();
        }

        //taken.setMinimumHeight(height);
        //taken.setMaxHeight(height);

        fv = 0;
        dr = 0;
        firstClick = true;
    }

    public void clearTextBox(View v) {
        if (v.getId() == R.id.textComments) ;
        {
            if (firstClick) {
                EditText e = (EditText) v;
                e.setText("");
                firstClick = false;
            }
        }
    }

    public void mealButton(View v) {
        //if tag == 1,2,3,4 set meal = corresponding value then do:

        mealChoice = v.getTag().toString();


        TextView prompt = (TextView) findViewById(R.id.textPrompts);
        prompt.setVisibility(View.INVISIBLE);

        LinearLayout lays = (LinearLayout) findViewById(R.id.LinLayLayouts);
        lays.setVisibility(View.INVISIBLE);

        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);


        LinearLayout nexts = (LinearLayout) findViewById(R.id.LinLayNexts);
        nexts.setVisibility(View.VISIBLE);

        //Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        //buttonSubmit.setVisibility(View.VISIBLE);

        //Button buttonFV = (Button) findViewById(R.id.buttonFV);
        //buttonFV.setVisibility(View.VISIBLE);
        //Button buttonDR = (Button) findViewById(R.id.buttonDR);
        //buttonDR.setVisibility(View.VISIBLE);

    }

    public void addToCounts(View v) {
        //Show counter buttons, cancel button and text view
        currCount = v.getTag().toString();

        Button buttFV = (Button) findViewById(R.id.buttonAdd);
        Button buttDR = (Button) findViewById(R.id.buttonMinus);
        Button buttConf = (Button) findViewById(R.id.buttonCountConfirm);

        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.INVISIBLE);


        buttFV.setVisibility(View.VISIBLE);
        buttDR.setVisibility(View.VISIBLE);
        buttConf.setVisibility(View.VISIBLE);

        TextView tv = (TextView) findViewById(R.id.textViewCount);
        tv.setVisibility(View.VISIBLE);

        TextView tv2 = (TextView) findViewById(R.id.portionPrompt);
        tv2.setVisibility(View.VISIBLE);

        ImageView taken = (ImageView) findViewById(R.id.imageTaken);
        taken.setVisibility(View.INVISIBLE);


        if (currCount.equals("Drink")) {
            tv.setText(String.valueOf(dr));
        } else {
            tv.setText(String.valueOf(fv));
        }


    }

    public void saveCount(View v) {
        Button buttFV = (Button) findViewById(R.id.buttonAdd);
        Button buttDR = (Button) findViewById(R.id.buttonMinus);
        Button buttConf = (Button) findViewById(R.id.buttonCountConfirm);

        buttFV.setVisibility(View.INVISIBLE);
        buttDR.setVisibility(View.INVISIBLE);
        buttConf.setVisibility(View.INVISIBLE);

        TextView tv = (TextView) findViewById(R.id.textViewCount);
        tv.setVisibility(View.INVISIBLE);
        TextView tv2 = (TextView) findViewById(R.id.portionPrompt);
        tv2.setVisibility(View.INVISIBLE);
        tv.setText("0");

        ImageView taken = (ImageView) findViewById(R.id.imageTaken);
        taken.setVisibility(View.VISIBLE);

        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);


    }

    public void incCount(View v) {
        TextView tv = (TextView) findViewById(R.id.textViewCount);
        int curr = Integer.valueOf(tv.getText().toString());
        if (curr < 8) {
            curr++;
        }
        tv.setText(String.valueOf(curr));

        updateLocals(curr);

    }

    public void decCount(View v) {
        TextView tv = (TextView) findViewById(R.id.textViewCount);
        int curr = Integer.valueOf(tv.getText().toString());
        if (curr > 0) {
            curr--;
        }
        tv.setText(String.valueOf(curr));
        updateLocals(curr);

    }

    public void updateLocals(int val) {
        if (currCount.equals("Drink")) {
            dr = val;
        } else {
            fv = val;
        }
    }

    public int saveImageToFile(byte[] datas) {
        FileOutputStream fos;
        //filename = theTime.toString();

        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(datas);
            fos.close();
            return 7;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public void doFood(View v) {
        LinearLayout fd = (LinearLayout) findViewById(R.id.LinLayFD);
        fd.setVisibility(View.INVISIBLE);
    }

    public void doDrink(View v) {

        //TODO: Refactor this, getting layouts instead of individual views
        //TODO: Combine with mealButton()

        mealChoice = v.getTag().toString();

        LinearLayout fd = (LinearLayout) findViewById(R.id.LinLayFD);
        fd.setVisibility(View.INVISIBLE);

        LinearLayout lays = (LinearLayout) findViewById(R.id.LinLayLayouts);
        lays.setVisibility(View.INVISIBLE);

        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);


        LinearLayout nexts = (LinearLayout) findViewById(R.id.LinLayNexts);
        nexts.setVisibility(View.VISIBLE);


        //Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        //buttonSubmit.setVisibility(View.VISIBLE);

        //Button buttonFV = (Button) findViewById(R.id.buttonFV);
        //buttonFV.setVisibility(View.VISIBLE);
        //Button buttonDR = (Button) findViewById(R.id.buttonDR);
        //buttonDR.setVisibility(View.VISIBLE);


        TextView prompt = (TextView) findViewById(R.id.textPrompts);
        prompt.setVisibility(View.INVISIBLE);

    }

    public void submitForm(View v) {
        EditText comments = (EditText) findViewById(R.id.textComments);
        comments.setVisibility(View.VISIBLE);
        commentData = comments.getText().toString();

        if (commentData.equals("Tap here to type a comment")) {
            commentData = "";
        }

        String fp = filename;

        DiaryData entry = new DiaryData(null, commentData, null, timetaken, mealChoice, fp, fv, dr);

        Toast t;
        int success = saveImageToFile(photoData);

        if (success == 7) {
            long l = dbCont.insertEntry(entry, getApplicationContext(), fv, dr);
            if (l != -1) {
                t = Toast.makeText(this, "Entry Submitted Successfully", Toast.LENGTH_SHORT);
                dbCont.updateCountsDB2(this, fv, dr, mealChoice, timetaken);
            } else {
                t = Toast.makeText(this, "ERROR Submitting Entry", Toast.LENGTH_SHORT);
            }
        } else {
            t = Toast.makeText(this, "ERROR Submitting Entry", Toast.LENGTH_SHORT);
        }

        thePic.recycle();
        photoData = null;
        t.show();


        //WeekviewActivity.showNotif = false;

        boolean targetCheck = DataHolder.checkCompleted(this);
        Intent i;

        if (targetCheck) {
            WeekviewActivity.showNotif = true;
            i = new Intent(getBaseContext(), WeekviewActivity.class);
        } else {
            i = new Intent(getBaseContext(), OptionsActivity.class);
        }

        this.startActivity(i);
    }


    public void cancelForm(View v) {

        Toast t = Toast.makeText(this, "Entry Cancelled", Toast.LENGTH_LONG);
        t.show();
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);


    }


}
