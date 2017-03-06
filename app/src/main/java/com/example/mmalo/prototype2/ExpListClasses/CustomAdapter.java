package com.example.mmalo.prototype2.ExpListClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mmalo.prototype2.R;

import java.util.ArrayList;

/**
 * Created by mmalo on 06/03/2017.
 */

public class CustomAdapter extends BaseAdapter {
        //http://stackoverflow.com/questions/15832335/android-custom-row-item-for-listview

        Context context;
    ArrayList<String[]>data;
        private static LayoutInflater inflater = null;

        public CustomAdapter(Context context, ArrayList<String[]> data) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.data = data;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
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

            String [] stringList = data.get(position);

            convertView =LayoutInflater.from(context).inflate(R.layout.imagebutton_layout,null);


            TextView day = (TextView) convertView.findViewById(R.id.tvChild);
            TextView date = (TextView) convertView.findViewById(R.id.tvChild1);
            TextView other = (TextView) convertView.findViewById(R.id.tvChild2);

            day.setText(stringList[0]);
            date.setText(stringList[1]);
            other.setText(stringList[2]);


            return convertView;
        }
}
