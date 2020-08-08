package com.bizcall.wayto.mentebit13;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterSendNotification extends RecyclerView.Adapter<AdapterSendNotification.Holder> {
    ArrayList<DataListCounselor> arrayList;
    Context context;
    int k;

    public AdapterSendNotification(ArrayList<DataListCounselor> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_send_notification, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final DataListCounselor details = arrayList.get(i);

        holder.txtCounsName.setText(details.getCounselorname());

        holder.txtCounsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("addedaccount", details.getCounselorid()+" / "+details.getCounselorname());

                if (ActivitySendNotify.selectCheckbox != 1) {
                    k++;
                    if (k % 2 == 0) {
                        holder.txtCounsName.setBackgroundColor(context.getResources().getColor(R.color.listcolor6));
                        DataListCounselor dataListCounselor = new DataListCounselor(details.getCounselorid(), details.getCounselorname());
                        ActivitySendNotify.arrayListNew.add(dataListCounselor);
                        //Log.d("addedacc", details.getCounselorid()+" / "+details.getCounselorname());
                    } else {
                        for (int z = 0; z < ActivitySendNotify.arrayListNew.size(); z++) {
                            if (details.getCounselorid().equals(ActivitySendNotify.arrayListNew.get(z).getCounselorid())) {
                                ActivitySendNotify.arrayListNew.remove(z);
                                holder.txtCounsName.setBackgroundColor(context.getResources().getColor(R.color.white));
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtCounsName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtCounsName = itemView.findViewById(R.id.txt_counselor_name);
        }
    }
}
