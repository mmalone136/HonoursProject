package com.example.mmalo.prototype2.ExpListClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.mmalo.prototype2.R;
        //tutorial.expandListView.R;
//import com.tutorial.expandListView.Classes.*;

//http://www.dreamincode.net/forums/topic/270612-how-to-get-started-with-expandablelistview/


import java.util.ArrayList;

/**
 * Created by mmalo on 14/02/2017.
 */

public class ExplAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ExpListGroup> groups;
    public ExplAdapter( Context cont, ArrayList<ExpListGroup> g){
        this.context = cont;
        this.groups = g;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        ExpListChild child = (ExpListChild) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.explist_child_item, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.tvChild);
        tv.setText(child.getName().toString());
        tv.setTag(child.getTag());
        // TODO Auto-generated method stub
        return view;
    }


    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        ExpListGroup group = (ExpListGroup) getGroup(groupPosition);
        try {
            if (view == null) {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.explist_group_item, null);
            }

            TextView tv = (TextView) view.findViewById(R.id.tvGroup);
            tv.setText(group.getName());
            // TODO Auto-generated method stub
            return view;
        }catch (Exception e){

            e.printStackTrace();
            return null;
        }
    }



    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        ArrayList<ExpListChild> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groups.size();
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        ArrayList<ExpListChild> chList = groups.get(groupPosition).getItems();

        return chList.size();

    }

    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

}
