package com.bizcall.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;

public class MailActiivity extends AppCompatActivity {


    EditText edtEmailAddress, edtEmailSubject, edtEmailText;
    TextView textImagePath;
    ImageView imgBack;
    String mailid;
    Button btnSelectImage, btnSendEmail_intent;
    final int RQS_LOADIMAGE = 0;
    Uri imageUri = null;
    ArrayList<Uri>  arrayList;
    Vibrator vibrator;
    ListView listViewImages;
    String url,clienturl,clientid,id1;
    SharedPreferences sp;
    RequestQueue requestQueue;
    ProgressDialog dialog;
    int flag=0;
    ImageView imgAttachment,imgSend;
    Spinner spinnerMailTemplate,spinnerSubjectTemplate;
    ArrayList<String> arrayMail;
    ArrayList<String> arraySubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        imgBack=findViewById(R.id.img_back);
        spinnerMailTemplate=findViewById(R.id.spinnerMailTemplate);
        spinnerSubjectTemplate=findViewById(R.id.spinnerSubjectTemplate);
        imgAttachment=findViewById(R.id.imgAttachment);
        imgSend=findViewById(R.id.imgSend);
        edtEmailAddress = (EditText) findViewById(R.id.email_address);
        edtEmailSubject = (EditText) findViewById(R.id.email_subject);
        edtEmailText = (EditText) findViewById(R.id.email_text);
        textImagePath = (TextView) findViewById(R.id.imagepath);
        btnSelectImage = (Button) findViewById(R.id.selectimage);
        btnSendEmail_intent = (Button) findViewById(R.id.btnSendEmail);
        arrayList=new ArrayList<>();

        sp=getSharedPreferences("settings",Context.MODE_PRIVATE);
        clientid=sp.getString("ClientId",null);
        clienturl=sp.getString("ClientUrl",null);
        id1=sp.getString("Id",null);

        mailid=getIntent().getStringExtra("Email");
        edtEmailAddress.setText(mailid);
        requestQueue=Volley.newRequestQueue(MailActiivity.this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
                }
        });
        arrayMail=new ArrayList<>();
        arraySubject=new ArrayList<>();
        arrayMail.add(0, "Select Mail body From Template");
        arrayMail.add(1,"Enquiry for MBBS in Ukraine");

        arraySubject.add(0, "Select Subject From Template");
        arraySubject.add(1, "Are you interested for doing MBBS in Ukraine?");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MailActiivity.this, R.layout.spinner_item1, arrayMail);
        spinnerMailTemplate.setAdapter(arrayAdapter);
        Log.d("Size**", String.valueOf(arrayMail.size()));
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(MailActiivity.this, R.layout.spinner_item1, arraySubject);
        spinnerSubjectTemplate.setAdapter(arrayAdapter1);

        spinnerSubjectTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {

                    String text = spinnerSubjectTemplate.getSelectedItem().toString();
                    edtEmailSubject.setText(text);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //isClicked = false;
            }
        });
        spinnerMailTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {

                    String text = spinnerMailTemplate.getSelectedItem().toString();
                    edtEmailText.setText(text);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //isClicked = false;
            }
        });
        btnSelectImage.setOnClickListener(buttonSelectImageOnClickListener);
        btnSendEmail_intent.setOnClickListener(buttonSendEmail_intentOnClickListener);
        imgSend.setOnClickListener(buttonSendEmail_intentOnClickListener);
        imgAttachment.setOnClickListener(buttonSelectImageOnClickListener);
    }
    View.OnClickListener buttonSelectImageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RQS_LOADIMAGE);
        }
    };

    View.OnClickListener buttonSendEmail_intentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            flag=0;
            if (edtEmailAddress.getText().toString().isEmpty()||!edtEmailAddress.getText().toString().matches(emailPattern) )
            {
                flag=1;
                edtEmailAddress.setError("Invalid address");
            }
            if(edtEmailSubject.getText().toString().isEmpty() )
            {
                flag=1;
                edtEmailSubject.setError("Invalid subject");
            }
            if(edtEmailText.getText().toString().isEmpty()) {
                flag=1;
                edtEmailText.setError("Invalid text");
                // Toast.makeText(MailActiivity.this, "Please enter valid info", Toast.LENGTH_SHORT).show();
            }
            if(flag==0)
            {
                    insertPointCollection(4);
                String emailAddress = edtEmailAddress.getText().toString();
                String emailSubject = edtEmailSubject.getText().toString();
                String emailText = edtEmailText.getText().toString();
                String emailAddressList[] = {emailAddress};


                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.putExtra(Intent.EXTRA_EMAIL, emailAddressList);
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, emailText);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");

              //  startActivity(Intent.createChooser(intent, "Send mail"));
                if (arrayList.isEmpty()) {
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                }else if(arrayList.size()==1){
                    Log.d("imageuri", imageUri + " ");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, arrayList.get(0));
                intent.setType("image/png");
                } else {
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_STREAM, arrayList);
                intent.setType("image/png");
                   // intent.setType("plain/text");
                }

                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQS_LOADIMAGE:
                    imageUri = data.getData();
                    arrayList.add(imageUri);
                    listViewImages=findViewById(R.id.listImages);
                    listViewImages.setVisibility(View.VISIBLE);
                    listViewImages.setAdapter(new ArrayAdapter<Uri>(MailActiivity.this,R.layout.adapter_listview,arrayList));
                    /*for(int i=0;i<arrayList.size();i++) {
                        String path11= String.valueOf(arrayList.get(i));
                        textImagePath.setText("\n"+path11);

                    }*/

                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MailActiivity.this,CounselorContactActivity.class);
        intent.putExtra("ActivityName","MailActivity");
        startActivity(intent);
    }
    public void insertPointCollection(int eid)
    {
        // String callId=sp.getString("SelectedCallingId",null);
        url=clienturl+"?clientid=" + clientid + "&caseid=36&nCounsellorId=" + id1 + "&nEventId="+eid;
        Log.d("PointCollectionUrl", url);
        if(CheckInternet.checkInternet(MailActiivity.this))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                Log.d("PointCollectionResponse", response);
                                if (response.contains("Data inserted successfully")) {
                                    Toast.makeText(MailActiivity.this, "Point inserted successfully", Toast.LENGTH_SHORT).show();
                                    //  dialog = ProgressDialog.show(CounselorContactActivity.this, "", "Inserting point...", true);
                                   // getPointCollection();
                                } else {
                                    Toast.makeText(MailActiivity.this, "Point not inserted", Toast.LENGTH_SHORT).show();
                                }
                                //   Log.d("Size**", String.valueOf(arrayList.size()));

                            }catch (Exception e)
                            {
                                Toast.makeText(MailActiivity.this,"Volley error while inserting point",Toast.LENGTH_SHORT);
                                dialog.dismiss();
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
                            if (error.networkResponse != null||error instanceof TimeoutError ||error instanceof NoConnectionError ||error instanceof AuthFailureError ||error instanceof ServerError ||error instanceof NetworkError ||error instanceof ParseError) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MailActiivity.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(MailActiivity.this,"Server Error",Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
        }else {
            dialog.dismiss();
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MailActiivity.this);
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

    }


}