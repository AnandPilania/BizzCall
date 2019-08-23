package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ActivityUploadDocs extends AppCompatActivity {

   Spinner mSpinnerDocuments;
   LinearLayout linearLayoutBrowse;
   Button mBtnBrowse,mBtnUpload,mBtnReset;
   TextView txtFileName;
   RequestQueue requestQueue;
   SharedPreferences sp;
   String temp;
   RecyclerView recyclerUploadedImage;
   AdapterUploadedImage adapterUploadedImage;
   String clienturl,clientid,sharedSrno,strSelectedDoc,docid;
    long filesize;
    ArrayList<String> documentIDArrayList;
    ArrayList<String> documentNameArrayList;

    RecyclerView recyclerViewUploadedDocs;
    DataUploadDocs detailUploadedDocs;
    ArrayList<DataUploadDocs> mDetailUploadedDocs;
    AdapterUploadedDocs adapterUploadedDocs;
    ProgressDialog progressDialog;
    int RESULT_LOAD_IMAGE = 1;
    int flag = 0;
    File f;

    String upLoadServerUri;
    int serverResponseCode = 0;
    long spinnerposition;
    String strMobile,counselorid;
    String[] filePathColumn;
    String uploadFilePath;
    String uploadFileName,strName;
    ImageView imgBack;
    TextView txtNoDocuments,txtFileNo,txtMobileNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uplaod_docs);
        try {
            requestQueue = Volley.newRequestQueue(ActivityUploadDocs.this);

            // Log.d("FileNo",sharedSrno);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clienturl = sp.getString("ClientUrl", null);
            clientid = sp.getString("ClientId", null);
            counselorid = sp.getString("Id", null);
            counselorid = counselorid.replace(" ", "");
            strName = sp.getString("FullName", null);
            sharedSrno = getIntent().getStringExtra("FileNo");
            strMobile = getIntent().getStringExtra("MobileNo");
            mSpinnerDocuments = findViewById(R.id.spinnerCategories);
            linearLayoutBrowse = findViewById(R.id.LinearBrowse);
            txtFileNo = findViewById(R.id.txtFileNo);
            txtMobileNo = findViewById(R.id.txtMobileNo);

            recyclerViewUploadedDocs = findViewById(R.id.recycler_uploadeddocs);
            recyclerUploadedImage = findViewById(R.id.recycler_uploadeddocs_image);
            imgBack = findViewById(R.id.img_back);
            mBtnBrowse = findViewById(R.id.btnBrowse);
            mBtnUpload = findViewById(R.id.btnUpload);
            mBtnReset = findViewById(R.id.btnReset);
            txtFileName = findViewById(R.id.txtFileName);
            txtNoDocuments = findViewById(R.id.txtNoDocument);
            documentIDArrayList = new ArrayList<>();
            documentNameArrayList = new ArrayList<>();
            txtFileNo.setText(sharedSrno + ". " + strName);
            txtMobileNo.setText(strMobile);

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        /* progressDialog = new ProgressDialog(ActivityUploadDocs.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Wait while getting document list...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();*/

            if (CheckInternetSpeed.checkInternet(ActivityUploadDocs.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // progressDialog.dismiss();
                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(ActivityUploadDocs.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // progressDialog.dismiss();
                            }
                        }).show();
            } else {
                String strDocumentList = clienturl + "?clientid=" + clientid + "&caseid=D7";
                Log.d("doclisturl", strDocumentList);
                // progressDialog=ProgressDialog.show(ActivityUploadDocs.this,"","Getting spinner data",true);
                progressDialog=ProgressDialog.show(ActivityUploadDocs.this,"","Getting List of Uploaded Document",false,true);

                loadSpinnerData(strDocumentList);
            }

            mBtnBrowse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    mBtnUpload.setVisibility(View.VISIBLE);
                    mBtnBrowse.setVisibility(View.GONE);
                }
            });

            mBtnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 0;
                    if (txtFileName.length() == 0) {
                        txtFileName.setError("");
                        mBtnUpload.setVisibility(View.GONE);
                        mBtnBrowse.setVisibility(View.VISIBLE);
                        flag = 1;
                    }
                    if (flag == 0) {
                        if (CheckInternetSpeed.checkInternet(ActivityUploadDocs.this).contains("0")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
                            alertDialogBuilder.setTitle("No Internet connection!!!")
                                    .setMessage("Can't do further process")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else if (CheckInternetSpeed.checkInternet(ActivityUploadDocs.this).contains("1")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
                            alertDialogBuilder.setTitle("Slow Internet speed!!!")
                                    .setMessage("Can't do further process")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            // progressDialog=ProgressDialog.show(ActivityUploadDocs.this,"","Uploading document",true);
                            getSingleName();

                        }
                    }
                }
            });

            mBtnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSpinnerDocuments.setEnabled(true);
                    mBtnBrowse.setVisibility(View.VISIBLE);
                    mBtnUpload.setVisibility(View.GONE);
                    txtFileName.setText("");
                }
            });

       /* strSelectedDoc = mSpinnerDocuments.getSelectedItem().toString();
        int pos = mSpinnerDocuments.getSelectedItemPosition();
        docid = documentIDArrayList.get(pos);*/

            mSpinnerDocuments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    strSelectedDoc = mSpinnerDocuments.getItemAtPosition(mSpinnerDocuments.getSelectedItemPosition()).toString();
                    docid = documentIDArrayList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // DO Nothing here
                    mSpinnerDocuments.setSelection(0);
                }
            });
        }catch (Exception e)
        {
            Log.d("ExcOncreateUploadDocs",e.toString());
            Toast.makeText(ActivityUploadDocs.this,"Errorcode-332 UploadDocs onCreate "+e.toString(),Toast.LENGTH_SHORT).show();        }
    }//onCreate
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ActivityUploadDocs.this,SummaryDetails.class);
        intent.putExtra("FileNo",sharedSrno);
        intent.putExtra("MobileNo",strMobile);
        startActivity(intent);
    }

