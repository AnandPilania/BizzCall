package com.bizcall.wayto.mentebit13;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class ActivitySocialMediaDialPad extends AppCompatActivity
{
    private String[] textArray = {"IN +91", "Afghan +93", "Africa +2", "Argentina +54", "Armenia +374", "Australia +61", "Banlga +880", "Belarus +375",
            "Belgium +32", "Bhutan +975", "Bulgaria +673", "Canada +1", "China +86", "Europe +3", "Georgia +995", "Germany +49", "Iraq +964", "Iran +98",
            "Japan +81", "Kazakhstan +7", "Kyrgyzstan +996", "Nepal +977", "NewZealand +64", "Nigeria +234","Pak +92", "Poland +48", "Qatar +974", "Russia +7", "Singapore +65",
            "SriLanka +94", "Sweden +46", "Switzerland +41", "Ukraine +380", "UAE +971", "UK +44", "US +1", "Zimbabwe +263"};
    private Integer[] imageArray = {R.drawable.india, R.drawable.afghan, R.drawable.africa, R.drawable.argentina, R.drawable.armenia, R.drawable.australia,
            R.drawable.bangla, R.drawable.belarus, R.drawable.belgium, R.drawable.bhutan, R.drawable.bulgaria, R.drawable.canada, R.drawable.china, R.drawable.europe,
            R.drawable.georgia, R.drawable.germany, R.drawable.iraq, R.drawable.iran, R.drawable.japan, R.drawable.kazakhstan, R.drawable.kyrgyzstan, R.drawable.nepal,
            R.drawable.newzealand, R.drawable.nigeria, R.drawable.pakistan,R.drawable.poland, R.drawable.qatar, R.drawable.russia, R.drawable.singapore, R.drawable.srilanka, R.drawable.sweden,
            R.drawable.switzerland, R.drawable.ukraine, R.drawable.uae, R.drawable.uk, R.drawable.us, R.drawable.zimbabwe};
    String clicked="",orderstatus="",filename="",msg, strCountryCode,phoneNo,invoicepath,receiptpath,email,sharewhat,name,amount,paymentfor,formno,paymentlink,receiptlink,invoicelink,receiptname,invoicename;
    CheckBox checkBoxPayment,checkBoxInvoice,checkBoxReceipt;
    int flag=0;
    TextView txtNotSelectedLink;
    Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_countrycode);

        final Spinner spiCountryCode = findViewById(R.id.spinner_countrycode);
        final EditText edtMobNo = findViewById(R.id.edt_mobno);
        Button btnSMS = findViewById(R.id.btn_padsms);
        Button btnWhatsapp = findViewById(R.id.btn_padwhatsapp);
        Button btnCancle = findViewById(R.id.btn_country_cancle);
        checkBoxPayment=findViewById(R.id.chkboxPaymentLink);
        checkBoxInvoice=findViewById(R.id.chkboxInvoiceLink);
        checkBoxReceipt=findViewById(R.id.chkboxReceiptLink);
        txtNotSelectedLink=findViewById(R.id.txtNotSelectedLink);
        sharewhat=getIntent().getStringExtra("Share");
        orderstatus=getIntent().getStringExtra("OrderStatus");

        if(sharewhat.equals("PDF"))
        {
            invoicepath = getIntent().getStringExtra("invoicepath");
            receiptpath=getIntent().getStringExtra("receiptpath");
            filename = getIntent().getStringExtra("filename");
            clicked=getIntent().getStringExtra("Clicked");
        }
        phoneNo = getIntent().getStringExtra("phoneNo");
        email = getIntent().getStringExtra("email");
        name=getIntent().getStringExtra("name");
        formno=getIntent().getStringExtra("formno");
        amount=getIntent().getStringExtra("amount");
        paymentfor=getIntent().getStringExtra("paymentfor");
        paymentlink=getIntent().getStringExtra("paymentlink");
        invoicelink=getIntent().getStringExtra("invoicelink");
        receiptlink=getIntent().getStringExtra("receiptlink");
        receiptname=getIntent().getStringExtra("receiptpdf");
        invoicename=getIntent().getStringExtra("invoicepdf");
        if(clicked.equals("Invoice")) {
            if(orderstatus.equals("Awaiting")) {
                msg = "Dear " + name + ", \n" +
                        "\n" +
                        "Thanking you for choosing Select Your University as your preferred study abroad partner. \n" +
                        "\n" +
                        "Your file number is " + formno + ". Total amount payable is INR " + amount + "/- against " + paymentfor +
                        "\n" +
                        "Kindly make the payment at the following link.\n " + paymentlink + " Please read the TandC before making payment. ";
            }
            else {
                msg = "Dear " + name + ", \n" +
                        "\n" +
                        "Thanking you for choosing Select Your University as your preferred study abroad partner. \n" +
                        "\n" +
                        "Your file number is " + formno + ". Total amount paid is INR " + amount + "/- against " + paymentfor +
                        "\n" +
                        "Kindly download invoice";
            }
        } else if(clicked.equals("Receipt"))
        { msg = "Dear " + name + ", \n" +
                    "\n" +
                    "Thanking you for choosing Select Your University as your preferred study abroad partner. \n" +
                    "\n" +
                    "Your file number is " + formno + ". Total amount paid is INR " + amount + "/- against " + paymentfor +
                    "\n" +
                    "Kindly download receipt";

        }else {
            if(orderstatus.equals("Awaiting")) {
                msg = "Dear " + name + ", \n" +
                        "\n" +
                        "Thanking you for choosing Select Your University as your preferred study abroad partner. \n" +
                        "\n" +
                        "Your file number is " + formno + ". Total amount payable is INR " + amount + "/- against " + paymentfor +
                        "\n" +
                        "Kindly make the payment at the following link.\n " + paymentlink + " Please read the TandC before making payment. ";
            }
            else {
                msg = "Dear " + name + ", \n" +
                        "\n" +
                        "Thanking you for choosing Select Your University as your preferred study abroad partner. \n" +
                        "\n" +
                        "Your file number is " + formno + ". Total amount paid is INR " + amount + "/- against " + paymentfor +
                        "\n" +
                        "Kindly download invoice";
            }
        }
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.lay_spinner_value, textArray, imageArray);
        spiCountryCode.setAdapter(adapter);

        edtMobNo.setText(phoneNo);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                flag=0;
                msg=msg+"\n\n" +
                        "Thanks & Regards,\n"  +
                        "Management \n" +
                        "Select Your University.";
                if(checkBoxPayment.isChecked())
                {
                    flag=1;
                    sendIntent.putExtra(Intent.EXTRA_TEXT,msg);
                }
                if (sharewhat.equals("Message")) {
                   /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNo));
                    intent.putExtra("sms_body", msg);
                    startActivity(intent);*/

                    String[] emailAddress = {email};
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setPackage("com.google.android.gm");
                    sendIntent.setType("plain/text");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                   // sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,msg);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Thank you for choosing Select Your University as study abroad partner");
                    sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //  if (flag == 1) {
                    startActivity(Intent.createChooser(sendIntent, "Send Mail"));
                    finish();
                }else {
                    if(filename.equals("Invoice")) {
                        File outputFile = new File(invoicepath + "/" + invoicename);
                        Log.d("filepath", outputFile + "");
                        uri = FileProvider.getUriForFile(ActivitySocialMediaDialPad.this, BuildConfig.APPLICATION_ID + ".provider", outputFile);
                    }else {
                        File outputFile = new File(receiptpath + "/" + receiptname);
                        Log.d("filepath", outputFile + "");
                         uri = FileProvider.getUriForFile(ActivitySocialMediaDialPad.this, BuildConfig.APPLICATION_ID + ".provider", outputFile);
                    }
                    String[] emailAddress = {email};
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setPackage("com.google.android.gm");
                    sendIntent.setType("application/pdf");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,msg);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Thank you for choosing Select Your University as partner");
                    /*if (checkBoxInvoice.isChecked()) {
                        flag = 1;
                        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    }
                    if (checkBoxReceipt.isChecked()) {
                        flag = 1;
                        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    }*/
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  //  if (flag == 1) {
                        startActivity(Intent.createChooser(sendIntent, "Send Mail"));
                        finish();
                       // txtNotSelectedLink.setVisibility(View.GONE);
                   /* } else {
                        txtNotSelectedLink.setVisibility(View.VISIBLE);
                    }*/
                }

            }
        });

        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strCountryCode == null) {
                    String countrycode = spiCountryCode.getSelectedItem().toString();
                    strCountryCode = countrycode.substring(countrycode.indexOf("+") + 1);
                    Log.d("wpurl", strCountryCode + phoneNo);
                }
                boolean installed = appInstalledOrNot("com.whatsapp");

                if (installed) {
                    try {
                        if (sharewhat.equals("Message")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                           // intent.putExtra(Intent.EXTRA_STREAM, uri);
                            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" +strCountryCode + phoneNo + "&text=" + msg));
                            startActivity(intent);
                        }
                        else {
                            flag = 0;
                            if(filename.equals("Invoice")) {
                                    File outputFile = new File(invoicepath + "/" + invoicename);
                                    Log.d("filepath", outputFile + "");
                                    uri = FileProvider.getUriForFile(ActivitySocialMediaDialPad.this, BuildConfig.APPLICATION_ID + ".provider", outputFile);
                                }else {
                                    File outputFile = new File(receiptpath + "/" + receiptname);
                                    Log.d("filepath", outputFile + "");
                                    uri = FileProvider.getUriForFile(ActivitySocialMediaDialPad.this, BuildConfig.APPLICATION_ID + ".provider", outputFile);
                            }
                            //Intent sendIntent = new Intent("android.intent.action.MAIN");

                            // sendIntent.setType("plain/text");
                           /* if (checkBoxPayment.isChecked()) {
                                flag = 1;
                                sendIntent.setType("text/plain");
                                // sendIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" +strCountryCode + phoneNo + "&text=" + msg));
                                sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                            }
                            if (checkBoxInvoice.isChecked()) {
                                flag = 1;
                                sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                sendIntent.setType("application/pdf");
                            }
                            if (checkBoxReceipt.isChecked()) {
                                flag = 1;
                                sendIntent.putExtra(Intent.EXTRA_STREAM, uri1);
                                sendIntent.setType("application/pdf");
                            }
                            if (flag == 1) {
                                Log.d("enter", "" + flag);*/
                          //  Uri urino = Uri.parse("https://api.whatsapp.com/send?phone=" +strCountryCode + phoneNo );
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                           // sendIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" +strCountryCode + phoneNo + "&text=" + msg));
                          // sendIntent.putExtra(Intent.EXTRA_STREAM,urino);
                            sendIntent.setType("text/plain");
                            sendIntent.putExtra(Intent.EXTRA_TEXT,msg);
                            sendIntent.setPackage("com.whatsapp");
                            sendIntent.setType("application/pdf");
                            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            sendIntent.putExtra("jid", strCountryCode + edtMobNo.getText().toString() + "@s.whatsapp.net");// here 91 is country code
                            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(sendIntent);
                                finish();
                              /*  txtNotSelectedLink.setVisibility(View.GONE);
                            } else {
                                txtNotSelectedLink.setVisibility(View.VISIBLE);
                            }*/
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ActivitySocialMediaDialPad.this, "Application not found. Please install WhatsApp.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
