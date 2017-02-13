package com.example.mmalo.prototype2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class GuideActivity extends AppCompatActivity {


    TextView generalInfo;
    ExpandableListView explExamples;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        generalInfo = (TextView) findViewById(R.id.tvGenInfo);
        explExamples = (ExpandableListView) findViewById(R.id.expListView);
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
