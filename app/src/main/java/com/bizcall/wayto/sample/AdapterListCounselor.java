package com.bizcall.wayto.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListCounselor extends RecyclerView.Adapter<AdapterListCounselor.MyHolder> {
    Context context;
    ArrayList<DataListCounselor> arrayList;
    SharedPreferences sp;
    String srno;
    SharedPreferences.Editor editor;

    public AdapterListCounselor(Context context, ArrayList<DataListCounselor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_listrecycler, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        DataListCounselor dataListCounselor = arrayList.get(i);
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        srno = sp.getString("SelectedSrNo", null);
        myHolder.txtCid.setText(dataListCounselor.getCounselorid());
        myHolder.txtCname.setText(dataListCounselor.getCounselorname());
        myHolder.txtSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cid = myHolder.txtCid.getText().toString();
                String cname = myHolder.txtCname.getText().toString();
                if (CounselorContactActivity.linearReallocateTo.getVisibility() == View.GONE) {
                    CounselorContactActivity.linearReallocateTo.setVisibility(View.VISIBLE);
                }

                CounselorContactActivity.txtSelecteCId.setText(cid);
                CounselorContactActivity.txtSelectedCname.setText(cname);
                CounselorContactActivity.txtSelctedSrNo.setText(srno);
                editor.putString("SCID", cid);
                editor.putString("CounselorName",cname);

                //editor.putString("SCNAME",cname);
                editor.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView txtCid, txtCname, txtSelect;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtSelect = itemView.findViewById(R.id.txtSelect);
            txtCid = itemView.findViewById(R.id.txtCounselorId);
            txtCname = itemView.findViewById(R.id.txtCounselorName);
        }
    }
}
