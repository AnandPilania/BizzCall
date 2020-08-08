package com.bizcall.wayto.mentebit13;

public class DataBalanceDetails {
    String category,amount,date,memo,remarks,categoryname,approvedby,approvedremarks,approvedreceiptname,categorytype,from,you;

    public DataBalanceDetails(String category,String amount, String date, String memo, String remarks, String categoryname, String approvedby, String approvedremarks, String approvedreceiptname,String categorytype
    ,String from,String you) {
        this.category=category;
        this.amount = amount;
        this.date = date;
        this.memo = memo;
        this.remarks = remarks;
        this.categoryname = categoryname;
        this.approvedby = approvedby;
        this.approvedremarks = approvedremarks;
        this.approvedreceiptname = approvedreceiptname;
        this.categorytype=categorytype;
        this.from=from;
        this.you=you;
    }

    public String getCategory() {
        return category;
    }

    public String getFrom() {
        return from;
    }

    public String getYou() {
        return you;
    }

    public String getCategorytype() {
        return categorytype;
    }

    public void setCategorytype(String categorytype) {
        this.categorytype = categorytype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }

    public String getApprovedremarks() {
        return approvedremarks;
    }

    public void setApprovedremarks(String approvedremarks) {
        this.approvedremarks = approvedremarks;
    }

    public String getApprovedreceiptname() {
        return approvedreceiptname;
    }

    public void setApprovedreceiptname(String approvedreceiptname) {
        this.approvedreceiptname = approvedreceiptname;
    }
}
