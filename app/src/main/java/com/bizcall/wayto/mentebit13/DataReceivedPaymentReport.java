package com.bizcall.wayto.mentebit13;

public class DataReceivedPaymentReport
{
    String fileno,fname,lname,refno,amountINR,paymentdate,paymentMode,paymentFrom,paymentremarks,receiptname,currentLocalRate,approvalremarks;

    public DataReceivedPaymentReport(String fileno, String fname, String lname, String refno, String amountINR, String paymentdate, String paymentMode, String paymentFrom, String paymentremarks, String receiptname, String currentLocalRate, String approvalremarks) {
        this.fileno = fileno;
        this.fname = fname;
        this.lname = lname;
        this.refno = refno;
        this.amountINR = amountINR;
        this.paymentdate = paymentdate;
        this.paymentMode = paymentMode;
        this.paymentFrom = paymentFrom;
        this.paymentremarks = paymentremarks;
        this.receiptname = receiptname;
        this.currentLocalRate = currentLocalRate;
        this.approvalremarks = approvalremarks;
    }

    public String getFileno() {
        return fileno;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getRefno() {
        return refno;
    }

    public String getAmountINR() {
        return amountINR;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getPaymentFrom() {
        return paymentFrom;
    }

    public String getPaymentremarks() {
        return paymentremarks;
    }

    public String getReceiptname() {
        return receiptname;
    }

    public String getCurrentLocalRate() {
        return currentLocalRate;
    }

    public String getApprovalremarks() {
        return approvalremarks;
    }
}
