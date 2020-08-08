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

public class AdapterSmsTemplate extends RecyclerView.Adapter<AdapterSmsTemplate.MyHolder> {
   Context context;
   ArrayList<DataTemplate> arrayListTemplate;
   ProgressDialog dialog;
   String clienturl,clientid,msg,counselorid;
   int smsid;
   RequestQueue requestQueue;
   SharedPreferences sp;

    public AdapterSmsTemplate(Context context, ArrayList<DataTemplate> arrayListTemplate) {
        this.context = context;
        this.arrayListTemplate = arrayListTemplate;
        requestQueue=Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_smstemplate,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        final DataTemplate dataTemplate=arrayListTemplate.get(i);
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid = sp.getString("Id", null);
        counselorid=counselorid.replaceAll(" ","");
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",clienturl);
        smsid=dataTemplate.getSmsid();
        //timeout=sp.getLong("TimeOut",0);

        myHolder.txtTemplate.setText(dataTemplate.getTemplate());

        myHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=ProgressDialog.show(context,"","Deleting template",false);
                    deleteMsgTemplate(counselorid,dataTemplate.getSmsid());

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListTemplate.size();
    }
    public void deleteMsgTemplate(String cid, int  sid) {
        if(CheckServer.isServerReachable(context)) {
           String url = clienturl + "?clientid=" + clientid +"&caseid=169&CounselorID=" + cid+"&SmsID="+sid;
            Log.d("DeleteMsgTemplateUrl", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("DeleteTeplate", response.toString());
                            try {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("TemplateMsgResponse", response);
                                if (response.contains("Data updated successfully")) {
                                   // edtMeggase.setText("");
                                 //   ((MessageTemplate)context).resetGraph(context);
                                    Intent intent=new Intent(context,MessageTemplate.class);
                                    intent.putExtra("Activity",MessageTemplate.activity);
                                    context.startActivity(intent);
                                    Toast.makeText(context, "Template deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Template not deleted", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Log.d("Exception", String.valueOf(e));
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
        }else {
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
                                        //edtName.setText("");
                                        //edtPassword.setText("");*//*
                            dialog.dismiss();

                        }
                    }).show();
        }
    }//close insertMsgTemplate

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtTemplate;
        ImageView imgDelete;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtTemplate=itemView.findViewById(R.id.txtTemplate);
            imgDelete=itemView.findViewById(R.id.imgDelete);
        }
    }
}
