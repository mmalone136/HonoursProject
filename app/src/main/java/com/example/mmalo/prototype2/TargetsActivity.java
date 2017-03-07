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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mmalo on 28/02/2017.
 */

public class TargetsActivity extends AppCompatActivity {
    public DBContainer dbCont;
    ImageView infoView;
    LinearLayout linLayIcons;
    ImageButton closeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targets);
        dbCont = new DBContainer();
        readCountData();
        updateTargetImages();

        infoView = (ImageView) findViewById(R.id.countInfo);
        linLayIcons = (LinearLayout) findViewById(R.id.linLayIcons);
        closeButton = (ImageButton) findViewById(R.id.crossButton);
    }

    public void readCountData() {
        try {
            int[] countData = dbCont.readCountData(this);


            DataHolder.todaysFV = countData[0];
            DataHolder.todaysDrinks = countData[1];

            if (DataHolder.todaysFV < 0) {
                DataHolder.todaysFV = 0;
            }
            if (DataHolder.todaysDrinks < 0) {
                DataHolder.todaysDrinks = 0;
            }


            int hadBreak = countData[2];
            int hadLunch = countData[3];
            int hadDinner = countData[4];

            DataHolder.todayBreak = (hadBreak > 0);
            DataHolder.todayLunch = (hadLunch > 0);
            DataHolder.todayDinner = (hadDinner > 0);

        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();

            DataHolder.todaysFV = 0;
            DataHolder.todaysDrinks = 0;

        }

        //FIXTHIS
//        TextView fv = (TextView) findViewById(R.id.textViewFV);
//        TextView dr = (TextView) findViewById(R.id.textViewDrinks);
//        TextView bf = (TextView) findViewById(R.id.tvBreak);
//        TextView lu = (TextView) findViewById(R.id.tvLunch);
//        TextView dn = (TextView) findViewById(R.id.tvDinn);
//
//        fv.setText("Fruit & Veg: " + DataHolder.todaysFV);
//        dr.setText("Drinks: " + DataHolder.todaysDrinks);
//        bf.setText("Had Breakfast: " + DataHolder.todayBreak);
//        lu.setText("Had Lunch: " + DataHolder.todayLunch);
//        dn.setText("Had Dinner: " + DataHolder.todayDinner);

    }

    public void updateTargetImages() {

        ImageView bfast = (ImageView) findViewById(R.id.imgBreak);
        ImageView lunch = (ImageView) findViewById(R.id.imgLunch);
        ImageView dinner = (ImageView) findViewById(R.id.imgDinner);


        if (DataHolder.todayBreak) {
            bfast.setImageResource(R.drawable.breakfast);
        } else {
            bfast.setImageResource(R.drawable.breakfast_grey);
        }

        if (DataHolder.todayLunch) {
            lunch.setImageResource(R.drawable.lunch);
        } else {
            lunch.setImageResource(R.drawable.lunchgrey);
        }

        if (DataHolder.todayDinner) {
            dinner.setImageResource(R.drawable.dinn);
        } else {
            dinner.setImageResource(R.drawable.dinngrey);
        }


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


        ImageView drinkCount = (ImageView) findViewById(R.id.imgDrinkCount);

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
        TextView tvDrinks = (TextView) findViewById(R.id.textViewDrinkCount);
        tvDrinks.setText("\n" + drinks + "\n_____\n\n8");


    }

    public void backOption(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }

    public void showInfo(View v) {
        //FVCount
        //Meals
        //Drink
        String tag = v.getTag().toString();

        if (tag.equals("FVCount")) {
            infoView.setImageResource(R.drawable.fvinfo);
        } else if (tag.equals("Meals")) {
            infoView.setImageResource(R.drawable.mealinfo);
        } else if (tag.equals("Drink")) {
            infoView.setImageResource(R.drawable.drinkinfo);
        }

        infoView.setVisibility(View.VISIBLE);
        linLayIcons.setVisibility(View.INVISIBLE);
        closeButton.setVisibility(View.VISIBLE);
    }


    public void hideInfo(View v) {
        infoView.setVisibility(View.INVISIBLE);
        linLayIcons.setVisibility(View.VISIBLE);
        closeButton.setVisibility(View.INVISIBLE);

    }

}
