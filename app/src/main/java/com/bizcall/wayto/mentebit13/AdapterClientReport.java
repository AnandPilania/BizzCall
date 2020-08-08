package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class AdapterClientReport extends RecyclerView.Adapter<AdapterClientReport.MyHolder>
{
   Context context;
   ArrayList<DataClientReport> arrayListClientReport;
    TextView txtCollegeFeeTotalD,txtCollegeFeeFinal,txtCollegeFeeReceivedD,txtCollegeFeeDueD,txtHostalTotalFeeD,txtHostalFinalFeeD,txtHostalReceivedFeeD,txtHostalDueFeeD,txtOTPTotalFeeD,
            txtOTPFeeFinalD,txtOTPReceivedFeeD,txtOTPDueFeeD,txtAccidentalTotal,txtAccidentalFinal,txtAccidentalReceived,txtAccidentalDue,txtMessTotal,txtMessFinal,txtMessReceived,txtMessDue,txtTotalFeeD,txtFeeFinalD,txtReceivedFeeD,txtDueFeeD;
    TextView txtTotalRecivedUSD,txtTotalRecivedINR,txtAirFareFeeTotal,txtAirFareFeeFinal,txtAirFareFeeReceived,txtAirFareFeeDues,txtIndiaOTCTotal,txtIndiaOTCFinal,txtIndiaOTCReceived,txtIndiaOTCDues,txtTotalFeeTotalR,txtTotalFeeFinalR,txtTotalFeeReceivedR,txtTotalFeeDuesR;
    TextView txtCollegeFee1YearD1,txtHostalFee1YearD1,txtOneTimeProcessingFeeD1,txtAccidentalInsurance1,txtMess1,txtTotalFeeYearD1,txtDiscountDetails;
    ImageView imgClose;
   SharedPreferences sp;
   RequestQueue requestQueue;
   ProgressDialog dialog;
  AlertDialog alertDialog1;
    ArrayList<String> arrayListClgId,arrayListCollege;
    LinearLayout linearPackageDetails;
   String clienturl,clientid,fileID,inr,usd;
    int clgfeetotal,clgfeeReceived,clgfeeDue,hostalFeeTotal,hostalFeeReceived,hostalFeeDue,OTPReceived,OTPTotal,OTPDue,accidentalTotal,
            accidentalReceived,accidentalDue,messTotal,messReceived,messDue,totalReceivedD,totalDueD,airfareTotal,airfareReceived,airfareDues,
            indiaOTCTotal,indiaOTCRecceived,indiaOTCDues,totalReceivedR,totalDuesR;
    String collegeFee1YearD1,collegeFeeFinal,hostalFee1YearD1,hostalFeeFinal,OTPFeeFinal,accidentalFeeFinal,messFeeFinal,IndiaOTCFeeFinal,AirFareFinal,totalFeeFinalD,totalFeeFinalR,oneTimeProcessingFeeD1,Accidental_Insurance,MessFeeD,IndiaOTC,AirFareFee;
    String collegeFee1YearD,hostalFee1YearD,oneTimeProcessingFeeD,otherFee1Head,otherFee1D,otherFee2Head,otherFee2D,otherFee3Head,otherFee3D,mess,totalFeeYearD,airFareFeeR,indiaOTC,totalFeeYearR;

    RecyclerView recyclerPaymentHistory;
    AdapterPaymentHistory adapterPaymentHistory;
    ArrayList<DataPaymentHistory> arrayListPaymentHistory;
    String paymentid,amountUSD,amountINR,date1,approved;
    LinearLayout linearPaymentHistory;

    public AdapterClientReport(Context context, ArrayList<DataClientReport> arrayListClientReport) {
        this.context = context;
        this.arrayListClientReport = arrayListClientReport;
        requestQueue= Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public AdapterClientReport.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_client_report,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterClientReport.MyHolder myHolder, int i) {
        DataClientReport dataClientReport=arrayListClientReport.get(i);
        myHolder.txtFileID.setText(dataClientReport.getFileID());
        myHolder.txtName.setText(dataClientReport.getName());
        myHolder.txtCounselorName.setText(dataClientReport.getCounselorname());
        myHolder.txtTotalINR.setText(dataClientReport.getTotalINR());
        myHolder.txtTotalUSD.setText(dataClientReport.getTotalUSD());
        myHolder.txtDiscountINR.setText(dataClientReport.getDiscountINR());
        myHolder.txtDiscountUSD.setText(dataClientReport.getDiscountUSD());
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        clienturl = sp.getString("ClientUrl", null);
        clientid = sp.getString("ClientId", null);

        myHolder.txtExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showAlert();
                fileID=myHolder.txtFileID.getText().toString();
                dialog=ProgressDialog.show(context,"","Loading package details",true);
                getPackageDetails(fileID);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListClientReport.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtFileID,txtName,txtCounselorName,txtTotalINR,txtTotalUSD,txtDiscountINR,txtDiscountUSD,txtExtend;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtFileID=itemView.findViewById(R.id.txtFileID);
            txtName=itemView.findViewById(R.id.txtName);
            txtCounselorName=itemView.findViewById(R.id.txtCounselorName);
            txtTotalINR=itemView.findViewById(R.id.txtTotalINR);
            txtTotalUSD=itemView.findViewById(R.id.txtTotalUSD);
            txtDiscountINR=itemView.findViewById(R.id.txtDiscountINR);
            txtDiscountUSD=itemView.findViewById(R.id.txtDiscountUSD);
            txtExtend=itemView.findViewById(R.id.txtExtend);
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
                                    //dialog.dismiss();
                                    getPaymentHistory();
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
                                        txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal=Integer.parseInt(txtIndiaOTCTotal.getText().toString());
                                        indiaOTCRecceived=Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues=indiaOTCTotal-indiaOTCRecceived;
                                        txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                        clgfeetotal=Integer.parseInt(txtCollegeFeeTotalD.getText().toString());
                                        clgfeeReceived=Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                        clgfeeDue=clgfeetotal-clgfeeReceived;
                                        txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal=Integer.parseInt(txtHostalTotalFeeD.getText().toString());
                                        hostalFeeReceived=Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue=hostalFeeTotal-hostalFeeReceived;
                                        txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal=Integer.parseInt(txtOTPTotalFeeD.getText().toString());
                                        OTPReceived=Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue=OTPTotal-OTPReceived;
                                        txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal=Integer.parseInt(txtAccidentalTotal.getText().toString());
                                        accidentalReceived=Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue=accidentalTotal-accidentalReceived;
                                        txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal=Integer.parseInt(txtMessTotal.getText().toString());
                                        messReceived=Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue=messTotal-messReceived;
                                        txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD=clgfeeReceived+hostalFeeReceived+OTPReceived+accidentalReceived+messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD=clgfeeDue+hostalFeeDue+OTPDue+accidentalDue+messDue;
                                        txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR=airfareReceived+indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR=airfareDues+indiaOTCDues;
                                        txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));
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

                                        if(oneTimeProcessingFeeD1 == null || oneTimeProcessingFeeD1.isEmpty() || oneTimeProcessingFeeD1.equals("null")||oneTimeProcessingFeeD1.contains("NA")) {
                                            txtOTPReceivedFeeD.setText("0");
                                        }
                                        else {
                                            txtOTPReceivedFeeD.setText(oneTimeProcessingFeeD1);
                                        }
                                        if(Accidental_Insurance == null || Accidental_Insurance.isEmpty() || Accidental_Insurance.equals("null")||Accidental_Insurance.contains("NA")) {
                                            txtAccidentalReceived.setText("0");
                                        }else {
                                            txtAccidentalReceived.setText(Accidental_Insurance);
                                        }
                                        if(MessFeeD == null || MessFeeD.isEmpty() || MessFeeD.equals("null")||MessFeeD.contains("NA"))
                                        {
                                            txtMessReceived.setText("0");
                                        }
                                        else {
                                            txtMessReceived.setText(MessFeeD);
                                        }
                                        if(AirFareFee == null || AirFareFee.isEmpty() || AirFareFee.equals("null")||AirFareFee.contains("NA")) {
                                            txtAirFareFeeReceived.setText("0");
                                        }
                                        else {
                                            txtAirFareFeeReceived.setText(AirFareFee);
                                        }
                                        if(IndiaOTC == null || IndiaOTC.isEmpty() || IndiaOTC.equals("null")||IndiaOTC.contains("NA"))
                                        {
                                            txtIndiaOTCReceived.setText("0");
                                        }
                                        else {
                                            txtIndiaOTCReceived.setText(IndiaOTC);
                                        }

                                      /*  txtOTPReceivedFeeD.setText(oneTimeProcessingFeeD1);
                                        txtAccidentalReceived.setText(Accidental_Insurance);
                                        txtMessReceived.setText(MessFeeD);
                                        txtAirFareFeeReceived.setText(AirFareFee);
                                        txtIndiaOTCReceived.setText(IndiaOTC);*/

                                        airfareTotal=Integer.parseInt(txtAirFareFeeFinal.getText().toString());
                                        airfareReceived=Integer.parseInt(txtAirFareFeeReceived.getText().toString());
                                        airfareDues=airfareTotal-airfareReceived;
                                        txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal=Integer.parseInt(txtIndiaOTCFinal.getText().toString());
                                        indiaOTCRecceived=Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues=indiaOTCTotal-indiaOTCRecceived;
                                        txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                        clgfeetotal=Integer.parseInt(txtCollegeFeeFinal.getText().toString());
                                        clgfeeReceived=Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                        clgfeeDue=clgfeetotal-clgfeeReceived;
                                        txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal=Integer.parseInt(txtHostalFinalFeeD.getText().toString());
                                        hostalFeeReceived=Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue=hostalFeeTotal-hostalFeeReceived;
                                        txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal=Integer.parseInt(txtOTPFeeFinalD.getText().toString());
                                        OTPReceived=Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue=OTPTotal-OTPReceived;
                                        txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal=Integer.parseInt(txtAccidentalFinal.getText().toString());
                                        accidentalReceived=Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue=accidentalTotal-accidentalReceived;
                                        txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal=Integer.parseInt(txtMessFinal.getText().toString());
                                        messReceived=Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue=messTotal-messReceived;
                                        txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD=clgfeeReceived+hostalFeeReceived+OTPReceived+accidentalReceived+messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD=clgfeeDue+hostalFeeDue+OTPDue+accidentalDue+messDue;
                                        txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR=airfareReceived+indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR=airfareDues+indiaOTCDues;
                                        txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));

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
    public void showAlert()
    {
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View view = li.inflate(R.layout.alert_packagedetails, null);

        txtAirFareFeeTotal=view.findViewById(R.id.txtAirFareFeeTotalR);
        txtAirFareFeeFinal=view.findViewById(R.id.txtAirFareFeeFinallR);
        txtAirFareFeeReceived=view.findViewById(R.id.txtAirFareFeeReceivedR);
        txtAirFareFeeDues=view.findViewById(R.id.txtAirFareFeeDuesR);
        txtIndiaOTCTotal=view.findViewById(R.id.txtIndiaOTCTotal);
        txtIndiaOTCFinal=view.findViewById(R.id.txtIndiaOTCFinal);
        txtIndiaOTCReceived=view.findViewById(R.id.txtIndiaOTCReceived);
        txtIndiaOTCDues=view.findViewById(R.id.txtIndiaOTCDues);
        txtTotalFeeTotalR=view.findViewById(R.id.txtTotalFeeTotalR);
        txtTotalFeeFinalR=view.findViewById(R.id.txtTotalFeeFinalR);
        txtTotalFeeReceivedR=view.findViewById(R.id.txtTotalFeeReceivedR);
        txtTotalFeeDuesR=view.findViewById(R.id.txtTotalFeeYearDuesR);
        linearPackageDetails=view.findViewById(R.id.linearPackageDetails);

        txtCollegeFeeTotalD=view.findViewById(R.id.txtCollegeFeeDTotalVal);
        txtCollegeFeeFinal=view.findViewById(R.id.txtCollegeFeeDFinal);
        txtCollegeFeeReceivedD=view.findViewById(R.id.txtCollegeFeeDReceivedVal);
        txtCollegeFeeDueD=view.findViewById(R.id.txtCollegeFeeDDueVal);
        txtHostalTotalFeeD=view.findViewById(R.id.txtHostalFeeDTotalVal);
        txtHostalFinalFeeD=view.findViewById(R.id.txtHostalFeeFinalD);
        txtHostalReceivedFeeD=view.findViewById(R.id.txtHostalFeeDReceivedVal);
        txtHostalDueFeeD=view.findViewById(R.id.txtHostalFeeDDueVal);
        txtOTPTotalFeeD=view.findViewById(R.id.txtOTPFeeDTotalVal);
        txtOTPFeeFinalD=view.findViewById(R.id.txtOTPFeeDFinalVal);
        txtOTPReceivedFeeD=view.findViewById(R.id.txtOTPFeeDReceivedVal);
        txtOTPDueFeeD=view.findViewById(R.id.txtOTPFeeDDueVal);
        txtAccidentalTotal=view.findViewById(R.id.txtAccidentalTotalVal);
        txtAccidentalFinal=view.findViewById(R.id.txtAccidentalFinalVal);
        txtAccidentalReceived=view.findViewById(R.id.txtAccidentalReceivedVal);
        txtAccidentalDue=view.findViewById(R.id.txtAccidentalDueVal);
        txtMessTotal=view.findViewById(R.id.txtMessTotalVal);
        txtMessFinal=view.findViewById(R.id.txtMessFinalVal);
        txtMessReceived=view.findViewById(R.id.txtMessReceivedVal);
        txtMessDue=view.findViewById(R.id.txtMessDueVal);
        txtTotalFeeD=view.findViewById(R.id.txtTotalFeeD);
        txtFeeFinalD=view.findViewById(R.id.txtFeeFinalD);
        txtReceivedFeeD=view.findViewById(R.id.txtReceivedFeeD);
        txtDueFeeD=view.findViewById(R.id.txtDueFeeD);
        imgClose=view.findViewById(R.id.imgClose);

        txtTotalRecivedUSD=view.findViewById(R.id.txtTotalRecivedUSD);
        txtTotalRecivedINR=view.findViewById(R.id.txtTotalReceivedINR);
        recyclerPaymentHistory=view.findViewById(R.id.recyclerPaymentHistory);
        linearPaymentHistory=view.findViewById(R.id.linearPaymentHistory);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        //Adding our dialog box to the view of alert dialog
        alert.setView(view);
        //Creating an alert dialog
        alertDialog1 = alert.create();
        alertDialog1.show();
        alertDialog1.setCancelable(false);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

    }

    public void getPackageTotalDetails()
    {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=121&FileNo="+fileID;
            Log.d("PackageReceivedUrl", url);
            if (CheckInternet.checkInternet(context)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PackageReceivedResponse", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                    dialog.dismiss();
                                    if (response.contains("[]")) {
                                        // linearPackageDetails.setVisibility(View.GONE);
                                        txtTotalRecivedUSD.setText("Amount Received in USD:0");
                                        txtTotalRecivedINR.setText("Amount Received in INR:0");
                                        //  Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // linearPackageDetails.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            inr= jsonObject1.getString("INR");
                                            usd = jsonObject1.getString("USD");
                                        }
                                        txtTotalRecivedUSD.setText("Amount Received in USD:"+usd);
                                        txtTotalRecivedINR.setText("Amount Received in INR:"+inr);

                                       // getPackageReceivedDetail();


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
                                   // dialog.dismiss();
                                    if (response.contains("[]")) {
                                        linearPackageDetails.setVisibility(View.GONE);
                                        //  Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        linearPackageDetails.setVisibility(View.VISIBLE);
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
                                        int totalFeeD=Integer.parseInt(txtCollegeFeeTotalD.getText().toString())+Integer.parseInt(txtHostalTotalFeeD.getText().toString())+
                                                Integer.parseInt(txtOTPTotalFeeD.getText().toString())+Integer.parseInt(txtAccidentalTotal.getText().toString())+Integer.parseInt(txtMessTotal.getText().toString());
                                        txtTotalFeeD.setText(String.valueOf(totalFeeD));
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
                                        int totalFeeR=Integer.parseInt(txtAirFareFeeTotal.getText().toString())+Integer.parseInt(txtIndiaOTCTotal.getText().toString());

                                        txtTotalFeeTotalR.setText(String.valueOf(totalFeeR));
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

                                        //getPackageTotalDetails();

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
    public void getPaymentHistory()
    {
        try {
           String url = clienturl + "?clientid=" + clientid + "&caseid=125&FileNo="+fileID;
            Log.d("PaymentHistoryUrl", url);
            if (CheckInternet.checkInternet(context)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PaymentHistoryRes", response);
                                try {
                                    arrayListPaymentHistory=new ArrayList<>();
                                    //dialog.dismiss();
                                    getPackageTotalDetails();
                                    if (response.contains("[]")) {
                                        linearPaymentHistory.setVisibility(View.GONE);
                                        //  Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        linearPaymentHistory.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            paymentid= jsonObject1.getString("nPaymentID");
                                            amountINR = jsonObject1.getString("cAmountINR");
                                            amountUSD = jsonObject1.getString("cAmountUSD");
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("dtPaymentDate");
                                            String paymentDate;
                                            date1=jsonObject2.getString("date");
                                            paymentDate=date1.substring(0,date1.indexOf(" "));
                                            approved=jsonObject1.getString("IsApprovedbyAccountant");
                                            if(amountUSD.length()==0)
                                            {
                                                amountUSD="0";
                                            }
                                            if(amountINR.length()==0)
                                            {
                                                amountINR="0";
                                            }
                                            DataPaymentHistory dataPaymentHistory=new DataPaymentHistory(fileID,paymentid,amountUSD,amountINR,paymentDate,approved);
                                            arrayListPaymentHistory.add(dataPaymentHistory);
                                        }
                                        adapterPaymentHistory=new AdapterPaymentHistory(context,arrayListPaymentHistory);
                                        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerPaymentHistory.setLayoutManager(layoutManager);
                                        recyclerPaymentHistory.setAdapter(adapterPaymentHistory);
                                        adapterPaymentHistory.notifyDataSetChanged();
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
}
