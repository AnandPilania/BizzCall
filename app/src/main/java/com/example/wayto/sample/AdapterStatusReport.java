package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterStatusReport extends RecyclerView.Adapter<AdapterStatusReport.MyHolder> {
    ArrayList<DataStatusTotal> arrayList;
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    TextView txtCloseWindow,txtName;
    ImageView imgCloseWindow,imgClearText;
    EditText edtMobile,edtMessage;
    Spinner spinnerTemplate;
    ArrayList<String> arraySms;
    Button btnSms,btnWhatsapp;
    String msg,smbl,clientid,clienturl;
    AlertDialog alertDialog;
    boolean isClicked;
    Long timeout;


    public AdapterStatusReport(ArrayList<DataStatusTotal> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterStatusReport.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_statustc, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
       // sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        //id1 = sp.getString("Id", null);
        //id1 = id1.replaceAll(" ", "");
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        timeout=sp.getLong("TimeOut",0);
        Log.d("ClientId", clientid);
        final DataStatusTotal dataStatusTotal = arrayList.get(i);
        myHolder.txtTotal.setText(dataStatusTotal.getTotal());
        myHolder.txtStatus.setText(dataStatusTotal.getStatus());

        myHolder.txtSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("SStatusId", dataStatusTotal.getCurrentstatus());
                editor.putString("SCStatus", dataStatusTotal.getStatus());
                editor.putString("Count", dataStatusTotal.getTotal());
                editor.commit();


               // Intent intent = new Intent(context, .class);
                /*intent.putExtra("CStatusId",dataStatusTotal.getCurrentstatus());
                intent.putExtra("Count",dataStatusTotal.getTotal());
                intent.putExtra("Status",dataStatusTotal.getStatus());*/
                //context.startActivity(intent);

                LayoutInflater li = LayoutInflater.from(context);
                //Creating a view to get the dialog box
                View template = li.inflate(R.layout.layout_sms, null);
                 txtCloseWindow = template.findViewById(R.id.txtCloseWindow);
                 imgCloseWindow = template.findViewById(R.id.imgCloseWindow);
                // edtMobile = template.findViewById(R.id.edtMobileNo);
                 //txtName = template.findViewById(R.id.txtUserName1);
                 edtMessage = template.findViewById(R.id.edtMessage1);
                spinnerTemplate = template.findViewById(R.id.spinnerMsgTemplate);
                btnSms = template.findViewById(R.id.btnSMS);
               imgClearText = template.findViewById(R.id.imgClearMsg);
                btnWhatsapp = template.findViewById(R.id.btnWhatsapp);

                //int pos = viewHolder.getAdapterPosition();
                // DataCounselor dataCounselo = arrayList.get(pos);
                //String mobile,cname;
                // mobile=dataCounselo.getMobile();
                //  Log.d("Mbl",mobile);
                // cname=dataCounselo.getCname();
                //  Log.d("cname",cname);
                String cid = sp.getString("Id", null);
                Log.d("CID", cid);
                cid = cid.replace(" ", "");


                dialog = ProgressDialog.show(context, "Loading", "Please wait.....", false, true);
                arraySms = new ArrayList<>();
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(context);
                urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=7&nCounselorID=" + cid + "&IsActive=1");
                Log.d("TemplatUrl", clienturl+"?clientid=" + clientid + "&caseid=7&nCounselorID=" + cid + "&IsActive=1");
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d("TemplateResponse", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("Json", jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                              String  sms = jsonObject1.getString("cSmsText");
                                arraySms.add(sms);
                            }
                            arraySms.add(0, "Select Message From Template");
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.spinner_item1, arraySms);

                            // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTemplate.setAdapter(arrayAdapter);
                            Log.d("Size**", String.valueOf(arraySms.size()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // String msg=response.substring(response.indexOf( "( [cSmsText] =>"+15),response.indexOf(")"));


                    }
                });
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                //Adding our dialog box to the view of alert dialog
                alert.setView(template);
                //Creating an alert dialog
                alertDialog = alert.create();
                alertDialog.show();
                imgCloseWindow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });
                imgClearText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtMessage.getText().clear();
                    }
                });

                spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {

                            String text = spinnerTemplate.getSelectedItem().toString();
                            edtMessage.setText(text);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        isClicked = false;
                    }
                });

              /*  btnWhatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        msg = edtMessage.getText().toString();
                        //smbl = edtMobile.getText().toString();
                        //int pos = viewHolder.getAdapterPosition();
                        //  DataCounselor dataCounselor1 = arrayList.get(pos);

                        if (msg.length() != 0) {
                            try {
                                //setSmsEntry();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + smbl + "&text=" + msg));
                                context.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            edtMessage.setError("Please enter message");
                        }
                    }

                });
                btnSms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        smbl = edtMobile.getText().toString();
                        msg = edtMessage.getText().toString();

                        if (msg.length() != 0) {
                           // setSmsEntry();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + smbl));
                            intent.putExtra("sms_body", msg);
                            context.startActivity(intent);

                        } else {
                            edtMessage.setError("Please enter message");
                        }
                    }
                });*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtTotal, txtStatus,txtSms;
        ImageView imgStatus;
        LinearLayout linearStatus;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtTotal = itemView.findViewById(R.id.txtStatusCountR);
            txtSms = itemView.findViewById(R.id.txtSMSR);
            txtStatus = itemView.findViewById(R.id.txtStatusR);
            linearStatus = itemView.findViewById(R.id.linearStatus);
        }
    }
}
