package com.bizcall.wayto.mentebit13;

public class DataAllAmountDetails {
    String paymentid,fileno,candidatename,counselorname,amountINR,amountUSD,accountname,dollarrate,paymentdate,receiveddate,paymentfrom,recieptname;

    public DataAllAmountDetails(String paymentid, String fileno, String candidatename, String counselorname, String amountINR, String amountUSD,String accountname,
                                String dollarrate,String paymentdate,String receiveddate,String paymentfrom,String recieptname) {
        this.paymentid = paymentid;
        this.fileno = fileno;
        this.candidatename = candidatename;
        this.counselorname = counselorname;
        this.amountINR = amountINR;
        this.amountUSD = amountUSD;
        this.accountname=accountname;
        this.dollarrate=dollarrate;
        this.paymentdate=paymentdate;
        this.receiveddate=receiveddate;
        this.paymentfrom=paymentfrom;
        this.recieptname=recieptname;
    }

    public String getDollarrate() {
        return dollarrate;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public String getReceiveddate() {
        return receiveddate;
    }

    public String getPaymentfrom() {
        return paymentfrom;
    }

    public String getRecieptname() {
        return recieptname;
    }

    public String getAccountname() {
        return accountname;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public String getFileno() {
        return fileno;
    }

    public String getCandidatename() {
        return candidatename;
    }

    public String getCounselorname() {
        return counselorname;
    }

    public String getAmountINR() {
        return amountINR;
    }

    public String getAmountUSD() {
        return amountUSD;
    }
}
