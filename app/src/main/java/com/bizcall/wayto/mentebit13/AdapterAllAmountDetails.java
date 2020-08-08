package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class AdapterAllAmountDetails extends RecyclerView.Adapter<AdapterAllAmountDetails.MyHolder> {
    Context context;
    ArrayList<DataAllAmountDetails> arrayListAmountDetails;
    List<DataAllAmountDetails> displayedList;
    //DataAllAmountDetails dataAllAmountDetails;
   AlertDialog alertDialog1;
    RequestQueue requestQueue;
    ProgressDialog dialog;
    String clienturl,clientid,remarks,paymentid,status;
    String paymentID,fileNo,cname,counselorname,amountINR,amountUSD;
    SharedPreferences sp;
    int flag=0;
    String totalFeeD="0",totalFeeR="0",fileID,candidatename;
    Thread thread;
    long timeout;

    int t11,t12,t13,t14,t15,t16,t17,ts,ti,totalUSD=0,totalINR=0,temp=0,res=0;
    String fname,lname,mobile,passport,panno,collegeFee1YearD,hostalFee1YearD,oneTimeProcessingFeeD,otherFee1Head,otherFee1D,otherFee2Head,otherFee2D,otherFee3Head,otherFee3D,mess,totalFeeYearD,airFareFeeR,indiaOTC,totalFeeYearR;
    int clgfeetotal,clgfeeReceived,clgfeeDue,hostalFeeTotal,hostalFeeReceived,hostalFeeDue,OTPReceived,OTPTotal,OTPDue,accidentalTotal,
            accidentalReceived,accidentalDue,messTotal,messReceived,messDue,totalReceivedD,totalDueD,airfareTotal,airfareReceived,airfareDues,
            indiaOTCTotal,indiaOTCRecceived,indiaOTCDues,totalReceivedR,totalDuesR;
    String collegeFee1YearD1,collegeFeeFinal,hostalFee1YearD1,hostalFeeFinal,OTPFeeFinal,accidentalFeeFinal,messFeeFinal,IndiaOTCFeeFinal,AirFareFinal,totalFeeFinalD,totalFeeFinalR,oneTimeProcessingFeeD1,Accidental_Insurance,MessFeeD,IndiaOTC,AirFareFee;
    String  CollegeFee1YearD,HostalFee1YearD,OneTimeProcessingFeeD,AirFareFeeR;
    ArrayList<String> arrayListClgId,arrayListCollege;
    TextView txtSubmit,txtTotalRecivedUSD,txtTotalRecivedINR;
    TextView txtCollegeFeeTotalD,txtCollegeFeeFinal,txtCollegeFeeReceivedD,txtHostalTotalFeeD,txtHostalFinalFeeD,txtHostalReceivedFeeD,txtOTPTotalFeeD,
            txtOTPFeeFinalD,txtOTPReceivedFeeD,txtAccidentalTotal,txtAccidentalFinal,txtAccidentalReceived,txtMessTotal,txtMessFinal,txtMessReceived,txtTotalFeeD,txtFeeFinalD,txtReceivedFeeD,txtDueFeeD;
    TextView txtAirFareFeeTotal,txtAirFareFeeFinal,txtAirFareFeeReceived,txtIndiaOTCTotal,txtIndiaOTCFinal,txtIndiaOTCReceived,txtTotalFeeTotalR,txtTotalFeeFinalR,txtTotalFeeReceivedR,txtTotalFeeDuesR;
    TextView txtGreaterAmtUSD,txtGreaterAmtINR, txtCollegeFee1YearD1,txtHostalFee1YearD1,txtOneTimeProcessingFeeD1,txtAccidentalInsurance1,txtMess1,txtTotalFeeYearD1,txtDiscountDetails;
    EditText txtCollegeFeeDueD,txtHostalDueFeeD,txtOTPDueFeeD,txtAccidentalDue,txtMessDue,txtAirFareFeeDues,txtIndiaOTCDues;

    public AdapterAllAmountDetails(Context context, ArrayList<DataAllAmountDetails> arrayListAmountDetails) {
        this.context = context;
        this.arrayListAmountDetails = arrayListAmountDetails;
        requestQueue= Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_allamount_details,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final DataAllAmountDetails dataAllAmountDetails=arrayListAmountDetails.get(i);
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        clientid=sp.getString("ClientId","");
        clienturl=sp.getString("ClientUrl","");
        timeout=sp.getLong("TimeOut",0);

        if(dataAllAmountDetails.getCounselorname().isEmpty())
        {
            myHolder.txtCounselorName.setText("0");
        }else
        {
            myHolder.txtCounselorName.setText(dataAllAmountDetails.getCounselorname());
        }

        if(dataAllAmountDetails.getAmountUSD().isEmpty())
        {
            myHolder.txtAmountUSD.setText("0");
        }else {
            myHolder.txtAmountUSD.setText(dataAllAmountDetails.getAmountUSD());
        }

        if(dataAllAmountDetails.getAmountINR().isEmpty())
        {
            myHolder.txtAmountINR.setText("0");
        }else {
            myHolder.txtAmountINR.setText(dataAllAmountDetails.getAmountINR());
        }
        if(dataAllAmountDetails.getPaymentid().isEmpty())
        {
            myHolder.txtPaymentId.setText("0");
        }else {

            myHolder.txtPaymentId.setText(dataAllAmountDetails.getPaymentid());
        }
        if(dataAllAmountDetails.getFileno().isEmpty())
        {
            myHolder.txtFileNo.setText("0");
        }else {
            myHolder.txtFileNo.setText(dataAllAmountDetails.getFileno());
        }
        if(dataAllAmountDetails.getCandidatename().isEmpty())
        {
            myHolder.txtCandidateName.setText("0");
        }else
            {
            myHolder.txtCandidateName.setText(dataAllAmountDetails.getCandidatename());
        }

        myHolder.imgApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountINR=dataAllAmountDetails.getAmountINR();
                amountUSD=dataAllAmountDetails.getAmountUSD();
                paymentID=dataAllAmountDetails.getPaymentid();
                fileNo=dataAllAmountDetails.getFileno();
                cname=dataAllAmountDetails.getCandidatename();
                counselorname=dataAllAmountDetails.getCounselorname();
                Intent intent=new Intent(context,PaymentApproveOrReject.class);
                intent.putExtra("PaymentID",paymentID);
                intent.putExtra("FileID",fileNo);
                intent.putExtra("CandidateName",cname);
                intent.putExtra("CounselorName",counselorname);
                intent.putExtra("AmountINR",amountINR);
                intent.putExtra("AmountUSD",amountUSD);
                intent.putExtra("AccountName",dataAllAmountDetails.getAccountname());
                intent.putExtra("DollarRate",dataAllAmountDetails.getDollarrate());
                intent.putExtra("PaymentDate",dataAllAmountDetails.getPaymentdate());
                intent.putExtra("ReceivedDate",dataAllAmountDetails.getReceiveddate());
                intent.putExtra("PaymentFrom",dataAllAmountDetails.getPaymentfrom());
                intent.putExtra("ReceiptName",dataAllAmountDetails.getRecieptname());
                context.startActivity(intent);
             /*   dialog=ProgressDialog.show(context,"","Loading package details",true);
                getPackageDetails(fileNo);
                getPackageReceivedDetail();
                showAlert();*/
            }
        });
    }


    public void showAlert()
    {

        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View view = li.inflate(R.layout.alert_approve, null);
        final TextView txtPaymentId1,txtFileNo1,txtCandidateName1,txtCounselorName1,txtAmountINR1,txtAmountUSD1,txtApprove,txtReject;
        final EditText edtRemarks;
        final ImageView imgClose;


        txtCollegeFeeTotalD=view.findViewById(R.id.txtCollegeFeeDTotalVal);
        txtCollegeFeeReceivedD=view.findViewById(R.id.txtCollegeFeeDReceivedVal);
        txtCollegeFeeDueD=view.findViewById(R.id.txtCollegeFeeDDueVal);
        txtTotalRecivedUSD=view.findViewById(R.id.txtTotalRecivedUSD);
        txtTotalRecivedINR=view.findViewById(R.id.txtTotalReceivedINR);
        txtHostalTotalFeeD=view.findViewById(R.id.txtHostalFeeDTotalVal);
        txtHostalReceivedFeeD=view.findViewById(R.id.txtHostalFeeDReceivedVal);
        txtHostalDueFeeD=view.findViewById(R.id.txtHostalFeeDDueVal);
        txtCollegeFeeFinal=view.findViewById(R.id.txtCollegeFeeDFinal);
        txtHostalFinalFeeD=view.findViewById(R.id.txtHostalFeeFinalD);
        txtOTPFeeFinalD=view.findViewById(R.id.txtOTPFeeDFinalVal);
        txtAccidentalFinal=view.findViewById(R.id.txtAccidentalFinalVal);
        txtMessFinal=view.findViewById(R.id.txtMessFinalVal);
        txtFeeFinalD=view.findViewById(R.id.txtFeeFinalD);
        txtAirFareFeeFinal=view.findViewById(R.id.txtAirFareFeeFinallR);
        txtIndiaOTCFinal=view.findViewById(R.id.txtIndiaOTCFinal);
        txtTotalFeeFinalR=view.findViewById(R.id.txtTotalFeeFinalR);

        txtOTPTotalFeeD=view.findViewById(R.id.txtOTPFeeDTotalVal);
        txtOTPReceivedFeeD=view.findViewById(R.id.txtOTPFeeDReceivedVal);
        txtOTPDueFeeD=view.findViewById(R.id.txtOTPFeeDDueVal);

        txtAccidentalTotal=view.findViewById(R.id.txtAccidentalTotalVal);
        txtAccidentalReceived=view.findViewById(R.id.txtAccidentalReceivedVal);
        txtAccidentalDue=view.findViewById(R.id.txtAccidentalDueVal);
        txtMessTotal=view.findViewById(R.id.txtMessTotalVal);
        txtMessReceived=view.findViewById(R.id.txtMessReceivedVal);
        txtMessDue=view.findViewById(R.id.txtMessDueVal);

        txtTotalFeeD=view.findViewById(R.id.txtTotalFeeD);
        txtReceivedFeeD=view.findViewById(R.id.txtReceivedFeeD);
        txtDueFeeD=view.findViewById(R.id.txtDueFeeD);

        txtCollegeFee1YearD1=view.findViewById(R.id.txtCollegeFee1YearD1);
        txtHostalFee1YearD1=view.findViewById(R.id.txtHostalFee1YearD1);
        txtOneTimeProcessingFeeD1=view.findViewById(R.id.txtOneTimeProcessingFeeD1);
        txtAccidentalInsurance1=view.findViewById(R.id.txtAccidentalInsurance1);
        txtMess1=view.findViewById(R.id.txtMess1);
        txtTotalFeeYearD1=view.findViewById(R.id.txtTotalFeeYearD1);
        txtAirFareFeeTotal=view.findViewById(R.id.txtAirFareFeeTotalR);
        txtAirFareFeeReceived=view.findViewById(R.id.txtAirFareFeeReceivedR);
        txtAirFareFeeDues=view.findViewById(R.id.txtAirFareFeeDuesR);
        txtIndiaOTCTotal=view.findViewById(R.id.txtIndiaOTCTotal);
        txtIndiaOTCReceived=view.findViewById(R.id.txtIndiaOTCReceived);
        txtIndiaOTCDues=view.findViewById(R.id.txtIndiaOTCDues);
        txtTotalFeeTotalR=view.findViewById(R.id.txtTotalFeeTotalR);
        txtTotalFeeReceivedR=view.findViewById(R.id.txtTotalFeeReceivedR);
        txtTotalFeeDuesR=view.findViewById(R.id.txtTotalFeeYearDuesR);
        txtGreaterAmtUSD=view.findViewById(R.id.txtGreaterAmtUSD);
        txtGreaterAmtINR=view.findViewById(R.id.txtGreaterAmtINR);


        txtPaymentId1=view.findViewById(R.id.txtPaymentID);
        txtFileNo1=view.findViewById(R.id.txtFileNo);
        txtCandidateName1=view.findViewById(R.id.txtCandidateName);
        txtCounselorName1=view.findViewById(R.id.txtCounselorName);
        txtAmountINR1=view.findViewById(R.id.txtAmountINR);
        txtAmountUSD1=view.findViewById(R.id.txtAmountUSD);
        txtApprove=view.findViewById(R.id.txtApprove);
        txtReject=view.findViewById(R.id.txtReject);
        edtRemarks=view.findViewById(R.id.edtRemarks);
        imgClose=view.findViewById(R.id.imgClose);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        //Adding our dialog box to the view of alert dialog
        alert.setView(view);
        //Creating an alert dialog
        alertDialog1 = alert.create();
        alertDialog1.show();
        alertDialog1.setCancelable(false);
        txtPaymentId1.setText("PaymentID - "+paymentID);
        txtFileNo1.setText(" ("+fileNo+" - ");
        txtCandidateName1.setText(cname+" )");
        txtCounselorName1.setText(counselorname+" - ");
        if(amountINR.isEmpty())
        {
            txtAmountINR1.setText("0");
        }
        else {
            txtAmountINR1.setText(amountINR);
        }
        if(amountUSD.isEmpty())
        {
            txtAmountUSD1.setText("0");
        }
        else {
            txtAmountUSD1.setText(amountUSD);
        }
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

        txtAirFareFeeDues.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getAmountINR("AirFee");
            }
            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        txtIndiaOTCDues.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getAmountINR("IndiaOTC");
            }
            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        txtCollegeFeeDueD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getAmountUSD("ClgFeeD");
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtHostalDueFeeD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String amt=edtHostalFeeYearD.getText().toString();
                getAmountUSD("HostalFeeD");
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtOTPDueFeeD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // String amt=edtOTPFeeD.getText().toString();
                getAmountUSD("OTPFee");
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtAccidentalDue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amt=txtAccidentalDue.getText().toString();
                getAmountUSD("Accidental");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtMessDue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // String amt=edtMess.getText().toString();
                getAmountUSD("Mess");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
                if(edtRemarks.getText().toString().length()==0)
                {
                    flag=1;
                    edtRemarks.setError("Please enter remarks");
                }
                remarks=edtRemarks.getText().toString();

                paymentid=txtPaymentId1.getText().toString();

                CollegeFee1YearD=String.valueOf(t11);
                HostalFee1YearD=String.valueOf(t12);
                OneTimeProcessingFeeD=String.valueOf(t13);
                Accidental_Insurance=String.valueOf(t14);
                MessFeeD=String.valueOf(t15);
                IndiaOTC=String.valueOf(t17);
                AirFareFeeR=String.valueOf(t16);
                ts=Integer.parseInt(txtAmountUSD1.getText().toString());
                ti=Integer.parseInt(txtAmountINR1.getText().toString());

                if(totalUSD>ts) {
                    txtGreaterAmtUSD.setVisibility(View.VISIBLE);
                    flag=1;
                }else {
                    txtGreaterAmtUSD.setVisibility(View.GONE);
                }

                if(totalINR>ti) {
                    txtGreaterAmtINR.setVisibility(View.VISIBLE);
                    flag=1;
                }else {
                    txtGreaterAmtINR.setVisibility(View.GONE);
                }

                if(flag==0) {
                    dialog=ProgressDialog.show(context,"","Accepting payment",true);
                    approveOrRejectPayment("1");
                }
            }
        });
        txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
                if(edtRemarks.getText().toString().length()==0)
                {
                    flag=1;
                    edtRemarks.setError("Please enter remarks");
                }
                remarks=edtRemarks.getText().toString();
                paymentid=txtPaymentId1.getText().toString();

                if(flag==0) {
                    dialog=ProgressDialog.show(context,"","Rejecting payment",true);
                    approveOrRejectPayment("3");
                }
            }
        });
    }
    public void getAmountUSD(String edtname)
    {
        if(edtname.contains("ClgFeeD"))
        {
            if(txtCollegeFeeDueD.getText().toString().isEmpty())
            {
                t11=0;
            }
            else
            {
                t11=Integer.parseInt(txtCollegeFeeDueD.getText().toString());
            }
        }
        else if(edtname.contains("HostalFeeD"))
        {
            if(txtHostalDueFeeD.getText().toString().isEmpty())
            {
                t12=0;
            }
            else {
                t12=Integer.parseInt(txtHostalDueFeeD.getText().toString());
            }
        }
        else if(edtname.contains("OTPFee"))
        {

            if(txtOTPDueFeeD.getText().toString().isEmpty())
            {
                t13=0;
            }
            else
            {
                t13=Integer.parseInt(txtOTPDueFeeD.getText().toString());
            }
        }
        else if(edtname.contains("Accidental"))
        {
            if(txtAccidentalDue.getText().toString().isEmpty())
            {
                t14=0;
            }
            else {
                t14=Integer.parseInt(txtAccidentalDue.getText().toString());
            }
        }
        else if(edtname.contains("Mess"))
        {
            if(txtMessDue.getText().toString().isEmpty())
            {
                t15=0;
            }
            else {
                t15=Integer.parseInt(txtMessDue.getText().toString());
            }
        }

        totalUSD=t11+t12+t13+t14+t15;
        totalFeeD=String.valueOf(totalUSD);

        txtDueFeeD.setText(totalFeeD);
    }
    public void getAmountINR(String edtname)
    {
        if(edtname.contains("AirFee"))
        {
            if(txtAirFareFeeDues.getText().toString().isEmpty())
            {
                t16=0;
            }
            else
            {
                t16=Integer.parseInt(txtAirFareFeeDues.getText().toString());
            }
        }
        else if(edtname.contains("IndiaOTC"))
        {
            if(txtIndiaOTCDues.getText().toString().isEmpty())
            {
                t17=0;
            }
            else {
                t17=Integer.parseInt(txtIndiaOTCDues.getText().toString());
            }
        }

        totalINR=t16+t17;
        totalFeeR=String.valueOf(totalINR);
      /*  if(total>Integer.parseInt(edtAmountInINR.getText().toString())) {
            txtGreaterAmtINR.setVisibility(View.VISIBLE);
        }else {
            txtGreaterAmtINR.setVisibility(View.GONE);
        }*/
        txtTotalFeeDuesR.setText(totalFeeR);
    }
    public void getPackageDetails(String fileID)
    {
        try {
           String url = clienturl + "?clientid=" + clientid + "&caseid=116&FileNo="+fileID;
            Log.d("PackageDetailsUrl", url);
            if (CheckInternet.checkInternet(context)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PackageDetailsResponse", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                    dialog.dismiss();
                                    if (response.contains("[]")) {

                                        //  Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            collegeFee1YearD= jsonObject1.getString("CollegeFee1YearD");
                                            collegeFeeFinal= jsonObject1.getString("CollegeFee1YearD_Final");
                                            hostalFee1YearD = jsonObject1.getString("HostalFee1YearD");
                                            hostalFeeFinal= jsonObject1.getString("HostalFee1YearD_Final");
                                            oneTimeProcessingFeeD = jsonObject1.getString("OneTimeProcessingFeeD");
                                            OTPFeeFinal= jsonObject1.getString("OneTimeProcessingFeeD_Final");
                                            otherFee1D=jsonObject1.getString("Accidental_Insurance");
                                            accidentalFeeFinal= jsonObject1.getString("Accidental_Insurance_Final");
                                            otherFee2D=jsonObject1.getString("MessFeeD");
                                            messFeeFinal= jsonObject1.getString("MessFeeD_Final");
                                            totalFeeYearD = jsonObject1.getString("TotalFeeYearD");
                                            //  collegeFeeFinal= jsonObject1.getString("CollegeFee1YearD");
                                            airFareFeeR = jsonObject1.getString("AirFareFeeR");
                                            AirFareFinal= jsonObject1.getString("AirFareFeeR_Final");
                                            otherFee3D=jsonObject1.getString("IndiaOTC");
                                            IndiaOTCFeeFinal= jsonObject1.getString("IndiaOTC_Final");
                                            totalFeeYearR = jsonObject1.getString("TotalFeeYearR");
                                            // collegeFeeFinal= jsonObject1.getString("CollegeFee1YearD");
                                        }
                                        if(collegeFee1YearD == null || collegeFee1YearD.isEmpty() || collegeFee1YearD.equals("null")||collegeFee1YearD.contains("NA")) {
                                            txtCollegeFeeTotalD.setText("0");
                                        }
                                        else {
                                            txtCollegeFeeTotalD.setText(collegeFee1YearD);
                                        }
                                        if(hostalFee1YearD == null || hostalFee1YearD.isEmpty() || hostalFee1YearD.equals("null")||hostalFee1YearD.contains("NA")) {

                                            txtHostalTotalFeeD.setText("0");
                                        }
                                        else {
                                            txtHostalTotalFeeD.setText(hostalFee1YearD);
                                        }
                                        if(oneTimeProcessingFeeD == null || oneTimeProcessingFeeD.isEmpty() || oneTimeProcessingFeeD.equals("null")||oneTimeProcessingFeeD.contains("NA")) {
                                            txtOTPTotalFeeD.setText("0");
                                        }
                                        else {
                                            txtOTPTotalFeeD.setText(oneTimeProcessingFeeD);
                                        }
                                        if(otherFee1D == null || otherFee1D.isEmpty() || otherFee1D.equals("null")||otherFee1D.contains("NA")) {
                                            txtAccidentalTotal.setText("0");
                                        }
                                        else {
                                            txtAccidentalTotal.setText(otherFee1D);
                                        }
                                        if(otherFee2D == null || otherFee2D.isEmpty() || otherFee2D.equals("null")||otherFee2D.contains("NA")) {
                                            txtMessTotal.setText("0");
                                        }
                                        else {
                                            txtMessTotal.setText(otherFee2D);
                                        }
                                        if(totalFeeYearD == null || totalFeeYearD.isEmpty() || totalFeeYearD.equals("null")||totalFeeYearD.contains("NA")) {
                                            txtTotalFeeD.setText("0");
                                        }
                                        else {
                                            txtTotalFeeD.setText(totalFeeYearD);
                                        }
                                        if(airFareFeeR == null || airFareFeeR.isEmpty() || airFareFeeR.equals("null")||airFareFeeR.contains("NA")) {
                                            txtAirFareFeeTotal.setText("0");
                                        }
                                        else {
                                            txtAirFareFeeTotal.setText(airFareFeeR);
                                        }
                                        if(otherFee3D == null || otherFee3D.isEmpty() || otherFee3D.equals("null")||otherFee3D.contains("NA")) {
                                            txtIndiaOTCTotal.setText("0");
                                        }
                                        else {
                                            txtIndiaOTCTotal.setText(otherFee3D);
                                        }
                                        if(totalFeeYearR == null || totalFeeYearR.isEmpty() || totalFeeYearR.equals("null")||totalFeeYearR.contains("NA")) {
                                            txtTotalFeeTotalR.setText("0");
                                        }
                                        else {
                                            txtTotalFeeTotalR.setText(totalFeeYearR);
                                        }
                                        if(collegeFeeFinal == null || collegeFeeFinal.isEmpty() || collegeFeeFinal.equals("null")||collegeFeeFinal.contains("NA")) {
                                            txtCollegeFeeFinal.setText("0");
                                        }
                                        else {
                                            txtCollegeFeeFinal.setText(collegeFeeFinal);
                                        }
                                        if(hostalFeeFinal == null || hostalFeeFinal.isEmpty() || hostalFeeFinal.equals("null")||hostalFeeFinal.contains("NA")) {
                                            txtHostalFinalFeeD.setText("0");
                                        }
                                        else {
                                            txtHostalFinalFeeD.setText(hostalFeeFinal);
                                        }
                                        if(OTPFeeFinal == null || OTPFeeFinal.isEmpty() || OTPFeeFinal.equals("null")||OTPFeeFinal.contains("NA")) {
                                            txtOTPFeeFinalD.setText("0");
                                        }
                                        else {
                                            txtOTPFeeFinalD.setText(OTPFeeFinal);
                                        }
                                        if(accidentalFeeFinal == null || accidentalFeeFinal.isEmpty() || accidentalFeeFinal.equals("null")||accidentalFeeFinal.contains("NA")) {
                                            txtAccidentalFinal.setText("0");
                                        }
                                        else {
                                            txtAccidentalFinal.setText(accidentalFeeFinal);
                                        }
                                        if(messFeeFinal == null || messFeeFinal.isEmpty() || messFeeFinal.equals("null")||messFeeFinal.contains("NA")) {
                                            txtMessFinal.setText("0");
                                        }
                                        else {
                                            txtMessFinal.setText(messFeeFinal);
                                        }

                                        int totalFinalD1=Integer.parseInt(txtCollegeFeeFinal.getText().toString())+Integer.parseInt(txtHostalFinalFeeD.getText().toString())+
                                                Integer.parseInt(txtOTPFeeFinalD.getText().toString())
                                                +Integer.parseInt(txtAccidentalFinal.getText().toString())+Integer.parseInt(txtMessFinal.getText().toString());

                                        txtFeeFinalD.setText(String.valueOf(totalFinalD1));
                                        if(AirFareFinal == null || AirFareFinal.isEmpty() || AirFareFinal.equals("null")||AirFareFinal.contains("NA")) {
                                            txtAirFareFeeFinal.setText("0");
                                        }
                                        else
                                        {
                                            txtAirFareFeeFinal.setText(AirFareFinal);
                                        }
                                        if(IndiaOTCFeeFinal == null || IndiaOTCFeeFinal.isEmpty() || IndiaOTCFeeFinal.equals("null")||IndiaOTCFeeFinal.contains("NA")) {
                                            txtIndiaOTCFinal.setText("0");
                                        }else
                                        {
                                            txtIndiaOTCFinal.setText(IndiaOTCFeeFinal);
                                        }
                                        int totalFinalR=Integer.parseInt(txtAirFareFeeFinal.getText().toString())+Integer.parseInt(txtIndiaOTCFinal.getText().toString());
                                        txtTotalFeeFinalR.setText(String.valueOf(totalFinalR));



                                        getPackageReceivedDetail();

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(context,"Errorcode-347 CollegeAttorny getCollegeListResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
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
            Toast.makeText(context,"Errorcode-346 CollegeAttorny getCollegeList "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }
    public void getPackageReceivedDetail()
    {
        try {
           String url = clienturl + "?clientid=" + clientid + "&caseid=122&FileNo="+fileID;
            Log.d("PckgReceivedDetailsUrl", url);
            if (CheckInternet.checkInternet(context)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PckgReceivedDetailsRes", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                    dialog.dismiss();
                                    if (response.contains("[]")) {
                                        txtCollegeFeeReceivedD.setText("0");
                                        txtHostalReceivedFeeD.setText("0");
                                        txtOTPReceivedFeeD.setText("0");
                                        txtAccidentalReceived.setText("0");
                                        txtMessReceived.setText("0");
                                        txtAirFareFeeReceived.setText("0");
                                        txtIndiaOTCReceived.setText("0");

                                        airfareTotal=Integer.parseInt(txtAirFareFeeTotal.getText().toString());
                                        airfareReceived=Integer.parseInt(txtAirFareFeeReceived.getText().toString());
                                        airfareDues=airfareTotal-airfareReceived;
                                       // txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal=Integer.parseInt(txtIndiaOTCTotal.getText().toString());
                                        indiaOTCRecceived=Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues=indiaOTCTotal-indiaOTCRecceived;
                                       // txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                        clgfeetotal=Integer.parseInt(txtCollegeFeeTotalD.getText().toString());
                                        clgfeeReceived=Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                        clgfeeDue=clgfeetotal-clgfeeReceived;
                                        //txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal=Integer.parseInt(txtHostalTotalFeeD.getText().toString());
                                        hostalFeeReceived=Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue=hostalFeeTotal-hostalFeeReceived;
                                       // txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal=Integer.parseInt(txtOTPTotalFeeD.getText().toString());
                                        OTPReceived=Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue=OTPTotal-OTPReceived;
                                      //  txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal=Integer.parseInt(txtAccidentalTotal.getText().toString());
                                        accidentalReceived=Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue=accidentalTotal-accidentalReceived;
                                     //   txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal=Integer.parseInt(txtMessTotal.getText().toString());
                                        messReceived=Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue=messTotal-messReceived;
                                     //   txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD=clgfeeReceived+hostalFeeReceived+OTPReceived+accidentalReceived+messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD=clgfeeDue+hostalFeeDue+OTPDue+accidentalDue+messDue;
                                      //  txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR=airfareReceived+indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR=airfareDues+indiaOTCDues;
                                     //   txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));
                                        // linearPackageDetails.setVisibility(View.GONE);
                                        // Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //  linearPackageDetails.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            collegeFee1YearD1= jsonObject1.getString("CollegeFee1YearD");
                                            hostalFee1YearD1 = jsonObject1.getString("HostalFee1YearD");
                                            oneTimeProcessingFeeD1 = jsonObject1.getString("OneTimeProcessingFeeD");
                                            Accidental_Insurance = jsonObject1.getString("Accidental_Insurance");
                                            MessFeeD=jsonObject1.getString("MessFeeD");
                                            IndiaOTC = jsonObject1.getString("IndiaOTC");
                                            AirFareFee=jsonObject1.getString("AirFareFeeR");
                                        }
                                        if(collegeFee1YearD1 == null || collegeFee1YearD1.isEmpty() || collegeFee1YearD1.equals("null")||collegeFee1YearD1.contains("NA")) {
                                            txtCollegeFeeReceivedD.setText("0");
                                        }
                                        else {
                                            txtCollegeFeeReceivedD.setText(collegeFee1YearD1);
                                        }
                                        if(hostalFee1YearD1 == null || hostalFee1YearD1.isEmpty() || hostalFee1YearD1.equals("null")||hostalFee1YearD1.contains("NA")) {
                                            txtHostalReceivedFeeD.setText("0");
                                        }else
                                        {
                                            txtHostalReceivedFeeD.setText(hostalFee1YearD1);
                                        }
                                        txtOTPReceivedFeeD.setText(oneTimeProcessingFeeD1);
                                        txtAccidentalReceived.setText(Accidental_Insurance);
                                        txtMessReceived.setText(MessFeeD);
                                        txtAirFareFeeReceived.setText(AirFareFee);
                                        txtIndiaOTCReceived.setText(IndiaOTC);

                                        airfareTotal=Integer.parseInt(txtAirFareFeeFinal.getText().toString());
                                        airfareReceived=Integer.parseInt(txtAirFareFeeReceived.getText().toString());
                                        airfareDues=airfareTotal-airfareReceived;
                                        //txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal=Integer.parseInt(txtIndiaOTCFinal.getText().toString());
                                        indiaOTCRecceived=Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues=indiaOTCTotal-indiaOTCRecceived;
                                       // txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                        clgfeetotal=Integer.parseInt(txtCollegeFeeFinal.getText().toString());
                                        clgfeeReceived=Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                        clgfeeDue=clgfeetotal-clgfeeReceived;
                                       // txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal=Integer.parseInt(txtHostalFinalFeeD.getText().toString());
                                        hostalFeeReceived=Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue=hostalFeeTotal-hostalFeeReceived;
                                      //  txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal=Integer.parseInt(txtOTPFeeFinalD.getText().toString());
                                        OTPReceived=Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue=OTPTotal-OTPReceived;
                                      //  txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal=Integer.parseInt(txtAccidentalFinal.getText().toString());
                                        accidentalReceived=Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue=accidentalTotal-accidentalReceived;
                                       // txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal=Integer.parseInt(txtMessFinal.getText().toString());
                                        messReceived=Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue=messTotal-messReceived;
                                     //   txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD=clgfeeReceived+hostalFeeReceived+OTPReceived+accidentalReceived+messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD=clgfeeDue+hostalFeeDue+OTPDue+accidentalDue+messDue;
                                      //  txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR=airfareReceived+indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR=airfareDues+indiaOTCDues;
                                      //  txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));

                                      /*  totalfeeReceived=Integer.parseInt(txtTotalFeeD.getText().toString());
                                        totalReceived=Integer.parseInt(txtReceivedFeeD.getText().toString());
                                        totalDue=totalfeeReceived-totalReceived;
                                        txtDueFeeD.setText(String.valueOf(totalDue));*/


                                         /* txtTotalFeeD.setText(totalFeeYearD);
                                            txtAirFareFeeR.setText("AirFareFeeR : "+airFareFeeR);
                                               txtIndiaOTC.setText(otherFee3Head+" : "+otherFee3D);
                                          txtTotalFeeYearR.setText("TotalFeeYearR :"+totalFeeYearR);*/
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(context,"Errorcode-347 CollegeAttorny getCollegeListResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
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
            Toast.makeText(context,"Errorcode-346 CollegeAttorny getCollegeList "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }
    public void approveOrRejectPayment(final String status1)
    {
        try{
            // String url="";
            String url = clienturl + "?clientid=" + clientid + "&caseid=132&Remarks="+remarks+"&PaymentID="+paymentID+"&Status="+status1+"&CollegeFee1YearD="+CollegeFee1YearD+"&HostalFee1YearD="+HostalFee1YearD+
                    "&OneTimeProcessingFeeD="+OneTimeProcessingFeeD+"&Accidental_Insurance="+Accidental_Insurance+"&MessFeeD="+MessFeeD+"&IndiaOTC="+IndiaOTC+"&AirFareFeeR="+AirFareFeeR;
            Log.d("PaymentEntryUrl", url);
            if (CheckInternet.checkInternet(context)) {
                if(CheckServer.isServerReachable(context)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("PaymentEntryResponse", response);
                                        dialog.dismiss();
                                        if(response.contains("Data updated successfully"))
                                        {
                                            if(status1.contains("1")) {
                                                Intent intent=new Intent(context,PaymentApprove.class);
                                                context.startActivity(intent);
                                                Toast.makeText(context, "Payment approved", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                Intent intent=new Intent(context,PaymentApprove.class);
                                                context.startActivity(intent);
                                                Toast.makeText(context,"Payment rejected",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            if(status1.contains("1")) {
                                                Toast.makeText(context, "Payment approve failed", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                Toast.makeText(context,"Payment rejection failed",Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(context, "Errorcode-308 MasterEntry searchDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.d("Exception", String.valueOf(e));
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
            Toast.makeText(context,"Errorcode-307 MasterEntry searchDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return arrayListAmountDetails.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtPaymentId,txtFileNo,txtCandidateName,txtCounselorName,txtAmountINR,txtAmountUSD;
        ImageView imgApprove;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imgApprove=itemView.findViewById(R.id.imgApprove);
            txtPaymentId=itemView.findViewById(R.id.txtPaymentID);
            txtFileNo=itemView.findViewById(R.id.txtFileNo);
            txtCandidateName=itemView.findViewById(R.id.txtCandidateName);
            txtCounselorName=itemView.findViewById(R.id.txtCounselorName);
            txtAmountINR=itemView.findViewById(R.id.txtAmountINR);
            txtAmountUSD=itemView.findViewById(R.id.txtAmountUSD);
        }

    }
}
