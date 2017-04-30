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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Models.DataHolder;
import com.example.mmalo.prototype2.Models.TutorialData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mmalo on 16/02/2017.
 */
public class TutorialActivity extends AppCompatActivity {

    /**
     * The Tutorial data list.
     */
    ArrayList<TutorialData> tutorialDataList;
    /**
     * The Picture view.
     */
    ImageView pictureView;
    /**
     * The Pic result.
     */
    ImageView picResult;
    /**
     * The Curr has fv.
     */
    boolean currHasFV;
    /**
     * The Curr has dr.
     */
    boolean currHasDR;
    /**
     * The User fv.
     */
    boolean userFV;
    /**
     * The User dr.
     */
    boolean userDR;
    private Random randGen;
    /**
     * The Button fv.
     */
    ImageButton buttonFV;
    /**
     * The Button dr.
     */
    ImageButton buttonDR;
    /**
     * The Left.
     */
    ImageButton left;
    /**
     * The Item text.
     */
    TextView itemText;
    /**
     * The Correct fv count.
     */
    int correctFVCount;
    /**
     * The Correct dr count.
     */
    int correctDRCount;
    /**
     * The Final sum.
     */
    ImageView finalSum;
    /**
     * The Next tut option.
     */
    Button nextTutOption;
    /**
     * The Submit option.
     */
    ImageButton submitOption;
    /**
     * The Instructions.
     */
    int[] instructions;
    /**
     * The Curr instr.
     */
    int currInstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create activity and initialise many variables and references
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        createList();
        randGen = new Random();
        userFV = false;
        userDR = false;
        buttonFV = (ImageButton) findViewById(R.id.buttonFV);
        buttonDR = (ImageButton) findViewById(R.id.buttonDR);
        submitOption = (ImageButton) findViewById(R.id.buttonSubmit2);
        DataHolder.readData(this);
        nextTutOption = (Button) findViewById(R.id.buttonNextImage);
        finalSum = (ImageView) findViewById(R.id.txtViewFinalSummary);
        correctFVCount = 0;
        correctDRCount = 0;
        currInstr = 0;
        picResult = (ImageView) findViewById(R.id.imageViewResult);
        itemText = (TextView) findViewById(R.id.currItemText);

        //Set up array of images to show - instruction images
        int[] temp = {R.drawable.tutorialintro, R.drawable.tutorial2, R.drawable.tutorial2_5,R.drawable.tutorial3,
                R.drawable.tutorial3_5,R.drawable.tutorial4, R.drawable.tutorial5};
        instructions = temp;

