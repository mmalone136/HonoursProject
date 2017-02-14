package com.example.mmalo.prototype2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
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

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {


    TextView generalInfo;
    ExpandableListView explExamples;
    //int prevOpenGroup = 0;


    private ExpandableListAdapter explAdapter;
    private ArrayList<ExpListGroup> explItems;
    private ExpandableListView dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        generalInfo = (TextView) findViewById(R.id.tvGenInfo);
        explExamples = (ExpandableListView) findViewById(R.id.expListView);
        explExamples.setVisibility(View.INVISIBLE);

        dataList = (ExpandableListView) findViewById(R.id.expListView);
        explItems = setDataGroups();
        explAdapter = new ExplAdapter(GuideActivity.this, explItems);
        dataList.setAdapter(explAdapter);



        //http://stackoverflow.com/questions/7862396/show-only-one-child-of-expandable-list-at-a-time?rq=1
        dataList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int prevOpenGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != prevOpenGroup)
                    dataList.collapseGroup(prevOpenGroup);
                prevOpenGroup = groupPosition;
            }
        });
    }





    public ArrayList<ExpListGroup> setDataGroups(){
        ArrayList<ExpListGroup> groups = new ArrayList<ExpListGroup>();
        ArrayList<ExpListChild> children = new ArrayList<ExpListChild>();

        ExpListGroup g1 = new ExpListGroup();
        g1.setName("Fruit & Vegetables");

        ExpListChild ch1 = new ExpListChild();
        ch1.setName("Apples");
        ch1.setTag(null);
        children.add(ch1);

        ExpListChild ch2 = new ExpListChild();
        ch2.setName("Oranges");
        ch2.setTag(null);
        children.add(ch2);

        ExpListChild ch3 = new ExpListChild();
        ch3.setName("Tomatoes");
        ch3.setTag(null);
        children.add(ch3);

        ExpListChild ch4 = new ExpListChild();
        ch4.setName("Cabbage");
        ch4.setTag(null);
        children.add(ch4);

        ExpListChild ch5 = new ExpListChild();
        ch5.setName("Broccoli");
        ch5.setTag(null);
        children.add(ch5);

        g1.setItems(children);
        groups.add(g1);



        children = new ArrayList<ExpListChild>();
        ExpListGroup g2 = new ExpListGroup();
        g2.setName("Drinks");

        ch1 = new ExpListChild();
        ch1.setName("Water");
        ch1.setTag(null);
        children.add(ch1);

        ch2 = new ExpListChild();
        ch2.setName("Low Sugar Diluting Juice");
        ch2.setTag(null);
        children.add(ch2);

        ch3 = new ExpListChild();
        ch3.setName("Tea");
        ch3.setTag(null);
        children.add(ch3);

        g2.setItems(children);
        groups.add(g2);



        children = new ArrayList<ExpListChild>();
        ExpListGroup g3 = new ExpListGroup();
        g3.setName("Things to avoid");

        ch1 = new ExpListChild();
        ch1.setName("Chocolate");
        ch1.setTag(null);
        children.add(ch1);

        ch2 = new ExpListChild();
        ch2.setName("Crisps");
        ch2.setTag(null);
        children.add(ch2);

        ch3 = new ExpListChild();
        ch3.setName("Fizzy Drinks");
        ch3.setTag(null);
        children.add(ch3);

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



    public void showGenInfo(View v){
        generalInfo.setVisibility(View.VISIBLE);
        explExamples.setVisibility(View.INVISIBLE);


    }


    public void showExamples(View v){
        generalInfo.setVisibility(View.INVISIBLE);
        explExamples.setVisibility(View.VISIBLE);

    }


    //OptionsActivity - Back button
    public void backToMain(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }




}
