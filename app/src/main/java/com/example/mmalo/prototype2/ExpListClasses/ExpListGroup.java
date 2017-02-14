package com.example.mmalo.prototype2.ExpListClasses;

import java.util.ArrayList;

/**
 * Created by mmalo on 14/02/2017.
 */

public class ExpListGroup {

    private String groupName;
    private ArrayList<ExpListChild> children;

    public String getName() {
        return groupName;
    }

    public void setName(String name) {
        this.groupName = name;
    }

    public ArrayList<ExpListChild> getItems() {
        return children;
    }

    public void setItems(ArrayList<ExpListChild> ch) {
        this.children = ch;
    }





}
