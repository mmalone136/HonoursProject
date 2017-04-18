package com.example.mmalo.prototype2.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Models.DataHolder;
import com.example.mmalo.prototype2.Models.DiaryData;
import com.example.mmalo.prototype2.OptionsActivity;
import com.example.mmalo.prototype2.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mmalo on 27/02/2017.
 */
public class DBContainer {

    /**
     * The Dbh.
     */
    DBHelper dbh;
    /**
     * The Db.
     */
    SQLiteDatabase db;

    /**
     * Instantiates a new DBContainer.
     */
    public DBContainer() {
    }

    /**
     * Drop tables.
     *
     * @param cont the cont
     */
    public void dropTables(Context cont) {
        //Connect to database
        dbh = new DBHelper(cont);
        db = dbh.getWritableDatabase();

        //Execute Delete table statements
        db.execSQL("DROP TABLE IF EXISTS diary_entries");
        db.execSQL("DROP TABLE IF EXISTS counts");
        //Close DB connection
        db.close();
    }

    /**
     * Create tables.
     *
     * @param cont the cont
     */
    public void createTables(Context cont) {
        //Connect to database
        dbh = new DBHelper(cont);
        db = dbh.getWritableDatabase();

        //Define string create and execute Statement
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS diary_entries (" +
                        " entry_ID INTEGER PRIMARY KEY," +
                        " photo_data BLOB ," +
                        " comment_data TEXT," +
                        " audio_data BLOB ," +
                        " time_stamp TEXT," +
                        " filepath TEXT," +
                        " meal TEXT," +
                        " fv_count INT," +
                        " drink_count INT)";
        db.execSQL(SQL_CREATE_ENTRIES);

        //Define string create statement and execute statement
        String SQL_CREATE_COUNTS =
                "CREATE TABLE IF NOT EXISTS counts (" +
                        " entry_ID INTEGER PRIMARY KEY," +
                        " time_stamp TEXT," +
                        " fv_count INT," +
                        " drink_count INT," +
                        " hadBreakfast BOOLEAN," +
                        " hadLunch BOOLEAN," +
                        " hadDinner BOOLEAN)";
        db.execSQL(SQL_CREATE_COUNTS);

        //Close Database connection
        db.close();
    }

    /**
     * Insert entry long.
     *
     * @param dd    the dd
     * @param cont  the cont
     * @param theFV the the fv
     * @param theDR the the dr
     * @return the long
     */
    public long insertEntry(DiaryData dd, Context cont, int theFV, int theDR) {
        //Open Database connection
        dbh = new DBHelper(cont);
        db = dbh.getWritableDatabase();

        //Create NameValue pairs and bind parameters
        ContentValues vals = new ContentValues();
        vals.put("comment_data", dd.getComment());
        vals.put("audio_data", dd.getSpokenData());
        vals.put("time_stamp", String.valueOf(dd.getTimestamp()));
        vals.put("meal", dd.getMeal());
        vals.put("filepath", dd.getFilepath());
        vals.put("fv_count", theFV);
        vals.put("drink_count", theDR);

        //Execute insert statement, return affected row ID
        long rowID = db.insert("diary_entries", null, vals);
        return rowID;
    }


    /**
     * Write to notification file
     * (Used to check if user should be notified of target achievement)
     *
     * @param cont      the cont
     * @param date      the date
     * @param notif     the notif
     * @param completed the completed
     */
    public void writeToNotifFile(Context cont,String date,int notif, int completed){
        try {
            //Build separated string based on all parameters
            String data=date+","+notif+"," + completed + "\n";

            //Open output file and append data
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(cont.openFileOutput("notif.txt", Context.MODE_PRIVATE));
            outputStreamWriter.append(data);

            //Close stream writer
            outputStreamWriter.close();
        }
        catch (Exception e) {

        }
    }


    /**
     * Read from notif file int [ ].
     * (Used to check if user should be notified of target achievement)
     *
     * @param cont       the cont
     * @param targetDate the target date
     * @return the int [ ]
     */
    public int[] readFromNotifFile(Context cont,String targetDate) {

        // Create toReturn array variable
        int[] toReturn = {10,10};
        try {
            //Open input file
            InputStream inputStream = cont.openFileInput("notif.txt");

            if ( inputStream != null ) {
                //Create stream objects and define string variables needed
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                String [] splitLine;

                //While next read line isn't empty
                while ( (receiveString = bufferedReader.readLine()) != null ) {

                    //Split input line by "," delimiter
                    splitLine = receiveString.split(",");

                    //If split data isn't empty, assign variable for output
                    if(splitLine!=null){
                        if(splitLine[0].equals(targetDate)){
                            toReturn[0] = Integer.parseInt(splitLine[1]);
                            toReturn[1] = Integer.parseInt(splitLine[2]);
                        }
                    }
                }
                inputStream.close();
            }

        } catch (Exception e) {

        }
        return toReturn;
    }


    /**
     * Read count data int [ ].
     *
     * @param cont the cont
     * @param curr the curr
     * @return the int [ ]
     */
    public int[] readCountData(Context cont, Date curr) {
        // Connect to DB
        dbh = new DBHelper(cont);
        db = dbh.getReadableDatabase();

        //Define variables needed
        int[] returnFields = new int[5];
        Date today = curr;
        String[] projection = {"entry_ID", "fv_count", "time_stamp", "drink_count", "hadBreakfast", "hadLunch", "hadDinner"};
        String select = "time_stamp Like ?";
        String[] selArgs = {"%" + today + "%"};

        // Argument Order => Table,Columns, Columns From Where, Values from where, togroup, tofilter groups, sortorder
        // Execute Query, from projection columns
        Cursor cursor = db.query("counts", projection, select, selArgs, null, null, null);

        //Get first response from query
        if (cursor.moveToFirst()) {
            do {
                try {
                    //Get value from each returned column from the cursor object
                    String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
                    int fvCount = cursor.getInt(cursor.getColumnIndexOrThrow("fv_count"));
                    int drinkCount = cursor.getInt(cursor.getColumnIndexOrThrow("drink_count"));
                    int bfast = cursor.getInt(cursor.getColumnIndexOrThrow("hadBreakfast"));
                    int lunch = cursor.getInt(cursor.getColumnIndexOrThrow("hadLunch"));
                    int dinn = cursor.getInt(cursor.getColumnIndexOrThrow("hadDinner"));

                    //Assign returned variables to return array
                    returnFields[0] = fvCount;
                    returnFields[1] = drinkCount;
                    returnFields[2] = bfast;
                    returnFields[3] = lunch;
                    returnFields[4] = dinn;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } while (cursor.moveToNext());
        }
        //Close cursor, DB connection and return array of values
        cursor.close();
        db.close();
        return returnFields;

    }

    /**
     * Update counts db boolean.
     *
     * @param cont       the cont
     * @param theDate    the the date
     * @param cv         the cv
     * @param updateArgs the update args
     * @return the boolean
     */
    //Update called from SummaryActivity
    public boolean updateCountsDB(Context cont, Date theDate, ContentValues cv, String[] updateArgs) {
        try {
            //Connect to DB
            dbh = new DBHelper(cont);
            db = dbh.getWritableDatabase();

            //Execute update statement with passed in argument parameters
            long a = db.update("counts", cv, "time_stamp = ?", updateArgs);

            //If rows affected is 0, insert new entry with same data
            if (a == 0) {
                cv.put("time_stamp", theDate.toString());
                long rowID = db.insert("counts", null, cv);
            }

            //Close DB and return true to show success
            db.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Update counts db 2.
     *
     * @param cont       the cont
     * @param fvCount    the fv count
     * @param drinkCount the drink count
     * @param mealChoice the meal choice
     * @param ts         the ts
     */
    public void updateCountsDB2(Context cont, int fvCount, int drinkCount, String mealChoice, Timestamp ts) {
        try {
            //Connect to DB
            dbh = new DBHelper(cont);
            db = dbh.getWritableDatabase();
            Date theDate = new Date(ts.getTime());

            //Calculate F&V count and drink count to add to parameters
            int f = fvCount + DataHolder.todaysFV;
            int d = drinkCount + DataHolder.todaysDrinks;

            //Create NameValue pairs and container
            ContentValues cv = new ContentValues();
            cv.put("fv_count", f);
            cv.put("drink_count", d);

            //Set which meal flag to update
            switch (mealChoice) {
                case "Breakfast":
                    cv.put("hadBreakfast", true);
                    break;
                case "Lunch":
                    cv.put("hadLunch", true);
                    break;
                case "Dinner":
                    cv.put("hadDinner", true);
                    break;
            }

            String[] updateArgs = {theDate.toString()};

            //Execute update statement
            long a = db.update("counts", cv, "time_stamp = ?", updateArgs);

            //If no rows returned, execute insert on same parameters
            if (a == 0) {
                cv.put("time_stamp", theDate.toString());
                long rowID = db.insert("counts", null, cv);
                System.out.print("");
            }

            //Update local copy of values and close db
            DataHolder.todaysFV = f;
            DataHolder.todaysDrinks = d;
            db.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
        }
    }

    /**
     * Update comment.
     *
     * @param cont       the cont
     * @param cv         the cv
     * @param updateArgs the update args
     */
    public void updateComment(Context cont, ContentValues cv, String[] updateArgs) {
        try {
            //Connect to db
            dbh = new DBHelper(cont);
            db = dbh.getWritableDatabase();

            //Execute update statement
            db.update("diary_entries", cv, "time_stamp = ?", updateArgs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Read entries for date array list.
     *
     * @param cont the cont
     * @param date the date
     * @return the array list
     */
    public ArrayList<DiaryData> readEntriesForDate(Context cont, String date) {
        //Connect to DB
        dbh = new DBHelper(cont);
        db = dbh.getReadableDatabase();

        //Define variables for columns to query, where clause and arguments
        String[] projection = { "entry_ID", "filepath", "comment_data", "time_stamp", "meal", "fv_count", "drink_count" };
        String select = "time_stamp Like ?";
        String[] selArgs = {"%" + date + "%"};

        //Execute query Statement
        Cursor cursor = db.query("diary_entries", projection, select, selArgs, null, null, null);
        ArrayList<DiaryData> entries = new ArrayList<DiaryData>();

        //Move cursor to first result
        if (cursor.moveToFirst()) {
            do {
                try {
                    DiaryData curr;

                    //Retrieve values from result
                    String comm = cursor.getString(cursor.getColumnIndexOrThrow("comment_data"));
                    String file = cursor.getString(cursor.getColumnIndexOrThrow("filepath"));
                    String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
                    String theMeal = cursor.getString(cursor.getColumnIndexOrThrow("meal"));
                    int fvCount = cursor.getInt(cursor.getColumnIndexOrThrow("fv_count"));
                    int drCount = cursor.getInt(cursor.getColumnIndexOrThrow("drink_count"));

                    Timestamp theTime = Timestamp.valueOf(tID);

                    //Create DiaryData object and add to list
                    curr = new DiaryData(null, comm, null, theTime, theMeal, file, fvCount, drCount);
                    entries.add(curr);
                } catch (Exception e) {
                    e.printStackTrace();

                    return null;
                }
            } while (cursor.moveToNext());
        }

        //Close cursor, database connection and return entries list
        cursor.close();
        db.close();
        return entries;
    }

    /**
     * Read unique dates array list.
     *
     * @param cont the cont
     * @return the array list
     */
    public ArrayList<String> readUniqueDates(Context cont) {
        //Connect to Database
        dbh = new DBHelper(cont);
        db = dbh.getReadableDatabase();

        //Set variables for query
        String[] projection = { "time_stamp" };
        String sort = "time_stamp ASC";

        //Execute query
        Cursor cursor = db.query("diary_entries", projection, null, null, null, null, sort);

        //Define lists for output data
        ArrayList<Timestamp> times = new ArrayList<Timestamp>();
        ArrayList<String> dates = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    //Retrieve and format result data
                    String tID = cursor.getString(cursor.getColumnIndexOrThrow("time_stamp"));
                    Timestamp theTime = Timestamp.valueOf(tID);
                    times.add(theTime);
                    Date seven = new Date(theTime.getTime());
                    SimpleDateFormat s = new SimpleDateFormat("EEEE");
                    String day = s.format(seven);

                    //Add to list
                    if (!dates.contains(seven.toString())) {
                        dates.add(seven.toString());//+ " | " + day
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());

        }
        //Close things and return list
        db.close();
        cursor.close();
        return dates;
    }


    /**
     * Delete date.
     *
     * @param cont the cont
     */
//---------- TEMP FUNCTIONS ------------
    public void deleteDate(Context cont) {
        try
        {
            dbh = new DBHelper(cont);
            db = dbh.getWritableDatabase();
            String date = "2017-03-20";
            db.execSQL("DELETE FROM diary_entries WHERE time_stamp LIKE '2017-03-20%'");

            db.execSQL("DELETE FROM counts WHERE time_stamp LIKE '2017-03-20%'");

            System.out.print("");
            db.close();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            ex.printStackTrace();
        }

    }

    /**
     * Insert entry temp.
     *
     * @param dd   the dd
     * @param cont the cont
     */
    public void insertEntryTemp(DiaryData dd, Context cont) {
        DBHelper dbh = new DBHelper(cont);
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues vals = new ContentValues();
        //vals.put("entry_ID",1);
        vals.put("photo_data", dd.getPhotoData());
        vals.put("comment_data", dd.getComment());
        vals.put("audio_data", dd.getSpokenData());
        vals.put("time_stamp", String.valueOf(dd.getTimestamp()));
        vals.put("meal", dd.getMeal());

        long rowID = db.insert("diary_entries", null, vals);
        db.close();
    }
}
