package com.bizcall.wayto.mentebit;

public class DataFormFilled {
    String strId,strFName,strMobile,strWebsite;

    public DataFormFilled(String strId, String strFName, String strMobile, String strWebsite) {
        this.strId = strId;
        this.strFName = strFName;
        this.strMobile = strMobile;
        this.strWebsite = strWebsite;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrFName() {
        return strFName;
    }

    public void setStrFName(String strFName) {
        this.strFName = strFName;
    }

    public String getStrMobile() {
        return strMobile;
    }

    public void setStrMobile(String strMobile) {
        this.strMobile = strMobile;
    }

    public String getStrWebsite() {
        return strWebsite;
    }

    public void setStrWebsite(String strWebsite) {
        this.strWebsite = strWebsite;
    }
}
