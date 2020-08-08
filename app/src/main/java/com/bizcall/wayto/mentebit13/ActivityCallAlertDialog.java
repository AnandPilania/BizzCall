package com.bizcall.wayto.mentebit13;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityCallAlertDialog extends Activity {
    AlertDialog.Builder builder;
    AlertDialog alert;
    DataCallLogs dataCallLogs;
    ArrayList<DataCallLogs> arrayListCallLogs;
    SimpleDateFormat sdfSaveArray;
    String callPhoneno, finalCallType, strdateFormated, callDuration, strIMEI1, strIMEI2, callphAccID;
    String strMNo, strmobNo, strCallType, strCallDt, strCallTm;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        strMNo = getIntent().getStringExtra("callnumber");
        strmobNo = getIntent().getStringExtra("mobno");
        getCalllogDetails();//insert call logs

        builder = new AlertDialog.Builder(ActivityCallAlertDialog.this);
        LayoutInflater li = LayoutInflater.from(ActivityCallAlertDialog.this);
        View newEducate = li.inflate(R.layout.layout_alertringing, null);

        ImageView imgCancle = newEducate.findViewById(R.id.img_cancle);
        TextView txtCallName = newEducate.findViewById(R.id.txt_contact_nmno);
        TextView txtLeadLable = newEducate.findViewById(R.id.txt_leadstatus);
        TextView txtLeadInfo = newEducate.findViewById(R.id.txt_leadinfo);
        Button btnAddLable = newEducate.findViewById(R.id.btn_add_lables);
        TextView txtreplywhatsapp = newEducate.findViewById(R.id.txt_replywhatsapp);
        TextView txtdeletelead = newEducate.findViewById(R.id.txt_deletelead);

        txtCallName.setText(strMNo);
        if (strCallType.equals(null) || strCallDt.equals(null) || strCallTm.equals(null)){
            txtLeadInfo.setText("New Lead saved");
            txtLeadLable.setText("New Lead");
        } else {
            txtLeadInfo.setText(strCallType+" "+strCallDt+" "+strCallTm);
            txtLeadLable.setText("Lead");
        }
        builder.setView(newEducate);
        builder.setCancelable(false);
        alert = builder.create();
        //alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();

        imgCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();
            }
        });

        btnAddLable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //---------------------------------- DBCallLogs ------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getCalllogDetails(){
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int phAccID = managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);

        arrayListCallLogs = new ArrayList<>();
        arrayListCallLogs.clear();

        while (managedCursor.moveToNext()) {
            callPhoneno = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            callDuration = managedCursor.getString(duration);
            callphAccID = managedCursor.getString(phAccID);
            finalCallType = null;

            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    finalCallType = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    finalCallType = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    finalCallType = "MISSED";
                    break;
            }
            sdfSaveArray = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
            strdateFormated = sdfSaveArray.format(callDayTime);

            //Log.d("calllogDetails", callPhoneno + " / " + finalCallType + " / " + strdateFormated + " / " + callDuration);
            dataCallLogs = new DataCallLogs(callPhoneno, finalCallType, strdateFormated, callDuration, strIMEI1, strIMEI2, callphAccID);
            arrayListCallLogs.add(dataCallLogs);

            if (callPhoneno.length()>9) {
                if (strmobNo.equals(callPhoneno.substring(callPhoneno.length() - 10))) {
                    strCallType = finalCallType;
                    strCallDt = strdateFormated.substring(0, 10);
                    strCallTm = strdateFormated.substring(11, 16);
                }
                Log.d("lastcall", strCallType+" "+strCallDt+" "+strCallTm);
            }
        }
        Log.d("callarraysize", arrayListCallLogs.size() + "");
    }
}
