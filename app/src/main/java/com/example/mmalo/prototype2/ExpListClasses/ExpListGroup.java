package com.example.mmalo.prototype2.ExpListClasses;

import java.util.ArrayList;

/**
 * Created by mmalo on 14/02/2017.
 */
public class ExpListGroup {

    private String groupName;
    private ArrayList<ExpListChild> children;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return groupName;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.groupName = name;
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    public ArrayList<ExpListChild> getItems() {
        return children;
    }

    /**
     * Sets items.
     *
     * @param ch the ch
     */
    public void setItems(ArrayList<ExpListChild> ch) {
        this.children = ch;
    }





}