public void sendNotification()
{
    //------------------------------Notification to counselor--------------------------------
    try{
    String strNotify = sharedSrno+"_Client_uploaded_"+txtFileName.getText().toString()+"_Document.";
    String strurl3 = clienturl + "?clientid="+sharedSrno + "&caseid=62&SrNo=" + sharedSrno + "&CounselorId=" + counselorid +
            "&Notification=" + strNotify;
    if(CheckServer.isServerReachable(ActivityUploadDocs.this)) {
        Log.d("newqualification", strurl3);
        StringRequest stringRequest4 = new StringRequest(Request.Method.GET, strurl3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("ChatResponse", response);
                if (response.contains("Data inserted successfully")) {
                    //dialog.dismiss();
                    Toast.makeText(ActivityUploadDocs.this, "Notification sent to Counselor.", Toast.LENGTH_SHORT).show();
                    uploadedDocName();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityUploadDocs.this, "Errorcode-334 UploadDocs NotificationResponse " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("VolleyError", String.valueOf(error));
            }
        });

        stringRequest4.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        stringRequest4.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest4);
    }else
    {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
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
        Log.d("ExcSendNotification",e.toString());
        Toast.makeText(ActivityUploadDocs.this,"Errorcode-333 UploadDocs sendNotification "+e.toString(),Toast.LENGTH_SHORT).show();
    }
}

