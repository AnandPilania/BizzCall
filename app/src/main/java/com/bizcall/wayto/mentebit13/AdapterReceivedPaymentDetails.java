package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AdapterReceivedPaymentDetails extends RecyclerView.Adapter<AdapterReceivedPaymentDetails.MyHolder> {
    Context context;
    ArrayList<DataReceivedPaymentDetails> arrayList;
    String path,path1;
    File directory;
    boolean success;

    public AdapterReceivedPaymentDetails(Context context, ArrayList<DataReceivedPaymentDetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_receivedpayment, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final DataReceivedPaymentDetails dataReceivedPaymentDetails = arrayList.get(i);
        if (dataReceivedPaymentDetails.getOrderstatus().equals("Success")) {
            myHolder.txtInvoiceLink.setText("Invoice");
            myHolder.txtReceiptLink.setText("Receipt");
            myHolder.txtPaymentLink.setText("Not Available");
        } else {
            myHolder.txtInvoiceLink.setText("Invoice");
            myHolder.txtReceiptLink.setText("Not Available");
            myHolder.txtPaymentLink.setText("PaymentLink");
        }
        if(myHolder.txtReceiptLink.getText().toString().equals("Not Available"))
        {
            myHolder.txtReceiptLink.setClickable(false);
            myHolder.txtReceiptLink.setEnabled(false);
        }
        if(myHolder.txtPaymentLink.getText().toString().equals("Not Available"))
        {
            myHolder.txtPaymentLink.setClickable(false);
            myHolder.txtPaymentLink.setEnabled(false);
        }
        myHolder.txtPaymentDate.setText(dataReceivedPaymentDetails.getPaymentDate());
        myHolder.txtOrderAmount.setText(dataReceivedPaymentDetails.getOrderamount());
        myHolder.txtOrderFor.setText(dataReceivedPaymentDetails.getOrderfor());
        myHolder.txtOrderStatus.setText(dataReceivedPaymentDetails.getOrderstatus());
        myHolder.txtOrderID.setText(dataReceivedPaymentDetails.getOrderid());
        myHolder.txtPaymethod.setText(dataReceivedPaymentDetails.getPaymethod());
        myHolder.txtInvoiceId.setText(dataReceivedPaymentDetails.getInvoiceId());
        myHolder.txtTrackingid.setText(dataReceivedPaymentDetails.getTrackingid());
        myHolder.txtBankrefno.setText(dataReceivedPaymentDetails.getBankrefno());
        myHolder.txtBillname.setText(dataReceivedPaymentDetails.getBillname());
        myHolder.txtBillPhone.setText(dataReceivedPaymentDetails.getBillphone());
        myHolder.txtBillemail.setText(dataReceivedPaymentDetails.getBillemail());
        myHolder.txtAddress.setText(dataReceivedPaymentDetails.getAddress());
        myHolder.txtCity.setText(dataReceivedPaymentDetails.getCity());
        myHolder.txtState.setText(dataReceivedPaymentDetails.getState());
        myHolder.txtPin.setText(dataReceivedPaymentDetails.getPin());
        myHolder.txtDatePay.setText(dataReceivedPaymentDetails.getDatepay());
        myHolder.txtWebsite.setText(dataReceivedPaymentDetails.getWebsite());
        myHolder.txtPaymentthrough.setText(dataReceivedPaymentDetails.getPaymentthrough());
        myHolder.txtFormno.setText(dataReceivedPaymentDetails.getFormno());
        myHolder.txtCounselorid.setText(dataReceivedPaymentDetails.getCounselorid());
        myHolder.txtPaymentLink1.setText(dataReceivedPaymentDetails.getPaymentlink());
        myHolder.txtInvoiceLink1.setText(dataReceivedPaymentDetails.getInvoicelink());
        myHolder.txtReceiptLink1.setText(dataReceivedPaymentDetails.getReceiptlink());

       /* Linkify.addLinks(myHolder.txtReceiptLink1 , Linkify.WEB_URLS);
        Linkify.addLinks(myHolder.txtInvoiceLink1 , Linkify.WEB_URLS);*/
        // Linkify.addLinks(myHolder.txtReceiptLink1 , Linkify.WEB_URLS);
        myHolder.txtPaymentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getReceipt(dataReceivedPaymentDetails.getBillname(), dataReceivedPaymentDetails.getBillphone(), dataReceivedPaymentDetails.getBillemail(), dataReceivedPaymentDetails.getOrderid(), dataReceivedPaymentDetails.getTrackingid(), dataReceivedPaymentDetails.getBankrefno(), dataReceivedPaymentDetails.getPaymentDate(), dataReceivedPaymentDetails.getOrderstatus(), dataReceivedPaymentDetails.getOrderamount());
               // getProforma(dataReceivedPaymentDetails.getBillname(), dataReceivedPaymentDetails.getBillphone(), dataReceivedPaymentDetails.getBillemail(), dataReceivedPaymentDetails.getPaymentDate(), dataReceivedPaymentDetails.getOrderid(), dataReceivedPaymentDetails.getOrderstatus(), dataReceivedPaymentDetails.getOrderamount());
                Intent intent = new Intent(context, ActivitySocialMediaDialPad.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("phoneNo",dataReceivedPaymentDetails.getBillphone());
                intent.putExtra("invoicepath", path);
                intent.putExtra("receiptpath",path1);
                intent.putExtra("Share","Message");
                intent.putExtra("OrderStatus",dataReceivedPaymentDetails.getOrderstatus());

                intent.putExtra("email",dataReceivedPaymentDetails.getBillemail());
                intent.putExtra("name",dataReceivedPaymentDetails.getBillname());
                intent.putExtra("amount",dataReceivedPaymentDetails.getOrderamount());
                intent.putExtra("paymentfor",dataReceivedPaymentDetails.getOrderfor());
                intent.putExtra("formno",dataReceivedPaymentDetails.getFormno());
                intent.putExtra("paymentlink",dataReceivedPaymentDetails.getPaymentlink());
                intent.putExtra("invoicelink",dataReceivedPaymentDetails.getInvoicelink());
                intent.putExtra("receiptlink",dataReceivedPaymentDetails.getReceiptlink());
                intent.putExtra("receiptpdf","Receipt_"+dataReceivedPaymentDetails.getOrderid()+".pdf");
                intent.putExtra("invoicepdf","Invoice_"+dataReceivedPaymentDetails.getOrderid()+".pdf");
                context.startActivity(intent);
            }
        });

        myHolder.txtReceiptLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternetSpeed.checkInternet(context).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("No Internet connection!!!")
                            .setMessage("Can't do further process")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                } else if (CheckInternetSpeed.checkInternet(context).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Slow Internet speed!!!")
                            .setMessage("Can't do further process")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    getReceipt(dataReceivedPaymentDetails.getBillname(), dataReceivedPaymentDetails.getBillphone(), dataReceivedPaymentDetails.getBillemail(), dataReceivedPaymentDetails.getOrderid(), dataReceivedPaymentDetails.getTrackingid(), dataReceivedPaymentDetails.getBankrefno(), dataReceivedPaymentDetails.getPaymentDate(), dataReceivedPaymentDetails.getOrderstatus(), dataReceivedPaymentDetails.getOrderamount(),dataReceivedPaymentDetails.getOrderfor());
                    //getProforma(dataReceivedPaymentDetails.getBillname(), dataReceivedPaymentDetails.getBillphone(), dataReceivedPaymentDetails.getBillemail(), dataReceivedPaymentDetails.getDatepay(), dataReceivedPaymentDetails.getOrderid(), dataReceivedPaymentDetails.getOrderstatus(), dataReceivedPaymentDetails.getOrderamount());
                    Intent intent = new Intent(context, ActivitySocialMediaDialPad.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("phoneNo",dataReceivedPaymentDetails.getBillphone());
                    intent.putExtra("invoicepath", path);
                    intent.putExtra("receiptpath",path1);
                    intent.putExtra("email",dataReceivedPaymentDetails.getBillemail());
                    intent.putExtra("name",dataReceivedPaymentDetails.getBillname());
                    intent.putExtra("amount",dataReceivedPaymentDetails.getOrderamount());
                    intent.putExtra("OrderStatus",dataReceivedPaymentDetails.getOrderstatus());
                    intent.putExtra("Share","PDF");
                    intent.putExtra("filename","Receipt");
                    intent.putExtra("Clicked","Receipt");
                    intent.putExtra("formno",dataReceivedPaymentDetails.getFormno());
                    intent.putExtra("paymentfor",dataReceivedPaymentDetails.getOrderfor());
                    intent.putExtra("paymentlink",dataReceivedPaymentDetails.getPaymentlink());
                    intent.putExtra("invoicelink",dataReceivedPaymentDetails.getInvoicelink());
                    intent.putExtra("receiptlink",dataReceivedPaymentDetails.getReceiptlink());
                    intent.putExtra("receiptpdf","Receipt_"+dataReceivedPaymentDetails.getOrderid()+".pdf");
                    intent.putExtra("invoicepdf","Invoice_"+dataReceivedPaymentDetails.getOrderid()+".pdf");
                    context.startActivity(intent);

                    /* SummaryDetails.webViewReceipt.setWebViewClient(new MyBrowser());
                    String url1=dataReceivedPaymentDetails.getReceiptlink().trim();
                    if(!url1.startsWith("u4l"))
                    {
                        url1="http://u4l.in?"+url1;
                    }else {
                        url1="http://"+url1;
                    }
                  //  String url ="http://"+dataReceivedPaymentDetails.getReceiptlink().trim();
                    Log.d("Url",url1);
                    SummaryDetails.webViewReceipt.getSettings().setLoadsImagesAutomatically(true);
                    SummaryDetails.webViewReceipt.getSettings().setJavaScriptEnabled(true);
                    SummaryDetails.webViewReceipt.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    SummaryDetails.webViewReceipt.loadUrl(url1);*/
                }
            }
        });
        myHolder.txtInvoiceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternetSpeed.checkInternet(context).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("No Internet connection!!!")
                            .setMessage("Can't do further process")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                } else if (CheckInternetSpeed.checkInternet(context).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Slow Internet speed!!!")
                            .setMessage("Can't do further process")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    getProforma(dataReceivedPaymentDetails.getBillname(), dataReceivedPaymentDetails.getBillphone(), dataReceivedPaymentDetails.getBillemail(), dataReceivedPaymentDetails.getPaymentDate(), dataReceivedPaymentDetails.getOrderid(), dataReceivedPaymentDetails.getOrderstatus(), dataReceivedPaymentDetails.getOrderamount(),dataReceivedPaymentDetails.getOrderfor());
                    Intent intent = new Intent(context, ActivitySocialMediaDialPad.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("phoneNo",dataReceivedPaymentDetails.getBillphone());
                    intent.putExtra("invoicepath", path);
                    intent.putExtra("receiptpath",path1);
                    intent.putExtra("email",dataReceivedPaymentDetails.getBillemail());
                    intent.putExtra("name",dataReceivedPaymentDetails.getBillname());
                    intent.putExtra("amount",dataReceivedPaymentDetails.getOrderamount());
                    intent.putExtra("Share","PDF");
                    intent.putExtra("paymentfor",dataReceivedPaymentDetails.getOrderfor());
                    intent.putExtra("OrderStatus",dataReceivedPaymentDetails.getOrderstatus());
                    intent.putExtra("filename","Invoice");
                    intent.putExtra("Clicked","Invoice");
                    intent.putExtra("formno",dataReceivedPaymentDetails.getFormno());
                    intent.putExtra("paymentlink",dataReceivedPaymentDetails.getPaymentlink());
                    intent.putExtra("invoicelink",dataReceivedPaymentDetails.getInvoicelink());
                    intent.putExtra("receiptlink",dataReceivedPaymentDetails.getReceiptlink());
                    intent.putExtra("receiptpdf","Receipt_"+dataReceivedPaymentDetails.getOrderid()+".pdf");
                    intent.putExtra("invoicepdf","Invoice_"+dataReceivedPaymentDetails.getOrderid()+".pdf");
                    context.startActivity(intent);

                    /* SummaryDetails.webViewInvoice.setWebViewClient(new MyBrowser());
                    String url ="https://"+dataReceivedPaymentDetails.getInvoicelink().trim();
                    Log.d("Url",url);
                    SummaryDetails.webViewInvoice.getSettings().setLoadsImagesAutomatically(true);
                    SummaryDetails.webViewInvoice.getSettings().setJavaScriptEnabled(true);
                    SummaryDetails.webViewInvoice.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    SummaryDetails.webViewInvoice.loadUrl(url);*/
                }
            }
        });
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void getReceipt(String name, String phone, String email, String orderid, String trackingid, String bankref, String dtTimeDate, String orderstatus, String amount,String paymentfor) {
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        try {
            path1 = Environment.getExternalStorageDirectory() + "/Bizcall/PaymentReceipt";
            File dir = new File(path1);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "Receipt_"+orderid+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);

            //special font sizes
            Font ftnote = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL, new BaseColor(255, 148, 34));
            Font ftsubtitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, new BaseColor(0, 0, 0));
            Font ftterms = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, new BaseColor(0, 0, 0));
            Font fttext1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, new BaseColor(0, 0, 0));
            Font fttext2 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.NORMAL, new BaseColor(0, 0, 0));
            Font fttext3 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));

            doc.open();

            Drawable d = context.getResources().getDrawable(R.drawable.select_logo);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAbsolutePosition(40f, 730f);
            image.scaleToFit(400, 80);
            doc.add(image);

            Chunk c = new Chunk("Order ID #:" + orderid + "\n", fttext3);
            Chunk c1 = new Chunk("Tracking ID:" + trackingid + "\n", fttext3);
            Chunk c2 = new Chunk("Bank Ref:" + bankref + "\n", fttext3);
            Chunk c3 = new Chunk(dtTimeDate + "\n\n", fttext3);

            Paragraph p1 = new Paragraph(c);
            p1.setAlignment(Paragraph.ALIGN_RIGHT);

            Paragraph p2 = new Paragraph(c1);
            p2.setAlignment(Paragraph.ALIGN_RIGHT);
            p1.add(p2);

            Paragraph p3 = new Paragraph(c2);
            p3.setAlignment(Paragraph.ALIGN_RIGHT);
            p1.add(p3);

            Paragraph p4 = new Paragraph(c3);
            p4.setAlignment(Paragraph.ALIGN_RIGHT);
            p1.add(p4);

            PdfPTable table0 = new PdfPTable(2);
            table0.setWidthPercentage(100);
            table0.addCell(getCell("3FCS-02, Ansal plaza, Opposite,\n Dabur Chowk, Vaishali Ghaziabad,\n Uttar Pradesh 201010 \n tel:+91 9555120101 \n www.selectyouruniversity.com", PdfPCell.ALIGN_LEFT));
            table0.addCell(getCell(name + ",\n" + phone + ",\n" + email, PdfPCell.ALIGN_RIGHT));
            p1.add(table0);
            table0.setSpacingAfter(10);

            Chunk c5 = new Chunk("Receipt\n", ftnote);

            Paragraph p6 = new Paragraph(c5);
            p6.setAlignment(Paragraph.ALIGN_CENTER);
            p1.add(p6);
            p6.setSpacingAfter(5);

            float[] columnWidth = {1, 1};
            PdfPTable table = new PdfPTable(columnWidth);
            table.setWidthPercentage(100);

            insertCell(table, "Payment Method", Element.ALIGN_LEFT, 1, fttext1);
            insertCell(table, "Unified Payments", Element.ALIGN_RIGHT, 1, fttext1);

            insertCell(table, "Order ID", Element.ALIGN_LEFT, 1, fttext2);
            insertCell(table, orderid, Element.ALIGN_RIGHT, 1, fttext2);

            insertCell(table, "Order Status", Element.ALIGN_LEFT, 1, fttext2);
            insertCell(table, orderstatus, Element.ALIGN_RIGHT, 1, fttext2);

            insertCell(table, "Payment For", Element.ALIGN_LEFT, 1, fttext2);
            insertCell(table, paymentfor, Element.ALIGN_RIGHT, 1, fttext2);
            p1.add(table);
            table.setSpacingAfter(5);
            //p1.add(Chunk.NEWLINE);
            Chunk c6 = new Chunk("Total: " + amount, fttext3);
            Paragraph p7 = new Paragraph(c6);
            p7.setAlignment(Paragraph.ALIGN_RIGHT);
            p1.add(p7);


            Chunk c7 = new Chunk("Terms and Conditions", ftsubtitle);
            Paragraph p8 = new Paragraph(c7);
            p8.setAlignment(Paragraph.ALIGN_CENTER);
            p1.add(p8);

            Chunk c8 = new Chunk("Introduction: These terms and conditions apply to the User who uses the Online Services provided for any payment made Select Your University. Kindly read these terms and conditions carefully. By authorizing a payment to Select Your University through the online payment service (\"the service\"), it would be treated as a deemed acceptance to these terms and conditions. Select Your University reserves all the rights to amend these terms and conditions at any time without giving prior notice. It is the responsibility of the User to have read the terms and conditions before using the Service.\n" +
                    "Key terms:\n" +
                    "The following is a summary of the key terms of this service:\n" +
                    "\n" +
                    "•Payment(s) through this Service may only be made with a Credit Card, Debit card, Net Banking, UPI or Wallet.\n" +
                    "\n" +
                    "•Before using this Service, it is recommended that the user shall make necessary enquiry about the charges or fees payable against the Credit/Debit card used from Credit Card or the Debit Card service provider i.e. the respective Bank.\n" +
                    "\n" +
                    "•The credit card information supplied at the time of using the service is processed by the payment gateway of the service provider and is not supplied to Select Your University. It is the sole responsibility of the User of the service to ensure that the information entered in the relevant fields are correct. It is recommended that you take and retain a copy of the transaction for record keeping purposes, which might assist in resolution of any disputes that may arise out or usage of the service\n" +
                    "\n" +
                    "•The Applicant agrees, understands and confirms that his/ her personal data including without limitation details relating to debit card/ credit card/net banking transmitted over the Internet may be susceptible to misuse, hacking, theft and/ or fraud and that Select Your University or the Payment Service Provider(s) have no control over such matters.\n" +
                    "\n" +
                    "•The service is provided using a payment gateway service provider through a secure website. However, neither the payment gateway service provider nor the Select Your University gives any assurance, that the information so provided online by a user is secured or may be read or intercepted by a third party. Select Your University does not accept or assume any liability in the event of such unauthorized interception, hacking or other unauthorized access to information provided by a user of the service.\n" +
                    "\n" +
                    "• Select Your University  and/or the Payment Service Providers shall not be liable for any inaccuracy, error or delay in, or omission of (a) any data, information or message, or (b) the transmission or delivery of any such data, information or message; or (c) any loss or damage arising from or occasioned by any such inaccuracy, error, delay or omission, non-performance or interruption in any such data, information or message. Under no circumstances shall the Select Your University and/or the Payment Service Providers, its employees, directors, and its third party agents involved in processing, delivering or managing the Services, be liable for any direct, indirect, incidental, special or consequential damages, or any damages whatsoever, including punitive or exemplary arising out of or in any way connected with the provision of or any inadequacy or deficiency in the provision of the Services or resulting from unauthorized access or alteration of transmissions of data or arising from suspension or termination of the Service.\n" +
                    "\n" +
                    "•The Applicant agrees that Select Your University or any of its employees will not be held liable By the Applicant for any loss or damages arising from your use of, or reliance upon the information contained on the Website, or any failure to comply with these Terms and Conditions where such failure is due to circumstance beyond Select Your University’s reasonable control.\n" +
                    "\n" +
                    "Debit/Credit Card, Bank Account Details:\n" +
                    "\n" +
                    "The Applicant agrees that the debit/credit card details provided by him/ her for use of the aforesaid Service(s) must be correct and accurate and that the Applicant shall not use a Debit/ credit card, that is not lawfully owned by him/ her or the use of which is not authorized by the lawful owner thereof. The Applicant further agrees and undertakes to Provide correct and valid debit/credit card details.\n" +
                    "\n" +
                    "•The Applicant may pay his/ her application/initial fees to Select Your University  by using a debit/credit card or through online banking account. The Applicant warrants, agrees and confirms that when he/ she initiates a payment transaction and/or issues an online payment instruction and provides his/ her card / bank details:\n" +
                    "\n" +
                    "oThe Applicant is fully and lawfully entitled to use such credit / debit card, bank account for such transactions;\n" +
                    "\n" +
                    "oThe Applicant is responsible to ensure that the card/ bank account details provided by him/ her are accurate;\n" +
                    "\n" +
                    "oThe Applicant authorizes debit of the nominated card/ bank account for the Payment of fees selected by such Applicantalong with the applicable Fees.\n" +
                    "\n" +
                    "oThe Applicant is responsible to ensure that sufficient credit is available on the nominated card/ bank account at the time of making the payment to permit the Payment of the dues payable or fees duesselected by the Applicant inclusive of the applicable Fee.\n", ftterms);

            Paragraph p9 = new Paragraph(c8);
            p9.setAlignment(Paragraph.ALIGN_MIDDLE);
            p1.add(p9);

            Chunk c4 = new Chunk("This is computer generated receipt", fttext3);
            Paragraph p5 = new Paragraph(c4);
            p5.setAlignment(Paragraph.ALIGN_LEFT);

            p1.add(p5);
            doc.add(p1);
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
    }

    public void getProforma(String name, String phone, String email, String dtTimeDate, String orderid, String orderstatus, String amount,String paymentfor) {
        com.itextpdf.text.Document doc = new Document();
        try
        {
            path = Environment.getExternalStorageDirectory() + "/Bizcall/Invoice";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "Invoice_"+orderid+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);

            //special font sizes
            Font fttitle = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.NORMAL, new BaseColor(0, 0, 0));
            Font ftadds = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0));
            Font ftnote = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL, new BaseColor(255, 148, 34));
            Font ftsubtitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, new BaseColor(0, 0, 0));
            Font ftsubtitle1 = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.NORMAL, new BaseColor(255, 0, 0));
            Font ftterms = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, new BaseColor(0, 0, 0));
            Font fttext1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, new BaseColor(0, 0, 0));
            Font fttext2 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.NORMAL, new BaseColor(0, 0, 0));
            Font fttext3 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));

            doc.open();
            Chunk c = new Chunk("Order ID #" + orderid + "\n", fttext3);
            Chunk c1 = new Chunk(dtTimeDate + "\n\n\n\n\n", fttext3);
            Chunk c3 = new Chunk("PROFORMA INVOICE\n", ftnote);


            Drawable d = context.getResources().getDrawable(R.drawable.select_logo);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAbsolutePosition(40f, 730f);
            image.scaleToFit(400, 80);
            doc.add(image);

            Paragraph p1 = new Paragraph(c);
            p1.setAlignment(Paragraph.ALIGN_RIGHT);

            Paragraph p4 = new Paragraph(c1);
            p4.setAlignment(Paragraph.ALIGN_RIGHT);
            p1.add(p4);

            PdfPTable table0 = new PdfPTable(2);
            table0.setWidthPercentage(100);
            table0.addCell(getCell("3FCS-02, Ansal plaza, Opposite,\n Dabur Chowk, Vaishali Ghaziabad,\n Uttar Pradesh 201010 \n tel:+91 9555120101 \n www.selectyouruniversity.com", PdfPCell.ALIGN_LEFT));
            table0.addCell(getCell(name + ",\n" + phone + ",\n" + email, PdfPCell.ALIGN_RIGHT));
            p1.add(table0);
            table0.setSpacingAfter(10);

            Paragraph p2 = new Paragraph(c3);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p1.add(p2);
            p2.setSpacingAfter(5);

            float[] columnWidth = {1};
            PdfPTable table = new PdfPTable(columnWidth);
            table.setWidthPercentage(100);

            insertCell(table, "Payment Method", Element.ALIGN_LEFT, 1, fttext1);
            p1.add(table);

            float[] columnWidth1 = {2, 1};
            PdfPTable table1 = new PdfPTable(columnWidth1);
            table1.setWidthPercentage(100);

            insertCell(table1, "Order ID", Element.ALIGN_LEFT, 1, fttext2);
            insertCell(table1, orderid, Element.ALIGN_RIGHT, 1, fttext2);

            insertCell(table1, "Order Status", Element.ALIGN_LEFT, 1, fttext2);
            insertCell(table1, orderstatus, Element.ALIGN_RIGHT, 1, fttext2);

            insertCell(table1, "Payment For", Element.ALIGN_LEFT, 1, fttext2);
            insertCell(table1, paymentfor, Element.ALIGN_RIGHT, 1, fttext2);

            p1.add(table1);
            table1.setSpacingAfter(5);
            //p1.add(Chunk.NEWLINE);

            Chunk c4 = new Chunk("Total:" + amount, fttext3);
          //  Chunk c5 = new Chunk("*Excluding taxes", fttext3);

            Paragraph pp4 = new Paragraph(c4);
            pp4.setAlignment(Paragraph.ALIGN_RIGHT);
           // Paragraph p5 = new Paragraph(c5);
            //p5.setAlignment(Paragraph.ALIGN_RIGHT);

            Chunk c6 = new Chunk("Terms and Conditions", ftsubtitle);
            Paragraph p6 = new Paragraph(c6);
            p6.setAlignment(Paragraph.ALIGN_CENTER);

            Chunk c7 = new Chunk("1] Payment will be made by Custemor within 15 days upon receipt of an invoice.\n" +
                    "2] In the event there is a delay in payment for more than 5 dyas from the due date.\n" +
                    "3] The Customer shall be liable to pay an interest of 1.5% per month or maximum permitted by applicable law," +
                    "whichever is less, on the delayed payments from the due date of payment.", ftterms);
            Paragraph p7 = new Paragraph(c7);
            p7.setAlignment(Paragraph.ALIGN_MIDDLE);

            p1.add(pp4);
          //  p1.add(p5);
            p1.add(p6);
            p1.add(p7);

            doc.add(p1);
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        table.addCell(cell);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtPaymentLink,txtPaymentLink1, txtInvoiceLink1, txtReceiptLink1, txtInvoiceLink, txtReceiptLink, txtPaymentDate, txtOrderAmount, txtOrderFor, txtOrderStatus, txtOrderID, txtPaymethod, txtInvoiceId, txtTrackingid,
                txtBankrefno, txtBillname, txtBillPhone, txtBillemail, txtAddress, txtCity, txtState, txtPin, txtDatePay, txtWebsite, txtPaymentthrough, txtFormno, txtCounselorid;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtPaymentLink=itemView.findViewById(R.id.txtPaymentLink);
            txtPaymentLink1 = itemView.findViewById(R.id.txtPaymentLink1);
            txtReceiptLink1 = itemView.findViewById(R.id.txtReceiptLink1);
            txtInvoiceLink1 = itemView.findViewById(R.id.txtInvoiceLink1);
            txtReceiptLink = itemView.findViewById(R.id.txtReceiptLink);
            txtInvoiceLink = itemView.findViewById(R.id.txtInvoiceLink);
            txtPaymentDate = itemView.findViewById(R.id.txtPaymentDate);
            txtOrderAmount = itemView.findViewById(R.id.txtOrderAmount);
            txtOrderFor = itemView.findViewById(R.id.txtOrderFor);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtOrderID = itemView.findViewById(R.id.txtOrderID);
            txtPaymethod = itemView.findViewById(R.id.txtPaymenthod);
            txtInvoiceId = itemView.findViewById(R.id.txtInvoiceID);
            txtTrackingid = itemView.findViewById(R.id.txtTrackingID);
            txtBankrefno = itemView.findViewById(R.id.txtBankRefNo);
            txtBillname = itemView.findViewById(R.id.txtBillName);
            txtBillPhone = itemView.findViewById(R.id.txtBillPhone);
            txtBillemail = itemView.findViewById(R.id.txtBillEmail);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtState = itemView.findViewById(R.id.txtState);
            txtPin = itemView.findViewById(R.id.txtPin);
            txtDatePay = itemView.findViewById(R.id.txtDatePay);
            txtWebsite = itemView.findViewById(R.id.txtWebsite);
            txtPaymentthrough = itemView.findViewById(R.id.txtPaymentThrough);
            txtFormno = itemView.findViewById(R.id.txtFormNo);
            txtCounselorid = itemView.findViewById(R.id.txtCounselorID);
        }
    }
}
