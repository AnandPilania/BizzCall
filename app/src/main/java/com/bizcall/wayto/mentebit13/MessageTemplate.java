package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import in.gauriinfotech.commons.Commons;

public class MessageTemplate extends AppCompatActivity {

    EditText edtMeggase,edtMailSubject;
    Button btnSubmit;
    SharedPreferences sp;
    String counselorid="", msg="",mailsubject="",mailbody="", clientid="",clienturl="";
    long timeout;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    int flag = 0;
    ImageView imageBack,imgRefresh;
    Vibrator vibrator;
    String url="";
    public static String activity="";
    RequestQueue requestQueue;
    TextView txtActivity,txtTitle,txtSubjectTitle;
    Thread thread;
    RecyclerView recyclerTemplate;
    ArrayList<DataTemplate> arrayListTemplate;
    AdapterSmsTemplate adapterSmsTemplate;
    String sms;
    int smsid;
    int isSelected1=0,isSelected2=0,isSelected3=0,isSelected4=0,isSelected5=0,isSelected6=0;

    ImageView imgReceipt1,imgReceipt2,imgReceipt3,imgReceipt4,imgReceipt5,imgPath,imgMailImagePre;
  //  public final int GET_FROM_GALLERY = 1;
    LinearLayout linearImage1,linearImage2,linearImage3,linearImage4,linearImage5,linearImage6;
    int RESULT_LOAD1 = 1,RESULT_LOAD2 = 1,RESULT_LOAD3 = 1,RESULT_LOAD4 = 1,RESULT_LOAD5 = 1,RESULT_LOAD6=1;
    String uploadFilePath1="", uploadFilePath2="", uploadFilePath3="",uploadFilePath4="", uploadFilePath5="",uploadFilePath6="";
    String uploadFileName1="", uploadFileName2="", uploadFileName3="",uploadFileName4="",uploadFileName5="",uploadFileName6="",attachment1="",ext1="",ext2="",ext3="",ext4="",ext5="",ext6="",attachment2="",attachment3="",attachment4="",attachment5="",mailImage;
    String upLoadServerUri = "";
    int serverResponseCode = 0;
    String clicked="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_template);
        try{
          initialize();
          if(activity.contains("Mail")) {
                txtSubjectTitle.setVisibility(View.VISIBLE);
                edtMeggase.setHint("Enter mail subject here");
                edtMailSubject.setVisibility(View.VISIBLE);
                edtMailSubject.setHint("Enter mail body here");
                txtTitle.setText(activity+" "+"Body");
              linearImage1.setVisibility(View.VISIBLE);
              linearImage2.setVisibility(View.VISIBLE);
              linearImage3.setVisibility(View.VISIBLE);
              linearImage4.setVisibility(View.VISIBLE);
              linearImage5.setVisibility(View.VISIBLE);
              linearImage6.setVisibility(View.VISIBLE);
          }
          else
           {
                templateMessage();
           }


            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
                imgRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MessageTemplate.this,MessageTemplate.class);
                        intent.putExtra("Activity",activity);
                        startActivity(intent);
                    }
                });
            linearImage6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked="l6";
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), RESULT_LOAD6);
                }
            });
                linearImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clicked="l1";
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
                        startActivityForResult(Intent.createChooser(intent, "Select file"), RESULT_LOAD1);
                    }
                });
            linearImage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicked="l2";
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
                    startActivityForResult(Intent.createChooser(intent, "Select file"), RESULT_LOAD2);
                }
            });
            linearImage3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicked="l3";
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
                    startActivityForResult(Intent.createChooser(intent, "Select file"), RESULT_LOAD3);
                }
            });
            linearImage4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicked="l4";
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
                    startActivityForResult(Intent.createChooser(intent, "Select file"), RESULT_LOAD4);
                }
            });
            linearImage5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicked="l5";
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
                    startActivityForResult(Intent.createChooser(intent, "Select file"), RESULT_LOAD5);
                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if(activity.contains("Message")) {
                            flag = 0;
                            msg = edtMeggase.getText().toString();
                            msg = msg.replaceAll("'", "");
                            if (msg.length() == 0) {
                                edtMeggase.setError("Please enter message");
                                flag = 1;
                            }
                            if (flag == 0) {
                                if (CheckInternetSpeed.checkInternet(MessageTemplate.this).contains("0")) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
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
                                } else if (CheckInternetSpeed.checkInternet(MessageTemplate.this).contains("1")) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
                                    alertDialogBuilder.setTitle("Slow Internet speed!!!")
                                            .setMessage("Can't do further process")

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //insertIMEI();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    dialog = ProgressDialog.show(MessageTemplate.this, "", "Inserting message in template", true);
                                    newThreadInitilization(dialog);
                                    //to insert message template to server so that counselor can use it while sending message to client
                                    insertMsgTemplate();
                                }
                            }
                        }
                        else if(activity.contains("Mail")) {
                            Log.d("Entered Mail","1");
                            flag = 0;
                            mailsubject = edtMailSubject.getText().toString();
                            mailbody=edtMeggase.getText().toString();
                            mailbody=mailbody.replaceAll("'","");
                            if (mailbody.length() == 0) {
                                edtMeggase.setError("Please enter body");
                                flag = 1;
                            }
                            if(mailsubject.length()==0)
                            {
                                edtMailSubject.setError("Please enter subject");
                                flag=1;
                            }
                            if(isSelected1==0)
                            {
                                attachment1="NA";
                            }
                            if(isSelected2==0)
                            {
                                attachment2="NA";
                            }
                            if(isSelected3==0)
                            {
                                attachment3="NA";
                            }
                            if(isSelected4==0)
                            {
                                attachment4="NA";
                            } if(isSelected5==0)
                            {
                                attachment5="NA";
                            }
                            if(isSelected6==0)
                            {
                                mailImage="NA";
                            }
                            if (flag == 0) {

                                    if (CheckInternetSpeed.checkInternet(MessageTemplate.this).contains("0")) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
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
                                    } else if (CheckInternetSpeed.checkInternet(MessageTemplate.this).contains("1")) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
                                        alertDialogBuilder.setTitle("Slow Internet speed!!!")
                                                .setMessage("Can't do further process")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //insertIMEI();
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    } else {


                                        dialog = ProgressDialog.show(MessageTemplate.this, "", "Inserting mail in template", true);
                                        newThreadInitilization(dialog);
                                        //to insert mail template to server so that counselor can use it while sending message to client
                                        insertMailTemplate();
                                    }

                                }
                            }

                      //  refreshWhenLoading();
                    }
            });
        }catch (Exception e)
        {
            Toast.makeText(MessageTemplate.this,"Errorcode-389 MessageTemplate onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("MsgTemplateException", String.valueOf(e));
        }
    }//onCreate
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
                                Toast.makeText(MessageTemplate.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
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
    public void initialize()
    {
        requestQueue=Volley.newRequestQueue(MessageTemplate.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        edtMeggase = findViewById(R.id.edtMessage1);
        edtMailSubject=findViewById(R.id.edtSubject);
        btnSubmit = findViewById(R.id.btnSubmitMsg);
        imageBack = findViewById(R.id.img_back);
        imgRefresh=findViewById(R.id.imgRefresh);
        txtActivity=findViewById(R.id.txtActivityName);
        txtTitle=findViewById(R.id.txtTitle);
        txtSubjectTitle=findViewById(R.id.txtTitleSubject);
        recyclerTemplate=findViewById(R.id.recyclerTemplate);
        imgMailImagePre=findViewById(R.id.imgMailImagePre);
       // checkboxAttachReceipt = findViewById(R.id.checkboxAttachReceipt);
       // linearImage = findViewById(R.id.linearImage);
        imgReceipt1 = findViewById(R.id.imgReceipt1);
        imgReceipt2 = findViewById(R.id.imgReceipt2);
        imgReceipt3 = findViewById(R.id.imgReceipt3);
        imgReceipt4 = findViewById(R.id.imgReceipt4);
        imgReceipt5 = findViewById(R.id.imgReceipt5);
        imgPath=findViewById(R.id.imgDoc1);
        linearImage1=findViewById(R.id.linearImage1);
        linearImage2=findViewById(R.id.linearImage2);
        linearImage3=findViewById(R.id.linearImage3);
        linearImage4=findViewById(R.id.linearImage4);
        linearImage5=findViewById(R.id.linearImage5);
        linearImage6=findViewById(R.id.linearImage6);

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid = sp.getString("Id", null);
        counselorid=counselorid.replaceAll(" ","");
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",clienturl);
        timeout=sp.getLong("TimeOut",0);
        activity=getIntent().getStringExtra("Activity");
        txtActivity.setText(activity+" "+"Template");
        txtTitle.setText(activity);
    }//close initialize
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
            dialog.dismiss();

            Log.d("uploadFileNotExist", "Source File not exist :");
                   // + uploadFilePath + "" + uploadFileName);


            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MessageTemplate.this, "Source File not exist :",Toast.LENGTH_SHORT).show();
                          //  + uploadFilePath + "" + uploadFileName, Toast.LENGTH_SHORT).show();
                }
            });

            return 0;
        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
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

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                /*if(serverResponseCode==409)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashAfterCall.this, "File already exist.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/

                if (serverResponseCode == 200) {
                    // dialog.dismiss();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MessageTemplate.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(MessageTemplate.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Uploadfiletoserver", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(MessageTemplate.this, "Got Exception while uploading record ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }//uploadFile


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            int dataSize=0;
            String path="";
            mailsubject=edtMailSubject.getText().toString();
            if(resultCode==RESULT_CANCELED)
            {

            }
            if(requestCode == RESULT_LOAD6 ) {
                Log.d("Entered", "6");
                if (resultCode == RESULT_OK && null != data) {
                    if (clicked.equals("l6")) {
                        isSelected6 = 1;
                        Uri selectedImage = data.getData();
                        //get path of selected image
                        if (selectedImage.toString().contains(".pdf")) {
                            path = selectedImage.getPath();
                        } else {
                            Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imgMailImagePre.setImageBitmap(bitmap1);
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        if (path.startsWith("/external")) {
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        Log.d("FilePath", path + " " + selectedImage);

                        uploadFilePath6 = path.substring(0, path.lastIndexOf("/") + 1);
                        //  Log.d("Path", uploadFilePath);

                        uploadFileName6 = path.substring(uploadFilePath6.lastIndexOf("/") + 1);
                        ext6 = uploadFileName6.substring(uploadFileName6.lastIndexOf(".") + 1);

                        Log.d("FileNamePath:", uploadFilePath6 + " " + uploadFileName6 + "." + ext6);
                        mailImage = counselorid + "_MailImage_" + mailsubject.trim();
                    }
                    if (clicked.equals("l1")) {
                        isSelected1 = 1;
                        Log.d("Entered", "1");
                        Uri selectedImage = data.getData();
                        //get path of selected image
                        if (selectedImage.toString().contains(".pdf")) {
                            path = selectedImage.getPath();

                        } else {
                            Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imgReceipt1.setImageBitmap(bitmap2);
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        if (path.startsWith("/external")) {
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        Log.d("FilePath", path + " " + selectedImage);

                        uploadFilePath1 = path.substring(0, path.lastIndexOf("/") + 1);
                        //  Log.d("Path", uploadFilePath);

                        uploadFileName1 = path.substring(uploadFilePath1.lastIndexOf("/") + 1);
                        ext1 = uploadFileName1.substring(uploadFileName1.lastIndexOf(".") + 1);

                        Log.d("FileNamePath:", uploadFilePath1 + " " + uploadFileName1 + "." + ext1);
                        attachment1 = counselorid + "_Attachment1_" + mailsubject.trim();
                    }

                    if (clicked.equals("l2")) {
                        isSelected2 = 1;
                        Log.d("Entered", "2");
                        Uri selectedImage = data.getData();
                        //get path of selected image
                        if (selectedImage.toString().contains(".pdf")) {
                            path = selectedImage.getPath();
                        } else {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imgReceipt2.setImageBitmap(bitmap);
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        if (path.startsWith("/external")) {
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        Log.d("FilePath", path + " " + selectedImage);

                        uploadFilePath2 = path.substring(0, path.lastIndexOf("/") + 1);
                        //  Log.d("Path", uploadFilePath);

                        uploadFileName2 = path.substring(uploadFilePath2.lastIndexOf("/") + 1);
                        ext2 = uploadFileName2.substring(uploadFileName2.lastIndexOf(".") + 1);

                        Log.d("FileNamePath:", uploadFilePath2 + " " + uploadFileName2 + "." + ext2);
                        attachment2 = counselorid + "_Attachment2_" + mailsubject.trim();
                    }
                    if (clicked.equals("l3")) {
                        isSelected3 = 1;
                        Log.d("Entered", "3");
                        Uri selectedImage = data.getData();
                        //get path of selected image
                        if (selectedImage.toString().contains(".pdf")) {
                            path = selectedImage.getPath();
                        } else {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imgReceipt3.setImageBitmap(bitmap);
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        if (path.startsWith("/external")) {
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        Log.d("FilePath", path + " " + selectedImage);

                        uploadFilePath3 = path.substring(0, path.lastIndexOf("/") + 1);
                        //  Log.d("Path", uploadFilePath);

                        uploadFileName3 = path.substring(uploadFilePath3.lastIndexOf("/") + 1);
                        ext3 = uploadFileName3.substring(uploadFileName3.lastIndexOf(".") + 1);

                        Log.d("FileNamePath:", uploadFilePath3 + " " + uploadFileName3 + "." + ext3);
                        attachment3 = counselorid + "_Attachment3_" + mailsubject.trim();
                    }
                    if (clicked.equals("l4")) {
                        isSelected4 = 1;
                        Log.d("Entered", "4");
                        Uri selectedImage = data.getData();
                        //get path of selected image
                        if (selectedImage.toString().contains(".pdf")) {
                            path = selectedImage.getPath();
                        } else {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imgReceipt4.setImageBitmap(bitmap);
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        if (path.startsWith("/external")) {
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        Log.d("FilePath", path + " " + selectedImage);

                        uploadFilePath4 = path.substring(0, path.lastIndexOf("/") + 1);
                        //  Log.d("Path", uploadFilePath);

                        uploadFileName4 = path.substring(uploadFilePath4.lastIndexOf("/") + 1);
                        ext4 = uploadFileName4.substring(uploadFileName4.lastIndexOf(".") + 1);

                        Log.d("FileNamePath:", uploadFilePath4 + " " + uploadFileName4 + "." + ext4);
                        attachment4 = counselorid + "_Attachment4_" + mailsubject.trim();
                    }
                    if (clicked.equals("l5")) {
                        isSelected5 = 1;
                        Log.d("Entered", "5");
                        Uri selectedImage = data.getData();
                        //get path of selected image
                        if (selectedImage.toString().contains(".pdf")) {
                            path = selectedImage.getPath();
                        } else {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imgReceipt5.setImageBitmap(bitmap);
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        if (path.startsWith("/external")) {
                            path = Commons.getPath(selectedImage, getApplicationContext());
                        }
                        Log.d("FilePath", path + " " + selectedImage);

                        uploadFilePath5 = path.substring(0, path.lastIndexOf("/") + 1);
                        //  Log.d("Path", uploadFilePath);

                        uploadFileName5 = path.substring(uploadFilePath5.lastIndexOf("/") + 1);
                        ext5 = uploadFileName5.substring(uploadFileName5.lastIndexOf(".") + 1);

                        Log.d("FileNamePath:", uploadFilePath5 + " " + uploadFileName5 + "." + ext5);
                        attachment5 = counselorid + "_Attachment5_" + mailsubject.trim();
                    }
                }
            }
               // secondString=secondString.replaceAll(" ","");

/*
            Log.d("spinnposition", (mSpinnerDocuments.getSelectedItemId() + 1) + "");
            Log.d("second string", secondString);
            Log.d("FileName", uploadFileName);*/

               // txtFileName.setText(secondString+"."+ext);

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

        }catch (Exception e)
        {
            Log.d("ExcOnActvtyRsltUpldDocs",e.toString());
            Toast.makeText(MessageTemplate.this,"Errorcode-339 UploadDocs onActivityResult "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close onActivityResult

    public void templateMessage()
    {
        try {
            if (CheckInternetSpeed.checkInternet(MessageTemplate.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
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
            } else if (CheckInternetSpeed.checkInternet(MessageTemplate.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                dialog = ProgressDialog.show(MessageTemplate.this, "", "Loading Message Template", true);
                newThreadInitilization(dialog);
                getTemplateMessage();
            }
        }catch (Exception e)
        {
            Toast.makeText(MessageTemplate.this,"Errorcode-276 MessageActivity loadTemplateMsg "+e.toString(),Toast.LENGTH_SHORT).show();            Log.d("MessageActException", String.valueOf(e));
        }
    }
    public void getTemplateMessage()
    {
        try {
            arrayListTemplate = new ArrayList<>();
            urlRequest = UrlRequest.getObject();
            urlRequest.setContext(MessageTemplate.this);
            urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=7&nCounselorID=" + counselorid + "&IsActive=1");
            Log.d("TemplatUrl", clienturl + "?clientid=" + clientid + "&caseid=7&nCounselorID=" + counselorid + "&IsActive=1");
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
                            sms = jsonObject1.getString("cSmsText");
                            smsid=jsonObject1.getInt("nSmsID");
                            DataTemplate dataTemplate=new DataTemplate(smsid,sms);
                            arrayListTemplate.add(dataTemplate);
                        }
                        adapterSmsTemplate=new AdapterSmsTemplate(MessageTemplate.this,arrayListTemplate);
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MessageTemplate.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerTemplate.setLayoutManager(linearLayoutManager);
                        recyclerTemplate.setAdapter(adapterSmsTemplate);
                        adapterSmsTemplate.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Toast.makeText(MessageTemplate.this,"Errorcode-284 MessageActivity getTemplateMsgResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    // String msg=response.substring(response.indexOf( "( [cSmsText] =>"+15),response.indexOf(")"));
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(MessageTemplate.this,"Errorcode-283 MessageActivity getTemplateMsg "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void insertMailTemplate() {
        if(CheckServer.isServerReachable(MessageTemplate.this)) {
            url = clienturl + "?clientid=" + clientid + "&caseid=170&CounselorID=" + counselorid+"&EmailSubject="+mailsubject+"&EmailBody="+mailbody+"&EmailAttachment1="+attachment1+"."+ext1+"&EmailAttachment2="+attachment2+"."+ext2+"&EmailAttachment3="+attachment3+"."+ext3+"&EmailAttachment4="+attachment4+"."+ext4+"&EmailAttachment5="+attachment5+"."+ext5+"&EmailImage="+mailImage+"."+ext6;
            Log.d("MailTemplateUrl", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("TemplateMailResponse", response);
                                if (response.contains("Data inserted successfully")) {
                                    edtMeggase.setText("");
                                   /* Intent intent=new Intent(MessageTemplate.this,MessageTemplate.class);
                                    intent.putExtra("Activity",activity);
                                    startActivity(intent);*/
                                   edtMailSubject.setText("");
                                   edtMeggase.setText("");
                                    Toast.makeText(MessageTemplate.this, "Template saved successfully", Toast.LENGTH_SHORT).show();

                                    if (isSelected6 == 1) {
                                        upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=171&DocName=" + mailImage+"&Ext11="+ext6;

                                        dialog = ProgressDialog.show(MessageTemplate.this, "", "Uploading Payment Receipt", true);

                                        new Thread(new Runnable() {
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //to upload photo of receipt selected to server
                                                uploadFile(uploadFilePath6 + "" + uploadFileName6);
                                                // insertCallInfo();

                                            }
                                        }).start();
                                    }
                                    if (isSelected1 == 1) {
                                        upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=171&DocName=" + attachment1+"&Ext11="+ext1;

                                        dialog = ProgressDialog.show(MessageTemplate.this, "", "Uploading Payment Receipt", true);

                                        new Thread(new Runnable() {
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //to upload photo of receipt selected to server
                                                uploadFile(uploadFilePath1 + "" + uploadFileName1);
                                                // insertCallInfo();

                                            }
                                        }).start();
                                    }
                                    if (isSelected2 == 1) {
                                        upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=171&DocName=" + attachment2+"&Ext11="+ext2;

                                      //  dialog = ProgressDialog.show(MessageTemplate.this, "", "Uploading mail template attachment2", true);

                                        new Thread(new Runnable() {
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //to upload photo of receipt selected to server
                                                uploadFile(uploadFilePath2 + "" + uploadFileName2);
                                                // insertCallInfo();

                                            }
                                        }).start();
                                    }
                                    if (isSelected3 == 1) {
                                        upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=171&DocName=" + attachment3+"&Ext11="+ext3;

                                        dialog = ProgressDialog.show(MessageTemplate.this, "", "Uploading mail template attachment3", true);

                                        new Thread(new Runnable() {
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //to upload photo of receipt selected to server
                                                uploadFile(uploadFilePath3 + "" + uploadFileName3);
                                                // insertCallInfo();

                                            }
                                        }).start();
                                    }
                                    if (isSelected4 == 1) {
                                        upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=171&DocName=" + attachment4+"&Ext11="+ext4;

                                        dialog = ProgressDialog.show(MessageTemplate.this, "", "Uploading mail template attachment4", true);

                                        new Thread(new Runnable() {
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //to upload photo of receipt selected to server
                                                uploadFile(uploadFilePath4 + "" + uploadFileName4);
                                                // insertCallInfo();

                                            }
                                        }).start();
                                    }
                                    if (isSelected5 == 1) {
                                        upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=171&DocName=" + attachment5+"&Ext11="+ext5;

                                        dialog = ProgressDialog.show(MessageTemplate.this, "", "Uploading mail template attachment1", true);

                                        new Thread(new Runnable() {
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //to upload photo of receipt selected to server
                                                uploadFile(uploadFilePath5 + "" + uploadFileName5);
                                                // insertCallInfo();

                                            }
                                        }).start();
                                    }



                                } else {
                                    Toast.makeText(MessageTemplate.this, "Template not saved", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
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
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
                                alertDialogBuilder.setTitle("Network issue!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(MessageTemplate.this, "Network issue", Toast.LENGTH_SHORT).show();
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
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
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
    }//close insertMsgTemplate
    public void insertMsgTemplate() {
        if(CheckServer.isServerReachable(MessageTemplate.this)) {
            url = clienturl + "?clientid=" + clientid + "&caseid=8&cSmsText=" + msg + "&IsActive=1&nCounselorID=" + counselorid;
            Log.d("MsgTemplateUrl", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("*******", response.toString());
                            try {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("TemplateMsgResponse", response);
                                if (response.contains("Data inserted successfully")) {
                                    edtMeggase.setText("");
                                    Intent intent=new Intent(MessageTemplate.this,MessageTemplate.class);
                                    intent.putExtra("Activity",activity);
                                    startActivity(intent);
                                    Toast.makeText(MessageTemplate.this, "Template saved successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MessageTemplate.this, "Template not saved", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
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
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
                                alertDialogBuilder.setTitle("Network issue!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(MessageTemplate.this, "Network issue", Toast.LENGTH_SHORT).show();
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
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
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
        }//close insertMsgTemplate

    @Override
    public void onBackPressed() {
        try{
        Intent intent=new Intent(MessageTemplate.this, Home.class);
        intent.putExtra("Activity","MessageTemplate");
        startActivity(intent);
        finish();
        //super.onBackPressed();
        }
        catch (Exception e)
        {
            Log.d("Exception", String.valueOf(e));
        }
       /* Intent intent=new Intent(MessageTemplate.this,Home.class);
        startActivity(intent);*/
    }
}