public void getSingleName()
{
    try{
        if(CheckServer.isServerReachable(ActivityUploadDocs.this)) {
            String strurl4 = clienturl + "?clientid=" + clientid + "&caseid=D91&SrNo=" + sharedSrno + "&docid=" + docid;
            Log.d("newqualification", strurl4);

            StringRequest stringRequest3 = new StringRequest(Request.Method.GET, strurl4, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("Data inserted successfully")) {
                        uploadDoc();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ActivityUploadDocs.this, "Errorcode-336 UploadDocs getSingleNameResponse " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", String.valueOf(error));
                }
            });

            stringRequest3.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                }
            });
            stringRequest3.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest3);
        }else {
          /*  if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
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
        Log.d("ExcSingleName",e.toString());
        Toast.makeText(ActivityUploadDocs.this,"Errorcode-335 UploadDocs getSingleName "+e.toString(),Toast.LENGTH_SHORT).show();
    }
}
public void uploadDoc()
{
    try{
        if(CheckServer.isServerReachable(ActivityUploadDocs.this)) {
            String strinserturl = clienturl + "?clientid=" + clientid + "&caseid=D9&SrNo=" + sharedSrno + "&docid=" + docid + "&docname=" + txtFileName.getText().toString();
            Log.d("insertdoc", strinserturl);
            StringRequest stringinsertdoc = new StringRequest(Request.Method.GET, strinserturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("Data inserted successfully")) {

                        progressDialog = new ProgressDialog(ActivityUploadDocs.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Document Uploading...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(100);
                        progressDialog.show();

                        new Thread(new Runnable() {
                            public void run() {
                                upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=D8" + "&filename=" + txtFileName.getText().toString();
                                Log.d("myurl", upLoadServerUri);
                                uploadFile(uploadFilePath + "" + uploadFileName);
                                uploadFileName = null;
                            }
                        }).start();
                        sendNotification();
                    } else {
                        Toast.makeText(ActivityUploadDocs.this, "Document uploading failed.", Toast.LENGTH_SHORT).show();
                       // progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ActivityUploadDocs.this, "Errorcode-338 UploadDocs uploadDocResponse " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", String.valueOf(error));
                }
            });
            requestQueue.add(stringinsertdoc);
        }else {
            /*if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
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
        Log.d("ExcUploadDocs",e.toString());
        Toast.makeText(ActivityUploadDocs.this,"Errorcode-337 UploadDocs uploadDoc "+e.toString(),Toast.LENGTH_SHORT).show();
    }
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
        int dataSize=0;
        if(resultCode==RESULT_CANCELED)
        {
            mSpinnerDocuments.setClickable(true);
            mSpinnerDocuments.setEnabled(true);
        }
        else if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            mSpinnerDocuments.setClickable(false);
            mSpinnerDocuments.setEnabled(false);
            Uri selectedImage = data.getData();
            String scheme = selectedImage.getScheme();
            filePathColumn = new String[]{MediaStore.Images.Media.DATA};

            String path = getPath(getApplicationContext(), selectedImage);

            System.out.println("Scheme type " + scheme);
            if(scheme.equals(ContentResolver.SCHEME_CONTENT))
            {
                try {
                    InputStream fileInputStream=getApplicationContext().getContentResolver().openInputStream(selectedImage);
                    dataSize = fileInputStream.available();
                } catch (Exception e) {
                    e.printStackTrace();
                }
              Log.d("FileSize",dataSize+"");
                filesize=dataSize;

            }
            else if(scheme.equals(ContentResolver.SCHEME_FILE))
            {
                try {
                    f = new File(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
              //  Log.d("FileSize*",f.length()+"");
                filesize=f.length();
            }
            String files=getFileSize(filesize);
            //Log.d("FileSizeMB",files);


            uploadFilePath = path.substring(path.indexOf("/storage"), path.lastIndexOf("/") + 1);
          //  Log.d("Path", uploadFilePath);

            uploadFileName = path.substring(uploadFilePath.lastIndexOf("/") + 1);
            //String secondString = "1_" + item + uploadFileName.substring(uploadFileName.lastIndexOf("."));
            //String secondString = sharedSrno + item + uploadFileName.substring(uploadFileName.lastIndexOf("."));

            String secondString = sharedSrno + "_" + strSelectedDoc;
            spinnerposition = (mSpinnerDocuments.getSelectedItemId());
/*
            Log.d("spinnposition", (mSpinnerDocuments.getSelectedItemId() + 1) + "");
            Log.d("second string", secondString);
            Log.d("FileName", uploadFileName);*/

            txtFileName.setText(secondString);

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            cursor.close();

            try {
                Bitmap bmp = getBitmapFromUri(selectedImage);
                //imgCamera.setImageBitmap(bmp);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // ImageView.setImageBitmap(bmp);
        }
        }catch (Exception e)
        {
            Log.d("ExcOnActvtyRsltUpldDocs",e.toString());
            Toast.makeText(ActivityUploadDocs.this,"Errorcode-339 UploadDocs onActivityResult "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
  //************************Get File Name From Gallery************************End

    private void loadSpinnerData(String url) {
        //requestQueue = Volley.newRequestQueue(ActivityUploadDocs.this);
        try {
            if(CheckServer.isServerReachable(ActivityUploadDocs.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("success") == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String strDocId = jsonObject1.getString("nDocumentID");
                                    String strDocSelect = jsonObject1.getString("cDocument");
                                    documentIDArrayList.add(strDocId);
                                    documentNameArrayList.add(strDocSelect);
                                }
                              //  Log.d("DocListSize", documentIDArrayList.size() + "");
                            }
                            temp = "Oncreate";
                            requestQueue = Volley.newRequestQueue(ActivityUploadDocs.this);
                            uploadedDocName();
                            mSpinnerDocuments.setAdapter(new ArrayAdapter<String>(ActivityUploadDocs.this, android.R.layout.simple_spinner_dropdown_item, documentNameArrayList));
                            //dialog.dismiss();
                        } catch (JSONException e) {
                            Toast.makeText(ActivityUploadDocs.this,"Errorcode-339 UploadDocs SpinnerDataResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);
            }else {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
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
            Toast.makeText(ActivityUploadDocs.this,"Errorcode-339 UploadDocs loadSpinnerData "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void uploadedDocName(){
        try {
            if(CheckServer.isServerReachable(ActivityUploadDocs.this)) {
                String uploadedDocUrl = clienturl + "?clientid=" + clientid + "&caseid=D10&SrNo=" + sharedSrno;
                Log.d("dd10", uploadedDocUrl);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, uploadedDocUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        mDetailUploadedDocs = new ArrayList<>();
                        mDetailUploadedDocs.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (response.contains("[]")) {
                                txtNoDocuments.setVisibility(View.VISIBLE);
                                Toast.makeText(ActivityUploadDocs.this, "No documents uploaded yet", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String uploadedDocId = jsonObject1.getString("nDocumentID");
                                    String uploadedDocSelect = jsonObject1.getString("cDocumentName");

                                    detailUploadedDocs = new DataUploadDocs(uploadedDocId, uploadedDocSelect);
                                    mDetailUploadedDocs.add(detailUploadedDocs);
                                }
                                adapterUploadedDocs = new AdapterUploadedDocs(mDetailUploadedDocs, ActivityUploadDocs.this);
                                LinearLayoutManager lm = new LinearLayoutManager(ActivityUploadDocs.this);
                                lm.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerViewUploadedDocs.addItemDecoration(new DividerItemDecoration(ActivityUploadDocs.this, DividerItemDecoration.VERTICAL));
                                recyclerViewUploadedDocs.setLayoutManager(lm);
                                recyclerViewUploadedDocs.setAdapter(adapterUploadedDocs);

                                adapterUploadedImage = new AdapterUploadedImage(mDetailUploadedDocs, ActivityUploadDocs.this);
                                recyclerUploadedImage.setLayoutManager(new GridLayoutManager(ActivityUploadDocs.this, 2));
                                recyclerUploadedImage.setAdapter(adapterUploadedImage);
                                adapterUploadedImage.notifyDataSetChanged();
                                adapterUploadedDocs.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            Toast.makeText(ActivityUploadDocs.this, "Errorcode-341 UploadDocs uploadedDocNameResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);
            }else {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
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
            Toast.makeText(ActivityUploadDocs.this,"Errorcode-340 UploadDocs uploadedDocName "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    public int uploadFile(String sourceFileUri) {

        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.exists()) {
            Log.d("uploadFileNotExist", "Source File not exist :" + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ActivityUploadDocs.this, "Source File not exist :"
                            + uploadFilePath + "" + uploadFileName, Toast.LENGTH_SHORT).show();
                }
            });
            return 0;
        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri); //url remaining.............................

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + fileName + "" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            txtFileName.setText("");
                            progressDialog.dismiss();

                                //finish();
                                Intent intent = new Intent(ActivityUploadDocs.this, ActivityUploadDocs.class);
                                intent.putExtra("FileNo", sharedSrno);
                                intent.putExtra("MobileNo",strMobile);
                                startActivity(intent);


                            Toast.makeText(ActivityUploadDocs.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(ActivityUploadDocs.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Uploadfiletoserver", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                //dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(ActivityUploadDocs.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Exception", "Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;
        } // End else block
    }



}
