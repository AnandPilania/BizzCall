package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> mListDataHeader; // header titles

    // child data in format of header title, child title
    private HashMap<String, List<String>> mListDataChild;
    ExpandableListView expandList;
    AlertDialog alertDialog;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData
                                 //        ,ExpandableListView mView
    )
    {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        //this.expandList = mView;
    }

    public ExpandableListAdapter (NavigationViewActivity navigationViewActivity, List<ExpandedMenuModel> listDataHeader, HashMap<ExpandedMenuModel,List<String>> listDataChild, ExpandableListView expandableList) {
    }

    @Override
    public int getGroupCount() {
        int i = mListDataHeader.size();
        //Log.d("GROUPCOUNT", String.valueOf(i));
        return i;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(
                this.mListDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //Log.d("CHILD", mListDataChild.get(this.mListDataHeader.get(groupPosition))
        //        .get(childPosition).toString());
        return this.mListDataChild.get(
                this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listheader, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.submenu);

        ImageView headerIcon = (ImageView) convertView.findViewById(R.id.iconimage);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        sp=mContext.getSharedPreferences("Settings",Context.MODE_PRIVATE);


        //lblListHeader.setText(headerTitle.getIconName());

       // headerIcon.setImageResource(Home.icon[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        int[] images = {
                R.drawable.dashboard1,
                R.drawable.dashboard1,
                R.drawable.ic_person_add_black_24dp,
                R.drawable.ic_file_upload_black_24dp ,
                R.drawable.remider1,
                R.drawable.ic_message_black_24dp,
                R.drawable.ic_call_24dp,
                R.drawable.ic_search_black_24dp,
                R.drawable.ic_menu_send,
               // R.drawable.report
        };

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_submenu, null);
        }

        ImageView imageView=(ImageView) convertView.findViewById(R.id.ImageViewIcon);

        TextView txtListChild = (TextView) convertView.findViewById(R.id.submenu);


        txtListChild.setText(childText);
        //imageView.setImageResource((Integer) getChild(0,1));

       // imageView.setImageResource(images[childPosition]);


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
