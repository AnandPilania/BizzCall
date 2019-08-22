package com.bizcall.wayto.mentebit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class NewClientEntry extends AppCompatActivity
{
    UrlRequest urlRequest;
    String id,formno,firstname,lastname,fathername,mothername,passport,mobile,addrs,paddresss,tate,permanentcity,permanentstate,permanentpin,parentmobile,college,schoolboard,tenthpercentage,tenthyear,tenthmarks,tenthoutof,twelthpercentage,twelthyear,twelthmarks,twelthoutof,neetyear,neetmarks,physics,chemistry,biology,pcb,aggregate,dtcreateddate,cWebsite;

    TextView txtDOB, txtCnamrError, txtStateError,txtQualficationError,txtAddNew,txtCname;
    ImageView imgCalender,imgBack;
    Spinner spinnerCounsellorName, spinnerStatus, spinnerQualification, spinnerState;
    EditText edtFName, edtLName, edtParentName, edtPanNo, edtAdharNo, edtPassport, edtMobile1, edtMobile2, edtEmailId, edtSrNo, edtRefNo,
            edtAddress, edtCity, edtPin, edtCourseName, edtCollegeName, edtPassingYear, edtMarks, edtOutOf, edtPercentage;
    String id1, counselorName, dob, status, qualification, firstName, lastName, parentName, panNo, adharNo, passportNo, mobile1, mobile2, emailid, srno, refNo,
            address, city, state, pin, course, clgName, passingYear, marks, outOf, percentage, clienturl, clientid, date1, sex, allocatedDate, remarks;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Button btnNext,btnSubmit;
    ArrayList<String> arrayListQualification;
    ArrayList<String> arrayListCounselorName;
    ArrayList<String> arrayListStatus;
    RadioGroup radioGroupSex;
    LinearLayout linearSelectDate,linearEducational,linearPersonalInfo;
    int year, day, month;
    int flag = 0;
    ProgressDialog dialog;
    Vibrator vibrator;
    TextView btnGetDetails;
    RequestQueue requestQueue;
    String totalcount,activity,clicked,fileid;
    ArrayList<String> arrayListStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_entry);
        try{
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            requestQueue= Volley.newRequestQueue(NewClientEntry.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor=sp.edit();
        clienturl = sp.getString("ClientUrl", null);
        Log.d("**Url**",clienturl);
        clientid = sp.getString("ClientId", null);
        id1 = sp.getString("Id", null);
        id1 = id1.replaceAll(" ", "");
        counselorName=sp.getString("Name",null);
            Log.d("**name**",counselorName);

            //srno = sp.getString("SelectedSrNo", null);

       txtAddNew=findViewById(R.id.txtAddNew);
        linearPersonalInfo=findViewById(R.id.linearPD);
        imgBack=findViewById(R.id.img_back);
        txtCnamrError = findViewById(R.id.txtCounserlorNameError);
        txtStateError = findViewById(R.id.txtStateError);
      //  spinnerCounsellorName = findViewById(R.id.spinnerCounselor);
        edtFName = findViewById(R.id.edtFirstName);
        edtLName = findViewById(R.id.edtLastName);
        edtParentName = findViewById(R.id.edtParentName);
        edtPanNo = findViewById(R.id.edtPanNo);
        edtAdharNo = findViewById(R.id.edtAdharNo);
        edtPassport = findViewById(R.id.edtPassportNo);
        edtMobile1 = findViewById(R.id.edtMobile1);
        edtMobile2 = findViewById(R.id.edtMobile2);
        edtEmailId = findViewById(R.id.edtEmail);
        edtSrNo = findViewById(R.id.edtSrNo);
        edtRefNo = findViewById(R.id.edtReference);
        edtAddress = findViewById(R.id.edtAddress);
        edtCity = findViewById(R.id.edtCity);
        edtPin = findViewById(R.id.edtPin);
        btnNext = findViewById(R.id.btnNext);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        txtDOB = findViewById(R.id.txtSelectedDate);
        imgCalender = findViewById(R.id.imgCalender);
        spinnerState = findViewById(R.id.spinnerState);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        linearSelectDate = findViewById(R.id.linearSelectDate);
        linearEducational=findViewById(R.id.linearEducational);

        spinnerQualification=findViewById(R.id.spinnerQualification);
        edtCourseName=findViewById(R.id.edtCoursename);
        edtCollegeName=findViewById(R.id.edtCollegeName);
        edtPassingYear=findViewById(R.id.edtPassingYear);
        edtMarks=findViewById(R.id.edtMarks);
        edtOutOf=findViewById(R.id.edtOutof);
        edtPercentage=findViewById(R.id.edtPercentage);
        btnSubmit=findViewById(R.id.btnSubmit);
        txtQualficationError=findViewById(R.id.txtQualificationError);
        btnGetDetails=findViewById(R.id.btnGetDetails);
        txtCname=findViewById(R.id.txtCounselorName);
        txtCname.setText(counselorName);

            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(NewClientEntry.this,
                    R.array.States, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinnerState.setAdapter(adapter1);
            arrayListStates = new ArrayList<String>(Arrays.<String>asList(String.valueOf(R.array.States)));
        activity=getIntent().getStringExtra("Activity");
        if(activity.contains("Contact"))
        {
            srno=getIntent().getStringExtra("SrNo");
            edtSrNo.setText(srno);
        }
        else if(activity.equals("FormFilled"))
        {
            srno=getIntent().getStringExtra("SrNo");
            edtSrNo.setText(srno);
            dialog=ProgressDialog.show(NewClientEntry.this,"","Loading details",false,true);
            getSrDetailsOnlineForm(srno);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
            }
        });

        arrayListStatus = new ArrayList<>();
        arrayListStatus.add(0, "Confirm");
        arrayListStatus.add(1, "Normal");
        arrayListStatus.add(2, "Cancel");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(NewClientEntry.this, R.layout.spinner_item1, arrayListStatus);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(arrayAdapter);
        spinnerStatus.setSelection(0);

        arrayListQualification=new ArrayList<>();
        arrayListQualification.add("-Select Qualification-");
        arrayListQualification.add("10th");
        arrayListQualification.add("12th");
        arrayListQualification.add("Graduate");
        arrayListQualification.add("PG");
        arrayListQualification.add("AIEEE");
        arrayListQualification.add("NEET");
        arrayListQualification.add("JEE");


        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(NewClientEntry.this, R.layout.spinner_item1, arrayListQualification);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQualification.setAdapter(arrayAdapter1);
       // spinnerStatus.setSelection(0);
        linearSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });
        // Log.d("Fname",firstName);

        txtAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                txtAddNewClicked();

            }
        });

        btnGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               btnGetDetailsClicked();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               btnNextClicked();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSubmitClicked();
            }
        });
        }catch (Exception e)
        {
            Toast.makeText(NewClientEntry.this,"Errorcode-289 NewClientEntry onCreate "+e.toString(),Toast.LENGTH_SHORT).show();        }
    }//onCreate

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent intent = new Intent(NewClientEntry.this, Home.class);
            intent.putExtra("Activity", "NewClientEntry");
            startActivity(intent);
        }catch (Exception e)
        {
            Toast.makeText(NewClientEntry.this,"Errorcode-290 NewClientEntry onBackPressed "+e.toString(),Toast.LENGTH_SHORT).show();

        }
    }
    public void txtAddNewClicked(){
        try{
            flag=0;
            qualification=spinnerQualification.getSelectedItem().toString();
            if (qualification.contains("Select Qualification")) {
                flag = 1;
                txtQualficationError.setVisibility(View.VISIBLE);
            } else {
                flag=0;
                txtQualficationError.setVisibility(View.GONE);
            }
            if(qualification.contains("10th"))
            {
                edtCourseName.setText("Tenth");
            }
            else if(qualification.contains("12th"))
            {
                edtCourseName.setText("Twelth");
            }

            course = edtCourseName.getText().toString();
            if (course.length() == 0) {
                flag = 1;
                edtCourseName.setError("Invalid Course");
            }
            clgName=edtCollegeName.getText().toString();
            if (clgName.length() == 0) {
                flag = 1;
                edtCollegeName.setError("Invalid College Name");
            }
            passingYear=edtPassingYear.getText().toString();
            if (passingYear.length() == 0) {
                flag = 1;
                edtPassingYear.setError("Invalid Passing Year");
            }
            marks=edtMarks.getText().toString();
               /* if (marks.length() == 0) {
                    flag = 1;
                    edtMarks.setError("Invalid marks");
                }*/
            outOf=edtOutOf.getText().toString();
               /* if (outOf.length() == 0) {
                    flag = 1;
                    edtOutOf.setError("Invalid out of marks");
                }*/
            percentage=edtPercentage.getText().toString();
               /* if (percentage.length() == 0) {
                    flag = 1;
                    edtPercentage.setError("Invalid percentage");
                }*/

            if(flag==0)
            {
                if(CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                else if(CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                }
                else {

                    dialog = ProgressDialog.show(NewClientEntry.this, "", "Inserting Educational details...", true);
                    //getCounselorData();
                    setEducationalDetails();
                }
            }
        }catch (Exception e)
        {
            Log.d("ExcAddEducation",e.toString());
            Toast.makeText(NewClientEntry.this,"Errorcode-291 NewClientEntry txtAddNewClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnGetDetailsClicked()
    {
        try{
            String sr_no=edtSrNo.getText().toString();
            if(sr_no.length()!=0) {
                clicked="GetDetails";
                if(CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                else if(CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                }
                else {
                    dialog = ProgressDialog.show(NewClientEntry.this, "", "Checking number is exist or not", false, true);
                    checkIfExistInDocMaster(sr_no);
                }
            }
            else
            {
                edtSrNo.setError("Invalid Serial number");
            }
        }catch (Exception e)
        {
            Log.d("ExcGetDetails",e.toString());
            Toast.makeText(NewClientEntry.this,"Errorcode-292 NewClientEntry btnGetDetailsClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnNextClicked(){
        try {
            flag = 0;
            // status = spinnerStatus.getSelectedItem().toString();
            //counselorName = spinnerCounsellorName.getSelectedItem().toString();
            counselorName = txtCname.getText().toString();
            if (counselorName.contains("Select Name")) {
                flag = 1;
                txtCnamrError.setVisibility(View.VISIBLE);
            } else {
                txtCnamrError.setVisibility(View.GONE);
            }
            firstName = edtFName.getText().toString();
            if (firstName.length() == 0) {
                flag = 1;
                edtFName.setError("Please enter first name");
            }
            lastName = edtLName.getText().toString();
            if (lastName.length() == 0) {
                flag = 1;
                edtLName.setError("Please enter last name");
            }
            dob = txtDOB.getText().toString();
            if (dob.contains("Select date")) {
                dob = "1900-01-01";
                //flag = 1;
                //txtDOB.setError("Please enter date of birth");
            }
            int rbSex = radioGroupSex.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(rbSex);
            sex = radioButton.getText().toString();

            parentName = edtParentName.getText().toString();

               /* if (parentName.length() == 0) {
                    flag = 1;
                    edtParentName.setError("Please enter parent name");
                }*/
            panNo = edtPanNo.getText().toString();
                /*if (panNo.length() == 0 || panNo.length() < 10) {
                    flag = 1;
                    edtPanNo.setError("Invalid pan number");
                }*/
            adharNo = edtAdharNo.getText().toString();
                /*if (adharNo.length() == 0 || adharNo.length() < 12) {
                    flag = 1;
                    edtAdharNo.setError("Invalid adhar number");
                }*/
            passportNo = edtPassport.getText().toString();
                /*if (passportNo.length() == 0) {
                    flag = 1;
                    edtPassport.setError("Invalid passport number");
                }*/
            mobile1 = edtMobile1.getText().toString();

            if (mobile1.length() == 0 || mobile1.length() < 10 || mobile1.length() >= 13) {
                flag = 1;
                edtMobile1.setError("Invalid phone number");
            }
            mobile2 = edtMobile2.getText().toString();
               /* if (mobile2.length() == 0 || mobile2.length() < 10||mobile1.length()>=13) {
                    flag = 1;
                    edtMobile2.setError("Invalid mobile number");
                }*/
            emailid = edtEmailId.getText().toString();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (emailid.matches(emailPattern) && emailid.length() == 0) {
                flag = 1;
                edtEmailId.setError("Invalid email pattern");
            }
            srno = edtSrNo.getText().toString();
               /* if (srno.length() == 0) {
                    flag = 1;
                    edtSrNo.setError("Invalid serial number");
                }*/
            refNo = edtRefNo.getText().toString();
                /*if (refNo.length() == 0) {
                    flag = 1;
                    edtRefNo.setError("Invalid ref number");
                }*/
            address = edtAddress.getText().toString();
                /*if (address.length() == 0) {
                    flag = 1;
                    edtAddress.setError("Invalid address");
                }*/
            city = edtCity.getText().toString();
               /* if (city.length() == 0) {
                    flag = 1;
                    edtCity.setError("Invalid city");
                }*/
            state = spinnerState.getSelectedItem().toString();
            if (state.contains("-Select State-")) {
                flag = 1;
                txtStateError.setVisibility(View.VISIBLE);
                txtStateError.setError("Invalid state");
            } else {

                txtStateError.setVisibility(View.GONE);
            }
            pin = edtPin.getText().toString();
                /*if (pin.length() == 0 || pin.length() < 6) {
                    flag = 1;
                    edtPin.setError("Invalid pincode");
                }*/
            if (flag == 0) {
                clicked = "Next";
                srno = edtSrNo.getText().toString();
                if (CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                } else if (CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                    dialog = ProgressDialog.show(NewClientEntry.this, "", "Checking number is exist or not", false, true);
                    checkIfExistInDocMaster(srno);
                }
            }
        } catch (Exception e) {
            Log.d("ExcNewClientEntry", e.toString());
            Toast.makeText(NewClientEntry.this,"Errorcode-293 NewClientEntry btnNextClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnSubmitClicked(){
        try {

            flag = 0;
            qualification = spinnerQualification.getSelectedItem().toString();
            if (qualification.contains("Select Qualification")) {
                flag = 1;
                txtQualficationError.setVisibility(View.VISIBLE);
            } else {
                flag = 0;
                txtQualficationError.setVisibility(View.GONE);
            }
            if (qualification.contains("10th")) {
                edtCourseName.setText("Tenth");
            } else if (qualification.contains("12th")) {
                edtCourseName.setText("Twelth");
            }

            course = edtCourseName.getText().toString();
            if (course.length() == 0) {
                flag = 1;
                edtCourseName.setError("Invalid Course");
            }
            clgName = edtCollegeName.getText().toString();
            if (clgName.length() == 0) {
                flag = 1;
                edtCollegeName.setError("Invalid College Name");
            }
            passingYear = edtPassingYear.getText().toString();
            if (passingYear.length() == 0) {
                flag = 1;
                edtPassingYear.setError("Invalid Passing Year");
            }
            marks = edtMarks.getText().toString();
            if (marks.length() == 0) {
                flag = 1;
                edtMarks.setError("Invalid marks");
            }
            outOf = edtOutOf.getText().toString();
            if (outOf.length() == 0) {
                flag = 1;
                edtOutOf.setError("Invalid out of marks");
            }
            percentage = edtPercentage.getText().toString();
            if (percentage.length() == 0) {
                flag = 1;
                edtPercentage.setError("Invalid percentage");
            }

            if (flag == 0) {
                if (CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                } else if (CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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

                    dialog = ProgressDialog.show(NewClientEntry.this, "", "Inserting educational details...", true);
                    setEducationalDetails();
                }
            }
        } catch (Exception e) {
            Log.d("ExcNewEntry", e.toString());
            Toast.makeText(NewClientEntry.this,"Errorcode-294 NewClientEntry btnSubmitClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void getSrDetailsOnlineForm(String serialno) {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=83&ID=" + serialno;
            Log.d("OFSrDetailsUrl", url);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("OFSrDetailsResponse", response);
                            try {
                                dialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d("Json", jsonObject.toString());

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("Length", String.valueOf(jsonArray.length()));
                               /* if(jsonArray.length()==0)
                                {
                                    if(act1.contains("OnlineLead"))
                                    {
                                        startActivity(new Intent(CounselorContactActivity.this,OnlineLead.class));
                                    }
                                    else {
                                        intent = new Intent(CounselorContactActivity.this, CounsellorData.class);
                                        intent.putExtra("Activity","CounselorData");
                                        startActivity(intent);
                                        //  startActivity(new Intent(CounselorContactActivity.this, CounsellorData.class));
                                        Toast.makeText(CounselorContactActivity.this, "This candidate is allocated to someone else", Toast.LENGTH_SHORT).show();
                                    }
                                }*/
                                //else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    id = String.valueOf(jsonObject1.getInt("id"));
                                    formno = jsonObject1.getString("formno");
                                    firstname = jsonObject1.getString("firstname");
                                    lastname = jsonObject1.getString("lastname");
                                    fathername = jsonObject1.getString("fathername");
                                    mothername = jsonObject1.getString("mothername");
                                    dob = jsonObject1.getString("dob");
                                    passport = jsonObject1.getString("passport");
                                    mobile = jsonObject1.getString("mobile");
                                    addrs = jsonObject1.getString("address");
                                    city = jsonObject1.getString("city");
                                    state = jsonObject1.getString("state");
                                    pin = jsonObject1.getString("pincode");
                                    emailid = jsonObject1.getString("email");
                                    paddresss = jsonObject1.getString("PermanentAddress");
                                    permanentcity = jsonObject1.getString("permanentcity");
                                    permanentstate = jsonObject1.getString("permanentstate");
                                    permanentpin = jsonObject1.getString("permanentpin");
                                    parentmobile = jsonObject1.getString("parentmobile");
                                    college = jsonObject1.getString("college");
                                    schoolboard = jsonObject1.getString("schoolboard");
                                    tenthpercentage = jsonObject1.getString("tenthpercentage");
                                    tenthyear = jsonObject1.getString("tenthyear");
                                    tenthmarks = jsonObject1.getString("tenthmarks");
                                    tenthoutof = jsonObject1.getString("tenthoutof");
                                    twelthpercentage = jsonObject1.getString("twelthpercentage");
                                    twelthyear = jsonObject1.getString("twelthyear");
                                    twelthmarks = jsonObject1.getString("twelthmarks");
                                    twelthoutof = jsonObject1.getString("twelthoutof");
                                    neetyear = jsonObject1.getString("neetyear");
                                    neetmarks = jsonObject1.getString("neetmarks");
                                    physics = jsonObject1.getString("physics");
                                    chemistry = jsonObject1.getString("chemistry");
                                    biology = jsonObject1.getString("biology");
                                    pcb = jsonObject1.getString("pcb");
                                    aggregate = jsonObject1.getString("aggregate");
                                    dtcreateddate = jsonObject1.getString("dtcreateddate");
                                    cWebsite = jsonObject1.getString("cWebsite");
                                }

                                edtFName.setText(firstname);
                                edtLName.setText(lastname);
                                edtMobile1.setText(mobile);
                                edtMobile2.setText(parentmobile);
                                edtParentName.setText(fathername);
                                txtDOB.setText(dob);
                                edtPassport.setText(passport);
                                edtEmailId.setText(emailid);
                                // edtSrNo.setText(srno);
                                // edtRefNo.setText(refNo);
                                edtAddress.setText(addrs);
                                edtCity.setText(city);
                                Log.d("State", state);
                               /* ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(NewClientEntry.this,
                                        R.array.States, android.R.layout.simple_spinner_item);
                                // Specify the layout to use when the list of choices appears
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // Apply the adapter to the spinner
                                spinnerState.setAdapter(adapter1);
                                arrayListStates = new ArrayList<String>(Arrays.<String>asList(String.valueOf(R.array.States)));*/
                                Log.d("SpinnerCount", spinnerState.getCount() + "");
                                for (int i = 0; i < spinnerState.getCount(); i++) {
                                    if (spinnerState.getItemAtPosition(i).toString().contains(state)) {
                                        Log.d("SpinnerItem", spinnerState.getItemAtPosition(i).toString());
                                        spinnerState.setSelection(i);
                                    }
                                }
                                edtPin.setText(pin);
                                //  allocatedDate = allocatedDate.substring(allocatedDate.indexOf("date") + 6, allocatedDate.indexOf(","));
                                //     Log.d("Date!!!!", allocatedDate);

                                //txtCurrentStatus.setText(status11);
                                // txtRemark.setText(remark);
                                //getStatus1();

                            } catch (JSONException e) {
                                Toast.makeText(NewClientEntry.this,"Errorcode-296 NewClientEntry SrDetialsOnlineFormResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(NewClientEntry.this, "Server Error", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
        }catch (Exception e)
        {
            Toast.makeText(NewClientEntry.this,"Errorcode-295 NewClientEntry getSrDetialsOnlineForm "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }


    public void getSrDetails(final String srno, String counselorid)
    {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=81&nSrNo=" + srno + "&CounselorID=" + counselorid;
            Log.d("SrDetails", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Log.d("CheckSrNoResponse", response);
                            try {
                                if (response.contains("[]")) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
                                    alertDialogBuilder.setTitle(srno + " is allocated to other counsellor")
                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    // arrayListTotal.clear();
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        //String id = String.valueOf(jsonObject1.getInt("cCounselorID"));
                                        firstName = jsonObject1.getString("cCandidateName");
                                        //refNo=jsonObject1.getString("refNo");
                                        course = jsonObject1.getString("cCourse");
                                        mobile1 = jsonObject1.getString("cMobile");
                                        address = jsonObject1.getString("cAddressLine");
                                        city = jsonObject1.getString("cCity");
                                        state = jsonObject1.getString("cState");
                                        pin = jsonObject1.getString("cPinCode");
                                        mobile2 = jsonObject1.getString("cParantNo");
                                        emailid = jsonObject1.getString("cEmail");
                                        // fetchedDataFrom=jsonObject1.getString("cDataFrom");
                                        // fetchedAllocatedTo=jsonObject1.getString("AllocatedTo");
                                        // allocatedDate=jsonObject1.getString("AllocationDate");
                                        // statusid=jsonObject1.getString("CurrentStatus");
                                        //  remarks = jsonObject1.getString("cRemarks");
                                        //date1 = jsonObject1.getString("dtCreatedDate");
                                        //status = jsonObject1.getString("cStatus");
                                    }

                                    edtFName.setText(firstName);
                                    edtMobile1.setText(mobile1);
                                    edtMobile2.setText(mobile2);
                                    edtEmailId.setText(emailid);
                                    // edtSrNo.setText(srno);
                                    edtRefNo.setText(refNo);
                                    edtAddress.setText(address);
                                    edtCity.setText(city);
                                    for (int i = 0; i < spinnerState.getCount(); i++) {
                                        if (spinnerState.getItemAtPosition(i).toString().equalsIgnoreCase(state)) {
                                            spinnerState.setSelection(i);
                                        }
                                    }
                                    edtPin.setText(pin);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(NewClientEntry.this, "Volley error while checking phone in database", Toast.LENGTH_SHORT);
                                dialog.dismiss();
                                Log.d("CheckPhoneException", String.valueOf(e));
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
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
                                alertDialogBuilder.setTitle("Server Error!!!")
                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(NewClientEntry.this, "Server Error", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
        }catch (Exception e)
        {
            Toast.makeText(NewClientEntry.this,"Errorcode-297 NewClientEntry getSrDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void checkIfExistInDocMaster(final String srno) {
        try {
            String url;
            if (clicked.equals("Next")) {
                url = clienturl + "?clientid=" + clientid + "&caseid=85&cCandidateFName=" + firstName + "&cCandidateLName=" + lastName;
            } else {
                url = clienturl + "?clientid=" + clientid + "&caseid=80&SrNo=" + srno + "&CounselorId=" + id1;

            }
            Log.d("CheckNumberUrl", url);
            if (CheckInternet.checkInternet(NewClientEntry.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("CheckSrNoResponse", response);
                                try {
                                    // arrayListTotal.clear();
                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log.d("Json",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        totalcount = jsonObject1.getString("total");
                                    }
                                    if (totalcount.equals("0")) {
                                        if (clicked.equals("Next")) {
                                            if (CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("0")) {
                                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                                            } else if (CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("1")) {
                                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                                                dialog = ProgressDialog.show(NewClientEntry.this, "", "Inserting client details", false, true);
                                                setNewEntry();
                                            }
                                        } else {
                                            if (CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("0")) {
                                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                                            } else if (CheckInternetSpeed.checkInternet(NewClientEntry.this).contains("1")) {
                                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
                                                dialog = ProgressDialog.show(NewClientEntry.this, "", "Loading Details", false, true);
                                                getSrDetails(srno, id1);
                                            }
                                        }
                                    } else {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
                                        alertDialogBuilder.setTitle(srno + "/" + firstName + "/" + lastName + " is already exist in DocumentMaster")
                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(NewClientEntry.this, "Volley error while checking phone in database", Toast.LENGTH_SHORT);
                                    dialog.dismiss();
                                    Log.d("CheckPhoneException", String.valueOf(e));
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(NewClientEntry.this,"Errorcode-299 NewClientEntry IfExistInDocMasterResponse "+error.toString(),Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
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
            Toast.makeText(NewClientEntry.this,"Errorcode-298 NewClientEntry checkIfExistInDocMaster "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    try {
                        Calendar myCalendar = Calendar.getInstance();
                        //arg0.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        myCalendar.set(Calendar.YEAR, arg1);
                        myCalendar.set(Calendar.MONTH, arg2);
                        myCalendar.set(Calendar.DAY_OF_MONTH, arg3);
                        String myFormat = "yyyy/MM/dd"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        txtDOB.setText(sdf.format(myCalendar.getTime()));

                        day = arg1;
                        month = arg2;
                        year = arg3;
                        showDate(day, month + 1, year);

                    } catch (Exception e) {
                        Toast.makeText(NewClientEntry.this,"Errorcode-300 NewClientEntry DataPickerDialog "+e.toString(),Toast.LENGTH_SHORT).show();
                        Log.d("Exception", String.valueOf(e));
                    }
                }
            };


    public void setDate(View view) {
        try{
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
        }catch (Exception e)
        {
            Toast.makeText(NewClientEntry.this,"Errorcode-301 NewClientEntry setDate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        try{
        if (id == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    myDateListener, year, month, day);
            //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }
        }catch (Exception e)
        {
            Toast.makeText(NewClientEntry.this,"Errorcode-302 NewClientEntry createDialog "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        try{
        txtDOB.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
        }catch (Exception e)
        {
            Log.d("Exception", String.valueOf(e));
        }
    }

    public void setNewEntry() {
        try{
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        String url = (clienturl + "?clientid=" + clientid + "&caseid=39&nSrNo=" + srno + "&nCounsellorId=" + id1 + "&cCandidateFName=" + firstName + "&cCandidateLName=" + lastName +
                "&dtDOB=" + dob + "&cGender=" + sex + "&cParentName=" + parentName + "&cPanNo=" + panNo + "&cAadhaarNo=" + adharNo + "&cPassportNo=" + passportNo + "&cMobile=" + mobile1 + "&cParantNo=" + mobile2 +
                "&cEmail=" + emailid + "&cStatus=" + status + "&cRef=" + refNo + "&cAddressLine=" + address + "&cCity=" + city + "&cState=" + state + "&cPinCode=" + pin + "&dtCreatedDate="+"&dtUpdatedDate="+
                "&IsActive=1&ExtraCol=1&cRemarks=" + remarks).trim();
        urlRequest.setUrl(url);
        Log.d("NewEntryUrl", url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                  dialog.dismiss();
                Log.d("NewEntryResponse", response);
                JSONObject jsonObject = new JSONObject(response);
                Log.d("Json", jsonObject.toString());
                if(response.contains("[]"))
                {
                    Toast.makeText(NewClientEntry.this, "New client entry failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        fileid = jsonObject1.getString("nFileID");
                    }

                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(NewClientEntry.this);
                    alertDialogBuilder.setTitle("File Number generated successfully")
                            .setMessage("File no is:" + fileid)

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    editor.putString("FileNo",fileid);
                                    editor.putString("MobileNo",mobile1);
                                    editor.putString("FullName",firstName+" "+lastName);

                                    editor.commit();
                                    Intent intent=new Intent(NewClientEntry.this,EducationalDetails.class);
                                    intent.putExtra("FileNo",fileid);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })


                            .show();
                }

            }
        });
        }catch (Exception e)
        {
            Toast.makeText(NewClientEntry.this,"Errorcode-303 NewClientEntry setNewEntry "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void setEducationalDetails() {
        try{
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        String url = clienturl + "?clientid=" + clientid + "&caseid=40&nSrNo=" +srno+"&cQualification="+qualification+"&cCourseName="+course+
                "&cCollegeName="+clgName+"&cPassingYear="+passingYear+"&cMarksOpt="+marks+"&cMarksOutOF="+outOf+"&cPersentange="+percentage+"&IsActive=1";
        urlRequest.setUrl(url);
        Log.d("EduUrl", url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                  dialog.dismiss();
                Log.d("EduResponse", response);
                if (response.contains("Data inserted successfully")) {
                    //linearEducational.setVisibility(View.VISIBLE);
                    spinnerQualification.setSelection(0);
                    edtCourseName.getText().clear();
                    edtCollegeName.getText().clear();
                    edtPassingYear.getText().clear();
                    edtMarks.getText().clear();
                    edtOutOf.getText().clear();
                    edtPercentage.getText().clear();
                    Toast.makeText(NewClientEntry.this, "Educational info inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                   // linearEducational.setVisibility(View.GONE);
                    Toast.makeText(NewClientEntry.this, "Educational info inserted not successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }catch (Exception e)
        {
            Toast.makeText(NewClientEntry.this,"Errorcode-304 NewClientEntry setEducationalDetails "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void getCounselorDetails() {
        arrayListCounselorName = new ArrayList<>();
        arrayListCounselorName.add(0, "Select Name");
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=30");
        Log.d("CounselorUrl", clienturl + "?clientid=" + clientid + "&caseid=30");
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
                        arrayListCounselorName.add(name);
                    }
                }catch (Exception e)
                {
                    Log.d("Exception", String.valueOf(e));
                }
                }
        });
    }

    public void getCounselorData() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=32&nSrNo=" + srno);
        Log.d("FetchedCounselorUrl", clienturl + "?clientid=" + clientid + "&caseid=32&nSrNo=" + srno);
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


                        firstName = jsonObject1.getString("cCandidateName");
                        refNo=jsonObject1.getString("refNo");
                        course = jsonObject1.getString("cCourse");
                        mobile1 = jsonObject1.getString("cMobile");
                        address = jsonObject1.getString("cAddressLine");
                        city = jsonObject1.getString("cCity");
                        state = jsonObject1.getString("cState");
                        pin = jsonObject1.getString("cPinCode");
                        mobile2 = jsonObject1.getString("cParantNo");
                        emailid = jsonObject1.getString("cEmail");
                        // fetchedDataFrom=jsonObject1.getString("cDataFrom");
                        // fetchedAllocatedTo=jsonObject1.getString("AllocatedTo");
                        // allocatedDate=jsonObject1.getString("AllocationDate");
                        // statusid=jsonObject1.getString("CurrentStatus");
                        remarks = jsonObject1.getString("cRemarks");
                        date1 = jsonObject1.getString("dtCreatedDate");
                        status = jsonObject1.getString("cStatus");
                    }
                    edtFName.setText(firstName);
                    edtMobile1.setText(mobile1);
                    edtMobile2.setText(mobile2);
                    edtEmailId.setText(emailid);
                   // edtSrNo.setText(srno);
                    edtRefNo.setText(refNo);
                    edtAddress.setText(address);
                    edtCity.setText(city);
                    for (int i=0;i<spinnerState.getCount();i++){
                        if (spinnerState.getItemAtPosition(i).toString().equalsIgnoreCase(state)){
                            spinnerState.setSelection(i);
                        }
                    }
                    edtPin.setText(pin);
                }catch (Exception e)
                {
                    Log.d("Exception", String.valueOf(e));
                }
            }
        });

    }
}