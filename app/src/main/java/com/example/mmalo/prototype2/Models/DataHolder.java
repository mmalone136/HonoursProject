package com.example.mmalo.prototype2.Models;

import android.content.Context;

import com.example.mmalo.prototype2.DB.DBContainer;

import java.sql.Date;

/**
 * Created by mmalo on 28/02/2017.
 */

public class DataHolder {

    public static int todaysFV;
    public static int todaysDrinks;
    public static boolean todayBreak;
    public static boolean todayLunch;
    public static boolean todayDinner;

    public static boolean todayComplete;
    public boolean doneNotification;

    public static boolean dataRead = false;

    public static void readData(Context cont){
        try {
            DBContainer dbCont = new DBContainer();

            java.util.Date theDate = new java.util.Date();
            Date today = new Date(theDate.getTime());

            int[] countData = dbCont.readCountData(cont, today);

            DataHolder.todaysFV = countData[0];
            DataHolder.todaysDrinks = countData[1];

            int hadBreak = countData[2];
            int hadLunch = countData[3];
            int hadDinner = countData[4];

            DataHolder.todayBreak = (hadBreak > 0);
            DataHolder.todayLunch = (hadLunch > 0);
            DataHolder.todayDinner = (hadDinner > 0);

            dataRead = true;

        } catch (Exception e) {
            e.printStackTrace();
            DataHolder.todaysFV = 0;
            DataHolder.todaysDrinks = 0;
        }

    }


    public static boolean checkCompleted(Context cont){

        readData(cont);

        if(todaysFV>=5&&todaysDrinks>=8&&todayBreak&&todayLunch&&todayDinner)
        {
            todayComplete = true;
        }
        else
        {
            todayComplete = false;
        }

        return todayComplete;
    }


}
