package com.example.mmalo.prototype2;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Controllers.CameraController;
import com.example.mmalo.prototype2.Controllers.CameraPreview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public int calorieTarget = 2500;
    public int calorieTotal = 1900;

    String[] options;
    ArrayList<String> optionsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initValues();
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


    public void takePhoto(View v) {
        System.out.println(v.getId());
        System.out.println("It did the thing");
        CameraController cc = new CameraController();
        boolean hasCam = cc.checkCameraHardware(this);
        System.out.println(hasCam);
        Toast toast;
        toast = Toast.makeText(this, "Has Camera: " + hasCam, Toast.LENGTH_LONG);
        //toast.show();
        // Camera c = cc.getCameraInstance();

        Intent i = new Intent(this, PTakenActivity.class);
        startActivity(i);

        //Intent i = new Intent(this, CameraActivity.class);
        //startActivity(i);

    }


    public void initValues()
    {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.background);
        ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressbar1);
        float theThing = (float)((float)calorieTotal/calorieTarget)*100;
        System.out.println("THEthing " + theThing);
        mProgress.setProgress((int)theThing);   // Main Progress
        System.out.println(calorieTotal + " / " + calorieTarget);
        //mProgress.setSecondaryProgress(calorieTarget); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

        options  = new String[] {  "Food Guide", "Option 2", "Option 3"  };
        optionsList = new ArrayList<String>(Arrays.asList(options));

       // ListView lv = (ListView) findViewById(R.id.lvMenu);

       // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
       //         (this, android.R.layout.simple_list_item_1, optionsList);

        // DataBind ListView with items from ArrayAdapter
       // lv.setAdapter(arrayAdapter);



        //TextView textCurr = (TextView) findViewById(R.id.textViewCurrentCalories);
        //textCurr.setText(Integer.toString(calorieTotal));

       // TextView textTarg = (TextView) findViewById(R.id.textViewTargetCalories);
       // textTarg.setText(Integer.toString(calorieTarget));

    }


    public void viewSummary(View v){
        Intent i = new Intent(getBaseContext(), SummaryActivity.class);
        this.startActivity(i);


    }


}
