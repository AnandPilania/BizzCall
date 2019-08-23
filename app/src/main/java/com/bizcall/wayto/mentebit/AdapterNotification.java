package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.MyHolder> {
   Context context;
   ArrayList<DataNotification> arrayList;
   SharedPreferences sp;
   SharedPreferences.Editor editor;
   UrlRequest urlRequest;
   String clienturl,clientid;

    public AdapterNotification(Context context, ArrayList<DataNotification> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.layout_adapternotification,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final DataNotification dataNotification=arrayList.get(i);
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        editor=sp.edit();
        clientid=sp.getString("ClientId",null);
        clienturl=sp.getString("ClientUrl",null);
        myHolder.txtNotification.setText(dataNotification.getStrNotificaion());
        final String notificationId=dataNotification.getNotificationId();

        myHolder.txtNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("SelectedSrNo",dataNotification.getSrno());
                editor.putString("ActivityContact","Home");
                editor.commit();
              //  Toast.makeText(context,"SrNo"+dataNotification.getSrno(),Toast.LENGTH_SHORT).show();
                if(dataNotification.getStrNotificaion().contains("Reminder"))
                {
                    Intent intent = new Intent(context, ReminderActivity.class);
                    intent.putExtra("ActivityName", "Home");
                    context.startActivity(intent);
                } else if(dataNotification.getStrNotificaion().contains("_Client_uploaded")||dataNotification.getStrNotificaion().contains("_Client_updated")) {
                   Intent intent=new Intent(context,MasterEntry.class);
                     context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, CounselorContactActivity.class);
                   intent.putExtra("ActivityName", "Home");
                    context.startActivity(intent);
                }
            }
        });
        myHolder.txtClearNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                clearNotification(notificationId);}
            }
        });
        }
    public void clearNotification(String notificationId)
    {
        try{
            if(CheckServer.isServerReachable(context)) {
                //String callId=sp.getString("SelectedCallingId",null);
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(context);
                urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=69&NotificationID=" + notificationId);
                Log.d("ClearReminder", clienturl + "?clientid=" + clientid + "&caseid=69&NotificationID=" + notificationId);
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        // dialog.dismiss();

                        Log.d("ClearReminderResonse", response);
                        if (response.contains("Row updated successfully")) {
                            Intent intent = new Intent(context, AllNotifications.class);
                            context.startActivity(intent);
                            //dialog = ProgressDialog.show(CounselorContactActivity.this, "", "Loading...", true);
                            Toast.makeText(context, "Notification Cleared", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Notification not cleared", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
               // dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(context, "Errorcode-399 AdapterNotification clearNotification", Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtNotification,txtClearNotification;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtNotification=itemView.findViewById(R.id.txtNotification);
            txtClearNotification=itemView.findViewById(R.id.txtClearNotification);
        }
    }
}
