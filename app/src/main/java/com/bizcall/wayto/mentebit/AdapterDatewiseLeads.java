package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;

public class AdapterDatewiseLeads extends RecyclerView.Adapter<AdapterDatewiseLeads.Holder> {
    private ArrayList<DatewiseLeads> arrayListDatewise;
    Context mContext;
    String strspannable;
    View view;

    public AdapterDatewiseLeads(ArrayList<DatewiseLeads> arrayListDatewise, Context mContext) {
        this.arrayListDatewise = arrayListDatewise;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_datewiseleads, viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        DatewiseLeads tableDetails = arrayListDatewise.get(i);
        holder.txtDate.setText(tableDetails.getmDate());
        holder.txtDateLeads.setText(tableDetails.getmTtlLeads());

        strspannable = holder.txtDate.getText().toString();
        SpannableString spannableString = new SpannableString(strspannable);
        spannableString.setSpan(new UnderlineSpan(),0, spannableString.length(), 0);
        holder.txtDate.setText(spannableString);

        holder.txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ActivityCounselorwiseLeads.class);
                intent.putExtra("DateLead",holder.txtDate.getText().toString());
                intent.putExtra("TtlDateLead",holder.txtDateLeads.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Animatoo.animateSlideLeft(view.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListDatewise.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtDate, txtDateLeads;
        public Holder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtleaddate);
            txtDateLeads = itemView.findViewById(R.id.txtdatelead);
        }
    }
}
