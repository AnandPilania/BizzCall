package com.bizcall.wayto.mentebit13;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import in.gauriinfotech.commons.Commons;

public class MainActivity extends AppCompatActivity {
    TextView txtManufacture, txtBrand, txtModel, txtSDK, txtVersion, txtAutoReocrdPath, txtBrowsePath, txtBrowse, txtDeviceInfo, txtFolderPath, txtAlertOK;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    public static String sharedPath, callPhoneno, callDuration, finalCallType, strdateFormated, devicedate = "", recordingName;
    int sharedvalue = 0;
    ImageView imgRefresh,imgBack;
    int recsame = 0, minrec = 0;
    ArrayList<DataAutoCallRecord> arrayListCallLogs;
    public static ArrayList<String> arrayListCallRecording, arrayListRemaingRec;
    RecyclerView recyclerView;
    AdapterCallRecording adapterCallRecording;
    public static String strBrand = "", recName = "", calllogdate = "", strManufacture, strModel, strSDK, strVersion;
    int checkP = 0;
    ProgressDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("Settings", 0); // 0 - for private mode
        imgBack=findViewById(R.id.img_back);
        imgRefresh=findViewById(R.id.imgRefresh);
        txtBrowse = findViewById(R.id.txt_browse);
        txtBrowsePath = findViewById(R.id.txt_browsepath);
        txtDeviceInfo = findViewById(R.id.btn_deviceinfo);
        recyclerView = findViewById(R.id.recycler_recording);
        arrayListCallLogs = new ArrayList<>();
        arrayListRemaingRec = new ArrayList<>();
        arrayListRemaingRec.clear();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG}, 1);
            checkP = 0;
            deviceInfo();
            return;
        } else {
            deviceInfo();
            // getCalllogDetails();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(MainActivity.this,Home.class);
        intent.putExtra("Activity","CallLogRecording");
        startActivity(intent);
        finish();
    }

    public void deviceInfo() {
        sharedPath = sp.getString("recordingbrowsepath", "Select the folder path.");
        Log.d("sharedvalue", sharedPath + "");

        strManufacture = Build.MANUFACTURER;
        strBrand = Build.BRAND;
        strModel = Build.MODEL;
        strSDK = Build.VERSION.SDK;
        strVersion = Build.VERSION.RELEASE;

        txtBrowsePath.setText(sharedPath);
        if (!sharedPath.contains("Select")) {
            ListRoot(new File(sharedPath));
        }

        txtBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sharedvalue", checkP + "");
                if (checkP == 0) {
                    if (txtBrowsePath.getText().toString().contains("Select")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, 2);
                        //  }
                    } else {
                        Toast.makeText(MainActivity.this, "Already selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txtDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.lay_alertdeviceinfo, null);
                dialogBuilder.setView(dialogView);

                txtManufacture = dialogView.findViewById(R.id.txt_manufacture);
                txtBrand = dialogView.findViewById(R.id.txt_brand);
                txtModel = dialogView.findViewById(R.id.txt_model);
                txtSDK = dialogView.findViewById(R.id.txt_sdk);
                txtVersion = dialogView.findViewById(R.id.txt_version);
                txtAutoReocrdPath = dialogView.findViewById(R.id.txt_settingpath);
                txtFolderPath = dialogView.findViewById(R.id.txt_folderpath);
                txtAlertOK = dialogView.findViewById(R.id.txt_alertok);

                txtManufacture.append(strManufacture);
                txtBrand.append(strBrand);
                txtModel.append(strModel);
                txtSDK.append(strSDK);
                txtVersion.append(strVersion);

                if (strBrand.contains("xiaomi")) {
                    txtAutoReocrdPath.setText("Open Dialer -> Setting -> Call Recording -> Record calls automatically");
                    txtFolderPath.setText("Phone Memory -> MIUI -> sound_recorder -> call_rec");
                } else if (strBrand.contains("realme")) {
                    txtAutoReocrdPath.setText("Open Dialer -> Setting -> Call Recording -> Record all calls");
                    txtFolderPath.setText("Phone Storage -> Music -> Recordings -> Call Recordings");
                } else if (strBrand.contains("oneplus")) {
                    txtAutoReocrdPath.setText("Open Dialer -> Setting -> Auto Call Recording");
                    txtFolderPath.setText("");
                } else if (strBrand.contains("oppo")) {
                    txtAutoReocrdPath.setText("Open Dialer -> Setting -> Call Recording -> Record all calls");
                    txtFolderPath.setText("");
                } else if (strBrand.contains("samsung")) {
                    txtAutoReocrdPath.setText("Open Dialer -> Setting -> Record Calls ->Auto record calls");
                    txtFolderPath.setText("Phone Memory -> Sounds -> Voice");
                } else if (strBrand.contains("vivo")) {
                    txtAutoReocrdPath.setText("Settings -> System Apps -> Phone -> Recording -> Automatic call recording");
                    txtFolderPath.setText("Phone Memory -> Recorder");
                } else if (strBrand.contains("nokia")) {
                    txtAutoReocrdPath.setText("Auto Call Recording facility is not supported in this device.");
                    txtFolderPath.setText("Folder is not prsent.");
                } else if (strBrand.contains("motorola")) {
                    txtAutoReocrdPath.setText("Auto Call Recording facility is not supported in this device.");
                    txtFolderPath.setText("Folder is not prsent.");
                }

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();

                txtAlertOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String FilePath = data.getData().getPath();
        String FileName = data.getData().getLastPathSegment();
        int lastPos = FilePath.length() - FileName.length();
        String Folder1 = FilePath.substring(0, lastPos);
        Log.d("recordfolderpath", FileName + " / " + Folder1 + " / " + FilePath);
        Uri selectedImage = data.getData();
        //  File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"");
        File root = new File(Environment.getExternalStorageDirectory() + "");
        Log.d("Pathrecording", root + "");
        String folrderPath = "";
        if (Build.VERSION.SDK_INT >= 29) {
            folrderPath = root + "/" + FileName.substring(FileName.indexOf(":") + 1, FileName.lastIndexOf("/"));
        } else {
            if (FilePath.startsWith("/external")) {
                folrderPath = Commons.getPath(selectedImage, getApplicationContext());
                folrderPath = folrderPath.substring(0, folrderPath.lastIndexOf("/"));
            }
            //folrderPath = FilePath.substring(0, FilePath.lastIndexOf("/"));
            // folrderPath = root +FilePath.substring(0, FilePath.lastIndexOf("/"));
        }
        Log.d("lastpath", folrderPath);
        txtBrowsePath.setText(folrderPath);

        editor = sp.edit();
        editor.putString("recordingbrowsepath", folrderPath);
        editor.putInt("flag", 1);
        editor.apply();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        // ListRoot(new File(folrderPath));
    }

    void ListRoot(File f) {
        dialog = ProgressDialog.show(MainActivity.this, "", "Loading call log", true);
        try {
            File[] files = f.listFiles();
            try {
                Log.d("Files", files + "");
            } catch (Exception e) {
                Log.d("Exc", e.toString());
            }
            arrayListCallRecording = new ArrayList<>();
            arrayListCallRecording.clear();
            for (File file1 : files) {
                arrayListCallRecording.add(file1.getName());
            }
            Log.d("listFile", arrayListCallRecording.size() + "");

            getCalllogDetails();

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            //recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(linearLayoutManager);

            /*for (int i = 0; i < arrayListCallRecording.size(); i++) {
                for (int j = 0; j < arrayListCallLogs.size(); j++) {
                    if (strBrand.contains("realme")) {
                        recName = arrayListCallRecording.get(i).substring(arrayListCallRecording.get(i).indexOf("-") + 1, arrayListCallRecording.get(i).lastIndexOf("."));
                        calllogdate = arrayListCallLogs.get(j).getLeadRealme();
                    } else if (strBrand.contains("xiaomi")) {
                        recName = arrayListCallRecording.get(i).substring(arrayListCallRecording.get(i).indexOf("_") + 1, arrayListCallRecording.get(i).lastIndexOf(".") - 2);
                        calllogdate = arrayListCallLogs.get(j).getLeadRealme();
                    } else if (strBrand.contains("samsung")) {
                        //String rec=arrayListCallRecording.get(i).replace(arrayListCallRecording.get(i).indexOf(arrayListCallRecording.get(i).lastIndexOf("_")),"");
                        recName = arrayListCallRecording.get(i).substring(arrayListCallRecording.get(i).indexOf("_") + 1, arrayListCallRecording.get(i).lastIndexOf(".") - 2);
                        recName = recName.replace("_", "");
                        calllogdate = arrayListCallLogs.get(j).getLeadRealme();
                    }
                    //Log.d("samerecord", recName+" / "+calllogdate+ " / "+ count);

                    if (recName.contains(calllogdate)) {
                        count++;
                        Log.d("samerecord1", recName + " / " + calllogdate + " / " + count);
                        DataRecordingCalls dr = new DataRecordingCalls(arrayListCallLogs.get(j).getLeadName(), arrayListCallLogs.get(j).getLeadNumber(), arrayListCallLogs.get(j).getLeadDateCompare(), arrayListCallLogs.get(j).getLeadRealme(), arrayListCallRecording.get(i));
                        arrayListRecfound.add(dr);
                    }
                }
            }
            ArrayList<String> list1 = new ArrayList<String>();
            for (int i = 0; i < arrayListCallRecording.size(); i++) {
                if (strBrand.contains("realme")) {
                    list1.add(arrayListCallRecording.get(i).substring(arrayListCallRecording.get(i).indexOf("-") + 1, arrayListCallRecording.get(i).lastIndexOf(".")));
                } else if (strBrand.contains("xiaomi")) {
                    list1.add(arrayListCallRecording.get(i).substring(arrayListCallRecording.get(i).indexOf("_") + 1, arrayListCallRecording.get(i).lastIndexOf(".") - 2));
                } else if (strBrand.contains("samsung")) {
                    String rec = arrayListCallRecording.get(i).substring(arrayListCallRecording.get(i).indexOf("_") + 1, arrayListCallRecording.get(i).lastIndexOf(".") - 2);
                    list1.add(rec.replace("_", ""));
                }

                //Log.d("list1", list1.get(i));
            }

            ArrayList<String> list2 = new ArrayList<String>();
            for (int i = 0; i < arrayListRecfound.size(); i++) {
                if (strBrand.contains("realme")) {
                    list2.add(arrayListRecfound.get(i).getStrRecording().substring(arrayListRecfound.get(i).getStrRecording().indexOf("-") + 1, arrayListRecfound.get(i).getStrRecording().lastIndexOf(".")));
                } else if (strBrand.contains("xiaomi")) {
                    list2.add(arrayListRecfound.get(i).getStrRecording().substring(arrayListRecfound.get(i).getStrRecording().indexOf("_") + 1, arrayListRecfound.get(i).getStrRecording().lastIndexOf(".") - 2));
                } else if (strBrand.contains("samsung")) {
                    String rec = arrayListRecfound.get(i).getStrRecording().substring(arrayListRecfound.get(i).getStrRecording().indexOf("_") + 1, arrayListRecfound.get(i).getStrRecording().lastIndexOf(".") - 2);
                    list2.add(rec.replace("_", ""));
                }

                //Log.d("list2", list2.get(i));
            }
            // Prepare a union
            ArrayList<String> union = new ArrayList<String>(list1);
            union.addAll(list2);
            // Prepare an intersection
            List<String> intersection = new ArrayList<String>(list1);
            intersection.retainAll(list2);
            // Subtract the intersection from the union
            union.removeAll(intersection);
            // Print the result
            for (String n : union) {
                //Log.d("union", n);
                long num = Long.parseLong(n);
                String nextrec = String.valueOf(num - 1);
                //Log.d("nextrec", nextrec);
                arrayListRemaingRec.add(nextrec);
            }

            for (int i = 0; i < arrayListRemaingRec.size(); i++) {
                for (int j = 0; j < arrayListCallLogs.size(); j++) {
                    String recName = arrayListRemaingRec.get(i);
                    if (strBrand.contains("realme")) {
                        calllogdate = arrayListCallLogs.get(j).getLeadRealme();
                    } else if (strBrand.contains("xiaomi")) {
                        calllogdate = arrayListCallLogs.get(j).getLeadRealme();
                    } else if (strBrand.contains("samsung")) {
                        calllogdate = arrayListCallLogs.get(j).getLeadRealme();
                    }
                    if (recName.contains(calllogdate)) {
                        count++;
                        long num = Long.parseLong(recName);
                        String nextrec = String.valueOf(num + 1);
                        Log.d("nextrec", nextrec);

                        for (int k = 0; k < arrayListCallRecording.size(); k++) {
                            String aaa = arrayListCallRecording.get(k);
                            if (aaa.contains(nextrec)) {
                                Log.d("1minrec", nextrec + " / " + arrayListCallRecording.get(k));
                                DataRecordingCalls dr = new DataRecordingCalls(arrayListCallLogs.get(j).getLeadName(), arrayListCallLogs.get(j).getLeadNumber(), arrayListCallLogs.get(j).getLeadDateCompare(), arrayListCallLogs.get(j).getLeadRealme(), arrayListCallRecording.get(k));
                                arrayListRecfound.add(dr);
                            }
                        }
                    }
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //function for getting call logs from mobile
    public void getCalllogDetails() {
        try {
            Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
            int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            int phAccID = managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);


            arrayListCallLogs.clear();

            while (managedCursor.moveToNext()) {
                String callName = managedCursor.getString(name);
                callPhoneno = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                callDuration = managedCursor.getString(duration);
                //callphAccID = managedCursor.getString(phAccID);
                finalCallType = "";

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
                SimpleDateFormat sdfSaveArray = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                strdateFormated = sdfSaveArray.format(callDayTime);
                if (strBrand.contains("realme") || strBrand.contains("samsung")) {
                    SimpleDateFormat sdfrealme = new SimpleDateFormat("yyMMddHHmm");
                    devicedate = sdfrealme.format(callDayTime);
                } else if (strBrand.contains("xiaomi")) {
                    SimpleDateFormat sdfMIUI = new SimpleDateFormat("yyyyMMddHHmm");
                    devicedate = sdfMIUI.format(callDayTime);
                }

                if (callName == null) {
                    callName = callPhoneno;
                }
                if (callPhoneno.length() >= 10) {
                    if (!finalCallType.contains("MISSED")) {
                        String s = callPhoneno.replace(" ", "");
                        String ss = s.substring(s.length() - 10);
                        recordingName = "null";

                        for (int j = 0; j < arrayListCallRecording.size(); j++) {
                            // if(j<MainActivity.arrayListCallRecording.size())
                            {
                                if (strBrand.contains("realme")) {
                                    recName = arrayListCallRecording.get(j).substring(arrayListCallRecording.get(j).indexOf("-") + 1, arrayListCallRecording.get(j).lastIndexOf("."));
                                } else if (MainActivity.strBrand.contains("xiaomi")) {
                                    recName = arrayListCallRecording.get(j).substring(arrayListCallRecording.get(j).indexOf("_") + 1, arrayListCallRecording.get(j).lastIndexOf(".") - 2);
                                } else if (MainActivity.strBrand.contains("samsung")) {
                                    recName = arrayListCallRecording.get(j).substring(arrayListCallRecording.get(j).indexOf("_") + 1, arrayListCallRecording.get(j).lastIndexOf(".") - 2);
                                    recName = recName.replace("_", "");
                                }

                                long num = Long.parseLong(recName);
                                String nextrec = String.valueOf(num - 1);
                                //Log.d("recompare", devicedate + " / " + recName);

                                if (devicedate.equals(recName)) {
                                    recsame++;
                                    //Log.d("samerecord1", recName + " / " + devicedate + " / " + recsame);
                                    if (!callDuration.equals("0")) {
                                        recordingName = arrayListCallRecording.get(j);
                                    }
                                } else if (devicedate.equals(nextrec)) {
                                    minrec++;
                                    long num1 = Long.parseLong(nextrec);
                                    String nextrec1 = String.valueOf(num1 + 1);

                                    String aaa = recName;
                                    if (aaa.contains(nextrec1)) {
                                        //Log.d("1minrecord", nextrec1 + " / " + devicedate + " / " + minrec+" / "+callName);
                                        if (!callDuration.equals("0")) {
                                            recordingName = arrayListCallRecording.get(j);
                                        }
                                    }
                                } else if (Integer.parseInt(callDuration) < 5) {
                                    recordingName = "null";
                                }
                            }
                        }
                        //Log.d("inrecord", recordingName + " / " + callName);
                        //if (recordingName.contains(callName) || recordingName.equals("null")){
                        Log.d("ssss", callName + " / " + ss + " / " + strdateFormated + " / " + callDuration + " / " + recordingName);
                        DataAutoCallRecord dataLeadInfo = new DataAutoCallRecord(callName, ss, finalCallType, devicedate, strdateFormated, callDuration, recordingName);
                        arrayListCallLogs.add(dataLeadInfo);
                        //}
                    }
                }
            }
            Collections.reverse(arrayListCallLogs);
            adapterCallRecording = new AdapterCallRecording(arrayListCallLogs, MainActivity.this);
            recyclerView.setAdapter(adapterCallRecording);
            adapterCallRecording.notifyDataSetChanged();
            dialog.dismiss();
            Log.d("arrayListCallLogs", arrayListCallLogs.size() + " / " + devicedate);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "ActivityHome : Plugins-007", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}