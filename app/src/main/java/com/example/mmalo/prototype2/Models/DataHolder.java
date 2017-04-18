package com.example.mmalo.prototype2.Models;

import android.content.Context;

import com.example.mmalo.prototype2.DB.DBContainer;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by mmalo on 28/02/2017.
 */
public class DataHolder {

    /**
     * The constant todaysFV.
     */
    public static int todaysFV;
    /**
     * The constant todaysDrinks.
     */
    public static int todaysDrinks;
    /**
     * The constant todayBreak.
     */
    public static boolean todayBreak;
    /**
     * The constant todayLunch.
     */
    public static boolean todayLunch;
    /**
     * The constant todayDinner.
     */
    public static boolean todayDinner;
    /**
     * The constant strCurrentDate.
     */
    public static String strCurrentDate;

    /**
     * The constant todayComplete.
     */
    public static boolean todayComplete;

    /**
     * The constant dataRead.
     */
    public static boolean dataRead = false;

    /**
     * Read data.
     *
     * @param cont the cont
     */
    public static void readData(Context cont) {
        try {

            java.util.Date date = new java.util.Date();
            SimpleDateFormat formDates = new SimpleDateFormat("yyyy-MM-dd");
            strCurrentDate = formDates.format(date);
            DBContainer dbCont = new DBContainer();
            java.util.Date theDate = new java.util.Date();
            Date today = new Date(theDate.getTime());

            //Read Data in from database
            int[] countData = dbCont.readCountData(cont, today);

            //Assign db values to static variables
            DataHolder.todaysFV = countData[0];
            DataHolder.todaysDrinks = countData[1];
            int hadBreak = countData[2];
            int hadLunch = countData[3];
            int hadDinner = countData[4];

            //Calculate flags for hadBreak,Lunch and Dinner
            //Store results in static variables
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


    /**
     * Check completed boolean.
     *
     * @param cont the cont
     * @return the boolean
     */
    public static boolean checkCompleted(Context cont) {

        readData(cont);

        //Check if all targets have been met
        if (todaysFV >= 5 && todaysDrinks >= 8 && todayBreak && todayLunch && todayDinner) {
            todayComplete = true;
        } else {
            todayComplete = false;
        }

        //Read from notification data file
        boolean shouldNotif = false;
        DBContainer dbCont = new DBContainer();
        int[] notif = dbCont.readFromNotifFile(cont, strCurrentDate);


        if (todayComplete) {
            //if notif null or (10) found nothing in file or (0) found previous entry to say not done
            if (notif == null || (notif[0] == 10 || notif[1] == 10) || (notif[0] == 0 || notif[1] == 0)) {
                dbCont.writeToNotifFile(cont, strCurrentDate, 1, 1);
                shouldNotif = true;
            }
        } else {
            //if not found in file or previous entry to say not done
            if ((notif[0] != 10 || notif[1] != 10) || (notif[0] == 1 || notif[1] == 1)) {
                dbCont.writeToNotifFile(cont, strCurrentDate, 0, 0);
            }

        }

        //Return results
        return (todayComplete && shouldNotif);
    }


}
