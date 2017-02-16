package com.example.mmalo.prototype2.Models;

import java.util.ArrayList;

/**
 * Created by mmalo on 15/02/2017.
 */

public class Week {
    Day [] thisWeek;

    public Week(){
        thisWeek = new Day[7];
        for(int i = 0; i<7; i++)
        {
            thisWeek[i] = null;
        }
    }

    public Day [] getWeek(){
        return this.thisWeek;
    }

    public void setWeek(Day [] week){
        this.thisWeek = week;
    }

}
