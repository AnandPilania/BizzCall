package com.bizcall.wayto.mentebit13;

public class DataReceivedPaymentDetails {
    String paymentDate,orderamount,orderfor,orderstatus,orderid,paymethod,invoiceId,trackingid,bankrefno,billname,billphone,
    billemail,address,city,state,pin,datepay,website,paymentthrough,formno,counselorid,paymentlink,invoicelink,receiptlink;

    public DataReceivedPaymentDetails(String paymentDate, String orderamount, String orderfor, String orderstatus, String orderid, String paymethod, String invoiceId, String trackingid, String bankrefno, String billname, String billphone, String billemail, String address, String city, String state, String pin, String datepay, String website, String paymentthrough, String formno, String counselorid,String paymentlink,String invoicelink,String receiptlink) {
        this.paymentDate = paymentDate;
        this.orderamount = orderamount;
        this.orderfor = orderfor;
        this.orderstatus = orderstatus;
        this.orderid = orderid;
        this.paymethod = paymethod;
        this.invoiceId = invoiceId;
        this.trackingid = trackingid;
        this.bankrefno = bankrefno;
        this.billname = billname;
        this.billphone = billphone;
        this.billemail = billemail;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.datepay = datepay;
        this.website = website;
        this.paymentthrough = paymentthrough;
        this.formno = formno;
        this.counselorid = counselorid;
        this.paymentlink=paymentlink;
        this.invoicelink=invoicelink;
        this.receiptlink=receiptlink;
    }

    public String getPaymentlink() {
        return paymentlink;
    }

    public String getInvoicelink() {
        return invoicelink;
    }

    public String getReceiptlink() {
        return receiptlink;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getOrderamount() {
        return orderamount;
    }

    public String getOrderfor() {
        return orderfor;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getTrackingid() {
        return trackingid;
    }

    public String getBankrefno() {
        return bankrefno;
    }

    public String getBillname() {
        return billname;
    }

    public String getBillphone() {
        return billphone;
    }

    public String getBillemail() {
        return billemail;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPin() {
        return pin;
    }

    public String getDatepay() {
        return datepay;
    }

    public String getWebsite() {
        return website;
    }

    public String getPaymentthrough() {
        return paymentthrough;
    }

    public String getFormno() {
        return formno;
    }

    public String getCounselorid() {
        return counselorid;
    }
}
