package com.bizcall.wayto.mentebit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ApplyOnline extends AppCompatActivity {

    TextView edtFname, edtLname, edtMotherName, edtFatherName, edtDOB, edtNationality, edtEmaiId, edtPhone, edtPassport,
            edtPAddress, edtCity, edtParentNo, edtPincode, edtCAddress, edtCity1, edtPin1, edtBoard, edtPer10,
            edtedtYear10, edtMarks10, edtTotalMarks10, edtPer12, edtYear12, edtMarks12, edtTotalMarks12, edtPhysics, edtChemistry, edtBiology,
            edtPCB, edtAggregate, edtNeetYear, edtNeetMarks;
    String fname, lname, fathername, mothername, dob, emailid, nationality, phone1, passport, paddress, caddress, city, state, state1, city1, parentno, pincode1,
            pincode2, board, per10, per12, mark10, mark12, year10, year12, totalmark10, school, totalmark12, physics, chemistry, biology, pcb, aggregae, neetyear, neetmarks;
    CheckBox chkPassport, chkAddress;
    TextView txtPassport, txtErrorState, txtErrorState1, txtErrorBoard;
    Spinner spinnerState, spinnerState1, spinnerBoard;
    Button btnSubmit;
    RadioGroup radioGroup;
    Calendar calendar1;
    RadioButton radioIndian, radioOther, radioButton;
    AlertDialog alertDialog;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_online);
        txtErrorState = findViewById(R.id.txtStateError);
        txtErrorState1 = findViewById(R.id.txtStateError1);
        txtErrorBoard = findViewById(R.id.txtBoardError);
        edtFname = findViewById(R.id.edtFName);
        edtLname = findViewById(R.id.edtLName);
        edtMotherName = findViewById(R.id.edtMotherName);
        edtFatherName = findViewById(R.id.edtFatherName);
        edtDOB = findViewById(R.id.edtDOB);
        // edtNationality=findViewById(R.id.edtNationality);
        edtEmaiId = findViewById(R.id.edtEmailId);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassport = findViewById(R.id.edtPassportNo);
        edtPAddress = findViewById(R.id.edtPAddress);
        edtCity = findViewById(R.id.edtCity1);
        edtParentNo = findViewById(R.id.edtParentNo);
        edtPincode = findViewById(R.id.edtPin);
        edtCAddress = findViewById(R.id.edtCAddress);
        edtCity1 = findViewById(R.id.edtCity11);
        edtPin1 = findViewById(R.id.edtPin1);
        edtPer10 = findViewById(R.id.edtPer10);
        edtPer12 = findViewById(R.id.edtPer12);
        edtedtYear10 = findViewById(R.id.edtYear10);
        edtYear12 = findViewById(R.id.edtYear12);
        edtMarks10 = findViewById(R.id.edtMarks10);
        edtMarks12 = findViewById(R.id.edtMarks12);
        edtTotalMarks10 = findViewById(R.id.edtTotalMarks10);
        edtBoard = findViewById(R.id.edtSchool);
        edtTotalMarks12 = findViewById(R.id.edtTotalMarks12);
        edtPhysics = findViewById(R.id.edtPhysics);
        edtChemistry = findViewById(R.id.edtChemistry);
        edtBiology = findViewById(R.id.edtBiology);
        edtPCB = findViewById(R.id.edtPCBPer);
        edtAggregate = findViewById(R.id.edtAggregate);
        edtNeetMarks = findViewById(R.id.edtNeetMarks);
        edtNeetYear = findViewById(R.id.edtNeetYear);
        chkPassport = findViewById(R.id.chkboxPassport);
        chkAddress = findViewById(R.id.chkboxAddress);
        spinnerState = findViewById(R.id.spinnerState1);
        spinnerState1 = findViewById(R.id.spinnerState2);
        spinnerBoard = findViewById(R.id.spinnerBoard);
        btnSubmit = findViewById(R.id.btnSubmitApplication);
        radioGroup = findViewById(R.id.radioNationality);
        radioIndian = findViewById(R.id.radioIndian);
        radioOther = findViewById(R.id.radioOther);
        txtPassport = findViewById(R.id.txtPassport);
        calendar1 = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, monthOfYear);
                calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.States, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerState.setAdapter(adapter1);
        spinnerState1.setAdapter(adapter1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.SchoolBoard, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBoard.setAdapter(adapter2);

        edtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ApplyOnline.this, date, calendar1
                        .get(Calendar.YEAR), calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        chkPassport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkPassport.isChecked()) {
                    edtPassport.setVisibility(View.VISIBLE);
                    txtPassport.setVisibility(View.VISIBLE);
                } else {
                    edtPassport.setVisibility(View.GONE);
                    txtPassport.setVisibility(View.GONE);
                }
            }
        });
        chkAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkAddress.isChecked()) {
                    edtCAddress.setText(edtPAddress.getText());
                    edtCity1.setText(edtCity.getText());
                    spinnerState1.setSelection((int) spinnerState.getSelectedItemId());
                    edtPin1.setText(edtPincode.getText());
                    edtCAddress.setClickable(false);
                    edtCAddress.setFocusable(false);
                    edtCAddress.setFocusableInTouchMode(false);
                    edtCity1.setClickable(false);
                    edtCity1.setFocusable(false);
                    edtCity1.setFocusableInTouchMode(false);
                    edtPin1.setClickable(false);
                    edtPin1.setFocusable(false);
                    edtPin1.setFocusableInTouchMode(false);
                    spinnerState1.setClickable(false);
                    spinnerState1.setFocusable(false);
                    spinnerState1.setEnabled(false);
                    spinnerState1.setFocusableInTouchMode(false);
                } else {
                    edtCAddress.setText("");
                    edtCity1.setText("");
                    edtPin1.setText("");
                    edtCAddress.setClickable(true);
                    edtCAddress.setFocusable(true);
                    edtCAddress.setFocusableInTouchMode(true);
                    edtCity1.setClickable(true);
                    edtCity1.setFocusable(true);
                    edtCity1.setFocusableInTouchMode(true);
                    edtPin1.setClickable(true);
                    edtPin1.setFocusable(true);
                    edtPin1.setFocusableInTouchMode(true);
                    spinnerState1.setClickable(true);
                    edtPin1.setFocusable(true);
                    edtPin1.setFocusableInTouchMode(true);
                    spinnerState1.setEnabled(true);
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = 0;
                // e1 = (TextView) findViewById(R.id.name);
                fname = edtFname.getText().toString();
                if (fname.length() == 0) {
                    edtFname.setError("Please enter first name");
                    flag = 1;
                }
                lname = edtLname.getText().toString();
                if (lname.length() == 0) {
                    edtLname.setError("Please enter last name");
                    flag = 1;
                }
                fathername = edtFatherName.getText().toString();
                if (fathername.length() == 0) {
                    edtFatherName.setError("Please enter father name");
                    flag = 1;
                }
                mothername = edtMotherName.getText().toString();
                if (mothername.length() == 0) {
                    edtMotherName.setError("Please enter mother name");
                    flag = 1;
                }
                emailid = edtEmaiId.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!emailid.matches(emailPattern)) {
                    edtEmaiId.setError("Invalid email");
                    flag = 1;
                }
                //  e1 = (TextView) findViewById(R.id.phone);

                phone1 = edtPhone.getText().toString();
                if (phone1.length() < 10 || phone1.length() == 0) {
                    edtPhone.setError("Invalid number");
                    flag = 1;
                }
                if (chkPassport.isChecked()) {
                    passport = edtPassport.getText().toString();
                    if (passport.length() == 0) {
                        edtPassport.setError("Please enter passport number");
                        flag = 1;
                    }
                }
                paddress = edtPAddress.getText().toString();
                if (paddress.length() == 0) {
                    edtPAddress.setError("Please enter address");
                    flag = 1;
                }
                city = edtCity.getText().toString();
                if (city.length() == 0) {
                    edtCity.setError("Please enter city");
                    flag = 1;
                }
                state = spinnerState.getSelectedItem().toString();
                if (state.contains("-Select State-")) {
                    txtErrorState.setVisibility(View.VISIBLE);
                    flag = 1;
                } else {
                    //flag=0;
                    txtErrorState.setVisibility(View.GONE);
                }

                pincode1 = edtPincode.getText().toString();
                String pin_pattern = "^[1-9][0-9]{5}$";
                if (!pincode1.matches(pin_pattern)) {
                    edtPincode.setError("Invalid pincode");
                    flag = 1;
                }
                if (chkAddress.isChecked()) {
                    caddress = paddress;
                    city1 = city;
                    state1 = state;
                    pincode2 = pincode1;
                } else {
                    caddress = edtCAddress.getText().toString();

                    if (caddress.length() == 0) {
                        edtCAddress.setError("Please enter address");
                        flag = 1;
                    }
                    city1 = edtCity1.getText().toString();
                    if (city1.length() == 0) {
                        edtCity1.setError("Please enter city");
                        flag = 1;
                    }
                    state1 = spinnerState1.getSelectedItem().toString();
                    if (state1.contains("-Select State-")) {
                        txtErrorState1.setVisibility(View.VISIBLE);
                        flag = 1;
                    } else {
                        //flag=0;
                        txtErrorState1.setVisibility(View.GONE);
                    }

                    pincode2 = edtPin1.getText().toString();
                    String pin_pattern1 = "^[1-9][0-9]{5}$";
                    if (!pincode2.matches(pin_pattern1)) {
                        edtPin1.setError("Invalid pincode");
                        flag = 1;
                    }

                }
                board = spinnerBoard.getSelectedItem().toString();
                if (state.contains("-Select Board-")) {
                    txtErrorBoard.setVisibility(View.VISIBLE);
                    flag = 1;
                } else {

                    txtErrorBoard.setVisibility(View.GONE);
                }

                per10 = edtPer10.getText().toString();
                if (per10.length() == 0) {
                    edtPer10.setError("Please enter percentage");
                    flag = 1;
                }
                year10 = edtedtYear10.getText().toString();
                if (year10.length() == 0) {
                    edtedtYear10.setError("Please enter year");
                    flag = 1;
                }
                mark10 = edtMarks10.getText().toString();
                if (mark10.length() == 0) {
                    edtMarks10.setError("Please enter marks");
                    flag = 1;
                }
                totalmark10 = edtTotalMarks10.getText().toString();
                if (totalmark10.length() == 0) {
                    edtTotalMarks10.setError("Please enter total marks");
                    flag = 1;
                }
                school = edtBoard.getText().toString();
                if (school.length() == 0) {
                    edtBoard.setError("Please enter college/board");
                    flag = 1;
                }
                per12 = edtPer12.getText().toString();
                if (per12.length() == 0) {
                    edtPer12.setError("Please enter percentage");
                    flag = 1;
                }
                year12 = edtYear12.getText().toString();
                if (year12.length() == 0) {
                    edtYear12.setError("Please enter year");
                    flag = 1;
                }
                mark12 = edtMarks12.getText().toString();
                if (mark12.length() == 0) {
                    edtMarks12.setError("Please enter marks");
                    flag = 1;
                }
                totalmark12 = edtTotalMarks12.getText().toString();
                if (totalmark12.length() == 0) {
                    edtTotalMarks12.setError("Please enter total marks");
                    flag = 1;
                }
                physics = edtPhysics.getText().toString();
                if (physics.length() == 0) {
                    edtPhysics.setError("Please enter physics marks");
                    flag = 1;
                }
                chemistry = edtChemistry.getText().toString();
                if (chemistry.length() == 0) {
                    edtChemistry.setError("Please enter chemistry marks");
                    flag = 1;
                }
                biology = edtBiology.getText().toString();
                if (biology.length() == 0) {
                    edtBiology.setError("Please enter biology marks");
                    flag = 1;
                }
                pcb = edtPCB.getText().toString();
                if (pcb.length() == 0) {
                    edtPCB.setError("Please enter PCB marks");
                    flag = 1;
                }
                aggregae = edtAggregate.getText().toString();
                if (aggregae.length() == 0) {
                    edtAggregate.setError("Please enter aggregate");
                    flag = 1;
                }
                neetyear = edtNeetYear.getText().toString();
                if (neetyear.length() == 0) {
                    edtNeetYear.setError("Please enter year");
                    flag = 1;
                }
                neetmarks = edtNeetMarks.getText().toString();
                if (neetmarks.length() == 0) {
                    edtNeetMarks.setError("Please enter marks");
                    flag = 1;
                }
                int selectedid = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedid);
                nationality = radioButton.getText().toString();
                Log.d("Nationality", nationality);

                // flag=0;
                if (flag == 0) {
                    //sendMsg();
                }

            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtDOB.setText(sdf.format(calendar1.getTime()));
    }

}
