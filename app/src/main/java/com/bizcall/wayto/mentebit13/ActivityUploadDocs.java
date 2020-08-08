package com.bizcall.wayto.mentebit13;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.BuildConfig;
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

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import in.gauriinfotech.commons.Commons;

public class ActivityUploadDocs extends AppCompatActivity {

    ProgressDialog dialog1;
   Spinner mSpinnerDocuments;
   LinearLayout linearLayoutBrowse;
   Button mBtnBrowse,mBtnUpload,mBtnReset;
   TextView txtFileName,txtMailId;
   RequestQueue requestQueue;
   SharedPreferences sp;
   String temp;
   RecyclerView recyclerUploadedImage;
   AdapterUploadedImage adapterUploadedImage;
   String clienturl,clientid,sharedSrno;
    long filesize;
    ArrayList<String> documentIDArrayList;
    ArrayList<String> documentNameArrayList;
    Thread thread;
    long timeout;

    RecyclerView recyclerViewUploadedDocs;
    DataUploadDocs detailUploadedDocs;
    ArrayList<DataUploadDocs> mDetailUploadedDocs;
    AdapterUploadedDocs adapterUploadedDocs;
    ProgressDialog progressDialog,dialog;;
    int RESULT_LOAD_IMAGE = 1;
    int flag = 0;
    File f;

    String upLoadServerUri;
    int serverResponseCode = 0;
    long spinnerposition;
    String uploadFilePath;
    String uploadFileName,strName,ext;
    String strMobile,counselorid,counselorname,mailid,directory_path,fileName;
    String[] filePathColumn;

    ImageView imgBack,imgRefresh;

    String strDocumentList,caseid,strDocId,strDocID1,secondString,strinserturl,strSelectedDoc,docid;;
    public static String activityname;
    SharedPreferences.Editor editor;
    TextView txtNoDocuments,txtFileNo,txtMobileNo;
    TableLayout table_alldetails;
    TextView txtImagePreview,txtAllDocument;
    Button btnEmailAttachements;

