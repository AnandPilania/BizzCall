package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterReallocation extends RecyclerView.Adapter<AdapterReallocation.MyHolder> {
    Context context;
    ArrayList<DataReallocation> arrayListReallocation;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public AdapterReallocation(Context context, ArrayList<DataReallocation> arrayListReallocation) {
        this.context = context;
        this.arrayListReallocation = arrayListReallocation;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_reallocation, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i)
    {
        if (i % 2 == 0) {
            myHolder.cardInfo.setBackgroundResource(R.color.off_white);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            myHolder.cardInfo.setBackgroundResource(R.color.off_white1);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        Log.d("InAdapter","AdapterReallocation");
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        editor=sp.edit();
        DataReallocation dataReallocation=arrayListReallocation.get(i);
            final String strToBy=sp.getString("ToorBy",null);

            myHolder.txtCounselorName.setText(dataReallocation.getCouselorname());
            myHolder.txtCName.setText(dataReallocation.getCname());
            myHolder.txtCourse.setText(dataReallocation.getCourse());
            myHolder.txtStatus.setText(dataReallocation.getStatus1());
            myHolder.txtSrno.setText(dataReallocation.getSr_no());
            myHolder.txtCount1.setText(dataReallocation.getSerial());

            myHolder.cardInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 if(strToBy.contains("By"))
                {
                    editor.putString("SelectedSrNo",myHolder.txtSrno.getText().toString());
                    editor.putString("ActivityContact","Allocation");
                    editor.commit();
                Intent intent=new Intent(context,CounselorContactActivity.class);
                intent.putExtra("ActivityName","Allocation");
                context.startActivity(intent);
                //"SelectedSrNo",myHolder.txtSrno.getText().toString());
                  }
                }
            });


    }

    @Override
    public int getItemCount() {
        return arrayListReallocation.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtSrno, txtCName, txtCourse, txtCount1, txtStatus,txtCounselorName;
        CardView cardAction, cardInfo;
        Button btnCall, btnMsg, btnMail;


        LinearLayout linearLayout, linearCounselor;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtCount1 = itemView.findViewById(R.id.txtCount1);
            txtCName = itemView.findViewById(R.id.txtCandidateName);
            txtCourse = itemView.findViewById(R.id.txtCourse);
            txtSrno = itemView.findViewById(R.id.txtSerial);
            txtCounselorName=itemView.findViewById(R.id.txtCounselorNameS);
           // txtMobile = itemView.findViewById(R.id.txtMobile1);
           // cardAction = itemView.findViewById(R.id.cardAction);
            cardInfo = itemView.findViewById(R.id.cardInfo);
            txtStatus=itemView.findViewById(R.id.txtStatusS);
           // linearLayout = itemView.findViewById(R.id.linearCounselorInfo);
            linearCounselor = itemView.findViewById(R.id.linearCounselor);
        }
    }
}
