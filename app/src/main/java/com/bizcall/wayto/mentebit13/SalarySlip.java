package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class SalarySlip extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sp;
    String counselorid,clientid,clienturl,counselorname;
    long timeout;
    Thread thread;
    ImageView imgBack,imgRefresh,imgPDF;
    FloatingActionButton fab;
    ProgressDialog dialog;
    Spinner spinnerMonth,spinnerYear;
    ArrayList<String> arrayListMonth,arrayListYear;
    LinearLayout linearSalarySlip;
    String month1,year1,path;

    TextView txtTotalDeductions,txtIncomeTax,txtDeptName,txtDesignaion,txtSubmit,txtNotFound, txtEmpID,txtEmpName,txtPancard,txtAccountID,txtBank,txtAttendence,txtTotalWorkDays,txtBasicPay,txtHRA,txtTransportAllowance,
    txtMedicalAllowance,txtLTA,txtIncentive,txtProfessionTax,txtIncomrTax,txtGrossPay,txtNetPay,txtMonthYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_salary_slip);
            initialize();


            imgPDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //to create pdf and save it to device
                    createandDisplayPdf();
                }
            });

            spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    month1 = spinnerMonth.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    spinnerMonth.setSelection(0);
                }
            });
            spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    year1 = spinnerYear.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    spinnerYear.setSelection(0);
                }
            });
            txtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    month1 = spinnerMonth.getSelectedItem().toString();
                    year1 = spinnerYear.getSelectedItem().toString();
                    dialog = ProgressDialog.show(SalarySlip.this, "", "Loading Salary Slip Details", true);
                    newThreadInitilization(dialog);
                    //to get salary slip for selected date
                    getSalaryslip();
                }
            });

            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SalarySlip.this, SalarySlip.class);
                    startActivity(intent);
                    finish();
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        catch (Exception e)
            {
                Toast.makeText(SalarySlip.this,"Errorcode-548 SalarySlip onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            }

    }//onCreeate

    private void initialize() {
        requestQueue = Volley.newRequestQueue(SalarySlip.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replaceAll(" ", "");
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        counselorname = sp.getString("Name", null);
        timeout = sp.getLong("TimeOut", 0);

        imgBack = findViewById(R.id.img_back);
        imgRefresh = findViewById(R.id.imgRefresh);
        txtEmpID = findViewById(R.id.txtEmpID);
        txtEmpName = findViewById(R.id.txtEmpName);
        txtPancard = findViewById(R.id.txtPanNo);
        txtAccountID = findViewById(R.id.txtAccountNo);
        txtBank = findViewById(R.id.txtBank);
        txtAttendence = findViewById(R.id.txtAttendence);
        txtTotalWorkDays = findViewById(R.id.txtTotalWorkDays);
        txtBasicPay = findViewById(R.id.txtBasicPay);
        txtHRA = findViewById(R.id.txtHRA);
        txtTransportAllowance = findViewById(R.id.txtTransportAllowance);
        txtMedicalAllowance = findViewById(R.id.txtMedicalAllowance);
        txtLTA = findViewById(R.id.txtLTA);
        txtIncentive = findViewById(R.id.txtIncentive);
        txtProfessionTax = findViewById(R.id.txtProfessionTax);
        // txtIncomrTax=findViewById(R.id.txIncomeTax);
        txtGrossPay = findViewById(R.id.txtGrossPay);
        txtNetPay = findViewById(R.id.txtNetPay);
        txtMonthYear = findViewById(R.id.txtMonthYear);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        linearSalarySlip = findViewById(R.id.linearSalarySlip);
        txtSubmit = findViewById(R.id.txtSubmit);
        txtNotFound = findViewById(R.id.txtNotFound);
        imgPDF = findViewById(R.id.imgPDF);
        txtDesignaion = findViewById(R.id.txtDesignaion);
        txtDeptName = findViewById(R.id.txtDeptName);
        txtIncomeTax = findViewById(R.id.txtIncomeTax);
        txtTotalDeductions = findViewById(R.id.txtTotalDeductions);

        arrayListYear = new ArrayList<>();

        //arrayListCourse.add("Select Course");

        arrayListYear.add("2019");
        arrayListYear.add("2020");
        arrayListYear.add("2021");

        ArrayAdapter<String> arrayAdapterYear = new ArrayAdapter<>(SalarySlip.this, R.layout.spinner_item1, arrayListYear);

        // Apply the adapter to the spinner
        spinnerYear.setAdapter(arrayAdapterYear);
        arrayListMonth = new ArrayList<>();

        //arrayListCourse.add("Select Course");
        arrayListMonth.add("January");
        arrayListMonth.add("February");
        arrayListMonth.add("March");
        arrayListMonth.add("April");
        arrayListMonth.add("May");
        arrayListMonth.add("June");
        arrayListMonth.add("July");
        arrayListMonth.add("August");
        arrayListMonth.add("September");
        arrayListMonth.add("October");
        arrayListMonth.add("November");
        arrayListMonth.add("December");

        ArrayAdapter<String> arrayAdapterMonth = new ArrayAdapter<>(SalarySlip.this, R.layout.spinner_item1, arrayListMonth);

        // Apply the adapter to the spinner
        spinnerMonth.setAdapter(arrayAdapterMonth);
    }//initialize

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent intent=new Intent(SalarySlip.this,Home.class);
        intent.putExtra("Activity","TotalCallMade");
        startActivity(intent);
        finish();
    }

    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    // dialog1.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(SalarySlip.this, "Connection Aborted.", Toast.LENGTH_SHORT).show();
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
    public void createandDisplayPdf() {

        Document doc=new Document();
        try {
            path = Environment.getExternalStorageDirectory() + "/Bizcall/SalarySlip";
            String monthyear=txtMonthYear.getText().toString();
            monthyear=monthyear.replace("/","_");
            String empname=txtEmpName.getText().toString();
            empname=empname.replaceAll(" ","");

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, empname+"_"+monthyear+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance((com.itextpdf.text.Document) doc, fOut);

            //special font sizes
            Font fttitle = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.NORMAL, new BaseColor(0, 0, 0));
            Font ftadds = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0));
            Font ftnote = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, new BaseColor(0, 0, 0));
            Font ftsubtitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, new BaseColor(0, 0, 0));
            Font ftsubtitle1 = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.NORMAL, new BaseColor(255, 0, 0));
            Font fttext1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, new BaseColor(0, 0, 0));
            Font fttext2 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.NORMAL, new BaseColor(0, 0, 0));
            Font fttext3 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));

            doc.open();
            Chunk c = new Chunk("Mentebit Software Solutions", fttitle);
            Chunk c1 = new Chunk("3rd Floor, Office 303, Vishal Complex,\nNear DSK Ranwara Building, DSK Ranwara Road,\nBavdhan, Pune-411021", ftadds);
            Chunk c2 = new Chunk("Salary Slip\n\n", ftsubtitle);

            Drawable d = getResources().getDrawable(R.drawable.mentebit_logo);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAbsolutePosition(70f,700f);
            image.scaleToFit(500,100);
            doc.add(image);

            Paragraph p1 = new Paragraph(c);
            p1.setAlignment(Paragraph.ALIGN_RIGHT);

            Paragraph p2 = new Paragraph(c1);
            p2.setAlignment(Paragraph.ALIGN_RIGHT);

            Paragraph p3 = new Paragraph(c2);
            p3.setAlignment(Paragraph.ALIGN_CENTER);

            p1.add(p2);
            p1.add(p3);

            float[] columnWidth = {1};
            PdfPTable table = new PdfPTable(columnWidth);
            table.setWidthPercentage(90f);

            insertCell(table, "Pay Slip for "+txtMonthYear.getText().toString(), Element.ALIGN_CENTER, 1, ftsubtitle1);
            p1.add(table);

            float[] columnWidth1 = {(float) 1.2, 2, (float) 1.4, (float) 1.6};
            PdfPTable table1 = new PdfPTable(columnWidth1);
            table1.setWidthPercentage(90f);

            insertCell(table1, "Emp ID:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtEmpID.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            insertCell(table1, "Designation:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtDesignaion.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            insertCell(table1, "Emp Name:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtEmpName.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            insertCell(table1, "Depart Name:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtDeptName.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            insertCell(table1, "Bank:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtBank.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            insertCell(table1, "Ttl Work Days:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtTotalWorkDays.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            insertCell(table1, "Bank AccNo:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtAccountID.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            insertCell(table1, "Attendance:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtAttendence.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            insertCell(table1, "PAN No:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtPancard.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            insertCell(table1, "Month/Year:", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table1, txtMonthYear.getText().toString(), Element.ALIGN_LEFT, 1, fttext2);

            p1.add(table1);
            table1.setSpacingAfter(10);
            //p1.add(Chunk.NEWLINE);

            float[] columnWidth2 = {2, 1, 1, 1};
            PdfPTable table2 = new PdfPTable(columnWidth2);
            table2.setWidthPercentage(90f);

            insertCell(table2, "Emoluments", Element.ALIGN_CENTER, 1, fttext1);
            insertCell(table2, "Amount Rs.", Element.ALIGN_CENTER, 1, fttext1);
            insertCell(table2, "Deductions", Element.ALIGN_CENTER, 1, fttext1);
            insertCell(table2, "Amount Rs.", Element.ALIGN_CENTER, 1, fttext1);

            insertCell(table2, "Basic Pay", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, txtBasicPay.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "Profession Tax", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, txtProfessionTax.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "House Rent Allowance", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, txtHRA.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "IncomeTax", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, txtIncomeTax.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "Transport Allowance", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, txtTransportAllowance.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, "", Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "Medical Allowance", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, txtMedicalAllowance.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, "", Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "Leave Travel Allowance", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, txtLTA.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, "", Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "Incentive", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, txtIncentive.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, "", Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "", Element.ALIGN_LEFT, 1, fttext3);
            insertCell(table2, "", Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "Total Deductions", Element.ALIGN_CENTER, 1, fttext3);
            insertCell(table2, txtTotalDeductions.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "Gross Pay", Element.ALIGN_CENTER, 1, fttext3);
            insertCell(table2, txtGrossPay.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            insertCell(table2, "Net Pay", Element.ALIGN_CENTER, 1, fttext3);
            insertCell(table2, txtNetPay.getText().toString(), Element.ALIGN_RIGHT, 1, fttext3);

            p1.add(table2);

            Chunk c3 = new Chunk("Note:- This is an electronically generated statement hence does not require any signature.", ftnote);
            Paragraph p4 = new Paragraph(c3);
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            p1.add(p4);
            doc.add(p1);

            File outputFile = new File(path + "/"+empname+"_"+monthyear+".pdf");
            Log.d("filepath", outputFile + "");
            Uri uri = FileProvider.getUriForFile(SalarySlip.this, BuildConfig.APPLICATION_ID + ".provider", outputFile);
            if(!uri.equals(null))
            {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SalarySlip.this);
                alertDialogBuilder.setTitle("Pdf created successfully at location Bizcall/SalarySlip")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        }).show();
               // Toast.makeText(SalarySlip.this,"Pdf created successfully at Bizcall/SalarySlip",Toast.LENGTH_SHORT).show();
            }
            else {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SalarySlip.this);
                alertDialogBuilder.setTitle("Pdf not created!!!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        }).show();
               // Toast.makeText(SalarySlip.this,"Pdf not created",Toast.LENGTH_SHORT).show();
            }
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("application/pdf");
            sendIntent.putExtra("jid", "918830992647" + "@s.whatsapp.net");// here 91 is country code
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            //startActivity(sendIntent);

        } catch (Exception e) {
            Toast.makeText(SalarySlip.this,"Errorcode-549 SalarySlip createandDisplayPDF "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.e("PDFCreator", "DocumentException:" + e);
        } finally {
            doc.close();
        }
    }//createandDisplayPdf

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
        try {

            PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
            cell.setHorizontalAlignment(align);
            cell.setColspan(colspan);
            if (text.trim().equalsIgnoreCase("")) {
                cell.setMinimumHeight(10f);
            }
            table.addCell(cell);
        }catch (Exception e)
        {
            Toast.makeText(SalarySlip.this,"Errorcode-550 SalarySlip insertCell "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//insertCell


    public void getSalaryslip()
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid=149&CounselorID="+counselorid+"&Month="+month1+"&Year="+year1;
            Log.d("SalarySlipUrl", url);

            if(CheckInternet.checkInternet(SalarySlip.this))
            {
                if(CheckServer.isServerReachable(SalarySlip.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();

                                    Log.d("SalarySlipResponse", response);
                                    try {
                                        if(response.contains("[]"))
                                        {
                                            linearSalarySlip.setVisibility(View.GONE);
                                            txtNotFound.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            imgPDF.setVisibility(View.VISIBLE);
                                            linearSalarySlip.setVisibility(View.VISIBLE);
                                            txtNotFound.setVisibility(View.GONE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String EmpIdNo = jsonObject1.getString("EmpIdNo");
                                                String EmpName = jsonObject1.getString("EmpFirstName")+" "+ jsonObject1.getString("EmpLastName");
                                                String cPanCard = jsonObject1.getString("cPanCard");
                                                String cBankAccountNo = jsonObject1.getString("cBankAccountNo");
                                                String cBank = jsonObject1.getString("cBank");
                                                String cAttendance = jsonObject1.getString("cAttendance");
                                                String cTotaklWorkDays = jsonObject1.getString("cTotaklWorkDays");
                                                String cBasicPay = jsonObject1.getString("cBasicPay");
                                                String cHRA = jsonObject1.getString("cHRA");
                                                String cTA = jsonObject1.getString("cTA");
                                                String cMA = jsonObject1.getString("cMA");
                                                String cLTA = jsonObject1.getString("cLTA");
                                                String cIncentive = jsonObject1.getString("cIncentive");
                                                String designation=jsonObject1.getString("EmpDesignation");
                                                String dept=jsonObject1.getString("EmpDepartment");
                                                String cProfessionTax = jsonObject1.getString("cProfessionTax");
                                                String cIncomeTax = jsonObject1.getString("cIncomeTax");
                                                String GrossPay = jsonObject1.getString("GrossPay");
                                                String cNetPay = jsonObject1.getString("cNetPay");
                                                String cMonth = jsonObject1.getString("cMonth");
                                                String cYear = jsonObject1.getString("cYear");

                                                txtEmpID.setText(EmpIdNo);
                                                txtEmpName.setText(EmpName);
                                                txtPancard.setText(cPanCard);
                                                txtAccountID.setText(cBankAccountNo);
                                                txtBank.setText(cBank);
                                                txtAttendence.setText(cAttendance);
                                                txtTotalWorkDays.setText(cTotaklWorkDays);
                                                txtBasicPay.setText(cBasicPay);
                                                txtHRA.setText(cHRA);
                                                txtTransportAllowance.setText(cTA);
                                                txtMedicalAllowance.setText(cMA);
                                                txtLTA.setText(cLTA);
                                                txtIncentive.setText(cIncentive);
                                                txtProfessionTax.setText(cProfessionTax);
                                                txtDesignaion.setText(designation);
                                                txtDeptName.setText(dept);
                                                txtIncomeTax.setText(cIncomeTax);
                                                txtGrossPay.setText(GrossPay);
                                                txtNetPay.setText(cNetPay);
                                                txtMonthYear.setText(cMonth+"/"+cYear);
                                              int totaldeductions=Integer.parseInt(txtProfessionTax.getText().toString())+Integer.parseInt(txtIncomeTax.getText().toString());
                                              txtTotalDeductions.setText(String.valueOf(totaldeductions));
                                            }

                                            //getPendingApproval();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(SalarySlip.this,"Errorcode-552 SalarySlip getSalarySlipResponse"+e.toString(),Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Log.d("Exceptio", String.valueOf(e));
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SalarySlip.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(SalarySlip.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SalarySlip.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SalarySlip.this);
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
            Toast.makeText(SalarySlip.this,"Errorcode-551 SalarySlip getSalarySlip "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcApprovedLeaves", String.valueOf(e));
        }
    }//getSalaryslip
}
