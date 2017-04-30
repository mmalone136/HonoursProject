package com.example.mmalo.prototype2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ScrollView;

import com.example.mmalo.prototype2.ExpListClasses.ExpListChild;
import com.example.mmalo.prototype2.ExpListClasses.ExpListGroup;
import com.example.mmalo.prototype2.ExpListClasses.ExplAdapter;
import com.example.mmalo.prototype2.Models.DataHolder;

import java.util.ArrayList;

/**
 * The type Guide activity.
 */
public class GuideActivity extends AppCompatActivity {

    //TODO: Refactor many things here

    /**
     * The General info.
     */
    ScrollView generalInfo;
    /**
     * The Expl examples.
     */
    ExpandableListView explExamples;

    private ExpandableListAdapter explAdapter;
    private ArrayList<ExpListGroup> explItems;
    private ExpandableListView dataList;
    /**
     * The Images.
     */
    ArrayList<Integer>[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create activity and set up variables and references
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        generalInfo = (ScrollView) findViewById(R.id.generalInfo);
        DataHolder.readData(this);
        explExamples = (ExpandableListView) findViewById(R.id.expListView);
        explExamples.setVisibility(View.INVISIBLE);
        dataList = (ExpandableListView) findViewById(R.id.expListView);
        explItems = setDataGroups();
        images = setDrawables();
        explAdapter = new ExplAdapter(GuideActivity.this, explItems, images);
        dataList.setAdapter(explAdapter);



        //http://stackoverflow.com/questions/7862396/show-only-one-child-of-expandable-list-at-a-time?rq=1
        //Keeps index to previously open group
        // Closes previous group when new one is opened
        dataList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int prevOpenGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != prevOpenGroup)
                    dataList.collapseGroup(prevOpenGroup);
                prevOpenGroup = groupPosition;
            }
        });
    }


    /**
     * Set drawables array list [ ].
     *
     * @return the array list [ ]
     */
    public ArrayList<Integer>[] setDrawables() {

        ArrayList<Integer>[] drawables = new ArrayList[3];



        // TODO: Refactor this lots
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int currInt;
            if (i == 0) {
                currInt = R.drawable.apple;
            }
            else if (i == 1)
            {
                currInt = R.drawable.orange;
            }
            else if (i == 2)
            {
                currInt = R.drawable.tomato;
            }
            else if (i == 3)
            {
                currInt = R.drawable.bananas;
            }
            else if (i == 4)
            {
                currInt = R.drawable.broccoli;
            }
            else if (i == 5)
            {
                currInt = R.drawable.corn;
            }
            else if (i == 6)
            {
                currInt = R.drawable.repeat;
            }
            else if (i == 7)
            {
                currInt = R.drawable.mushroom;
            }
            else if (i == 8)
            {
                currInt = R.drawable.cucumber;
            }
            else if (i == 9)
            {
                currInt = R.drawable.carrot;
            }
            else
            {
                currInt = R.drawable.orange;
            }
            temp.add(currInt);
        }

        ArrayList<Integer> tempTwo = new ArrayList<>();
        for (int i = 0; i < 7; i++) {

            int currInt;

            if (i == 1 || i == 4) {
                currInt = R.drawable.juice;
            } else if (i==2) {
                currInt = R.drawable.teamug;
            }
            else if (i==3) {
                currInt = R.drawable.coffee;
            }
            else if (i==5) {
                currInt = R.drawable.dietbottle;
            }
            else if (i==6) {
                currInt = R.drawable.milk;
            }
            else {
                currInt = R.drawable.glassfull;
            }
        tempTwo.add(currInt);
    }


        ArrayList<Integer> tempthree = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int currInt;
            if (i == 0) {
                currInt = R.drawable.chocolate;
            }
            else if (i == 1)
            {
                currInt = R.drawable.crisps;
            }
            else if (i == 2)
            {
                currInt = R.drawable.notdietbottle;
            }
            else if (i == 3)
            {
                currInt = R.drawable.biscuit;
            }
            else if (i == 4)
            {
                currInt = R.drawable.icecream;
            }
            else if (i == 5)
            {
                currInt = R.drawable.ketchup;
            }
            else if (i == 6)
            {
                currInt = R.drawable.candy;
            }
            else if (i == 7)
            {
                currInt = R.drawable.fastfood;
            }
            else if (i == 8)
            {
                currInt = R.drawable.fries;
            }
            else
            {
                currInt = R.drawable.cake;
            }
            tempthree.add(currInt);
        }

    drawables[0]=temp;
    drawables[1]=tempTwo;
    drawables[2]=tempthree;

    return drawables;
}


    /**
     * Sets data groups.
     *
     * @return the data groups
     */
    public ArrayList<ExpListGroup> setDataGroups() {
        ArrayList<ExpListGroup> groups = new ArrayList<ExpListGroup>();
        ArrayList<ExpListChild> children = new ArrayList<ExpListChild>();

       // Code adapted from code in:
        ////http://www.dreamincode.net/forums/topic/270612-how-to-get-started-with-expandablelistview/




        //For each object to be added to list below
        //Create overall group holder object
        //Name and add each child object to group
        //Add group to list of groups
        //Next

        ExpListGroup group1 = new ExpListGroup();
        group1.setName("Fruit & Vegetables");

        ExpListChild child = new ExpListChild();
        child.setName("Apples");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Oranges");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Tomatoes");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Banana");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Broccoli");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Sweetcorn");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Peas");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Mushrooms");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Cucumber");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Carrots");
        child.setTag(null);
        children.add(child);

        group1.setItems(children);
        groups.add(group1);


        children = new ArrayList<ExpListChild>();
        ExpListGroup group2 = new ExpListGroup();
        group2.setName("Healthy Drinks");

        child = new ExpListChild();
        child.setName("Water");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("'No Added Sugar' Diluting Juice");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Tea with no sugar");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Coffee with no sugar");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("1 glass of fruit juice");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("DIET or ZERO fizzy drinks");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Low fat Milk");
        child.setTag(null);
        children.add(child);

        group2.setItems(children);
        groups.add(group2);


        children = new ArrayList<ExpListChild>();
        ExpListGroup group3 = new ExpListGroup();
        group3.setName("Eat less often, in small amounts");

        child = new ExpListChild();
        child.setName("Chocolate");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Crisps");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Full sugar Fizzy Drinks (Cola, Energy Drinks)");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Biscuits");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Ice-Cream");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Ketchup");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Sweets");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Take-aways");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Chips");
        child.setTag(null);
        children.add(child);

        child = new ExpListChild();
        child.setName("Cakes");
        child.setTag(null);
        children.add(child);

        group3.setItems(children);
        groups.add(group3);


        return groups;
    }


    /**
     * Show general info tab.
     *
     * @param v the v
     */
    public void showGenInfo(View v) {
        generalInfo.setVisibility(View.VISIBLE);
        explExamples.setVisibility(View.INVISIBLE);
    }


    /**
     * Show examples tab.
     *
     * @param v the v
     */
    public void showExamples(View v) {
        generalInfo.setVisibility(View.INVISIBLE);
        explExamples.setVisibility(View.VISIBLE);
    }


    /**
     * Back to main.
     *
     * @param v the v
     */
//OptionsActivity - Back button
    public void backToMain(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }


}
