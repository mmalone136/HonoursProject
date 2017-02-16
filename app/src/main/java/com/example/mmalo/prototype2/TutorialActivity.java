package com.example.mmalo.prototype2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Models.TutorialData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mmalo on 16/02/2017.
 */

public class TutorialActivity extends AppCompatActivity {

    ArrayList<TutorialData> tutorialDataList;
    ImageView pictureView;
    boolean currFV;
    boolean currDR;
    private Random randGen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        createList();
        randGen = new Random();

        setCurrentPhoto(getNextPhoto());

    }


    public void createList() {
        tutorialDataList = new ArrayList<TutorialData>();
        TutorialData tutData;

        String id = "@drawable/apple";
        boolean fv = true;
        boolean dr = false;
        tutData = new TutorialData(id, fv, dr);
        tutorialDataList.add(tutData);

        id = "@drawable/glass2";
        fv = false;
        dr = true;
        tutData = new TutorialData(id, fv, dr);
        tutorialDataList.add(tutData);

        id = "@drawable/glass2";
        fv = false;
        dr = true;
        tutData = new TutorialData(id, fv, dr);
        tutorialDataList.add(tutData);

        id = "@drawable/healthymeal";
        fv = true;
        dr = false;
        tutData = new TutorialData(id, fv, dr);
        tutorialDataList.add(tutData);

        id = "@drawable/spaghetti";
        fv = false;
        dr = false;
        tutData = new TutorialData(id, fv, dr);
        tutorialDataList.add(tutData);

        id = "@drawable/mealdrink";
        fv = true;
        dr = true;
        tutData = new TutorialData(id, fv, dr);
        tutorialDataList.add(tutData);


    }

    public void setCurrentPhoto(TutorialData tutData) {
        try {
            String identifier = tutData.getFilePath();
            boolean currFV = tutData.getHasFV();
            boolean currDR = tutData.getHasDR();
            pictureView = (ImageView) findViewById(R.id.imageViewTut);
            String test = "@drawable/apple";

            //http://stackoverflow.com/questions/11737607/how-to-set-the-image-from-drawable-dynamically-in-android?noredirect=1&lq=1
            int id = getResources().getIdentifier(identifier, null, getPackageName());
            Drawable curr = getResources().getDrawable(id);
            pictureView.setImageDrawable(curr);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public TutorialData getNextPhoto() {

        //http://stackoverflow.com/questions/5034370/retrieving-a-random-item-from-arraylist
        int nextIndex = randGen.nextInt(tutorialDataList.size());
        TutorialData nextData = tutorialDataList.get(nextIndex);
        tutorialDataList.remove(nextIndex);
        return nextData;
    }

    public void addToDrinks(View v) {
        //toggle drinks on/off

    }

    public void addToFV(View v) {
        //toggle FV on/off

    }

    public void goToNext(View v) {

        //compareuser choices to actual value and update score

        if (tutorialDataList.size() > 0) {
            setCurrentPhoto(getNextPhoto());
        }else
        {
            Toast t = Toast.makeText(this, "Images finished", Toast.LENGTH_LONG);
            t.show();
            Intent i = new Intent(getBaseContext(), OptionsActivity.class);
            this.startActivity(i);
        }
    }

    public void backOption(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }


}
