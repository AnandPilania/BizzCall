package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterReminder extends RecyclerView.Adapter<AdapterReminder.MyHolder> {
    Context context;
    ArrayList<DataReminder> arrayList;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    String counsellorid,clienturl,sr_no,clientid,callid;
    int eventid;


    public AdapterReminder(Context context, ArrayList<DataReminder> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view=LayoutInflater.from(context).inflate(R.layout.layout_adapter_reminder,viewGroup,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor=sp.edit();
        counsellorid=sp.getString("Id",null);
        counsellorid=counsellorid.replace(" ","");
        clientid=sp.getString("ClientId",null);

        counsellorid=sp.getString("Id",null);
        counsellorid=counsellorid.replace(" ","");
        clienturl=sp.getString("ClientUrl",null);
        Log.d("Client",clienturl);
        final DataReminder dataReminder = arrayList.get(i);
        myHolder.txtSrno.setText(dataReminder.getmId());
        myHolder.txtName.setText(dataReminder.getmName());
        myHolder.txtCourse.setText(dataReminder.getmCourse());
        myHolder.txtMobile.setText(dataReminder.getmMobile1());
        myHolder.txtMobile2.setText(dataReminder.getmMobile2());
        myHolder.txtRemarks.setText(dataReminder.getmRemarks());
        myHolder.txtCallDate.setText(dataReminder.getmDate().substring(0,10));
        if (dataReminder.getmTime().length() == 9){
            myHolder.txtCallTime.setText("0"+dataReminder.getmTime().replace(".",""));
        }else
            myHolder.txtCallTime.setText(dataReminder.getmTime().replace(".",""));

        myHolder.txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sr=dataReminder.getmId();
                callid=dataReminder.getCallingId();
                Log.d("SR",sr);
                editor.putString("SelectedSrNo",sr);
                editor.putString("SelectedCallingId",callid);
                editor.putString("ActivityContact","ReminderActivity");
                editor.commit();
                Intent intent=new Intent(context,CounselorContactActivity.class);
                intent.putExtra("ActivityName","ReminderActivity");
                context.startActivity(intent);
            }
        });
        myHolder.txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(context, "", "Clearing reminder...", true);
                sr_no=dataReminder.getmId();
                callid=dataReminder.getCallingId();
                if(CheckInternetSpeed.checkInternet(context).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("No Internet connection!!!")
                            .setMessage("Can't do further process")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                    dialog.dismiss();

                                }
                            }).show();
                }
                else if(CheckInternetSpeed.checkInternet(context).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Slow Internet speed!!!")
                            .setMessage("Can't do further process")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //insertIMEI();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                else {
                    clearReminder();
                }
            }
        });

    }

    public void insertPointCollection(int eid)
    {
        try {
            urlRequest = UrlRequest.getObject();
            urlRequest.setContext(context);
            urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=36&nCounsellorId=" + counsellorid + "&nEventId=" + eid);
            Log.d("PointCollectionResponse", clienturl + "?clientid=" + clientid + "&caseid=36&nCounsellorId=" + counsellorid + "&nEventId=" + eid);
            urlRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    try {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d("PointCollectionResponse", response);
                        if (response.contains("Data inserted successfully")) {
                            getPointCollection();
                            Toast.makeText(context, "Added point for update details", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Point not added for update details", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        Toast.makeText(context, "Errorcode-409 AdapterReminder PointCollectionResponse", Toast.LENGTH_SHORT).show();
                    }
                    //   Log.d("Size**", String.valueOf(arrayList.size()));
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(context, "Errorcode-408 AdapterReminder insertPointCollection", Toast.LENGTH_SHORT).show();
        }
    }
    public void getPointCollection() {
            try {
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(context);
                //String clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php";
                urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=37&nCounsellorId=" + counsellorid);
                Log.d("CojnUrl", clienturl + "?clientid=" + clientid + "&caseid=37&nCounsellorId=" + counsellorid);
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d("CoinResponse", response);
                        try {
                            // arrayListTotal.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            // Log.d("Json",jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String totalcoin = jsonObject1.getString("Total Coin");
                                ReminderActivity.txtCoin.setText(totalcoin);
                                editor = sp.edit();
                                editor.putString("TotalCoin", totalcoin);
                                editor.commit();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "Errorcode-411 AdapterReminder getPointCollectionRes", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }catch (Exception e)
            {
                Toast.makeText(context, "Errorcode-410 AdapterReminder getPointCollection", Toast.LENGTH_SHORT).show();
            }
    }
    public void clearReminder()
    {
        try {
            if (CheckServer.isServerReachable(context)) {
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(context);
                urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=35&cUpdatedBy=" + counsellorid + "&nSrNo=" + sr_no + "&nCallingId=" + callid);
                Log.d("ClearReminder", clienturl + "?clientid=" + clientid + "&caseid=35&cUpdatedBy=" + counsellorid + "&nSrNo=" + sr_no + "&nCallingId=" + callid);
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        try{
                        dialog.dismiss();

                        Log.d("ClearReminderResonse", response);
                        if (response.contains("Row updated successfully")) {
                            eventid = 14;
                            insertPointCollection(eventid);
                            Intent intent = new Intent(context, ReminderActivity.class);
                            context.startActivity(intent);
                            Toast.makeText(context, "Reminder cleared", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Reminder not cleared", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                        {
                            Toast.makeText(context, "Errorcode-413 AdapterReminder clearReminderResponse", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Network issue!!!!")
                        .setMessage("Try after some time!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                dialog.dismiss();

                            }
                        }).show();
            }
        }catch (Exception e)
        {
            Toast.makeText(context, "Errorcode-412 AdapterReminder clearReminder", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView txtSrno,txtName,txtCourse,txtMobile,txtMobile2,txtRemarks,txtCallDate,txtCallTime,txtCall,txtClear;
        public MyHolder(@NonNull View itemView)
        {
            super(itemView);
            txtCall=itemView.findViewById(R.id.txtCall);
            txtClear=itemView.findViewById(R.id.txtClearReminder);
            txtSrno=itemView.findViewById(R.id.txtReminderSrNo);
            txtName=itemView.findViewById(R.id.txtReminderName);
            txtCourse=itemView.findViewById(R.id.txtReminderCourse);
            txtMobile=itemView.findViewById(R.id.txtReminderMobile);
            txtMobile2=itemView.findViewById(R.id.txtReminderMobile2);
            txtRemarks=itemView.findViewById(R.id.txtReminderRemarks);
            txtCallDate=itemView.findViewById(R.id.txtReminderCallDate);
            txtCallTime=itemView.findViewById(R.id.txtReminderCallTime);
        }
    }
}
