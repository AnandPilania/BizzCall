package com.bizcall.wayto.mentebit13;

public class DataFirstCallReport {
    String strCallid,strCounselorName,strCallDate,strCallDuration;

    public DataFirstCallReport(String strCallid, String strCounselorName, String strCallDate, String strCallDuration) {
        this.strCallid = strCallid;
        this.strCounselorName = strCounselorName;
        this.strCallDate = strCallDate;
        this.strCallDuration = strCallDuration;
    }

    public String getStrCallid() {
        return strCallid;
    }

    public void setStrCallid(String strCallid) {
        this.strCallid = strCallid;
    }

    public String getStrCounselorName() {
        return strCounselorName;
    }

    public void setStrCounselorName(String strCounselorName) {
        this.strCounselorName = strCounselorName;
    }

    public String getStrCallDate() {
        return strCallDate;
    }

    public void setStrCallDate(String strCallDate) {
        this.strCallDate = strCallDate;
    }

    public String getStrCallDuration() {
        return strCallDuration;
    }

    public void setStrCallDuration(String strCallDuration) {
        this.strCallDuration = strCallDuration;
    }
}
