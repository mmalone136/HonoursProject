package com.example.mmalo.prototype2.ExpListClasses;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    int selector;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, ArrayList<String[]> data, int sel) {
        this.context = context;
        this.data = data;
        this.selector = sel;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (selector == 1) {
            String[] stringList = data.get(position);

            convertView = LayoutInflater.from(context).inflate(R.layout.imagebutton_layout, null);

            TextView day = (TextView) convertView.findViewById(R.id.tvChild);
            TextView date = (TextView) convertView.findViewById(R.id.tvChild1);
            TextView other = (TextView) convertView.findViewById(R.id.tvChild2);

            day.setText(stringList[0]);
            date.setText(stringList[1]);
            other.setText(stringList[2]);
        } else if (selector == 2) {

            String[] stringList = data.get(position);
            convertView = LayoutInflater.from(context).inflate(R.layout.dateview_list_layout, null);

            int seven;
            switch (stringList[0]) {
                case "Breakfast":
                    seven = R.drawable.breakfast;
                    break;
                case "Lunch":
                    seven = R.drawable.lunch;
                    break;
                case "Dinner":
                    seven = R.drawable.dinn;
                    break;
                case "Drink":
                    seven = R.drawable.glassfull;
                    break;
                case "Snack":
                    seven = R.drawable.teamug;
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

            TextView day = (TextView) convertView.findViewById(R.id.tvChild);
            day.setText(str);
        }

        return convertView;
    }
}
