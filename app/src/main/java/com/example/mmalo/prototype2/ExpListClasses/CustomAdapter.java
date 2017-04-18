package com.example.mmalo.prototype2.ExpListClasses;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmalo.prototype2.Models.DataHolder;
import com.example.mmalo.prototype2.R;

import java.util.ArrayList;


/**
 * Created by mmalo on 06/03/2017.
 *
 *
 * Class Adapted from tutorial:
 * //http://stackoverflow.com/questions/15832335/android-custom-row-item-for-listview
 *
 */
public class CustomAdapter extends BaseAdapter {

    /**
     * The Context.
     */
    Context context;
    /**
     * The Data.
     */
    ArrayList<String[]> data;
    /**
     * The More data.
     */
    String[] moreData;
    /**
     * The Today position.
     */
    int todayPosition;
    /**
     * The Selector.
     */
    int selector;
    /**
     * The Star flags.
     */
    boolean[] starFlags;
    /**
     * The T.
     */
    int t;
    /**
     * The Seven.
     */
    TextView seven;
    private static LayoutInflater inflater = null;

    /**
     * Instantiates a new Custom adapter.
     *
     * @param context the context
     * @param data    the data
     * @param sel     the sel
     */
    public CustomAdapter(Context context, ArrayList<String[]> data, int sel) {
        this.context = context;
        this.data = data;
        this.selector = sel;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * Instantiates a new Custom adapter.
     *
     * @param context  the context
     * @param data     the data
     * @param sel      the sel
     * @param forImage the for image
     * @param today    the today
     * @param text     the text
     */
    public CustomAdapter(Context context, ArrayList<String[]> data, int sel, boolean[] forImage, int today,TextView text) {
        this.context = context;
        this.data = data;
        this.selector = sel;
        todayPosition = today;
        this.starFlags = forImage;
        seven = text;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    /**
     * Instantiates a new Custom adapter.
     *
     * @param context the context
     * @param theData the the data
     * @param sel     the sel
     */
    public CustomAdapter(Context context, String[] theData, int sel) {
        this.context = context;
        this.moreData = theData;
        this.selector = sel;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (data == null) {
            return moreData.length;
        } else {
            return data.size();
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (selector == 1) {
            String[] stringList = data.get(position);
            boolean current = starFlags[position];
            t=position;

            convertView = LayoutInflater.from(context).inflate(R.layout.imagebutton_layout, null);

            TextView day = (TextView) convertView.findViewById(R.id.tvChild);
            TextView date = (TextView) convertView.findViewById(R.id.tvChild1);
            //TextView other = (TextView) convertView.findViewById(R.id.tvChild2);
            //ImageButton star = (ImageButton) convertView.findViewById(R.id.ivStar);
            ImageView star = (ImageView) convertView.findViewById(R.id.ivStar);

            day.setText(stringList[0]);
            date.setText(stringList[1]);
            //other.setText(stringList[2]);


            if (position == todayPosition) {
                //convertView.setBackgroundResource(R.color.explvGroupBackGood);
                LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.thing);
                ll.setBackgroundResource(R.color.explvGroupBackGood);
                //other.setText("TODAY");
                String str = stringList[1] + "\nTODAY";
                date.setText(str);

            }
            else if(stringList[2].equals("NO ENTRIES")){
                //LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.thing);
                //ll.setBackgroundResource(R.color.colorPrimaryDark);

            }else{
                LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.thing);
                ll.setBackgroundResource(R.color.aBlue);
            }

             if (current) {
                star.setImageResource(R.drawable.goldstar);
            } else {
                star.setImageResource(R.drawable.greystar);
            }



            }else if (selector == 2) {

                String[] stringList = data.get(position);
                convertView = LayoutInflater.from(context).inflate(R.layout.dateview_list_layout, null);

                int seven;
                switch (stringList[0]) {
                    case "Breakfast":
                        seven = R.drawable.breakfasticon;
                        break;
                    case "Lunch":
                        seven = R.drawable.lunchicon;
                        break;
                    case "Dinner":
                        seven = R.drawable.dinnicon;
                        break;
                    case "Drink":
                        seven = R.drawable.glassfull;
                        break;
                    case "Snack":
                        seven = R.drawable.snackicon;
                        break;
                    default:
                        seven = R.drawable.glassempty1;
                        break;
                }


                ImageView ivMeal = (ImageView) convertView.findViewById(R.id.ivMeal);
                TextView day = (TextView) convertView.findViewById(R.id.tvChild);
                TextView date = (TextView) convertView.findViewById(R.id.tvChild1);


                day.setText(stringList[0]);
                date.setText(stringList[1]);
                ivMeal.setImageResource(seven);
            } else if (selector == 3) {
            String str = moreData[position];

            //convertView = LayoutInflater.from(context).inflate(R.layout.summary_list_layout, null);

            int symb;
            int symb2 = R.drawable.glassfull;

            if (position == 0) {
                switch (str) {
                    case "Breakfast":
                        symb = R.drawable.breakfasticon;
                        break;
                    case "Lunch":
                        symb = R.drawable.lunchicon;
                        break;
                    case "Dinner":
                        symb = R.drawable.dinnicon;
                        break;
                    case "Drink":
                        symb = R.drawable.glassfull;
                        break;
                    case "Snack":
                        symb = R.drawable.snackicon;
                        break;
                    default:
                        symb = R.drawable.glassempty1;
                        break;
                }
            } else if (position == 1) {
                symb = R.drawable.fruitbowl;
                symb2 = R.drawable.glassfull;
            } else {
                symb = R.drawable.comments;
            }


            if (position == 1) {

                convertView = LayoutInflater.from(context).inflate(R.layout.summary_list2_layout, null);


                String [] temp = str.split("#");


                ImageView symbol = (ImageView) convertView.findViewById(R.id.ivSymbol);
                symbol.setImageResource(symb);
                ImageView symbol2 = (ImageView) convertView.findViewById(R.id.ivSymbol2);
                symbol2.setImageResource(symb2);
                TextView day = (TextView) convertView.findViewById(R.id.tvChild);
                day.setText(temp[0]);
                TextView day2 = (TextView) convertView.findViewById(R.id.tvChild2);
                day2.setText(temp[1]);

            } else {

                convertView = LayoutInflater.from(context).inflate(R.layout.summary_list_layout, null);


                ImageView symbol = (ImageView) convertView.findViewById(R.id.ivSymbol);
                TextView day = (TextView) convertView.findViewById(R.id.tvChild);
                day.setText(str);
                symbol.setImageResource(symb);
            }
        }
            return convertView;
        }
    }
