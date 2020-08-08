package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterFormFilled extends RecyclerView.Adapter<AdapterFormFilled.MyHolder> {
   Context context;
   ArrayList<DataFormFilled> arrayListFormFilled;
   SharedPreferences sp;
   SharedPreferences.Editor editor;

    public AdapterFormFilled(Context context, ArrayList<DataFormFilled> arrayListFormFilled) {
        this.context = context;
        this.arrayListFormFilled = arrayListFormFilled;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_formfilled,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        if (i % 2 == 0) {
            myHolder.cardView.setBackgroundResource(R.color.off_white);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            myHolder.cardView.setBackgroundResource(R.color.off_white1);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        DataFormFilled dataFormFilled=arrayListFormFilled.get(i);
        myHolder.txtSrno.setText(dataFormFilled.getStrId());
        myHolder.txtCandidatename.setText(dataFormFilled.getStrFName());
        myHolder.txtMobile.setText(dataFormFilled.getStrMobile());
        myHolder.txtWebsite.setText(dataFormFilled.getStrWebsite());
        myHolder.txtCount.setText((i + 1) + ".");
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        editor=sp.edit();
        myHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String Selectedsrno=myHolder.txtSrno.getText().toString();
                editor.putString("SelectedSrNo",Selectedsrno);
                editor.commit();
                Intent intent=new Intent(context,CounselorContactActivity.class);
                intent.putExtra("ActivityName","FormFilled");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListFormFilled.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtSrno,txtCandidatename,txtMobile,txtWebsite,txtCount;
        CardView cardView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtCount=itemView.findViewById(R.id.txtCount1);
            cardView=itemView.findViewById(R.id.cardInfo);
            txtSrno=itemView.findViewById(R.id.txtSerial);
            txtCandidatename=itemView.findViewById(R.id.txtCandidateName);
            txtMobile=itemView.findViewById(R.id.txtMobile1);
            txtWebsite=itemView.findViewById(R.id.txtWebsite);
        }
    }
}
