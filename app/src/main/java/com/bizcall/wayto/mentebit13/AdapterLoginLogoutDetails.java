package com.bizcall.wayto.mentebit13;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterLoginLogoutDetails extends RecyclerView.Adapter<AdapterLoginLogoutDetails.MyHolder>
{
    Context context;
    ArrayList<DataLoginLogoutDetails> arrayList;

    public AdapterLoginLogoutDetails(Context context, ArrayList<DataLoginLogoutDetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_loginlogout_details,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        DataLoginLogoutDetails dataLoginLogoutDetails=arrayList.get(i);
        myHolder.txtDate1.setText(dataLoginLogoutDetails.getLogindate());
        myHolder.txtLoginTime.setText(dataLoginLogoutDetails.getLogintime());
        myHolder.txtLogoutDate.setText(dataLoginLogoutDetails.getLogouttime());
        myHolder.txtTotalHrs.setText(dataLoginLogoutDetails.getTotalhrs());

      /* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        try {

           String logintime=myHolder.txtLoginTime.getText().toString();
           String logouttime=myHolder.txtLogoutDate.getText().toString();
            Log.d("CLiked", logintime+" "+logouttime);
            Date date1 = simpleDateFormat.parse(logintime);
            Date date2 = simpleDateFormat.parse(logouttime);
            long difference = date2.getTime() - date1.getTime();
           String totaltime= String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(difference),
                    TimeUnit.MILLISECONDS.toMinutes(difference) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference)));
           // TimeUnit.MILLISECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference))



        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       TextView txtDate1,txtLoginTime,txtLogoutDate,txtTotalHrs;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtDate1=itemView.findViewById(R.id.txtDate);
            txtLoginTime=itemView.findViewById(R.id.txtLoginTime);
            txtLogoutDate=itemView.findViewById(R.id.txtLogoutTime);
            txtTotalHrs=itemView.findViewById(R.id.txtTotalHrs);

        }
    }
}
