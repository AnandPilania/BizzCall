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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class AdapterPendingApproval extends RecyclerView.Adapter<AdapterPendingApproval.MyHolder> {

    Context context;
    ArrayList<DataPendingApproval> arrayListPendingApproval;
    ProgressDialog dialog;
    String clienturl,clientid,leaveid,counselorid;
    RequestQueue requestQueue;
    long timeout;
    SharedPreferences sp;


    public AdapterPendingApproval(Context context, ArrayList<DataPendingApproval> arrayListPendingApproval) {
        this.context = context;
        this.arrayListPendingApproval = arrayListPendingApproval;
        requestQueue= Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_pendingapproval,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {

        DataPendingApproval dataPendingApproval=arrayListPendingApproval.get(i);
        if (i % 2 == 0) {
            myHolder.linearLeaveDetails.setBackgroundResource(R.color.off_white);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            myHolder.linearLeaveDetails.setBackgroundResource(R.color.off_white1);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replaceAll(" ", "");
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
       // counselorname = sp.getString("Name", null);
        timeout = sp.getLong("TimeOut", 0);
        myHolder.txtLeaveID.setText(dataPendingApproval.getLeaveID());
        myHolder.txtDateFrom.setText(dataPendingApproval.getDateFrom());
        myHolder.txtDateTo.setText(dataPendingApproval.getDateTo());
        myHolder.txtTotalDays.setText(dataPendingApproval.getTotalDays());
        myHolder.txtRemarks.setText(dataPendingApproval.getRemarks());
        myHolder.txtApplicationDate.setText(dataPendingApproval.getApplicationdate());
        myHolder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=ProgressDialog.show(context,"","Cancelling leave",true);
                cancelAppliedLeave(myHolder.txtLeaveID.getText().toString());
            }
        });

    }
    public void cancelAppliedLeave(String leaveID)
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid=154&LeaveID="+leaveID+"&CounselorID="+counselorid;
            Log.d("ApprovedUrl", url);

            if(CheckInternet.checkInternet(context))
            {
                if(CheckServer.isServerReachable(context)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();

                                    Log.d("CancelledLeaveResponse", response);
                                    try {
                                        if(response.contains("Data updated successfully"))
                                        {
                                            Intent intent=new Intent(context,LeaveReport.class);
                                            context.startActivity(intent);
                                            Toast.makeText(context,"Leave application cancelled",Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(context,"Leave cancellation failed",Toast.LENGTH_SHORT).show();
                                            //getPendingApproval();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(context,"Errorcode-534 LeaveReport ApprovedLeavesResponse"+e.toString(),Toast.LENGTH_SHORT).show();
                                       // dialog.dismiss();
                                        Log.d("CounselorDetailExceptio", String.valueOf(e));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;
                                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(context, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            }else {
                dialog.dismiss();
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
        }catch (Exception e)
        {
            Toast.makeText(context,"Errorcode-533 LeaveReport getApprovedLeaves "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcApprovedLeaves", String.valueOf(e));
        }
    }

    @Override
    public int getItemCount() {
        return arrayListPendingApproval.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtLeaveID,txtDateFrom,txtDateTo,txtTotalDays,txtRemarks,txtApplicationDate;
        LinearLayout linearLeaveDetails;
        ImageView imgCancel;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            linearLeaveDetails=itemView.findViewById(R.id.linearLeaveDetails);
            txtLeaveID=itemView.findViewById(R.id.txtLeaveID);
            txtDateFrom=itemView.findViewById(R.id.txtDateFrom);
            txtDateTo=itemView.findViewById(R.id.txtDateTo);
            txtTotalDays=itemView.findViewById(R.id.txtTotalDays);
            txtRemarks=itemView.findViewById(R.id.txtRemarks);
            txtApplicationDate=itemView.findViewById(R.id.txtApplicationDate);
            imgCancel=itemView.findViewById(R.id.imgCancel);
        }
    }

}
