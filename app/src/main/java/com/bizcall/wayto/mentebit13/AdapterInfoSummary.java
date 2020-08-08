package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterInfoSummary extends RecyclerView.Adapter<AdapterInfoSummary.Holder> {
    private ArrayList<DataSummary> mSummaryArrayList;
    private Context mContext;
    DataSummary detailsSummary;
    String strName;
    SharedPreferences sp;
    public AdapterInfoSummary(ArrayList<DataSummary> mSummaryArrayList, Context mContext) {
        this.mSummaryArrayList = mSummaryArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterInfoSummary.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_summaryinfo, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterInfoSummary.Holder holder, int i) {
        detailsSummary = mSummaryArrayList.get(i);
        sp=mContext.getSharedPreferences("Settings",Context.MODE_PRIVATE);

        holder.txtTitle.setText(detailsSummary.getStrDocumentNM());

        if (detailsSummary.getStrDocumentCount().contains("0")){
            holder.imgTick.setImageResource(R.drawable.wrong_tick);
        }else {
            holder.imgTick.setImageResource(R.drawable.right_tick);
        }

        strName = holder.txtTitle.getText().toString();
        if (strName.contains("Personal")){
            holder.imgTick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,PersonalInfoActivity.class);
                    intent.putExtra("FileNo",sp.getString("FileNo",null));
                    intent.putExtra("MobileNo",sp.getString("MobileNo",null));
                    mContext.startActivity(intent);
                }
            });
        } else if (strName.contains("Education")){
            holder.imgTick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,EducationalDetails.class);
                    intent.putExtra("FileNo",sp.getString("FileNo",null));
                    intent.putExtra("MobileNo",sp.getString("MobileNo",null));
                    mContext.startActivity(intent);
                }
            });
        } else if (strName.contains("Document")){
            holder.imgTick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,ActivityUploadDocs.class);
                    intent.putExtra("FileNo",sp.getString("FileNo",null));
                    intent.putExtra("MobileNo",sp.getString("MobileNo",null));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSummaryArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView imgTick;
        public Holder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_summ_title);
            imgTick = itemView.findViewById(R.id.img_tickmark);
        }
    }
}

