package com.example.mmalo.prototype2.Models;

/**
 * Created by mmalo on 15/02/2017.
 */

public class Day {
    String theDay;
    String theDate;

    public Day(String dy, String dt){
        this.theDay = dy;
        this.theDate = dt;
    }

    public void setTheDay(String d){
        this.theDay = d;
    }

    public void setDate(String d){
        this.theDate = d;
    }

    public String getTheDay(){
        return this.theDay;
    }

    public String getDate(){
        return this.theDate;
    }

}
