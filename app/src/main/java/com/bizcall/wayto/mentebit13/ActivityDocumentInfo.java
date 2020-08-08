package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ActivityDocumentInfo extends AppCompatActivity {

    String fileno,mobile,strDocname,strDocID,ext,docCol1,docCol2,docCol3,docCol4,docCol5,docCol6,docCol7,docValue1,
            docValue2,docValue3,docValue4,docValue5,docValue6,docValue7;
    EditText edtSub1Value,edtSub2Value,edtSub3Value,edtSub4Value,edtSub5Value,edtCol1Value,edtCol2Value;
    EditText edtSub1Name,edtSub2Name,edtSub3Name,edtSub4Name,edtSub5Name,edtCol1Name,edtCol2Name;
    LinearLayout linearUnderImgAdd,linearDoc1,linearDoc2,linearDoc3,linearDoc4,linearDoc5,linearDoc6,linearDoc7;
   TextView txtAddInfo;
   Spinner spinnerDocProperties;
   ArrayList<String> arrayListDocProperties;
   TextView txtDocName,txtSubmit;
   int count=0;
   RequestQueue requestQueue;
   ProgressDialog dialog;
   SharedPreferences sp;
   String clienturl,clientid,clienturl1,strImageUrl;
   long timeout=0;
   Thread thread;
   ImageView imgPreview,imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_info);
        initialize();
        deleteCache(ActivityDocumentInfo.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        ext = getIntent().getStringExtra("Ext");
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        timeout=sp.getLong("TimeOut",0);
        requestQueue= Volley.newRequestQueue(ActivityDocumentInfo.this);
        strDocname=getIntent().getStringExtra("DocName");
        strDocID=getIntent().getStringExtra("DocID");
        fileno=getIntent().getStringExtra("FileNo");
        mobile=getIntent().getStringExtra("Mobile");
        arrayListDocProperties=new ArrayList<>();
        arrayListDocProperties=new ArrayList<>();
        arrayListDocProperties.add("PassPort_IssueDate"+"_"+strDocname);
        arrayListDocProperties.add("Passport_ValidUPTO"+"_"+strDocname);
        arrayListDocProperties.add("Passport_No"+"_"+strDocname);
        arrayListDocProperties.add("Passport_IssuingAuthority"+"_"+strDocname);
        arrayListDocProperties.add("Passport_IssuingAuthority"+"_"+strDocname);
        arrayListDocProperties.add("Passport_Value1"+"_"+strDocname);
        arrayListDocProperties.add("Passport_Value2"+"_"+strDocname);
        arrayListDocProperties.add("Passport_Value3"+"_"+strDocname);
        txtDocName.setText(strDocname+"."+ext);
        clienturl1 = clienturl.substring(0, clienturl.lastIndexOf("/"));
        strImageUrl = clienturl1 + "/upload-empdoc/" + strDocname+"."+ext;
        Log.d("Imageurl",strImageUrl);

        if(ext.equals("pdf"))
        {
            imgPreview.setImageResource(R.drawable.pdf);
        }
        else if(ext.equals("doc"))
        {
            imgPreview.setImageResource(R.drawable.doc);
        }
        else if(ext.equals("xlsx")||ext.equals("xls"))
        {
            imgPreview.setImageResource(R.drawable.excel);
        }
        else
        {
            Picasso.with(ActivityDocumentInfo.this).load(strImageUrl).into(imgPreview);
        }

        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(ActivityDocumentInfo.this,
                android.R.layout.simple_spinner_dropdown_item, arrayListDocProperties);
        spinnerDocProperties.setAdapter(mSpinnerAdapter);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docValue1=edtSub1Value.getText().toString();
                docValue2=edtSub2Value.getText().toString();
                docValue3=edtSub3Value.getText().toString();
                docValue5=edtSub4Value.getText().toString();
                docValue6=edtSub5Value.getText().toString();
                docValue7=edtCol1Value.getText().toString();
                docValue4=edtCol2Value.getText().toString();

                docCol1=edtSub1Name.getText().toString();
                docCol2=edtSub2Name.getText().toString();
                docCol3=edtSub3Name.getText().toString();
                docCol4=edtSub4Name.getText().toString();
                docCol5=edtSub5Name.getText().toString();
                docCol6=edtCol1Name.getText().toString();
                docCol7=edtCol2Name.getText().toString();

                dialog=ProgressDialog.show(ActivityDocumentInfo.this,"","Updating document info",true);
                newThreadInitilization(dialog);
                submitDocInfo();
            }
        });



        txtAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                linearUnderImgAdd.setVisibility(View.VISIBLE);
                // linearSub1.setVisibility(View.VISIBLE);
                if(count==1)
                {
                    linearDoc1.setVisibility(View.VISIBLE);
                    //  txtSub2.setText(spinnerSubject.getSelectedItem().toString());
                }
                if(count==2)
                {
                    linearDoc2.setVisibility(View.VISIBLE);
                    //  txtSub2.setText(spinnerSubject.getSelectedItem().toString());
                }
                if(count==3)
                {
                    linearDoc3.setVisibility(View.VISIBLE);
                    // txtSub3.setText(spinnerSubject.getSelectedItem().toString());
                }
                if(count==4)
                {
                    linearDoc4.setVisibility(View.VISIBLE);
                    // txtSub3.setText(spinnerSubject.getSelectedItem().toString());
                }
                if(count==5)
                {
                    linearDoc5.setVisibility(View.VISIBLE);
                    // txtSub3.setText(spinnerSubject.getSelectedItem().toString());
                }
                if(count==6)
                {
                    linearDoc6.setVisibility(View.VISIBLE);
                    // txtSub3.setText(spinnerSubject.getSelectedItem().toString());
                }
                if(count==7)
                {
                    linearDoc7.setVisibility(View.VISIBLE);
                    // txtSub3.setText(spinnerSubject.getSelectedItem().toString());
                }
            }
        });
        spinnerDocProperties.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String subject=spinnerDocProperties.getSelectedItem().toString();
                if(count==1)
                {
                    edtSub1Name.setText(subject);
                }
                if(linearDoc2.getVisibility()== View.VISIBLE)
                {
                    if(count==2) {
                        edtSub2Name.setText(subject);
                    }
                }
                if(linearDoc3.getVisibility()== View.VISIBLE)
                {
                    if(count==3) {
                        edtSub3Name.setText(subject);
                    }
                }
                if(linearDoc4.getVisibility()== View.VISIBLE)
                {
                    if(count==4) {
                        edtSub4Name.setText(subject);
                    }
                }
                if(linearDoc5.getVisibility()== View.VISIBLE)
                {
                    if(count==5) {
                        edtSub5Name.setText(subject);
                    }
                }
                if(linearDoc6.getVisibility()== View.VISIBLE)
                {
                    if(count==6) {
                        edtCol1Name.setText(subject);
                    }
                }
                if(linearDoc7.getVisibility()== View.VISIBLE)
                {
                    if(count==7) {
                        edtCol2Name.setText(subject);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ActivityDocumentInfo.this,ActivityUploadDocs.class);
        intent.putExtra("Activity", "DocInfo");
        intent.putExtra("FileNo",fileno);
        intent.putExtra("MobileNo", mobile);
        // uploaddocs.putExtra("Email",strMail);
       // intent.putExtra("Name",strName);
        startActivity(intent);
    }

    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(ActivityDocumentInfo.this, "Connection aborted", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(Home.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Log.d("TimeThread","cdvmklmv");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    public void submitDocInfo()
    {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=A83&DocCol1="+docCol1+"&DocValue1="+docValue1+"&DocCol2="+docCol2+"&DocValue2="+docValue2+"&DocCol3="+docCol3+"&DocValue3="+docValue3+"&DocCol4="+docCol4+"&DocValue4="+docValue4+"&DocCol5="+docCol5+"&DocValue5="+docValue5+"&DocCol6="+docCol6+"&DocValue6="+docValue6+"&DocCol7="+docCol7+"&DocValue7="+docValue7+"&DocID="+strDocID;
            Log.d("DocInfoUrl", url);
            if (CheckInternet.checkInternet(ActivityDocumentInfo.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("DocInfoResponse", response);
                                try {
                                    if(response.contains("Data inserted successfully"))
                                    {

                                    }


                                } catch (Exception e) {
                                    Toast.makeText(ActivityDocumentInfo.this,"Errorcode-443 ClientAccount submitCollegeResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityDocumentInfo.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ActivityDocumentInfo.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityDocumentInfo.this);
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
            Toast.makeText(ActivityDocumentInfo.this,"Errorcode-442 ClientAccount submitCollege "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//submitCollege
    public void initialize()
    {
        edtSub1Value=findViewById(R.id.edtDocValue1);
        edtSub2Value=findViewById(R.id.edtDocValue2);
        edtSub3Value=findViewById(R.id.edtDocValue3);
        edtSub4Value=findViewById(R.id.edtDocValue4);
        edtSub5Value=findViewById(R.id.edtDocValue5);
        edtCol1Value=findViewById(R.id.edtDocValue6);
        edtCol2Value=findViewById(R.id.edtDocValue7);

        imgBack=findViewById(R.id.img_back);
        imgPreview=findViewById(R.id.imgPreview);
        txtDocName=findViewById(R.id.txtDocName);
        txtSubmit=findViewById(R.id.txtSubmit);
        edtSub1Name=findViewById(R.id.edtDocCol1);
        edtSub2Name=findViewById(R.id.edtDocCol2);
        edtSub3Name=findViewById(R.id.edtDocCol3);
        edtSub4Name=findViewById(R.id.edtDocCol4);
        edtSub5Name=findViewById(R.id.edtDocCol5);
        edtCol1Name=findViewById(R.id.edtDocCol6);
        edtCol2Name=findViewById(R.id.edtDocCol7);

        linearDoc1=findViewById(R.id.linearDoc1);
        linearDoc2=findViewById(R.id.linearDoc2);
        linearDoc3=findViewById(R.id.linearDoc3);
        linearDoc4=findViewById(R.id.linearDoc4);
        linearDoc5=findViewById(R.id.linearDoc5);
        linearDoc6=findViewById(R.id.linearDoc6);
        linearDoc7=findViewById(R.id.linearDoc7);
        spinnerDocProperties=findViewById(R.id.spinnerFileProperties);
        txtAddInfo=findViewById(R.id.txtAddInfo);
        linearUnderImgAdd=findViewById(R.id.linearUnderImgAdd);

    }
}
