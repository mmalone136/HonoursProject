package com.example.mmalo.prototype2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    ImageView picResult;
    boolean currHasFV;
    boolean currHasDR;
    boolean userFV;
    boolean userDR;
    private Random randGen;
    Button buttonFV;
    Button buttonDR;
    int correctFVCount;
    int correctDRCount;
    TextView finalSum;
    Button nextTutOption;
    Button submitOption;
    int[] instructions;
    int currInstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        createList();
        randGen = new Random();
        userFV = false;
        userDR = false;
        buttonFV = (Button) findViewById(R.id.buttonFV);
        buttonDR = (Button) findViewById(R.id.buttonDR);
        submitOption = (Button) findViewById(R.id.buttonSubmit2);

        nextTutOption = (Button) findViewById(R.id.buttonNextImage);
        finalSum = (TextView) findViewById(R.id.txtViewFinalSummary);
        setCurrentPhoto(getNextPhoto());
        correctFVCount = 0;
        correctDRCount = 0;
        currInstr = 0;
        picResult = (ImageView) findViewById(R.id.imageViewResult);

        int[] temp = {R.drawable.tutorialintro, R.drawable.tutorial2, R.drawable.tutorial3,R.drawable.tutorial4};
        instructions = temp;

        picResult.setImageResource(instructions[currInstr]);
        picResult.setVisibility(View.VISIBLE);

        pictureView.setVisibility(View.INVISIBLE);

    }


    public void createList() {
        tutorialDataList = new ArrayList<TutorialData>();
        TutorialData tutData;

        String id = "@drawable/apple";
        tutData = new TutorialData(id, true, false);
        tutorialDataList.add(tutData);

        id = "@drawable/glass2";
        tutData = new TutorialData(id, false, true); //id, fv, dr
        tutorialDataList.add(tutData);

        id = "@drawable/fruitandveg";
        tutData = new TutorialData(id, true, false);
        tutorialDataList.add(tutData);

        id = "@drawable/healthymeal";
        tutData = new TutorialData(id, true, false);
        tutorialDataList.add(tutData);

        id = "@drawable/spaghetti";
        tutData = new TutorialData(id, false, false);
        tutorialDataList.add(tutData);

        id = "@drawable/mealdrink";
        tutData = new TutorialData(id, true, true);
        tutorialDataList.add(tutData);


    }

    public void setCurrentPhoto(TutorialData tutData) {
        try {
            String identifier = tutData.getFilePath();
            currHasFV = tutData.getHasFV();
            currHasDR = tutData.getHasDR();
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
        userDR = !userDR;
        if (userDR) {
            changeButtonColours(buttonDR, "#50BF0B");
        } else {
            changeButtonColours(buttonDR, "#DB2F09");
        }

    }

    public void addToFV(View v) {
        //toggle FV on/off
        userFV = !userFV;
        if (userFV) {
            changeButtonColours(buttonFV, "#50BF0B");
        } else {
            changeButtonColours(buttonFV, "#DB2F09");
        }
    }

    public void changeButtonColours(Button curr, String colour) {
        curr.setBackgroundColor(Color.parseColor(colour));
    }

    public void goToNext(View v) {
        //compare user choices to actual value and update score
        boolean fvResult = (userFV == currHasFV);
        boolean drResult = (userDR == currHasDR);

        if (fvResult) {
            correctFVCount++;
        }
        if (drResult) {
            correctDRCount++;
        }

        changeButtonColours(buttonFV, "#b0b4b7");
        changeButtonColours(buttonDR, "#b0b4b7");
        userFV = false;
        userDR = false;

        String review = "";

        if (fvResult && drResult) {
            review = "Correct for both";

            pictureView.setVisibility(View.INVISIBLE);
            picResult.setImageResource(R.drawable.bothcorrect);
            picResult.setVisibility(View.VISIBLE);

            // finalSum.setText("Correct!\n\nWell Done, you assigned both categories correctly!");
            //finalSum.setVisibility(View.VISIBLE);
            nextTutOption.setVisibility(View.VISIBLE);
            buttonFV.setVisibility(View.INVISIBLE);
            buttonDR.setVisibility(View.INVISIBLE);
            submitOption.setVisibility(View.INVISIBLE);

        } else if ((fvResult) && (!drResult)) {
            review = "Correct For fruit & veg, incorrect for drinks";

            pictureView.setVisibility(View.INVISIBLE);
            picResult.setImageResource(R.drawable.almostfood);
            picResult.setVisibility(View.VISIBLE);

            //finalSum.setText("Almost There!\n\nYou assigned to the Fruit and Vegetable category correctly but the drinks were wrong!");
            //finalSum.setVisibility(View.VISIBLE);
            nextTutOption.setVisibility(View.VISIBLE);
            buttonFV.setVisibility(View.INVISIBLE);
            buttonDR.setVisibility(View.INVISIBLE);
            submitOption.setVisibility(View.INVISIBLE);

        } else if ((!fvResult) && (drResult)) {
            review = "Correct for drinks, incorrect for fruit & veg";
            //things

            pictureView.setVisibility(View.INVISIBLE);
            picResult.setImageResource(R.drawable.almostdrink);
            picResult.setVisibility(View.VISIBLE);
            //finalSum.setText("Almost There!\n\nYou assigned to the Drinks category correctly but the Fruit and Vegetable category was wrong!");
            //finalSum.setVisibility(View.VISIBLE);
            nextTutOption.setVisibility(View.VISIBLE);
            buttonFV.setVisibility(View.INVISIBLE);
            buttonDR.setVisibility(View.INVISIBLE);
            submitOption.setVisibility(View.INVISIBLE);
        } else {
            //if(!(fvResult && drResult)){
            review = "Incorrect for both";

            pictureView.setVisibility(View.INVISIBLE);
            picResult.setImageResource(R.drawable.bothincorrect);
            picResult.setVisibility(View.VISIBLE);

            //finalSum.setText("Incorrect!\n\nTry again, you assigned both categories incorrectly!");
            //finalSum.setVisibility(View.VISIBLE);
            nextTutOption.setVisibility(View.VISIBLE);
            buttonFV.setVisibility(View.INVISIBLE);
            buttonDR.setVisibility(View.INVISIBLE);
            submitOption.setVisibility(View.INVISIBLE);
        }

        Toast t = Toast.makeText(this, review, Toast.LENGTH_LONG);
        //t.show();

        if (tutorialDataList.size() > 0) {
            setCurrentPhoto(getNextPhoto());
        } else {
            LinearLayout tutButtons = (LinearLayout) findViewById(R.id.ButtonLayout);
            tutButtons.setVisibility(View.INVISIBLE);

            Button replay = (Button) findViewById(R.id.buttonReplayTut);
            replay.setVisibility(View.VISIBLE);
            nextTutOption.setVisibility(View.INVISIBLE);
            finalSum.setText("Tutorial Complete!\n\nFruit & Veg Correct: " + correctFVCount + "\n\nDrinks Correct: " + correctDRCount);
            finalSum.setVisibility(View.VISIBLE);
        }
    }

    public void replay(View v) {
        this.recreate();
    }

    public void backOption(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }

    public void nextOption(View v) {
        finalSum.setVisibility(View.INVISIBLE);
        nextTutOption.setVisibility(View.INVISIBLE);
        picResult.setVisibility(View.INVISIBLE);
        buttonFV.setVisibility(View.VISIBLE);
        buttonDR.setVisibility(View.VISIBLE);
        submitOption.setVisibility(View.VISIBLE);
        pictureView.setVisibility(View.VISIBLE);
    }


    public void nextInstruct(View v) {
        currInstr++;
        if(currInstr==instructions.length) {
            //picResult.setImageResource(instructions[currInstr]);
            picResult.setVisibility(View.INVISIBLE);
        }
        else
        {
            picResult.setImageResource(instructions[currInstr]);
            picResult.setVisibility(View.VISIBLE);

            if(currInstr==(instructions.length-1))
            {
                Button begin = (Button) findViewById(R.id.beginTut);
                begin.setVisibility(View.VISIBLE);
            }
        }
    }

    public void prevInstruct(View v) {
        if (currInstr > 0) {
            //This may not be correct
            if(currInstr==(instructions.length-1))
            {
                Button begin = (Button) findViewById(R.id.beginTut);
                begin.setVisibility(View.INVISIBLE);
            }
            currInstr--;
            picResult.setImageResource(instructions[currInstr]);
            picResult.setVisibility(View.VISIBLE);


        }
    }

    public void beginTutorial(View v){
        picResult.setVisibility(View.INVISIBLE);
        Button left = (Button) findViewById(R.id.buttonPrevInst);
        Button right = (Button) findViewById(R.id.buttonNextInst);
        left.setVisibility(View.INVISIBLE);
        right.setVisibility(View.INVISIBLE);
        Button begin = (Button) findViewById(R.id.beginTut);
        begin.setVisibility(View.INVISIBLE);

        LinearLayout ll = (LinearLayout) findViewById(R.id.ButtonLayout);
        ll.setVisibility(View.VISIBLE);

        pictureView.setVisibility(View.VISIBLE);
    }


}
