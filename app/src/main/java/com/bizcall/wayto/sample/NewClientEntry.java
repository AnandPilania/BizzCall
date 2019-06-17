package com.bizcall.wayto.sample;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NewClientEntry extends AppCompatActivity
{
    UrlRequest urlRequest;
    TextView txtDOB, txtCnamrError, txtStateError,txtQualficationError,txtAddNew;
    ImageView imgCalender,imgBack;
    Spinner spinnerCounsellorName, spinnerStatus, spinnerQualification, spinnerState;
    EditText edtFName, edtLName, edtParentName, edtPanNo, edtAdharNo, edtPassport, edtMobile1, edtMobile2, edtEmailId, edtSrNo, edtRefNo,
            edtAddress, edtCity, edtPin, edtCourseName, edtCollegeName, edtPassingYear, edtMarks, edtOutOf, edtPercentage;
    String id1, counselorName, dob, status, qualification, firstName, lastName, parentName, panNo, adharNo, passportNo, mobile1, mobile2, emailid, srno, refNo,
            address, city, state, pin, course, clgName, passingYear, marks, outOf, percentage, clienturl, clientid, date1, sex, allocatedDate, remarks;
    SharedPreferences sp;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_entry);
        try{
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clienturl = sp.getString("ClientUrl", null);
        Log.d("**Url**",clienturl);
        clientid = sp.getString("ClientId", null);
        id1 = sp.getString("Id", null);
        id1 = id1.replaceAll(" ", "");
        srno = sp.getString("SelectedSrNo", null);

       txtAddNew=findViewById(R.id.txtAddNew);
        linearPersonalInfo=findViewById(R.id.linearPD);
        imgBack=findViewById(R.id.img_back);
        txtCnamrError = findViewById(R.id.txtCounserlorNameError);
        txtStateError = findViewById(R.id.txtStateError);
        spinnerCounsellorName = findViewById(R.id.spinnerCounselor);
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
        dialog = ProgressDialog.show(NewClientEntry.this, "", "Loading...", true);
        getCounselorData();
        getCounselorDetails();

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

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(NewClientEntry.this,
                R.array.States, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerState.setAdapter(adapter1);

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
                if (marks.length() == 0) {
                    flag = 1;
                    edtMarks.setError("Invalid marks");
                }
                outOf=edtOutOf.getText().toString();
                if (outOf.length() == 0) {
                    flag = 1;
                    edtOutOf.setError("Invalid out of marks");
                }
                percentage=edtPercentage.getText().toString();
                if (percentage.length() == 0) {
                    flag = 1;
                    edtPercentage.setError("Invalid percentage");
                }

                if(flag==0)
                {
                    setEducationalDetails();
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                status = spinnerStatus.getSelectedItem().toString();
                counselorName = spinnerCounsellorName.getSelectedItem().toString();
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
                    flag = 1;
                    txtDOB.setError("Please enter date of birth");
                }
                int rbSex = radioGroupSex.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(rbSex);
                sex = radioButton.getText().toString();

                parentName = edtParentName.getText().toString();

                if (parentName.length() == 0) {
                    flag = 1;
                    edtParentName.setError("Please enter parent name");
                }
                panNo = edtPanNo.getText().toString();
                if (panNo.length() == 0 || panNo.length() < 10) {
                    flag = 1;
                    edtPanNo.setError("Invalid pan number");
                }

                adharNo = edtAdharNo.getText().toString();
                if (adharNo.length() == 0 || adharNo.length() < 12) {
                    flag = 1;
                    edtAdharNo.setError("Invalid adhar number");
                }
                passportNo = edtPassport.getText().toString();
                if (passportNo.length() == 0 || passportNo.length() < 9) {
                    flag = 1;
                    edtPassport.setError("Invalid passport number");
                }
                mobile1 = edtMobile1.getText().toString();
                if (mobile1.length() == 0 || mobile1.length() < 10) {
                    flag = 1;
                    edtMobile1.setError("Invalid phone number");
                }
                mobile2 = edtMobile2.getText().toString();
                if (mobile2.length() == 0 || mobile2.length() < 10) {
                    flag = 1;
                    edtMobile2.setError("Invalid mobile number");
                }
                emailid = edtEmailId.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (emailid.matches(emailPattern) && emailid.length() == 0) {
                    flag = 1;
                    edtEmailId.setError("Invalid email pattern");
                }
                srno = edtSrNo.getText().toString();
                if (srno.length() == 0) {
                    flag = 1;
                    edtSrNo.setError("Invalid serial number");
                }
                refNo = edtRefNo.getText().toString();
                if (refNo.length() == 0) {
                    flag = 1;
                    edtRefNo.setError("Invalid ref number");
                }
                address = edtAddress.getText().toString();
                if (address.length() == 0) {
                    flag = 1;
                    edtAddress.setError("Invalid address");
                }
                city = edtCity.getText().toString();
                if (city.length() == 0) {
                    flag = 1;
                    edtCity.setError("Invalid city");
                }
                state = spinnerState.getSelectedItem().toString();
                if (state.contains("-Select State-")) {
                    flag = 1;
                    txtStateError.setVisibility(View.VISIBLE);
                    txtStateError.setError("Invalid state");
                }
                else {

                    txtStateError.setVisibility(View.GONE);
                }
                pin = edtPin.getText().toString();
                if (pin.length() == 0 || pin.length() < 6) {
                    flag = 1;
                    edtPin.setError("Invalid pincode");
                }
                if (flag == 0) {
                    setNewEntry();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (marks.length() == 0) {
                    flag = 1;
                    edtMarks.setError("Invalid marks");
                }
                outOf=edtOutOf.getText().toString();
                if (outOf.length() == 0) {
                    flag = 1;
                    edtOutOf.setError("Invalid out of marks");
                }
                percentage=edtPercentage.getText().toString();
                if (percentage.length() == 0) {
                    flag = 1;
                    edtPercentage.setError("Invalid percentage");
                }

                if(flag==0)
                {
                    setEducationalDetails();
                }
            }
        });
        }catch (Exception e)
        {
            Log.d("Exception", String.valueOf(e));
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
                //  dialog.dismiss();
                Log.d("ReminderResponse", response);
                if (response.contains("Data inserted successfully")) {
                    linearEducational.setVisibility(View.VISIBLE);
                    linearPersonalInfo.setVisibility(View.GONE);
                    Toast.makeText(NewClientEntry.this, "New client entry successful", Toast.LENGTH_SHORT).show();
                } else {
                    linearPersonalInfo.setVisibility(View.VISIBLE);
                    linearEducational.setVisibility(View.GONE);
                    Toast.makeText(NewClientEntry.this, "New client entry failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }catch (Exception e)
        {
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
                //  dialog.dismiss();
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
                // dialog.dismiss();
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
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(NewClientEntry.this, R.layout.spinner_item1, arrayListCounselorName);
                    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCounsellorName.setAdapter(arrayAdapter);
                    spinnerCounsellorName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            counselorName = spinnerCounsellorName.getSelectedItem().toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            spinnerCounsellorName.setSelection(0);
                        }
                    });


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
                    edtSrNo.setText(srno);
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