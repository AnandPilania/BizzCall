package com.bizcall.wayto.mentebit13;

public class DataPaymentHistory
{
    String fileID,paymentid,amountUSD,amountINR,date1,approved;

    public DataPaymentHistory(String fileID,String paymentid, String amountUSD, String amountINR, String date1, String approved) {
       this.fileID=fileID;
        this.paymentid = paymentid;
        this.amountUSD = amountUSD;
        this.amountINR = amountINR;
        this.date1 = date1;
        this.approved = approved;
    }

    public String getFileID() {
        return fileID;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public String getAmountUSD() {
        return amountUSD;
    }

    public String getAmountINR() {
        return amountINR;
    }

    public String getDate1() {
        return date1;
    }

    public String getApproved() {
        return approved;
    }
}
