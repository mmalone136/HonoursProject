package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.DB.DBContainer;
import com.example.mmalo.prototype2.Models.DataHolder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mmalo on 28/02/2017.
 */
public class TargetsActivity extends AppCompatActivity {
    /**
     * The Db cont.
     */
    public DBContainer dbCont;
    /**
     * The Info view.
     */
    ImageView infoView;
    /**
     * The Lin lay icons.
     */
    LinearLayout linLayIcons;
    /**
     * The Close button.
     */
    ImageButton closeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Create activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targets);

        //Set up needed variables
        dbCont = new DBContainer();
        readCountData();
        DataHolder.readData(this);
        updateTargetImages();

        //Set up needed references
        infoView = (ImageView) findViewById(R.id.countInfo);
        linLayIcons = (LinearLayout) findViewById(R.id.linLayIcons);
        closeButton = (ImageButton) findViewById(R.id.crossButton);
    }

    /**
     * Read count data.
     */
    public void readCountData() {
        try {
            //Get current date
            java.util.Date theDate = new java.util.Date();
            Date today = new Date(theDate.getTime());

            //Read counts data for current day
            int[] countData = dbCont.readCountData(this,today);

            //Store count data in static holder variables
            DataHolder.todaysFV = countData[0];
            DataHolder.todaysDrinks = countData[1];

            //Threshold values, cap them as a minimum of zero
            // This avoids errors and broken things
            if (DataHolder.todaysFV < 0) {
                DataHolder.todaysFV = 0;
            }
            if (DataHolder.todaysDrinks < 0) {
                DataHolder.todaysDrinks = 0;
            }

            //Store count data in specific local variables for easy comparison
            int hadBreak = countData[2];
            int hadLunch = countData[3];
            int hadDinner = countData[4];

            //Compare each with zero - set static holders to boolean return value
            DataHolder.todayBreak = (hadBreak > 0);
            DataHolder.todayLunch = (hadLunch > 0);
            DataHolder.todayDinner = (hadDinner > 0);

        } catch (Exception e) {
            //Error has occurred - set static values to zero so nothing nulls and breaks
            e.printStackTrace();
            DataHolder.todaysFV = 0;
            DataHolder.todaysDrinks = 0;
        }
   }

    /**
     * Update target images.
     */
    public void updateTargetImages() {

        // Get reference to ImageView objects
        ImageView bfast = (ImageView) findViewById(R.id.imgBreak);
        ImageView lunch = (ImageView) findViewById(R.id.imgLunch);
        ImageView dinner = (ImageView) findViewById(R.id.imgDinner);

        // Check if current day has had breakfast entry
        // Set Colour image (true) or Greyed out (false) based on this value
        if (DataHolder.todayBreak) {
            bfast.setImageResource(R.drawable.breakfast);
        } else {
            bfast.setImageResource(R.drawable.breakfast_grey);
        }

        // Check if current day has had Lunch entry
        // Set Colour image (true) or Greyed out (false) based on this value
        if (DataHolder.todayLunch) {
            lunch.setImageResource(R.drawable.lunch);
        } else {
            lunch.setImageResource(R.drawable.lunchgrey);
        }

        // Check if current day has had Dinner entry
        // Set Colour image (true) or Greyed out (false) based on this value
        if (DataHolder.todayDinner) {
            dinner.setImageResource(R.drawable.dinn);
        } else {
            dinner.setImageResource(R.drawable.dinngrey);
        }

        //Create list of references to ImageView object for F&V progress to allow easy updating
        List<ImageView> fvCounts = new ArrayList<>();
        ImageView currFV = (ImageView) findViewById(R.id.imgFV1);
        fvCounts.add(currFV);
        currFV = (ImageView) findViewById(R.id.imgFV2);
        fvCounts.add(currFV);
        currFV = (ImageView) findViewById(R.id.imgFV3);
        fvCounts.add(currFV);
        currFV = (ImageView) findViewById(R.id.imgFV4);
        fvCounts.add(currFV);
        currFV = (ImageView) findViewById(R.id.imgFV5);
        fvCounts.add(currFV);

        //Loop 0 to value of current day F&V count
        // Colours a portion of progress bar as number increases to max of i = 4 (Where count will go up to 5)
        for (int i = 0; i < DataHolder.todaysFV; i++) {
            if (i == 0) {
                fvCounts.get(i).setImageResource(R.drawable.apple);
            }else if(i==1){
                fvCounts.get(i).setImageResource(R.drawable.orange);
            }else if(i==2){
                fvCounts.get(i).setImageResource(R.drawable.broccoli);
            }else if(i==3){
                fvCounts.get(i).setImageResource(R.drawable.mushroom);
            }else if(i==4){
                fvCounts.get(i).setImageResource(R.drawable.carrot);
            }
        }

        //Get reference to glass progress bar ImageView
        ImageView drinkCount = (ImageView) findViewById(R.id.imgDrinkCount);

        //Get drinks total and switch case to find the current value
        //Set image based on value of progress - max at 8 for full glass
        int drinks = DataHolder.todaysDrinks;
        switch (drinks) {
            case 0:
                drinkCount.setImageResource(R.drawable.glassempty1);
                break;
            case 1:
                drinkCount.setImageResource(R.drawable.glass1_8);
                break;
            case 2:
                drinkCount.setImageResource(R.drawable.glass1_4);
                break;
            case 3:
                drinkCount.setImageResource(R.drawable.glass1_4);
                break;
            case 4:
                drinkCount.setImageResource(R.drawable.glasshalf);
                break;
            case 5:
                drinkCount.setImageResource(R.drawable.glass5_8);
                break;
            case 6:
                drinkCount.setImageResource(R.drawable.glass3_4);
                break;
            case 7:
                drinkCount.setImageResource(R.drawable.glass7_8);
                break;
            case 8:
                drinkCount.setImageResource(R.drawable.glassfull);
                break;
            default:
                if (drinks >= 8) {
                    drinkCount.setImageResource(R.drawable.glassfull);
                } else {
                    drinkCount.setImageResource(R.drawable.emptyglass2);
                }
                break;

        }
        //Update text representation of total
        TextView tvDrinks = (TextView) findViewById(R.id.textViewDrinkCount);
        tvDrinks.setText("\n" + drinks + "\n_____\n\n8");
    }

    /**
     * Back option - go back to main menu.
     *
     * @param v the v
     */
    public void backOption(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }

    /**
     * Show info - changes depending on button pressed
     *
     * @param v the v
     */
    public void showInfo(View v) {

        //Get tag of button which called the function.
        String tag = v.getTag().toString();

        //Check which tag it is and show correct option
        if (tag.equals("FVCount")) {
            infoView.setImageResource(R.drawable.fvinfo);
        } else if (tag.equals("Meals")) {
            infoView.setImageResource(R.drawable.mealinfo);
        } else if (tag.equals("Drink")) {
            infoView.setImageResource(R.drawable.drinkinfo);
        }

        //Show and hide necessary layouts to show info image
        infoView.setVisibility(View.VISIBLE);
        linLayIcons.setVisibility(View.INVISIBLE);
        closeButton.setVisibility(View.VISIBLE);
    }


    /**
     * Hide info.
     *
     * @param v the v
     */
    public void hideInfo(View v) {
        infoView.setVisibility(View.INVISIBLE);
        linLayIcons.setVisibility(View.VISIBLE);
        closeButton.setVisibility(View.INVISIBLE);

    }

}
