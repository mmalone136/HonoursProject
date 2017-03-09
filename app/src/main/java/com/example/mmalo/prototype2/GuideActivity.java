package com.example.mmalo.prototype2;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.mmalo.prototype2.ExpListClasses.ExpListChild;
import com.example.mmalo.prototype2.ExpListClasses.ExpListGroup;
import com.example.mmalo.prototype2.ExpListClasses.ExplAdapter;
import com.example.mmalo.prototype2.Models.DataHolder;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {


    TextView generalInfo;
    ExpandableListView explExamples;
    //int prevOpenGroup = 0;


    private ExpandableListAdapter explAdapter;
    private ArrayList<ExpListGroup> explItems;
    private ExpandableListView dataList;
    ArrayList<Integer>[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        generalInfo = (TextView) findViewById(R.id.tvGenInfo);
        generalInfo.setMovementMethod(new ScrollingMovementMethod());
        DataHolder.readData(this);

        explExamples = (ExpandableListView) findViewById(R.id.expListView);
        explExamples.setVisibility(View.INVISIBLE);

        dataList = (ExpandableListView) findViewById(R.id.expListView);
        explItems = setDataGroups();
        images = setDrawables();
        explAdapter = new ExplAdapter(GuideActivity.this, explItems, images);
        dataList.setAdapter(explAdapter);


        //http://stackoverflow.com/questions/7862396/show-only-one-child-of-expandable-list-at-a-time?rq=1
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


    public ArrayList<Integer>[] setDrawables() {

        ArrayList<Integer>[] drawables = new ArrayList[3];

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


    drawables[0]=temp;
    drawables[1]=tempTwo;
    drawables[2]=temp;

    return drawables;
}


    public ArrayList<ExpListGroup> setDataGroups() {
        ArrayList<ExpListGroup> groups = new ArrayList<ExpListGroup>();
        ArrayList<ExpListChild> children = new ArrayList<ExpListChild>();

        ExpListGroup g1 = new ExpListGroup();
        g1.setName("Fruit & Vegetables");

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

        g1.setItems(children);
        groups.add(g1);


        children = new ArrayList<ExpListChild>();
        ExpListGroup g2 = new ExpListGroup();
        g2.setName("Drinks");

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

        g2.setItems(children);
        groups.add(g2);


        children = new ArrayList<ExpListChild>();
        ExpListGroup g3 = new ExpListGroup();
        g3.setName("Eat less often, in small amounts");

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

        g3.setItems(children);
        groups.add(g3);


        return groups;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
//            case R.id.menuGuide:
//                intent = new Intent(this, GuideActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.menuMainLayout:
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showGenInfo(View v) {
        generalInfo.setVisibility(View.VISIBLE);
        explExamples.setVisibility(View.INVISIBLE);


    }


    public void showExamples(View v) {
        generalInfo.setVisibility(View.INVISIBLE);
        explExamples.setVisibility(View.VISIBLE);

    }


    //OptionsActivity - Back button
    public void backToMain(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }


}
