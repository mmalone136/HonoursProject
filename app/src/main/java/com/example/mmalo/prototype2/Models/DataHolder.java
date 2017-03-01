package com.example.mmalo.prototype2.Models;

import android.content.Context;

import com.example.mmalo.prototype2.DB.DBContainer;

/**
 * Created by mmalo on 28/02/2017.
 */

public class DataHolder {

    public static int todaysFV;
    public static int todaysDrinks;
    public static boolean todayBreak;
    public static boolean todayLunch;
    public static boolean todayDinner;

    public static boolean dataRead = false;

    public static void readData(Context cont){
        try {
            DBContainer dbCont = new DBContainer();
            int[] countData = dbCont.readCountData(cont);

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


}
