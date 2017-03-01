package com.example.mmalo.prototype2;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targets);
        dbCont = new DBContainer();
        readCountData();
        updateTargetImages();
    }

    public void readCountData() {
        try {
            int[] countData = dbCont.readCountData(this);

            DataHolder.todaysFV = countData[0];
            DataHolder.todaysDrinks = countData[1];

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

    public void updateTargetImages(){

        ImageView bfast = (ImageView) findViewById(R.id.imgBreak);
        ImageView lunch = (ImageView) findViewById(R.id.imgLunch);
        ImageView dinner = (ImageView) findViewById(R.id.imgDinner);


        if(DataHolder.todayBreak){
            bfast.setImageResource(R.drawable.breakfast);
        }else{
            bfast.setImageResource(R.drawable.breakfast_grey);
        }

        if(DataHolder.todayLunch){
            lunch.setImageResource(R.drawable.lunch);
        }else{
            lunch.setImageResource(R.drawable.lunchgrey);
        }

        if(DataHolder.todayDinner){
            dinner.setImageResource(R.drawable.dinn);
        }else{
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

        for(int i = 0;i<DataHolder.todaysFV;i++){

                fvCounts.get(i).setImageResource(R.drawable.apple);
        }

    }



    public void backOption(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }

}
