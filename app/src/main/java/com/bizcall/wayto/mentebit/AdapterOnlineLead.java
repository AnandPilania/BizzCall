package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class AdapterOnlineLead extends RecyclerView.Adapter<AdapterOnlineLead.ViewHolder> {
    Context context;
    ArrayList<DataCounselor> arrayList;
    ArrayList<String> arraySms;
    Intent intent;
    AlertDialog alertDialog;
    long no, no1;
    AudioManager audioManager;
    String mbl, course, sr_no, name, email,status,statusid,remarks;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int selectedposition = -1, count = 0;
    ProgressDialog dialog;
    UrlRequest urlRequest;
    RequestQueue requestQueue;
    String totalcount;
    String sms, clientid,clienturl, parentno,counselorid,Selectedsrno,Selectembl;
    TextView txtName, txtCloseWindow;
    TextView edtMessage, edtMobile;
    Spinner spinnerTemplate;
    ImageView imgCloseWindow, imgClearText;
    Button btnSms, btnWhatsapp;
    boolean isClicked = false;
    int p1 = 1;
    WifiManager wifiManager;
    String actname;
    DataCounselor dataCounselor;
    ViewHolder viewHolder1;
    NetworkInfo info;
    String clicked,sr_onlinelead;


    public AdapterOnlineLead(Context context, ArrayList<DataCounselor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdapterOnlineLead.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_onlinelead, viewGroup, false);
        //  audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        //   audioManager.setMode(AudioManager.MODE_IN_CALL);

        return new AdapterOnlineLead.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterOnlineLead.ViewHolder viewHolder, final int i)
    {
        info = getInfo(context);
         dataCounselor = arrayList.get(i);
         viewHolder1=viewHolder;
        if (i % 2 == 0) {
            viewHolder.cardInfo.setBackgroundResource(R.color.off_white);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            viewHolder.cardInfo.setBackgroundResource(R.color.off_white1);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid=sp.getString("Id",null);
        counselorid=counselorid.replaceAll(" ","");
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        editor = sp.edit();
        actname=sp.getString("ActOnlineLead",null);
        Log.d("Actname", actname);

        if(actname.equals("ConvertedOnlineLead"))
        {
            editor.putString("ActivityContact","ConvertedOnlineLead");
            editor.commit();
            viewHolder.txtMobile.setVisibility(View.GONE);
            viewHolder.txtStatus.setVisibility(View.VISIBLE);
        }
        else if(actname.equals("OpenLeads"))
        {
            editor.putString("ActivityContact","OpenLeads");
            editor.commit();
        }
        else if(actname.equals("IVRMissed"))
        {
            editor.putString("ActivityContact","IVRMissed");
            editor.commit();
            viewHolder.txtStatus.setVisibility(View.GONE);
            viewHolder.txtMobile.setVisibility(View.VISIBLE);
        }
        else
        {
            editor.putString("ActivityContact","OnlineLead");
            editor.commit();
            viewHolder.txtStatus.setVisibility(View.GONE);
            viewHolder.txtMobile.setVisibility(View.VISIBLE);
        }

        viewHolder.txtCName.setText(dataCounselor.getCname());
        viewHolder.txtCourse.setText(dataCounselor.getCourse());
        viewHolder.txtSrno.setText(dataCounselor.getSr_no());
        //viewHolder.txtEmail.setText(dataCounselor.getEmail());
        viewHolder.txtMobile.setText(dataCounselor.getMobile());
        viewHolder.txtStatus.setText(dataCounselor.getStatus1());
        viewHolder.txtParent.setText(dataCounselor.getParentno());
        if(actname.equals("OpenLeads")||actname.equals("ConvertedOnlineLead")||actname.equals("IVRMissed"))
        {
            viewHolder.imgRemove.setVisibility(View.GONE);
        }
        viewHolder.txtCount1.setText((i + 1) + ".");

        // viewHolder.txtCount1.setText(dataCounselor.getStrSno());
        viewHolder.txtSrno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectedsrno=viewHolder.txtSrno.getText().toString();
                Selectembl=viewHolder.txtMobile.getText().toString();
                parentno = viewHolder.txtParent.getText().toString();
                getConnectionQuality();

            }
        });
        viewHolder.txtCName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectedsrno=viewHolder.txtSrno.getText().toString();
                Selectembl=viewHolder.txtMobile.getText().toString();
                parentno = viewHolder.txtParent.getText().toString();
                getConnectionQuality();

            }
        });
        viewHolder.txtMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectedsrno=viewHolder.txtSrno.getText().toString();
                Selectembl=viewHolder.txtMobile.getText().toString();
                parentno = viewHolder.txtParent.getText().toString();
                getConnectionQuality();

            }
        });
        viewHolder.txtCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectedsrno=viewHolder.txtSrno.getText().toString();
                Selectembl=viewHolder.txtMobile.getText().toString();
                parentno = viewHolder.txtParent.getText().toString();
                getConnectionQuality();

            }
        });
        viewHolder.txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectedsrno=viewHolder.txtSrno.getText().toString();
                Selectembl=viewHolder.txtMobile.getText().toString();
                parentno = viewHolder.txtParent.getText().toString();
                getConnectionQuality();

            }
        });
        viewHolder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectedsrno=viewHolder.txtSrno.getText().toString();
                clicked="Remove";
                dialog=ProgressDialog.show(context,"","Updating Online Leads",false,true);
                updateOnlineLead(Selectedsrno);
            }
        });



      /*  final Spannabl
      eString content = new SpannableString(mbl);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        viewHolder.txtMobile.setText(content);*/
        // viewHolder.cardAction.setVisibility(View.GONE);
        // viewHolder.txtDataFrom.setText(dataCounselor.getDatafrom());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();

        }
    public String getConnectionQuality() {

        if (!CheckInternet.checkInternet(context)) {
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


          //  Toast.makeText(context, "Service Unavailable", Toast.LENGTH_SHORT).show();
        }
        else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            if (level == 2||level==1) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
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
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        /* .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();

                             }
                         })*/
                        .show();

            //    Toast.makeText(context, "Wifi POOR", Toast.LENGTH_SHORT).show();
            }/* else if (level == 3) {
                Toast.makeText(context, "Wifi MODERATE", Toast.LENGTH_SHORT).show();
            }*/ else if (level==3||level == 4||level == 5) {
                if(actname.contains("ConvertedOnlineLead"))
                {
                    editor.putString("SelectedSrNo",Selectedsrno);
                    editor.commit();
                    Log.d("SrNo&&&",Selectedsrno);
                    Intent intent = new Intent(context, CounselorContactActivity.class);
                    intent.putExtra("ActivityName", "ConvertedOnlineLead");
                   /* intent.putExtra("Name",name);
                    intent.putExtra("Course",course);
                    intent.putExtra("SrNo",sr_no);
                    intent.putExtra("Email",email);*/
                    context.startActivity(intent);
                }
                else if(actname.contains("IVRMissed"))
                {
                    editor.putString("SelectedSrNo",Selectedsrno);
                    editor.commit();
                    Log.d("SrNo&&&",Selectedsrno);
                    Intent intent = new Intent(context, CounselorContactActivity.class);
                    intent.putExtra("ActivityName", "IVRMissed");
                    context.startActivity(intent);
                }
                else {
                    dialog = ProgressDialog.show(context, "", "Checking number in database", true);
                    checkPhoneNumber(parentno, counselorid,Selectedsrno);
                   // Log.d("StatusAdapter", dataCounselor.getStatus1());
                }

               // Toast.makeText(context, "Wifi GOOD", Toast.LENGTH_SHORT).show();
            } /*else if (level == 5) {
                Toast.makeText(context, "Wifi EXCELLENT", Toast.LENGTH_SHORT).show();
            }*//* else {
                Toast.makeText(context, "Wifi Slow", Toast.LENGTH_SHORT).show();
            }*/
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkClass = getNetworkClass(getNetworkType(context));
            if (networkClass == 1) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
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
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                       /* .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })*/
                        .show();
                Toast.makeText(context, "Mobile Data POOR", Toast.LENGTH_SHORT).show();
            } else if (networkClass == 2||networkClass == 3) {
                if(actname.equals("ConvertedOnlineLead"))
                {
                    editor.putString("SelectedSrNo", dataCounselor.getSr_no());
                    editor.commit();
                    Log.d("SrNo&&&",dataCounselor.getSr_no());
                    Intent intent = new Intent(context, CounselorContactActivity.class);
                    intent.putExtra("ActivityName", "ConvertedOnlineLead");
                   /* intent.putExtra("Name",name);
                    intent.putExtra("Course",course);
                    intent.putExtra("SrNo",sr_no);
                    intent.putExtra("Email",email);*/
                    context.startActivity(intent);
                }
                else {
                   // parentno = dataCounselor.getParentno();
                    dialog = ProgressDialog.show(context, "", "Checking number in database", true);
                    checkPhoneNumber(parentno, counselorid, Selectedsrno);
                    Log.d("StatusAdapter", dataCounselor.getStatus1());
                }

              //  Toast.makeText(context, "Mobile Data GOOD", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(context, "Check Your Service", Toast.LENGTH_SHORT).show();
        return "UNKNOWN";
    }

    public NetworkInfo getInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    public int getNetworkClass(int networkType) {
        try {
            return getNetworkClassReflect(networkType);
        } catch (Exception ignored) {
        }

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case 16: // TelephonyManager.NETWORK_TYPE_GSM:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return 1;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case 17: // TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return 2;
            case TelephonyManager.NETWORK_TYPE_LTE:
            case 18: // TelephonyManager.NETWORK_TYPE_IWLAN:
                return 3;
            default:
                return 0;
        }
    }

    private int getNetworkClassReflect(int networkType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getNetworkClass = TelephonyManager.class.getDeclaredMethod("getNetworkClass", int.class);
        if (!getNetworkClass.isAccessible()) {
            getNetworkClass.setAccessible(true);
        }
        return (Integer) getNetworkClass.invoke(null, networkType);
    }

    public static int getNetworkType(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType();
    }


    public void checkPhoneNumber(String number, String cid, final String selectedSrNo) {
        try {
            if(CheckServer.isServerReachable(context)) {
                requestQueue = Volley.newRequestQueue(context);
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(context);
                String url = clienturl + "?clientid=" + clientid + "&caseid=70&CounselorID=" + cid + "&PhoneNumber=" + number;
                urlRequest.setUrl(url);
                Log.d("CheckNumberUrl", url);
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {

                        try {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Log.d("CheckNumberResponse", response);
                            // arrayListTotal.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            // Log.d("Json",jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                totalcount = jsonObject1.getString("mobilecount");
                            }
                            if (totalcount.equals("0")) {
                                editor.putString("SelectedSrNo", selectedSrNo);
                                editor.commit();
                                Log.d("SrNo&&&", selectedSrNo);
                                Intent intent = new Intent(context, CounselorContactActivity.class);
                                if (actname.contains("OpenLeads") || actname.contains("RefreshOpenLeads") || actname.contains("ContactOpenLead")) {
                                    intent.putExtra("ActivityName", "RefreshOpenLeads");
                                } else {
                                    intent.putExtra("ActivityName", "OnlineLead");
                                }
                   /* intent.putExtra("Name",name);
                    intent.putExtra("Course",course);
                    intent.putExtra("SrNo",sr_no);
                    intent.putExtra("Email",email);*/
                                context.startActivity(intent);
                            } else {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("This number is allocated to someone else")
                                        .setMessage("You cannot make a call")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                clicked = "CardView";
                                                if (actname.contains("OnlineLead")) {
                                                    updateOnlineLead(selectedSrNo);
                                                } else if (actname.contains("OpenLeads")) {
                                                    updateOpenLeads(selectedSrNo);
                                                }
                                            }
                                        })

                                        .show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Errorcode-401 AdapterOnlineLead checkPhoneNumberResponse", Toast.LENGTH_SHORT).show();
                            Log.d("Exception", String.valueOf(e));
                        }
                    }
                });
            }else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
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
        }catch (Exception e){
            Toast.makeText(context, "Errorcode-400 AdapterOnlineLead checkPhoneNumber", Toast.LENGTH_SHORT).show();
        }
    }
    public void insertOnlineLead(String srno)
    {try {
        String url = clienturl + "?clientid=" + clientid + "&caseid=84&RefNo=" + srno;
        Log.d("InsertOnlineUrl", url);
        if (CheckInternet.checkInternet(context)) {
            if(CheckServer.isServerReachable(context)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                //Log.d("*******", response.toString());
                                try {

                                    Log.d("InsertOnlineResponse", response);
                                    Log.d("InsertOnlineResponse", response);

                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());

                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        sr_onlinelead = jsonObject1.getString("nSRNO");
                                    }
                                    Intent intent = new Intent(context, OnlineLead.class);
                                    intent.putExtra("ActivityLeads", "RefreshOnlineLead");
                                    context.startActivity(intent);

                                } catch (Exception e) {
                                    Toast.makeText(context, "Errorcode-403 AdapterOnlineLead insertOnlineLeadResponse", Toast.LENGTH_SHORT).show();
                                    // dialog.dismiss();
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
                                    alertDialogBuilder.setTitle("Server Error!!!")
                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
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
        } else {
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
        Toast.makeText(context, "Errorcode-402 AdapterOnlineLead insertOnlineLead", Toast.LENGTH_SHORT).show();
    }

    }

    public void updateOnlineLead(final String sr)
    {
        try {
            if(CheckServer.isServerReachable(context)) {
                requestQueue = Volley.newRequestQueue(context);

                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(context);
                String urlSatatus = clienturl + "?clientid=" + clientid + "&caseid=66&RefNo=" + sr;
                urlRequest.setUrl(urlSatatus);
                Log.d("updateOnlineUrl", urlSatatus);
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        try {
                            dialog.dismiss();
                            Log.d("updateOnlineRes", response);
                            if (response.contains("Row updated successfully")) {
                                // getOnlineLeadRefno(sr_onlinelead);
                                //dialog.dismiss();
                                if (actname.contains("OpenLeads")) {
                                    Intent intent = new Intent(context, OpenLeads.class);
                                    intent.putExtra("Activity", actname);
                                    context.startActivity(intent);
                                } else {
                                    if (clicked.equals("Remove")) {
                                        insertOnlineLead(sr);
                                    } else {
                                        Intent intent = new Intent(context, OnlineLead.class);
                                        intent.putExtra("ActivityLeads", "Refresh" + actname);
                                        context.startActivity(intent);
                                    }
                                }
                                Toast.makeText(context, "Online lead updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Online lead not updated ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Errorcode-405 AdapterOnlineLead updateOnlineLeadResponse", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
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
            Toast.makeText(context, "Errorcode-404 AdapterOnlineLead updateOnlineLead", Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void updateOpenLeads(String sr)
    {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=77&nSrNo=" + sr + "&CounselorID=" + counselorid;
            Log.d("UpdateOpenLeadUrl", url);
            if (CheckInternet.checkInternet(context)) {
                if(CheckServer.isServerReachable(context)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    // Log.d("*******", response.toString());
                                    try {
                                        dialog.dismiss();
                                        Log.d("updateOpenLeadRes", response);
                                        if (response.contains("Row updated successfully")) {
                                            Intent intent = new Intent(context, OpenLeads.class);
                                            intent.putExtra("Activity", "RefreshedOpenLead");
                                            context.startActivity(intent);
                                            // getOnlineLeadRefno(sr_onlinelead);
                                            Toast.makeText(context, "Open lead updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Open lead not updated ", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(context, "Errorcode-407 AdapterOnlineLead updateOpenLeadResponse", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;
                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                                        alertDialogBuilder.setTitle("Server Error!!!")

                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
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
            } else {
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
            Toast.makeText(context, "Errorcode-406 AdapterOnlineLead updateOpenLead", Toast.LENGTH_SHORT).show();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSrno, txtCName, txtCourse, txtCount1, txtMobile, txtStatus,txtParent;
        CardView cardAction, cardInfo;
        Button btnCall, btnMsg, btnMail;

        LinearLayout linearLayout, linearCounselor;
        ImageView imgRemove;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
           imgRemove=itemView.findViewById(R.id.imgRemove);
            txtCount1 = itemView.findViewById(R.id.txtCount1);
            txtParent=itemView.findViewById(R.id.txtParentNo);
            txtCName = itemView.findViewById(R.id.txtCandidateName);
            txtCourse = itemView.findViewById(R.id.txtCourse);
            txtSrno = itemView.findViewById(R.id.txtSerial);
            txtMobile = itemView.findViewById(R.id.txtMobile1);
            txtStatus=itemView.findViewById(R.id.txtStatus);
            cardAction = itemView.findViewById(R.id.cardAction);
            cardInfo = itemView.findViewById(R.id.cardInfo);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnMsg = itemView.findViewById(R.id.btnMessage);
            btnMail = itemView.findViewById(R.id.btnMail);
            linearLayout = itemView.findViewById(R.id.linearCounselorInfo);
            linearCounselor = itemView.findViewById(R.id.linearCounselor);

            // txtSMS = itemView.findViewById(R.id.txtSMS);
            // imgWhatsapp = itemView.findViewById(R.id.imgWhatsapp);
        }
    }

}

