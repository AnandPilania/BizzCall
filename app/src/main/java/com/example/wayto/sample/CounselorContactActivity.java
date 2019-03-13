package com.example.wayto.sample;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CounselorContactActivity extends FragmentActivity {

    static final int TIME_DIALOG_ID = 1111;
    int pos;
    String fetchedName,fetchedCourse,fetchedMobile,fetchedAddress,fetchedCity,fetchedState,fetchedPin,fetchedParentNo,fetchedEmail,fetchedDataFrom,fetchedAllocatedTo,fetchedAllocationDate,fetchedCurrentStatus,fetchedRemarks,fetchedCreatedDate,fetchedStatus;
    int flag=0,flag1=0,flag2=0,call1,call2;
    long timeout;
    String clienturl;
    ArrayList<String> arrayListStatusId;
    public static TextView txtSelectedCname, txtSelecteCId, txtSelctedSrNo;
    public static LinearLayout linearReallocateTo, linearSelectDate, linearSelectTime;
    TextView txtSrno, txtCName, txtCourse, txtCount1, txtMobile, txtSMS,txtCurrentStatus,txtRemark,txtCoin;
    CardView cardAction, cardInfo;
    ImageView imgBack, imgClock,imgCoin;
    EditText edtName1, edtAddress1, edtCity1, edtPincode1, edtParent1, edtEmail1;
    Spinner spinnerState1;
    Button btnCall, btnMsg, btnMail, btnDetails, btnCancel;
    LinearLayout linearLayout;
    int eventid;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String name11, address1, city1, state11, parentno1, pincode1,status11,statusid,remark,enteredRemark;
    String msg, text1, smbl, mbl, sms, clientid, name, course, sr_no, email, email1, activityName, remarks, path1, duration1, callDate, id1, parentno;
    String adrs, city, state1, pincode,updatedStatus;
    Intent intent;
    int status1;
    ArrayList<String> arraySms;
    UrlRequest urlRequest;
    boolean isClicked;
    String uploadFilePath = "";
    String uploadFileName = "";
    ProgressDialog dialog, dialog1;
    AlertDialog alertDialog;
    TextView txtName, txtCloseWindow, txtTime, txtSelect,txtReallocationTitle,txtReportTitle;
    LinearLayout linearReport;
    EditText edtMessage, edtMobile;
    Spinner spinnerTemplate;
    ImageView imgCloseWindow, imgClearText;
    Button btnSms, btnWhatsapp, btnUpload, btnReminder, btnReallocate;
    Spinner spinnerStatus, spinnerMobile;
    TextView edtRemarks;
    ImageView imgCalender;
    TextView txtDateView,txtReminderTitle,txtDiamond;
    int serverResponseCode = 0;
    LinearLayout linearReallocation;
    String upLoadServerUri = null;
    ArrayList<String> arrayList1;
    DataListCounselor dataListCounselor;
    RecyclerView recyclerCounselorDetails;
    ArrayList<DataListCounselor> arrayListCounselorDetails;
    int year, day, month;
    String time11, date11, selectedmbl, allocatedDate, newAllocatedTo,totalcoins;
    private int hour;
    private int minute;
    LinearLayout linearReminder;
    private TabLayout tabLayout;
    private ViewPager viewPager;



    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;
            updateTime(hour, minute);
        }
    };
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    Calendar myCalendar = Calendar.getInstance();
                    //arg0.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    myCalendar.set(Calendar.YEAR, arg1);
                    myCalendar.set(Calendar.MONTH, arg2);
                    myCalendar.set(Calendar.DAY_OF_MONTH, arg3);
                    String myFormat = "yyyy/MM/dd"; //Change as you need
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                    txtDateView.setText(sdf.format(myCalendar.getTime()));

                    day = arg1;
                    month = arg2;
                    year = arg3;
                    showDate(day, month + 1, year);
                }
            };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_contact);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        id1 = sp.getString("Id", null);
        id1 = id1.replaceAll(" ", "");
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        timeout=sp.getLong("TimeOut",0);
        Log.d("ClientId", clientid);
        totalcoins=sp.getString("TotalCoin",null);
        editor = sp.edit();
            arrayListStatusId=new ArrayList<>();


      /*  updateStatus=sp.getInt("UpdateStatus",0);
        call=sp.getInt("Call",0);
        confirmstatus=sp.getInt("ConfirmSuccess",0);
        reminder=sp.getInt("Reminder",0);
        remark1=sp.getInt("Remark1",0);
        reallocation=sp.getInt("Reallocation",0);*/
        activityName = getIntent().getStringExtra("ActivityName");
        Log.d("ActivityName",activityName);
        sr_no = sp.getString("SelectedSrNo", null);
        getCounselorData();

        txtCoin=findViewById(R.id.txtCoin);
        imgCoin=findViewById(R.id.imgCoin);
        txtDiamond=findViewById(R.id.txtDiamond);
        txtSelctedSrNo = findViewById(R.id.txtSelectedSrno);
        txtSelecteCId = findViewById(R.id.txtSelectedCounselorId);
        txtSelectedCname = findViewById(R.id.txtSelectedCName);
        linearReallocateTo = findViewById(R.id.linearReallocateTo);
        linearSelectDate = findViewById(R.id.linearSelectDate);
        linearSelectTime = findViewById(R.id.linearSelectTime);
        linearReport=findViewById(R.id.linearReport);
        txtReportTitle=findViewById(R.id.txtReportTitle);
        txtCName = findViewById(R.id.txtCandidateName1);
        txtCourse = findViewById(R.id.txtCourse1);
        txtSrno = findViewById(R.id.txtSrNo1);
        btnCall = findViewById(R.id.btnCall);
        btnMsg = findViewById(R.id.btnMessage);
        btnMail = findViewById(R.id.btnMail);
        btnDetails = findViewById(R.id.btnDetails);
        spinnerStatus = findViewById(R.id.spinnerStatus11);
        imgBack = findViewById(R.id.img_back);
        imgCalender = findViewById(R.id.imgCalender);
        txtTime = findViewById(R.id.txtSelectTime);
        imgClock = findViewById(R.id.imgClock);
        txtDateView = findViewById(R.id.txtSelectedDate);
        btnReminder = findViewById(R.id.btnReminder);
        spinnerMobile = findViewById(R.id.spinnerMobile);
        btnReallocate = findViewById(R.id.btnReallocate);
        btnCancel = findViewById(R.id.btnCancel1);
        txtReallocationTitle=findViewById(R.id.txtReallocationTitle);
        linearReallocation=findViewById(R.id.linearReallocation);
        txtCurrentStatus=findViewById(R.id.txtCurrentStatus);
        txtRemark=findViewById(R.id.txtRemarks);
        //txtSrno=findViewById(R.id.txtSerialNo1);
        // txtName=findViewById(R.id.txtCName);
        // txtMobile=findViewById(R.id.txtMobNumber);
        //txtPath=findViewById(R.id.txtPath1);
        edtRemarks = findViewById(R.id.edtRemarks);
        txtReminderTitle=findViewById(R.id.txtReminderTitle);
        linearReminder=findViewById(R.id.linearReminder);
        btnUpload = findViewById(R.id.btnUploadStatus);
        arrayList1 = new ArrayList<>();
        recyclerCounselorDetails = findViewById(R.id.recyclerCounselorDetails);
        arrayListCounselorDetails = new ArrayList<>();


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        txtCoin.setText(totalcoins);
        imgCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CounselorContactActivity.this,PointCollectionDetails.class));
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Remarks"));
        tabLayout.addTab(tabLayout.newTab().setText("Status"));
        tabLayout.addTab(tabLayout.newTab().setText("Call"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        upLoadServerUri = clienturl+"?clientid=" + clientid + "&caseid=4";
        //  getStatus1();
       /* mbl = sp.getString("SelectedMobile", null);
        parentno = sp.getString("SelectedParentNo", null);
        name = sp.getString("SelectedName", null);
        course = sp.getString("SelectedCourse", null);
        sr_no = sp.getString("SelectedSrNo", null);
        email = sp.getString("SelectedEmail", null);
        allocatedDate = sp.getString("AllocatedDate", null);
        adrs = sp.getString("SelectedAddress", null);
        city = sp.getString("SelectedCity", null);
        state1 = sp.getString("SelectedState", null);
        pincode = sp.getString("SelectedPinCode", null);
        status11=sp.getString("SelectedStatus",null);
        statusid=sp.getString("SelectedStatusId",null);
        remark=sp.getString("SelectedRemark",null);
*/
        //Log.d("Mbl", mbl);
        //Log.d("Status***",status11);

     //   getStatus1();
        getCounselorDetails();



        // selectedmbl=spinnerMobile.getSelectedItem().toString();
        // Log.d("SMbl",selectedmbl);
        dialog = ProgressDialog.show(CounselorContactActivity.this, "", "Loading...", true);
       /* new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                    }
                });*/

                /*  }
        }).start();*/
        if (activityName.contains("ServiceRecord")) {
            path1 = sp.getString("RecordedFile", null);
            duration1 = sp.getString("Dur", null);
            Log.d("Path!!!", path1);
           // int d1= Integer.parseInt(duration1);

         /*   if(20)
            {
                eventid=11;
                insertPointCollection(eventid);

            }
            else if(d1>60)
            {
                eventid=12;
                insertPointCollection(eventid);
            }*/
            uploadFilePath = path1.substring(path1.indexOf("/storage"), path1.indexOf("MyRecords/") + 10);
            uploadFileName = path1.substring(path1.indexOf("MyRecords/") + 10);
            callDate = uploadFileName.substring(11, 21);
            //callDate="1_12_2018_";

            if (callDate.endsWith("_")) {
                callDate = callDate.substring(0, callDate.length() - 1);
            }
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                        }
                    });

            uploadFile(uploadFilePath + "" + uploadFileName);
            insertCallInfo();

               }
            }).start();
        }
        txtReminderTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(linearReminder.getVisibility()==View.GONE)
                {
                    linearReminder.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearReminder.setVisibility(View.GONE);
                }
            }
        });

        txtReallocationTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearReallocation.getVisibility()==View.GONE)
                {
                    linearReallocation.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearReallocation.setVisibility(View.GONE);
                }
            }
        });

        txtReportTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearReport.getVisibility()==View.GONE)
                {
                    linearReport.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearReport.setVisibility(View.GONE);
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time1 = txtTime.getText().toString();
                String date12 = txtDateView.getText().toString();
                date12 = date12 + " " + "00:00:00.000000";
                try {
                    date11 = URLEncoder.encode(date12, "UTF-8");
                    time11 = URLEncoder.encode(time1, "UTF-8");
                    Log.d("EncodedDate", date11);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                setReminder();

                //  Calendar cal = Calendar.getInstance();
               /* Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", time1);
                intent.putExtra("allDay", false);
                intent.putExtra("rrule", "FREQ=DAILY");
                intent.putExtra("endTime", time1+60*60*1000);
                intent.putExtra("title", "A Test Event from android app");
                startActivity(intent);*/

            }
        });

        btnReallocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAllocatedTo = sp.getString("SCID", null);
                setReallocation();
                setReallocateDatabaselist();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // txtSelctedSrNo.setText("");
                //txtSelecteCId.setText("");
                //txtSelectedCname.setText("");
                linearReallocateTo.setVisibility(View.GONE);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getStatus1();
                enteredRemark=edtRemarks.getText().toString();
                flag=0;
                if(enteredRemark.length()==0)
                {
                    edtRemarks.setError("Please enter remark");
                    flag=1;
                }
                if(flag==0)
                {
                    remarkBackup();
                }
                updateStatus();
                statusBackup();
               /*// if(flag1==1 || flag2==1)
                {

                }*/
                }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int pos = viewHolder.getAdapterPosition();
                // DataCounselor dataCounselor1 = arrayList.get(pos);

                editor.putString("Sr.No", sr_no);
                editor.putString("CName", name);
                editor.putString("MobileNo", selectedmbl);
                editor.commit();
                // Toast.makeText(context,"SrNo:"+dataCounselor1.getSr_no(),Toast.LENGTH_SHORT).show();
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                //Creating a view to get the dialog box
                View confirmCall = li.inflate(R.layout.layout_confirm_call, null);

                //confirmCall.setClipToOutline(true);

                TextView txtYes = (TextView) confirmCall.findViewById(R.id.txtYes);
                TextView txtNo = (TextView) confirmCall.findViewById(R.id.txtNo);
                AlertDialog.Builder alert = new AlertDialog.Builder(CounselorContactActivity.this);
                //Adding our dialog box to the view of alert dialog
                alert.setView(confirmCall);
                //Creating an alert dialog
                alertDialog = alert.create();
                alertDialog.show();

                txtYes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + selectedmbl + ""));
                       // call=call+2;

                        //Toast.makeText(CounselorContactActivity.this,"Call"+String.valueOf(call),Toast.LENGTH_SHORT).show();
                        eventid=2;
                        insertPointCollection(eventid);
                        //intent.setAction("android.intent.action.CALL");
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            startActivity(intent);
                            // audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
                            alertDialog.dismiss();
                        }
                    }
                });

                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(CounselorContactActivity.this);
                //Creating a view to get the dialog box
                View template = li.inflate(R.layout.layout_msg_template, null);
                txtCloseWindow = template.findViewById(R.id.txtCloseWindow);
                imgCloseWindow = template.findViewById(R.id.imgCloseWindow);
                edtMobile = template.findViewById(R.id.edtMobileNo);
                txtName = template.findViewById(R.id.txtUserName1);
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

                edtMobile.setText("+" + selectedmbl);
                txtName.setText(name);
                smbl = edtMobile.getText().toString();
                dialog = ProgressDialog.show(CounselorContactActivity.this, "Loading", "Please wait.....", false, true);
                arraySms = new ArrayList<>();
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(CounselorContactActivity.this);
                urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=7&nCounselorID=" + cid + "&IsActive=1");
                Log.d("TemplatUrl", "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=7&nCounselorID=" + cid + "&IsActive=1");
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
                                arraySms.add(sms);
                            }
                            arraySms.add(0, "Select Message From Template");
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CounselorContactActivity.this, R.layout.spinner_item1, arraySms);

                            // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTemplate.setAdapter(arrayAdapter);
                            Log.d("Size**", String.valueOf(arraySms.size()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // String msg=response.substring(response.indexOf( "( [cSmsText] =>"+15),response.indexOf(")"));


                    }
                });
                AlertDialog.Builder alert = new AlertDialog.Builder(CounselorContactActivity.this);
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

                btnWhatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        msg = edtMessage.getText().toString();
                        smbl = edtMobile.getText().toString();
                        //int pos = viewHolder.getAdapterPosition();
                        //  DataCounselor dataCounselor1 = arrayList.get(pos);

                        if (msg.length() != 0) {
                            try {
                                setSmsEntry();
                                eventid=3;
                                insertPointCollection(eventid);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + smbl + "&text=" + msg));
                                startActivity(intent);
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
                            setSmsEntry();
                            eventid=3;
                            insertPointCollection(eventid);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + smbl));
                            intent.putExtra("sms_body", msg);
                            startActivity(intent);

                        } else {
                            edtMessage.setError("Please enter message");
                        }
                    }
                });
            }
        });
        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String mail=dataCounselor.getEmail();

                eventid=4;
                insertPointCollection(eventid);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                //  i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                // i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    // context.startActivity(i);
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CounselorContactActivity.this,DetailsActivity.class));
                           }
        });

        linearSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });
        linearSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if(activityName.contains("AdapterSearch"))
        {
            startActivity(new Intent(CounselorContactActivity.this,Home.class));
        }
        else if(activityName.contains("ReminderActivity"))
        {
            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            //Creating a view to get the dialog box
            View confirmCall = li.inflate(R.layout.layout_confirm_call, null);

            //confirmCall.setClipToOutline(true);

            TextView txtYes = (TextView) confirmCall.findViewById(R.id.txtYes);
            TextView txtTitle=confirmCall.findViewById(R.id.txtConfirmDelete);
            TextView txtNo = (TextView) confirmCall.findViewById(R.id.txtNo);
            txtTitle.setText("Do you want to clear reminder?");
            AlertDialog.Builder alert = new AlertDialog.Builder(CounselorContactActivity.this);
            //Adding our dialog box to the view of alert dialog
            alert.setView(confirmCall);
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();

            txtYes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                   /* intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + selectedmbl + ""));
                    call=call+2;
                    editor.putInt("Call",call);
                    editor.commit();*/
                   // Toast.makeText(CounselorContactActivity.this,"Call"+String.valueOf(call),Toast.LENGTH_SHORT).show();

                    clearReminder();
                    startActivity(new Intent(CounselorContactActivity.this,ReminderActivity.class));
                    }
            });

            txtNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    startActivity(new Intent(CounselorContactActivity.this,ReminderActivity.class));
                }
            });

            }
        else {
            intent = new Intent(CounselorContactActivity.this, CounsellorData.class);
            startActivity(intent);
        }
    }
    public void clearReminder()
    {
       String callId=sp.getString("SelectedCallingId",null);
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=35&cUpdatedBy=" +id1+"&nSrNo=" + sr_no+"&nCallingId="+callId);
        Log.d("ClearReminder", "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=35&cUpdatedBy=" +id1+"&nSrNo="+sr_no+"&nCallingId="+callId );
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();

                Log.d("ClearReminderResonse", response);
                if (response.contains("Row updated successfully"))
                {
                    eventid=14;
                    insertPointCollection(eventid);
                    Toast.makeText(CounselorContactActivity.this, "Cleared reminder", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "Reminder not cleared", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "P.M.";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "A.M.";
        } else if (hours == 12)
            timeSet = "P.M.";
        else
            timeSet = "A.M.";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        // String aTime = new StringBuilder().append(hours).append(" ").append(timeSet).toString();
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        txtTime.setText(aTime);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    myDateListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }
        if (id == TIME_DIALOG_ID) {

            // set time picker as current time
            return new TimePickerDialog(this, timePickerListener, hour, minute,
                    false);
        }

        return null;
    }

    private void showDate(int year, int month, int day) {
        txtDateView.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }


    public void insertPointCollection(int eid)
    {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=36&nCounsellorId=" + id1 + "&nEventId="+eid);
        Log.d("PointCollectionResponse", clienturl+"?clientid=" + clientid + "&caseid=36&nCounsellorId=" + id1 + "&nEventId="+eid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("PointCollectionResponse", response);
                if (response.contains("Data inserted successfully")) {
                    Toast.makeText(CounselorContactActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                }
                //   Log.d("Size**", String.valueOf(arrayList.size()));
            }
        });
    }


    public void getCounselorDetails() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=30");
        Log.d("CounselorUrl", clienturl+"?clientid=" + clientid + "&caseid=30");
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("CounselorResponse1", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Json", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = String.valueOf(jsonObject1.getInt("cCounselorID"));
                        String name = jsonObject1.getString("cCounselorName");
                        dataListCounselor = new DataListCounselor(id, name);
                        arrayListCounselorDetails.add(dataListCounselor);
                    }
                    AdapterListCounselor adapterListCounselor = new AdapterListCounselor(CounselorContactActivity.this, arrayListCounselorDetails);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(CounselorContactActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerCounselorDetails.setLayoutManager(layoutManager);
                    recyclerCounselorDetails.setAdapter(adapterListCounselor);
                    adapterListCounselor.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getCounselorData() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=32&nSrNo="+sr_no);
        Log.d("FetchedCounselorUrl", clienturl+"?clientid=" + clientid + "&caseid=32&nSrNo="+sr_no);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("FetchedResponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Json", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        //String id = String.valueOf(jsonObject1.getInt("cCounselorID"));
                  /*      mbl=fetchedMobile;
                        parentno=fetchedParentNo;
                        name=fetchedName;
                        course=fetchedCourse;
                        email=fetchedEmail;
                        allocatedDate=fetchedAllocationDate;
                        adrs=fetchedAddress;
                        city=fetchedCity;
                        state1=fetchedState;
                        pincode=fetchedPin;
                        status11=fetchedStatus;
                        statusid=fetchedCurrentStatus;
                        remark=fetchedRemarks;*/

                         name = jsonObject1.getString("cCandidateName");
                         course=jsonObject1.getString("cCourse");
                         mbl=jsonObject1.getString("cMobile");
                         adrs=jsonObject1.getString("cAddressLine");
                         city=jsonObject1.getString("cCity");
                         state1=jsonObject1.getString("cState");
                         pincode=jsonObject1.getString("cPinCode");
                         parentno=jsonObject1.getString("cParantNo");
                         email=jsonObject1.getString("cEmail");
                         fetchedDataFrom=jsonObject1.getString("cDataFrom");
                         fetchedAllocatedTo=jsonObject1.getString("AllocatedTo");
                         allocatedDate=jsonObject1.getString("AllocationDate");
                         statusid=jsonObject1.getString("CurrentStatus");
                         remark=jsonObject1.getString("cRemarks");
                         fetchedCreatedDate=jsonObject1.getString("dtCreatedDate");
                         status11=jsonObject1.getString("cStatus");
                         }

                    allocatedDate = allocatedDate.substring(allocatedDate.indexOf("date") + 4, allocatedDate.indexOf(","));
                    Log.d("Date!!!!", allocatedDate);
                    txtCName.setText(name);
                    txtSrno.setText(sr_no);
                    txtCourse.setText(course);
                    txtCurrentStatus.setText(status11);
                    txtRemark.setText(remark);
                    ArrayList<String> arrayListMbl = new ArrayList<>();
                    arrayListMbl.add(mbl);
                    arrayListMbl.add(parentno);

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CounselorContactActivity.this, R.layout.spinner_item1, arrayListMbl);
                    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMobile.setAdapter(arrayAdapter);
                    spinnerMobile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedmbl = spinnerMobile.getSelectedItem().toString();
                            selectedmbl = "91" + selectedmbl;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            spinnerMobile.setSelection(0);
                        }
                    });

                    getStatus1();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void getStatus1() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=2");
        Log.d("StatusUrl1", "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=2");
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("StatusResponse1", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Json", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String status1 = jsonObject1.getString("cStatus");
                        statusid=jsonObject1.getString("nStatusID");
                        Log.d("Status11",status1);
                        //StatusInfo statusInfo=new StatusInfo(status1,statusid);
                            arrayListStatusId.add(statusid);
                        // Log.d("Json33333",statusInfo.toString());
                        //arrayList.add(statusInfo);
                        arrayList1.add(status1);
                        // Log.d("Json11111",arrayList1.toString());
                    }
                    String setselected=status11;

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CounselorContactActivity.this, R.layout.spinner_item1, arrayList1);
                    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatus.setAdapter(arrayAdapter);

                    for(int i=0;i<arrayList1.size();i++)
                    {
                        if(arrayList1.get(i).matches(status11))
                        {
                            spinnerStatus.setSelection(i);
                        }
                    }
                    Log.d("Size**", String.valueOf(arrayList1.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateStatus()
    {
        status1 = Integer.parseInt(arrayListStatusId.get(pos));

        Log.d("updateStatus", String.valueOf(status1));
        // status=status.replaceAll(" ","");
        remarks = edtRemarks.getText().toString();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=5&nSrNo=" + sr_no + "&CurrentStatus=" + status1 + "&cRemarks=" + enteredRemark);
        Log.d("StatusUrl", "http://anilsahasrabuddhe.in/CRM/AnDe828500/updateStatus.php?clientid=" + clientid + "&caseid=5&nSrNo=" + sr_no + "&CurrentStatus=" + status1 + "&cRemarks=" + enteredRemark);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                updatedStatus=spinnerStatus.getSelectedItem().toString();
                Log.d("UStatus",updatedStatus);
                txtCurrentStatus.setText(updatedStatus);
                Log.d("UpdatedStatus", response);
                if (response.contains("Row updated successfully"))
                {
                  eventid=6;
                  insertPointCollection(eventid);
                    /*editor.putInt("UpdateStatus",updateStatus);
                    editor.putInt("Remark1",remark1);
                    editor.commit();*/
                    if(updatedStatus.contains("CONFIRM SUCCESS"))
                    {
                     eventid=7;
                     insertPointCollection(eventid);
                       /* editor.putInt("ConfirmSuccess",confirmstatus);
                        editor.commit();*/
                    }
                    Toast.makeText(CounselorContactActivity.this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "Status not updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void statusBackup()
    {
        status1 =  Integer.parseInt(arrayListStatusId.get(pos));
       // updatedStatus=spinnerStatus.getSelectedItem().toString();
        Log.d("updateStatus", String.valueOf(status1));
        // status=status.replaceAll(" ","");
        //remarks = edtRemarks.getText().toString();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=19&SrNo=" + sr_no + "&StatusId=" + status1 +"&CounsellorId="+id1);
        Log.d("StatusBackupUrl", clienturl+"?clientid=" + clientid + "&caseid=19&SrNo=" + sr_no + "&StatusId=" + status1 +"&CounsellorId="+id1);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                flag2=1;
                Log.d("StatusBackup", response);
                if (response.contains("Data inserted successfully")) {
                    Toast.makeText(CounselorContactActivity.this, "Status inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "Status not inserted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void remarkBackup() {
       // status1 = (int) spinnerStatus.getSelectedItemId();
       // Log.d("updateStatus", String.valueOf(status1));
        // status=status.replaceAll(" ","");
        //remarks = edtRemarks.getText().toString();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=20&SrNo=" + sr_no + "&sRemarks=" + remark +"&CounsellorId="+id1);
        Log.d("RemarkBackupUrl", clienturl+"?clientid=" + clientid + "&caseid=20&SrNo=" + sr_no + "&sRemarks=" + remark +"&CounsellorId="+id1);
        urlRequest.getResponse(new ServerCallback()
        {
            @Override

            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                txtRemark.setText(enteredRemark);
                edtRemarks.setText("");
                Log.d("RemarkBackup", response);
                if (response.contains("Data inserted successfully")) {
                    Toast.makeText(CounselorContactActivity.this, "Remark inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "Remark not inserted successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void setReminder() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=26&nSrNo=" + sr_no +  "&cCounselorID=" + id1 +"&dtCallDateTime=" + date11 + "&cCallTime=" + time11 + "&nStatus=" + String.valueOf(status1) + "&cUpdatedBy=" + id1);
        Log.d("ReminderUrl", clienturl+"?clientid=" + clientid + "&caseid=26&nSrNo=" + sr_no +"&cCounselorID=" + id1 + "&dtCallDateTime=" + date11 +  "&cCallTime=" + time11 + "&nStatus=" + String.valueOf(status1) + "&cUpdatedBy=" + id1);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("ReminderResponse", response);
                if (response.contains("Data inserted successfully"))
                {
                        eventid=9;
                        insertPointCollection(eventid);
                   /* editor.putInt("Reminder",reminder);
                    editor.commit();*/
                    Toast.makeText(CounselorContactActivity.this, "Reminder set successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "Reminder not set successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setSmsEntry()
    {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/SMSEntry.php?nSrNo=" + sr_no + "&cMobileNo=" + smbl + "&cCounselorID=" + id1 + "&cSms=" + msg);
        Log.d("SMSUrl", "http://anilsahasrabuddhe.in/CRM/AnDe828500/SMSEntry.php?nSrNo=" + sr_no + "&cMobileNo=" + smbl + "&cCounselorID=" + id1 + "&cSms=" + msg);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("SMSResponse", response);
                if (response.contains("Data inserted successfully")) {

                    Toast.makeText(CounselorContactActivity.this, "message inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "message not inserted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setReallocation() {
        status1 = Integer.parseInt(arrayListStatusId.get(pos));
        Log.d("updateStatus", String.valueOf(status1));
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/InsertReallocatedData.php?nSrNo=" + sr_no + "&nCurrentStatus=" + status1 + "&nPreAllocatedTo=" + id1 + "&nNewAllocatedTo=" + newAllocatedTo + "&dtPreAllocationDate=" + allocatedDate);
        Log.d("ReallocationUrl", "http://anilsahasrabuddhe.in/CRM/AnDe828500/http://anilsahasrabuddhe.in/CRM/AnDe828500/InsertReallocatedData.php?nSrNo=" + sr_no + "&nCurrentStatus=" + status1 + "&nPreAllocatedTo=" + id1 + "&nNewAllocatedTo=" + newAllocatedTo + "&dtPreAllocationDate=" + allocatedDate);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("ReallocationResponse", response);
                if (response.contains("Data inserted successfully"))
                {
                        eventid=10;
                        insertPointCollection(eventid);
                   // Toast.makeText(CounselorContactActivity.this,"Reallocation:"+String.valueOf(reallocation),Toast.LENGTH_SHORT).show();
                    Toast.makeText(CounselorContactActivity.this, "Reallocated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "Not Reallocated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setReallocateDatabaselist() {
        status1 = Integer.parseInt(arrayListStatusId.get(pos));
        Log.d("updateStatus", String.valueOf(status1));
        // status=status.replaceAll(" ","");
        remarks = edtRemarks.getText().toString();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/UpdateReallocateDatabaselist.php?nSrNo=" + sr_no + "&cCounselorID=" + id1);
        Log.d("StatusUrl", "http://anilsahasrabuddhe.in/CRM/AnDe828500/UpdateReallocateDatabaselist.php?nSrNo=" + sr_no + "cCounselorID=" + id1);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException
            {
                dialog.dismiss();
                Log.d("UpdatedStatus", response);
                if (response.contains("Row updated successfully")) {
                    Toast.makeText(CounselorContactActivity.this, "Databaselist updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "Databaselist not updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void insertCallInfo() {
        String url = clienturl+"?clientid=" + clientid + "&caseid=6&nSrNo=" + sr_no + "&cFileName=" + uploadFileName + "&cMobileNo=" + mbl + "&cCallDuration=" + duration1 + "&cCallDate=" + callDate + "&nCounselorID=" + id1;
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(url);
        Log.d("InsertUrl", url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

                dialog.dismiss();
                Log.d("InsertedStatus", response);
                if (response.contains("Data inserted successfully")) {
                    Toast.makeText(CounselorContactActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CounselorContactActivity.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            dialog.dismiss();

            Log.d("uploadFileNotExist", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);


            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(CounselorContactActivity.this, "Source File not exist :"
                            + uploadFilePath + "" + uploadFileName, Toast.LENGTH_SHORT).show();
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

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CounselorContactActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
               /* else
                {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(SplashAfterCall.this, "File already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
*/
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
                        Toast.makeText(CounselorContactActivity.this, "MalformedURLException",
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
                        Toast.makeText(CounselorContactActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }
}