    public static ArrayList<String> attachmentArrayList;
    ArrayList<Uri> attachmentPath;
    private AsyncTask mMyTask;
    Uri savedImageURI, path;
    private NotificationManager mNotifyManager;
    File file, file1;
    Handler handler = new Handler();
    String clienturl1,subject,strDocName,strUrl,btnClick="",mail;
    int notFountCount=0,found=0,entered=0,entered1=0;
////////////////////////////////////This activity is used 2 times first to upload client document and second to upload own user documents
@Override
    protected void onCreate(Bundle savedInstanceState) {
    sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
    editor=sp.edit();
    activityname=sp.getString("Activity","");
    if(activityname.equals("CollegeActivity"))
    {
        setTheme(R.style.Theme_AppCompat_Light_Dialog_Alert);
    }
        super.onCreate(savedInstanceState);

       // setContentView(R.layout.activity_uplaod_docs);
        setContentView(R.layout.activity_uplaod_docs);

        try {
            initialize();

            editor.putString("ActName","UploadDocs");
            editor.commit();
            deleteCache(ActivityUploadDocs.this);
            btnEmailAttachements.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
                                })
                                .show();
                    } else {

                        attachmentPath = new ArrayList<Uri>();
                        entered = 0;
                        notFountCount = 0;
                        clienturl1 = clienturl.substring(0, clienturl.lastIndexOf("/"));
                        mail = sp.getString("ClientEmail", null);
                        subject = "Candidate Document";
                        // btnClick = "Attachment";
                        Log.d("attachmentArrayList", attachmentArrayList.size() + "");
                        for (int i = 0; i < attachmentArrayList.size(); i++) {
                            dialog1 = ProgressDialog.show(ActivityUploadDocs.this,"","Downloading documents",true);
                            dialog1.setCancelable(true);

                            if(i==attachmentArrayList.size())
                            {
                                dialog1.dismiss();
                            }

                            strDocName = attachmentArrayList.get(i);
                            ext = strDocName.substring(strDocName.lastIndexOf(".") + 1);
                            Log.d("qq", strDocName + " / " + ext);
                            if (ext.equalsIgnoreCase("pdf")||ext.equalsIgnoreCase("doc")||ext.equalsIgnoreCase("docx")||ext.equalsIgnoreCase("xslx")||ext.equalsIgnoreCase("xsl")) {
                                File directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs");
                                directory.mkdirs();
                                strUrl = clienturl1 + "/upload-empdoc/" + strDocName;
                                Log.d("docdata", strUrl + " / " + mail);
                                // Create a file to save the image
                                file = new File(directory, strDocName);
                                new Thread(new Runnable() {
                                    public void run() {
                                        DownloadFile(strUrl, file, strDocName);
                                    }
                                }).start();
                            }
                            else {
                                strUrl = clienturl1 + "/upload-empdoc/" + strDocName;
                                mMyTask = new ActivityUploadDocs.DownloadTask().execute(stringToURL(strUrl));
                            }
                        }
                        //   Log.d("Entered", entered + "");

                        for (int i = 0; i < attachmentArrayList.size(); i++) {
                            strDocName = attachmentArrayList.get(i);
                            File directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs");
                            directory.mkdirs();
                            file1 = new File(directory, strDocName);
                            path = FileProvider.getUriForFile(ActivityUploadDocs.this, ActivityUploadDocs.this.getApplicationContext().getPackageName() + ".provider", file1);
                            Log.d("attachpath", path + "" + entered);
                            attachmentPath.add(path);
                        }
                        //   Log.d("NotFound", notFountCount + "");

                    }

                    //  Log.d("Entered", entered + "");
                    if(attachmentArrayList.size()==0)
                    { Toast.makeText(ActivityUploadDocs.this, "Please select file to send", Toast.LENGTH_SHORT).show();
                    }
                    dialog = new ProgressDialog(ActivityUploadDocs.this);
                    dialog.setCancelable(true);
                    dialog.setMessage("Downloading Documents...");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setProgress(0);
                    dialog.setMax(100);
                    //dialog.show();
                }


            });
            txtAllDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(table_alldetails.getVisibility()==View.VISIBLE)
                    {
                        table_alldetails.setVisibility(View.GONE);
                    }else {
                        table_alldetails.setVisibility(View.VISIBLE);
                    }
                }
            });
            txtImagePreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerUploadedImage.getVisibility()==View.VISIBLE)
                    {
                        recyclerUploadedImage.setVisibility(View.GONE);
                    }
                    else {
                        recyclerUploadedImage.setVisibility(View.VISIBLE);
                    }
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityUploadDocs.this, ActivityUploadDocs.class);
                    intent.putExtra("Activity",activityname);
                    if(!activityname.equals("EmpDocs"))
                    {
                        intent.putExtra("FileNo",sharedSrno);
                        intent.putExtra("MobileNo",strMobile);
                    }
                    startActivity(intent);
                    finish();
                }
            });


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

                if(activityname.equals("EmpDocs")) {
                    txtFileNo.setText(counselorid + ". " +counselorname );
                   caseid="155";

                }
                else {
                    strName = sp.getString("FullName", null);
                    sharedSrno = getIntent().getStringExtra("FileNo");
                    strMobile = getIntent().getStringExtra("MobileNo");
                    txtFileNo.setText(sharedSrno + ". " +strName );
                    editor.putString("FileNo",sharedSrno);
                    editor.putString("MobileNo1",strMobile);
                    editor.commit();
                     caseid="D7";
                }
                editor.putString("Caseid",caseid);
                editor.commit();
              //  Log.d("doclisturl", strDocumentList);
                // progressDialog=ProgressDialog.show(ActivityUploadDocs.this,"","Getting spinner data",true);
                progressDialog=ProgressDialog.show(ActivityUploadDocs.this,"","Getting List of Uploaded Document",false,true);
                newThreadInitilization(progressDialog);
                //to get list of documents name that to be upload
                loadSpinnerData(caseid);
            }

            mBtnBrowse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //code to select pdf doc or image from file manager

                    String[] mimeTypes = {"image/*", "application/pdf"};

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                        if (mimeTypes.length > 0) {
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                        }
                    } else {
                        String mimeTypesStr = "";

                        for (String mimeType : mimeTypes) {
                            mimeTypesStr += mimeType + "|";
                        }
                        intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                    }
                    startActivityForResult(Intent.createChooser(intent, "Select file"), RESULT_LOAD_IMAGE);




                  // String[] mimeTypes = {"image/*", "application/pdf"};

                 //  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                   // intent.addCategory(Intent.CATEGORY_DEFAULT);
                    //intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                    //if (mimeTypes.length > 0) {
                     //   intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                      //  startActivityForResult(intent, RESULT_LOAD_IMAGE);
                   //}
                    mBtnUpload.setVisibility(View.VISIBLE);
                    mBtnBrowse.setVisibility(View.GONE);
                   // Intent intent = new Intent(Intent.ACTION_PICK);
                   //intent.addCategory(Intent.CATEGORY_DEFAULT);
                   //intent.setType("*/*");
                   //startActivityForResult(intent, RESULT_LOAD_IMAGE);
                  //  mBtnUpload.setVisibility(View.VISIBLE);
                   // mBtnBrowse.setVisibility(View.GONE);

                   /* Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    mBtnUpload.setVisibility(View.VISIBLE);
                    mBtnBrowse.setVisibility(View.GONE);*/
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
                            if(caseid.equals("155")) {
                                //to insert doc id to database
                               // getSingleName();
                                uploadDoc();
                            }else {
                                //insert doc id to database
                                getSingleName();
                            }
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

    private void initialize() {
        requestQueue = Volley.newRequestQueue(ActivityUploadDocs.this);
        // Log.d("FileNo",sharedSrno);

        clienturl = sp.getString("ClientUrl", null);
        clientid = sp.getString("ClientId", null);
        counselorid = sp.getString("Id", null).trim();
        mailid=sp.getString("MailId","");
        strMobile=sp.getString("MobileNo","");
        //counselorid = counselorid.replace(" ", "");
        counselorname=sp.getString("Name",null);
        timeout=sp.getLong("TimeOut",0);

        mSpinnerDocuments = findViewById(R.id.spinnerCategories);
        linearLayoutBrowse = findViewById(R.id.LinearBrowse);
        txtFileNo = findViewById(R.id.txtFileNo);
        txtMobileNo = findViewById(R.id.txtMobileNo);
        imgRefresh=findViewById(R.id.imgRefresh);
        table_alldetails=findViewById(R.id.table_alldetails);
        txtImagePreview=findViewById(R.id.txtImagePreview);
        txtAllDocument=findViewById(R.id.txtAllDocument);

        recyclerViewUploadedDocs = findViewById(R.id.recycler_uploadeddocs);
        recyclerUploadedImage = findViewById(R.id.recycler_uploadeddocs_image);
        imgBack = findViewById(R.id.img_back);
        mBtnBrowse = findViewById(R.id.btnBrowse);
        mBtnUpload = findViewById(R.id.btnUpload);
        mBtnReset = findViewById(R.id.btnReset);
        txtFileName = findViewById(R.id.txtFileName);
        txtMailId=findViewById(R.id.txtMailId);
        txtNoDocuments = findViewById(R.id.txtNoDocument);
        btnEmailAttachements=findViewById(R.id.btn_EmailAttachement);

        documentIDArrayList = new ArrayList<>();
        documentNameArrayList = new ArrayList<>();
        txtMobileNo.setText(strMobile);
        txtMailId.setText(mailid);
        attachmentArrayList = new ArrayList<>();


    }//close initialize

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
                                Toast.makeText(ActivityUploadDocs.this, "Connection Aborted.", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(activityname.equals("EmpDocs"))
        {
            Intent intent = new Intent(ActivityUploadDocs.this, Home.class);
            intent.putExtra("Activity", "EmpDocs");
            startActivity(intent);
            finish();
        }/*else if(activityname.equals("CollegeActivity"))
        {
            Intent intent=new Intent(ActivityUploadDocs.this,CollegeActivity.class);
            intent.putExtra("FileNo",sharedSrno);
            intent.putExtra("AllocatedTo",counselorid);
            intent.putExtra("MobileNo",strMobile);
            intent.putExtra("Name",strName);
            startActivity(intent);
        }*/
        else
         {
            Intent intent=new Intent(ActivityUploadDocs.this,SummaryDetails.class);
            intent.putExtra("FileNo",sharedSrno);
            intent.putExtra("MobileNo",strMobile);
            intent.putExtra("Activity", "UploadDocs");
            startActivity(intent);
            finish();
        }
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
    }catch (Exception e)
    {
        Log.d("ExcSendNotification",e.toString());
        Toast.makeText(ActivityUploadDocs.this,"Errorcode-333 UploadDocs sendNotification "+e.toString(),Toast.LENGTH_SHORT).show();
    }
}//close sendNotification

    public void insertEmpDocDetails()
    {
        try{
            if(CheckServer.isServerReachable(ActivityUploadDocs.this)) {
                String strurl4 = clienturl + "?clientid=" + clientid + "&caseid=159&CounselorID=" + counselorid + "&DocName=" + strSelectedDoc+"&DocFileName="+txtFileName.getText().toString();
                Log.d("insertEmpDocUrl", strurl4);

                StringRequest stringRequest3 = new StringRequest(Request.Method.GET, strurl4, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("insertEmpDocRes", response);
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
        }catch (Exception e)
        {
            Log.d("ExcSingleName",e.toString());
            Toast.makeText(ActivityUploadDocs.this,"Errorcode-335 UploadDocs getSingleName "+e.toString(),Toast.LENGTH_SHORT).show();
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
    }catch (Exception e)
    {
        Log.d("ExcSingleName",e.toString());
        Toast.makeText(ActivityUploadDocs.this,"Errorcode-335 UploadDocs getSingleName "+e.toString(),Toast.LENGTH_SHORT).show();
    }
}//close getSingleName
public void uploadDoc()
{
    try{
        if(CheckServer.isServerReachable(ActivityUploadDocs.this)) {
            if(caseid.equals("155")) {
                strinserturl = clienturl + "?clientid=" + clientid + "&caseid=159&CounselorID=" + counselorid + "&DocName=" + docid + "&DocFileName=" + txtFileName.getText().toString();
            }else{
                strinserturl = clienturl + "?clientid=" + clientid + "&caseid=D9&SrNo=" + sharedSrno + "&docid=" + docid + "&docname=" + txtFileName.getText().toString();
            }

            Log.d("insertdoc", strinserturl);
            StringRequest stringinsertdoc = new StringRequest(Request.Method.GET, strinserturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("nDocID")) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            strDocID1= jsonObject1.getString("nDocID");
                        }
                        progressDialog = new ProgressDialog(ActivityUploadDocs.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Document Uploading...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(100);
                        progressDialog.show();
                        newThreadInitilization(progressDialog);

                        new Thread(new Runnable() {
                            public void run() {
                                if(caseid.equals("155"))
                                {
                                    upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=158" + "&DocName=" + secondString+"&Ext11="+ext;
                                }
                                else {
                                    sendNotification();
                                    upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=D8" + "&filename=" +secondString+"&Ext11="+ext;

                                }Log.d("myurl", upLoadServerUri);
                                uploadFile(uploadFilePath + "" + uploadFileName);
                             //   uploadFileName = null;
                            }
                        }).start();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
    }catch (Exception e)
    {
        Log.d("ExcUploadDocs",e.toString());
        Toast.makeText(ActivityUploadDocs.this,"Errorcode-337 UploadDocs uploadDoc "+e.toString(),Toast.LENGTH_SHORT).show();
    }
}//close uploadDoc
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       try{
        int dataSize=0;
        String path="";
        if(resultCode==RESULT_CANCELED)
        {
            mSpinnerDocuments.setClickable(true);
            mSpinnerDocuments.setEnabled(true);
        }
        else if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            mSpinnerDocuments.setClickable(false);
            mSpinnerDocuments.setEnabled(false);
            Uri selectedImage = data.getData();
          //  String scheme = selectedImage.getScheme();
         //   filePathColumn = new String[]{MediaStore.Images.Media.DATA};
            //get path of selected image
            if(selectedImage.toString().contains(".pdf"))
            {
                 path=selectedImage.getPath();
            }else {
                 path = Commons.getPath(selectedImage, getApplicationContext());
            }


            if(path.startsWith("/external"))
            {
                path = Commons.getPath(selectedImage, getApplicationContext());
            }
           Log.d("FilePath",path+" "+selectedImage);

           // System.out.println("Scheme type " + scheme);
         /*   if(scheme.equals(ContentResolver.SCHEME_CONTENT))
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
            }*/
         //   String files=getFileSize(filesize);
            //Log.d("FileSizeMB",files);
            uploadFilePath = path.substring(0, path.lastIndexOf("/") + 1);
          //  Log.d("Path", uploadFilePath);

            uploadFileName = path.substring(uploadFilePath.lastIndexOf("/") + 1);
            ext=uploadFileName.substring(uploadFileName.lastIndexOf(".")+1);

          //  uploadFileName=uploadFileName.replaceAll(" ","");
            Log.d("FileNamePath:",uploadFilePath+" "+uploadFileName+"."+ext);
            //String secondString = "1_" + item + uploadFileName.substring(uploadFileName.lastIndexOf("."));
            //String secondString = sharedSrno + item + uploadFileName.substring(uploadFileName.lastIndexOf("."));
            if(caseid.equals("155")) {
                secondString = counselorid + "_" + strSelectedDoc.trim();
            }else {
                secondString = sharedSrno + "_" + strSelectedDoc.trim();
            }
            secondString=secondString.replaceAll(" ","");
            spinnerposition = (mSpinnerDocuments.getSelectedItemId());
/*
            Log.d("spinnposition", (mSpinnerDocuments.getSelectedItemId() + 1) + "");
            Log.d("second string", secondString);
            Log.d("FileName", uploadFileName);*/

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {

                if (path.contains(".pdf"))
                {
                    directory_path = Environment.getExternalStorageDirectory().getAbsolutePath() + fileName;
                    String targetPdf = directory_path;
                    String aa = directory_path.substring(0, directory_path.lastIndexOf("/") + 1);
                    String bb = uploadFilePath.substring(uploadFilePath.lastIndexOf(":") + 1);
                    uploadFilePath = aa + bb;
                    Log.d("directory_path", directory_path+ " -- " + uploadFilePath);
                    txtFileName.setText(secondString+"."+ext);
                } else {
                    txtFileName.setText(secondString+"."+ext);
                }
            }else {
                txtFileName.setText(secondString+"."+ext);

            }

           /* Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            cursor.close();*/

           /* try {
                Bitmap bmp = getBitmapFromUri(selectedImage);
                //imgCamera.setImageBitmap(bmp);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
            // ImageView.setImageBitmap(bmp);
        }
       }catch (Exception e)
        {
            Log.d("ExcOnActvtyRsltUpldDocs",e.toString());
            Toast.makeText(ActivityUploadDocs.this,"Errorcode-339 UploadDocs onActivityResult "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close onActivityResult

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String getPath(Context context, Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
            {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri))
                {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type))
                    {
                        return Environment.getExternalStorageDirectory() + "/"
                                + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri))
                {
                     String id = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        id = DocumentsContract.getDocumentId(uri);
                    }

                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri))
                {
                    String docId = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        docId = DocumentsContract.getDocumentId(uri);
                    }
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;

                    if ("image".equals(type))
                    {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if ("video".equals(type))
                    {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if ("audio".equals(type))
                    {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] { split[1] };

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme()))
            {
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme()))
            {
                return uri.getPath();
            }
        }

     /*   String result = null;
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
       // return result;*/
       return null;
    }//close getPath
    private static String getDataColumn(Context context, Uri uri,
                                        String selection, String[] selectionArgs)
    {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try
        {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst())
            {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        finally
        {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    /**
     * @param uri      - The Uri to check.
     * @return         - Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri       - The Uri to check.
     * @return          - Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri       - The Uri to check.
     * @return          - Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri       - The Uri to check.
     * @return          - Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri)
    {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException
    {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
  //************************Get File Name From Gallery************************End

    private void loadSpinnerData(final String caseid) {
        //requestQueue = Volley.newRequestQueue(ActivityUploadDocs.this);
        try {
            strDocumentList = clienturl + "?clientid=" + clientid + "&caseid="+caseid;
            Log.d("LoadSpinnerDataUrl",strDocumentList);
            if(CheckServer.isServerReachable(ActivityUploadDocs.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, strDocumentList, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    if(caseid.equals("155"))
                                    {
                                        strDocId = jsonObject1.getString("nEmpDocumentID");
                                    }
                                    else {
                                        strDocId = jsonObject1.getString("nDocumentID");
                                    }
                                    String strDocSelect = jsonObject1.getString("cDocument");
                                    documentIDArrayList.add(strDocId);
                                    documentNameArrayList.add(strDocSelect);
                                }
                              //  Log.d("DocListSize", documentIDArrayList.size() + "");

                            temp = "Oncreate";
                            requestQueue = Volley.newRequestQueue(ActivityUploadDocs.this);
                            if(caseid.equals("155"))
                            {
                                //to get list of already uploaded employee documents
                                uploadedEmpDocName();
                            }
                            else {
                                //to get list of already uploaded documents
                                uploadedDocName();
                            }
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
        }catch (Exception e)
        {
            Toast.makeText(ActivityUploadDocs.this,"Errorcode-339 UploadDocs loadSpinnerData "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close loadSpinnerData
    public void uploadedEmpDocName(){
        try {
            if(CheckServer.isServerReachable(ActivityUploadDocs.this)) {
                String uploadedDocUrl = clienturl + "?clientid=" + clientid + "&caseid=156&CounselorID=" + counselorid;
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
                            Log.d("UploadedEmpRes",response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (response.contains("[]")) {
                                txtNoDocuments.setVisibility(View.VISIBLE);
                                Toast.makeText(ActivityUploadDocs.this, "No documents uploaded yet", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String uploadedDocId = jsonObject1.getString("nEmpDocID");
                                    strDocID1=jsonObject1.getString("nDocID");
                                    String uploadedDocSelect = jsonObject1.getString("cDocFileName");

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
        }catch (Exception e)
        {
            Toast.makeText(ActivityUploadDocs.this,"Errorcode-340 UploadDocs uploadedDocName "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close uploadedEmpDocName

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
                                btnEmailAttachements.setVisibility(View.GONE);
                                Toast.makeText(ActivityUploadDocs.this, "No documents uploaded yet", Toast.LENGTH_SHORT).show();
                            } else {
                                btnEmailAttachements.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String uploadedDocId = jsonObject1.getString("nDocID");
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
        }catch (Exception e)
        {
            Toast.makeText(ActivityUploadDocs.this,"Errorcode-340 UploadDocs uploadedDocName "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close uploadedDocName
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
                                Intent intent = new Intent(ActivityUploadDocs.this, ActivityDocumentInfo.class);
                                intent.putExtra("Activity",activityname);
                                intent.putExtra("FileNo", sharedSrno);
                                intent.putExtra("Mobile",strMobile);
                                intent.putExtra("DocName",secondString);
                                intent.putExtra("Ext",ext);
                                intent.putExtra("DocID",strDocID1);
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
    }//close uploadFile

    private class DownloadTask extends AsyncTask<URL, Void, Bitmap> {
        // Before the tasks execution
        protected void onPreExecute() {
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();

                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Save bitmap to internal storage
                dialog1.dismiss();
                Uri imageInternalUri = saveImageToInternalStorage(result);

                //   DownloadNotification(strDocName);

            } else {
                notFountCount++;
                entered++;
                dialog1.dismiss();
                sendMail();
                Toast.makeText(ActivityUploadDocs.this, "Download failed..", Toast.LENGTH_LONG).show();
            }
        }
    }
    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        Log.d("SaveImage", "Image");
        // Initialize ContextWrapper
        File directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs");
        directory.mkdirs();

        // Create a file to save the image
        File file = new File(directory, strDocName);

        try {
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        savedImageURI = Uri.parse(file.getAbsolutePath());
        path = FileProvider.getUriForFile(ActivityUploadDocs.this, ActivityUploadDocs.this.getPackageName() + ".provider", directory);
        found++;
        entered++;
        Log.d("SavedPath",savedImageURI+"");
        if(savedImageURI!=null) {
            sendMail();
        }
        // Uri path = Uri.fromFile(file);
        /*if (btnClick.equals("Attachment")) {
            sendMail(path, mail, subject);
        }
        Log.d("mypath", savedImageURI + "");*/
        Toast.makeText(ActivityUploadDocs.this, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
        // Return the saved image Uri
        return savedImageURI;
    }

    public void DownloadNotification(String documentName) {
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        final int NotiId = m;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String id = this.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = this.getString(R.string.default_notification_channel_title); // Default Channel
        NotificationCompat.Builder builder;
        if (mNotifyManager == null) {
            mNotifyManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = mNotifyManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 100});
                mNotifyManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs/" + strDocName);
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                  FileProvider.getUriForFile(this, com.android.volley.BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID+".provider",file), "*/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded Image");
            builder.setContentText(documentName);
            builder.setSubText("Tap to view Document.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        } else {
            builder = new NotificationCompat.Builder(this, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs/" + strDocName);
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                    FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file), "image/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded Image");
            builder.setContentText(documentName);
            builder.setSubText("Tap to view Document.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        }
        mNotifyManager.notify(NotiId, builder.build());
    }

    private void DownloadFile(String fileURL, File directory, String docName) {
        try {
            FileOutputStream f = new FileOutputStream(directory);
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            //  c.setDoOutput(true);
            c.connect();
            InputStream in = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            if (len1 != 0) {
                // Save bitmap to internal storage
                dialog1.dismiss();
                savedImageURI = Uri.parse(directory.getAbsolutePath());
                found++;
                Log.d("mypath", savedImageURI + ""+found);
                path = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", directory);
                entered++;
                sendMail();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityUploadDocs.this, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                //  DownloadNotification(docName);
            } else {
                entered++;
                dialog1.dismiss();
                notFountCount++;
                sendMail();
                Toast.makeText(this, "Download failed..", Toast.LENGTH_LONG).show();
            }
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (e.toString().contains("FileNotFoundException")) {
                dialog1.dismiss();
                entered++;
                notFountCount++;
                Log.d("NotFoundEntered","1");

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                sendMail();
                                Toast.makeText(ActivityUploadDocs.this, "File not found.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();

            }
        }
    }
    public void sendMail()
    {
        // if (btnClick.equals("Attachment")) {
        Log.d("Entered",entered+" "+notFountCount);
        if(entered==attachmentArrayList.size()) {
            if (notFountCount == 0)
            {
                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // set the type to 'email'
                emailIntent.setType("vnd.android.cursor.dir/email");
                String to[] = {mailid};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                // the attachment
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentPath);
                // the mail subject
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                //startActivity(emailIntent);
            } else {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityUploadDocs.this);
                alertDialogBuilder.setTitle("Some files not downloaded")
                        .setMessage("Do you want to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                // set the type to 'email'
                                emailIntent.setType("vnd.android.cursor.dir/email");
                                String to[] = {mailid};
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                                // the attachment
                                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentPath);
                                // the mail subject
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .show();
            }
        }
    }

    protected URL stringToURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