        picResult.setImageResource(instructions[currInstr]);
        picResult.setVisibility(View.VISIBLE);
        left = (ImageButton) findViewById(R.id.buttonPrevInst);
        left.setVisibility(View.INVISIBLE);


    }


    /**
     * Create list of images to be shown.
     */
    public void createList() {
        //Create list and holder variable
        tutorialDataList = new ArrayList<>();
        TutorialData tutData;

        //Define next id, reinitialise Tutorial Data Object, add to list
        String id = "@drawable/burger";
        tutData = new TutorialData(id, false, false, "Burger");
        tutorialDataList.add(tutData);

        //Define next id, reinitialise Tutorial Data Object, add to list
        id = "@drawable/fruit";
        tutData = new TutorialData(id, true, false, "Some Fruit");
        tutorialDataList.add(tutData);

        //Define next id, reinitialise Tutorial Data Object, add to list
        id = "@drawable/icecream2";
        tutData = new TutorialData(id, false, false, "Ice-Cream");
        tutorialDataList.add(tutData);

        //Define next id, reinitialise Tutorial Data Object, add to list
        id = "@drawable/pastry";
        tutData = new TutorialData(id, false, false, "Cream-Pastries");
        tutorialDataList.add(tutData);

        //Define next id, reinitialise Tutorial Data Object, add to list
        id = "@drawable/pizza";
        tutData = new TutorialData(id, false, false,"Pizza");
        tutorialDataList.add(tutData);

        //Define next id, reinitialise Tutorial Data Object, add to list
        id = "@drawable/salad2";
        tutData = new TutorialData(id, true, false, "Salad");
        tutorialDataList.add(tutData);

        //Define next id, reinitialise Tutorial Data Object, add to list
        id = "@drawable/strawbs";
        tutData = new TutorialData(id, true, false, "Strawberries");
        tutorialDataList.add(tutData);

        //Define next id, reinitialise Tutorial Data Object, add to list
        id = "@drawable/tea";
        tutData = new TutorialData(id, false, true,"Sugar-Free Tea");
        tutorialDataList.add(tutData);

        //Define next id, reinitialise Tutorial Data Object, add to list
        id = "@drawable/water";
        tutData = new TutorialData(id, false, true, "Water");
        tutorialDataList.add(tutData);
    }

    /**
     * Sets current photo.
     *
     * @param tutData the tut data
     */
    public void setCurrentPhoto(TutorialData tutData) {
        try {
            //Get filepath for next image - read answer values too
            String identifier = tutData.getFilePath();
            currHasFV = tutData.getHasFV();
            currHasDR = tutData.getHasDR();
            pictureView = (ImageView) findViewById(R.id.imageViewTut);
            itemText.setText(tutData.getPicName());

            //Code adapted from:
            //http://stackoverflow.com/questions/11737607/how-to-set-the-image-from-drawable-dynamically-in-android?noredirect=1&lq=1
            int id = getResources().getIdentifier(identifier, null, getPackageName());
            Drawable curr = getResources().getDrawable(id);
            pictureView.setImageDrawable(curr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets next photo - random value.
     *
     * @return the next photo
     */
    public TutorialData getNextPhoto() {
        //http://stackoverflow.com/questions/5034370/retrieving-a-random-item-from-arraylist
        int nextIndex = randGen.nextInt(tutorialDataList.size());
        TutorialData nextData = tutorialDataList.get(nextIndex);
        tutorialDataList.remove(nextIndex);
        return nextData;
    }

    /**
     * Add to drinks.
     *
     * @param v the v
     */
    public void addToDrinks(View v) {
        //toggle drinks on/off
        //No longer changes colour - shows tick or cross
        userDR = !userDR;
        if (userDR) {
            changeButtonColours(buttonDR, "#50BF0B", 2);
        } else {
            changeButtonColours(buttonDR, "#DB2F09", 2);
        }

    }

    /**
     * Add to fv.
     *
     * @param v the v
     */
    public void addToFV(View v) {
        //toggle FV on/off
        //No longer changes colour - shows tick or cross
        userFV = !userFV;
        if (userFV) {
            changeButtonColours(buttonFV, "#50BF0B", 1);
        } else {
            changeButtonColours(buttonFV, "#DB2F09", 1);
        }
    }


    /**
     * Change button colours.
     *
     * @param curr   the curr
     * @param colour the colour
     * @param flag   the flag
     */
    public void changeButtonColours(ImageButton curr, String colour, int flag) {
        //Toggles image on button on or off

        if (colour.equals("#50BF0B")) {
            if (flag == 1) {
                curr.setImageResource(R.drawable.bowltick);
            } else {
                curr.setImageResource(R.drawable.glasstick);
            }
        } else {
            if (flag == 1) {
                curr.setImageResource(R.drawable.bowlcross);
            } else {
                curr.setImageResource(R.drawable.glasscross);
            }
        }
    }

    /**
     * Go to next.
     *
     * @param v the v
     */
    public void goToNext(View v) {
        //compare user choices to actual value and update score
        boolean fvResult = (userFV == currHasFV);
        boolean drResult = (userDR == currHasDR);

        //If correct - increment score counts
        if (fvResult) {
            correctFVCount++;
        }
        if (drResult) {
            correctDRCount++;
        }

        //Reset button states
        changeButtonColours(buttonFV, "#b0b4b7", 1);
        changeButtonColours(buttonDR, "#b0b4b7", 2);
        userFV = false;
        userDR = false;

        String review = "";

        //Checks results and shows feedback image based on answers
        if (fvResult && drResult) {
            review = "Correct for both";

            pictureView.setVisibility(View.INVISIBLE);
            picResult.setImageResource(R.drawable.bothcorrect);
            picResult.setVisibility(View.VISIBLE);
            itemText.setVisibility(View.INVISIBLE);
            nextTutOption.setVisibility(View.VISIBLE);
            buttonFV.setVisibility(View.INVISIBLE);
            buttonDR.setVisibility(View.INVISIBLE);
            submitOption.setVisibility(View.INVISIBLE);

        } else if ((fvResult) && (!drResult)) {
            review = "Correct For fruit & veg, incorrect for drinks";

            pictureView.setVisibility(View.INVISIBLE);
            picResult.setImageResource(R.drawable.almostfood);
            picResult.setVisibility(View.VISIBLE);
            itemText.setVisibility(View.INVISIBLE);
            nextTutOption.setVisibility(View.VISIBLE);
            buttonFV.setVisibility(View.INVISIBLE);
            buttonDR.setVisibility(View.INVISIBLE);
            submitOption.setVisibility(View.INVISIBLE);

        } else if ((!fvResult) && (drResult)) {
            review = "Correct for drinks, incorrect for fruit & veg";

            pictureView.setVisibility(View.INVISIBLE);
            picResult.setImageResource(R.drawable.almostdrink);
            picResult.setVisibility(View.VISIBLE);
            itemText.setVisibility(View.INVISIBLE);
            nextTutOption.setVisibility(View.VISIBLE);
            buttonFV.setVisibility(View.INVISIBLE);
            buttonDR.setVisibility(View.INVISIBLE);
            submitOption.setVisibility(View.INVISIBLE);
        } else {
            review = "Incorrect for both";

            pictureView.setVisibility(View.INVISIBLE);
            picResult.setImageResource(R.drawable.bothincorrect);
            picResult.setVisibility(View.VISIBLE);
            itemText.setVisibility(View.INVISIBLE);
            nextTutOption.setVisibility(View.VISIBLE);
            buttonFV.setVisibility(View.INVISIBLE);
            buttonDR.setVisibility(View.INVISIBLE);
            submitOption.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Replay tutorial
     *
     * @param v the v
     */
    public void replay(View v) {
        this.recreate();
    }

    /**
     * Back option.
     *
     * @param v the v
     */
    public void backOption(View v) {
        Intent i = new Intent(getBaseContext(), OptionsActivity.class);
        this.startActivity(i);
    }

    /**
     * Goes to next image - submitting current answers.
     *
     * @param v the v
     */
    public void nextOption(View v) {
        //Show and hide necessary views
        finalSum.setVisibility(View.INVISIBLE);
        nextTutOption.setVisibility(View.INVISIBLE);
        picResult.setVisibility(View.INVISIBLE);
        buttonFV.setVisibility(View.VISIBLE);
        buttonDR.setVisibility(View.VISIBLE);
        submitOption.setVisibility(View.VISIBLE);
        pictureView.setVisibility(View.VISIBLE);
        itemText.setVisibility(View.VISIBLE);

        //Check if last image has been done
        if (tutorialDataList.size() > 0) {
            //Still photos left - keep going with next image
            setCurrentPhoto(getNextPhoto());
        } else {
            //Run out of images - tutorial complete

            LinearLayout tutButtons = (LinearLayout) findViewById(R.id.ButtonLayout);
            tutButtons.setVisibility(View.INVISIBLE);
            ImageButton replay = (ImageButton) findViewById(R.id.buttonReplayTut);
            replay.setVisibility(View.VISIBLE);
            nextTutOption.setVisibility(View.INVISIBLE);
            finalSum.setVisibility(View.VISIBLE);

            //Calculate final total
            int total = correctFVCount + correctDRCount;

            //Show feedback based on total
            if (total==18)
            {
                finalSum.setImageResource(R.drawable.finalallcorrect);
            }else if(total>9&& total<18){
                finalSum.setImageResource(R.drawable.finalalmost);
            }
            else
            {
                finalSum.setImageResource(R.drawable.finalgettingthere);
            }

            pictureView.setVisibility(View.INVISIBLE);
            picResult.setVisibility(View.INVISIBLE);
            itemText.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Next instruct.
     *
     * @param v the v
     */
    public void nextInstruct(View v) {
        //Increment index
        currInstr++;
        //Check if at last instruction (Checks index wont go out of bounds)
        if (currInstr == instructions.length) {
            currInstr--;
        } else {
            //Set appropriate image for index
            picResult.setImageResource(instructions[currInstr]);
            picResult.setVisibility(View.VISIBLE);

            //If at end - show begin button
            if (currInstr == (instructions.length - 1)) {
                LinearLayout begin = (LinearLayout) findViewById(R.id.beginLay);
                begin.setVisibility(View.VISIBLE);
                begin.bringToFront();
            }
        }
        if(currInstr>0){
            left.setVisibility(View.VISIBLE);
        }


    }

    /**
     * Go to Previous instruction.
     *
     * @param v the v
     */
    public void prevInstruct(View v) {
        if (currInstr > 0) {
            //This may not be correct
            if (currInstr == (instructions.length - 1)) {
                LinearLayout begin = (LinearLayout) findViewById(R.id.beginLay);
                begin.setVisibility(View.INVISIBLE);
            }
            currInstr--;
            picResult.setImageResource(instructions[currInstr]);
            picResult.setVisibility(View.VISIBLE);
        }
        if(currInstr==0){
            left.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Begin tutorial - shows layouts relating to tutorial playing
     * Hides layouts associated with instructions section of tutorial.
     *
     * @param v the v
     */
    public void beginTutorial(View v) {
        picResult.setVisibility(View.INVISIBLE);

        setCurrentPhoto(getNextPhoto());

        LinearLayout ll2 = (LinearLayout) findViewById(R.id.place);
        ll2.setVisibility(View.INVISIBLE);

        LinearLayout begin = (LinearLayout) findViewById(R.id.beginLay);
        begin.setVisibility(View.INVISIBLE);


        LinearLayout ll = (LinearLayout) findViewById(R.id.ButtonLayout);
        ll.setVisibility(View.VISIBLE);

        pictureView.setVisibility(View.VISIBLE);
        itemText.setVisibility(View.VISIBLE);
    }


}
