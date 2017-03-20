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
 */

public class CustomAdapter extends BaseAdapter {
    //http://stackoverflow.com/questions/15832335/android-custom-row-item-for-listview

    Context context;
    ArrayList<String[]> data;
    String[] moreData;
    int todayPosition;
    int selector;
    boolean[] starFlags;
    int t;
    TextView seven;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, ArrayList<String[]> data, int sel) {
        this.context = context;
        this.data = data;
        this.selector = sel;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


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
            TextView other = (TextView) convertView.findViewById(R.id.tvChild2);
            //ImageButton star = (ImageButton) convertView.findViewById(R.id.ivStar);
            ImageView star = (ImageView) convertView.findViewById(R.id.ivStar);

            day.setText(stringList[0]);
            date.setText(stringList[1]);
            other.setText(stringList[2]);


            if (position == todayPosition) {
                //convertView.setBackgroundResource(R.color.explvGroupBackGood);
                LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.thing);
                ll.setBackgroundResource(R.color.explvGroupBackGood);
                other.setText("TODAY");
            }

             if (current) {
                star.setImageResource(R.drawable.goldstar);
            } else {
                star.setImageResource(R.drawable.greystar);
            }


//            star.setOnTouchListener(new View.OnTouchListener()
//            {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if(todayPosition!=77) {
//                        //Toast to = Toast.makeText(context, "Position: " + position, Toast.LENGTH_SHORT);
//                        //to.show();
//
//                        if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                            seven.setVisibility(View.VISIBLE);
//
//                            String toDisplay = "Position: " + position +
//                                    "\nFV: " + position +
//                                    "\nDR: " + position +
//                                    "\nBREAKFAST: " + position +
//                                    "\nLUNCH: " + position +
//                                    "\ndinner: " +position;
//
//                            seven.setText(toDisplay);
//                            seven.bringToFront();
//                        } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
//                            seven.setVisibility(View.INVISIBLE);
//                        }
//
//
//                    }
//                    return false;
//                }});


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

                convertView = LayoutInflater.from(context).inflate(R.layout.summary_list_layout, null);

                int symb;

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
                    symb = R.drawable.comments;

                } else if (position == 2) {
                    symb = R.drawable.fruitbowl;
                } else {
                    symb = R.drawable.glassfull;
                }


                ImageView symbol = (ImageView) convertView.findViewById(R.id.ivSymbol);
                TextView day = (TextView) convertView.findViewById(R.id.tvChild);
                day.setText(str);
                symbol.setImageResource(symb);
            }

            return convertView;
        }
    }
