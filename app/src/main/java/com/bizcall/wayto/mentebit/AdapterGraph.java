package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterGraph  extends RecyclerView.Adapter<AdapterGraph.UserHolder> {


    ArrayList<DataGraph> arrayListGraph;
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String actname;

    public AdapterGraph(ArrayList<DataGraph> arrayListGraph, Context context) {
        this.arrayListGraph = arrayListGraph;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterGraph.UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
       View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_adapter_graph,viewGroup,false);
        UserHolder userHolder=new  UserHolder(view);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterGraph.UserHolder userHolder, int i) {

        DataGraph sampleData=arrayListGraph.get(i);
        String date1;
        //Log.d("CallDate",date1);
            sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
            editor=sp.edit();
            actname=sp.getString("ReportActivity",null);
            if(actname.equals("CallLog Report"))
            {
                userHolder.txtCallType.setVisibility(View.VISIBLE);
                userHolder.txtCallType.setText(sampleData.getCalltype());
            }

            userHolder.txtCallDate.setText(sampleData.getDate());
        date1=userHolder.txtCallDate.getText().toString();
        SpannableString content = new SpannableString(date1);
        content.setSpan(new UnderlineSpan(), 0, content.length(),0);
        userHolder.txtCallDate.setText(content);
       // userHolder.txtCallDate.setText(date1);

        userHolder.txtTotalCall.setText(Integer.toString(sampleData.getTime()));
        userHolder.txtCallDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                editor.putString("ReportDate",userHolder.txtCallDate.getText().toString());
                editor.commit();
                Intent intent=new Intent(context,ReportDetails.class);
                if(actname.equals("CallLog Report"))
                {
                    intent.putExtra("CallType",userHolder.txtCallType.getText().toString());
                }
                intent.putExtra("ReportDate",userHolder.txtCallDate.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
               }
        });
        }
        @Override
    public int getItemCount() {
        return arrayListGraph.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        TextView txtCallDate, txtTotalCall,txtCallType;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            txtCallDate = itemView.findViewById(R.id.txtCallDate);
            txtCallType=itemView.findViewById(R.id.txtCallType);

            txtTotalCall = itemView.findViewById(R.id.txtTotalCall);

        }
    }
}

