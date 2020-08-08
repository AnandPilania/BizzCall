package com.bizcall.wayto.mentebit13;

public class DataContactList {
    String conName, conNumber, conEmail, conCompName, conUploaded, conRemark;

    public DataContactList(String conName, String conNumber, String conEmail, String conCompName, String conUploaded, String conRemark) {
        this.conName = conName;
        this.conNumber = conNumber;
        this.conEmail = conEmail;
        this.conCompName = conCompName;
        this.conUploaded = conUploaded;
        this.conRemark = conRemark;
    }

    public String getConName() {
        return conName;
    }

    public String getConNumber() {
        return conNumber;
    }

    public String getConEmail() {
        return conEmail;
    }

    public String getConCompName() {
        return conCompName;
    }

    public String getConUploaded() {
        return conUploaded;
    }

    public String getConRemark() {
        return conRemark;
    }
}
