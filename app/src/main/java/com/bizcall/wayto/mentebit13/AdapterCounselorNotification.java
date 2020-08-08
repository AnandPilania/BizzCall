package com.bizcall.wayto.mentebit13;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterCounselorNotification extends ArrayAdapter<StateVO> {
   Context mContext;
     ArrayList<StateVO> listState;
    ArrayList<StateVO>selectedListState;

    AdapterCounselorNotification myAdapter;
    private boolean isFromView = false;

    public AdapterCounselorNotification(Context context, int resource, List<StateVO> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<StateVO>) objects;
        this.myAdapter = this;
    }

    public AdapterCounselorNotification(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.adapter_counselorlist, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.txtCounselorName);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkboxCounselor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       // holder.mTextView.setText(listState.get(0).getTitle());
        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        holder.mCheckBox.setTag(position);
        selectedListState=new ArrayList<>();
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                Log.d("GetPosition",getPosition+"");
                if(position==0)
                {
                    for ( int i=0; i < listState.size(); i++) {
                    if(listState.get(0).isSelected()) {
                        listState.get(i).setSelected(true);

                      //  holder.mCheckBox.setChecked(true);
                    }
                    else {
                        listState.get(i).setSelected(false);
                      /*  AdapterCounselorNotification adapterCounselorNotification=new AdapterCounselorNotification(mContext,0,listState);
                        adapterCounselorNotification.notifyDataSetChanged();*/
                      //  holder.mCheckBox.setChecked(false);
                    }

                }
                    myAdapter=new AdapterCounselorNotification(mContext,0,listState);
                    CounselorNotification.spinnerCounselorlist.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }

                if (!isFromView)
                {
                    listState.get(position).setSelected(isChecked);

                }
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        private TextView mTextView;
         CheckBox mCheckBox;
    }
}
