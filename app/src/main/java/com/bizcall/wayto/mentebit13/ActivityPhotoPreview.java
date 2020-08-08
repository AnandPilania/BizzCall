package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityPhotoPreview extends AppCompatActivity {
    ImageView imgPreview;
    SharedPreferences sp;
    String clientid,clienturl,clienturl1,mobile,fileno,counselorid,strImageUrl,ext,strImage,docid;
    ImageView img_back,imgApprove,imgDelete;
    ProgressDialog dialog;
    RequestQueue requestQueue;
    Thread thread;
    long timeout;
    TextView txtDocName;
    Handler handler = new Handler();
    ProgressDialog dialog1;
    String strUrl,strDocName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        requestQueue= Volley.newRequestQueue(ActivityPhotoPreview.this);
        txtDocName=findViewById(R.id.txtDocName);
        imgPreview=findViewById(R.id.imgPreview);
        img_back=findViewById(R.id.img_back);
        imgApprove=findViewById(R.id.imgApprove);
        imgDelete=findViewById(R.id.imgDelete);
        sp =getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        timeout=sp.getLong("TimeOut",0);        counselorid = sp.getString("Id", null).trim();
       // mailid=sp.getString("MailId","");
        mobile=sp.getString("MobileNo1","");
        fileno=sp.getString("FileNo","");

        clienturl1 = clienturl.substring(0, clienturl.lastIndexOf("/"));
        strImage=getIntent().getStringExtra("ImageName");
        docid=getIntent().getStringExtra("DocID");
        strImageUrl = clienturl1 + "/upload-empdoc/" + strImage;
        ext = strImage.substring(strImage.lastIndexOf(".") + 1);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtDocName.setText(docid+"."+strImage);

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
            Picasso.with(ActivityPhotoPreview.this).load(strImageUrl).into(imgPreview);
        }

        if(ext.equals("pdf"))
        {
            imgPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog1=ProgressDialog.show(ActivityPhotoPreview.this,"","Downloading document",false);
                    File directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs");
                    directory.mkdirs();
                    strUrl = clienturl1 + "/upload-empdoc/" + strImage;
                    Log.d("docdata", strUrl );
                    // Create a file to save the image
                   final File file = new File(directory, strImage);
                    new Thread(new Runnable() {
                        public void run() {
                            DownloadFile(strUrl, file, strImage);
                        }
                    }).start();




                    // Create a file to save the image
                   // File file = new File(directory, strImage);

                }
            });
        }

        imgApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityPhotoPreview.this);
                alertDialogBuilder.setTitle("")
                        .setMessage("Are you sure you want to approve?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                                dialog=ProgressDialog.show(ActivityPhotoPreview.this,"","Updating client status",true);
                                newThreadInitilization(dialog);
                                approveDoc();

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
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityPhotoPreview.this);
                alertDialogBuilder.setTitle("")
                        .setMessage("Are you sure you want to reject?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                                dialog=ProgressDialog.show(ActivityPhotoPreview.this,"","Updating client status",true);
                                newThreadInitilization(dialog);
                                rejectDoc();

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
        });
    }
    public void newThreadInitilization(final ProgressDialog dialog1) {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(ActivityPhotoPreview.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
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
    private void DownloadFile(final String fileURL, File directory, String docName) {
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
               //vedImageURI = Uri.parse(directory.getAbsolutePath());
               // found++;
                Log.d("mypath", directory+"");
                Uri  path = FileProvider.getUriForFile(ActivityPhotoPreview.this, ActivityPhotoPreview.this.getPackageName() + ".provider", directory);

                // Uri path = Uri.fromFile(path);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pdfOpenintent.setDataAndType(path, "application/pdf");
                try {
                    startActivity(pdfOpenintent);
                }
                catch (ActivityNotFoundException e) {

                }

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityPhotoPreview.this, "Download Path : " + fileURL, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                //  DownloadNotification(docName);
            } else {

                dialog1.dismiss();
                Toast.makeText(this, "Download failed..", Toast.LENGTH_LONG).show();
            }
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (e.toString().contains("FileNotFoundException")) {
                dialog1.dismiss();

                Log.d("NotFoundEntered","1");

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityPhotoPreview.this, "File not found.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();

            }
        }
    }
    public void sendNotification()
    {
        //------------------------------Notification to counselor--------------------------------
        try{
            String strNotify = strImage+" has been rejected.Please upload document again.";
            String strurl3 = clienturl + "?clientid="+clientid + "&caseid=62&SrNo=" + fileno + "&CounselorId=" + counselorid +
                    "&Notification=" + strNotify;
            if(CheckServer.isServerReachable(ActivityPhotoPreview.this)) {
                Log.d("newqualification", strurl3);
                StringRequest stringRequest4 = new StringRequest(Request.Method.GET, strurl3, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("ChatResponse", response);
                        if (response.contains("Data inserted successfully")) {
                            //dialog.dismiss();
                            Toast.makeText(ActivityPhotoPreview.this, "Notification sent to Counselor.", Toast.LENGTH_SHORT).show();
                           // uploadedDocName();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityPhotoPreview.this, "Errorcode-334 UploadDocs NotificationResponse " + error.toString(), Toast.LENGTH_SHORT).show();
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
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityPhotoPreview.this);
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
            Toast.makeText(ActivityPhotoPreview.this,"Errorcode-333 UploadDocs sendNotification "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close sendNotification
    public void rejectDoc() {
        try{
            if(CheckServer.isServerReachable(ActivityPhotoPreview.this))
            {
                String  url=clienturl+"?clientid=" + clientid + "&caseid=A85&DocID="+docid+"&Remarks=";
                Log.d("rejectDocUrl", url);
                if(CheckInternet.checkInternet(ActivityPhotoPreview.this))
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("*******", response.toString());
                                    try {
                                        dialog.dismiss();

                                        Log.d("rejectDocResponse", response);
                                        if (response.contains("Data updated successfully")) {
                                            Intent intent=new Intent(ActivityPhotoPreview.this,ActivityUploadDocs.class);
                                            intent.putExtra("Activity", "PhotoPreview");
                                            intent.putExtra("FileNo", fileno);
                                            intent.putExtra("MobileNo", mobile);
                                            // uploaddocs.putExtra("Email",strMail);
                                           // intent.putExtra("Name",strName);
                                            startActivity(intent);
                                           sendNotification();
                                            Toast.makeText(ActivityPhotoPreview.this, "Document rejected successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ActivityPhotoPreview.this, "Document not rejected", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        //  Toast.makeText(CollegeActivity.this,"Errorcode-213 CounselorContact updateRemarkResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityPhotoPreview.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")
                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ActivityPhotoPreview.this,"Network issue",Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                }else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityPhotoPreview.this);
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
                } }else
            {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityPhotoPreview.this);
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
            // Toast.makeText(ActivityCollegeMaster.this,"Errorcode-212 CounselorContact updateRemarks "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcAddCollege", String.valueOf(e));
        }

    }
    public void approveDoc() {
        try{
            if(CheckServer.isServerReachable(ActivityPhotoPreview.this))
            {
                String  url=clienturl+"?clientid=" + clientid + "&caseid=A84&DocID="+docid;
                Log.d("ApproveDocUrl", url);
                if(CheckInternet.checkInternet(ActivityPhotoPreview.this))
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("*******", response.toString());
                                    try {
                                        dialog.dismiss();

                                        Log.d("ApproveDocResponse", response);
                                        if (response.contains("Data updated successfully")) {
                                            Intent intent=new Intent(ActivityPhotoPreview.this,ActivityUploadDocs.class);
                                            intent.putExtra("Activity", "PhotoPreview");
                                            intent.putExtra("FileNo", fileno);
                                            intent.putExtra("MobileNo", mobile);
                                            // uploaddocs.putExtra("Email",strMail);
                                            // intent.putExtra("Name",strName);
                                            startActivity(intent);
                                            Toast.makeText(ActivityPhotoPreview.this, "Document approved successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ActivityPhotoPreview.this, "Document not approved", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        //  Toast.makeText(CollegeActivity.this,"Errorcode-213 CounselorContact updateRemarkResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityPhotoPreview.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")
                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ActivityPhotoPreview.this,"Network issue",Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                }else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityPhotoPreview.this);
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
                } }else
            {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityPhotoPreview.this);
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
            // Toast.makeText(ActivityCollegeMaster.this,"Errorcode-212 CounselorContact updateRemarks "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcAddCollege", String.valueOf(e));
        }

    }
}
