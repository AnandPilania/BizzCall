package com.bizcall.wayto.mentebit;

public class DataLastLogin {
    String LoginId,CounselorName,Logindate,ipadrs;

    public DataLastLogin(String loginId, String counselorName, String logindate,String ipadrs) {
        LoginId = loginId;
        CounselorName = counselorName;
        Logindate = logindate;
        this.ipadrs=ipadrs;
    }

    public String getIpadrs() {
        return ipadrs;
    }

    public void setIpadrs(String ipadrs) {
        this.ipadrs = ipadrs;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getCounselorName() {
        return CounselorName;
    }

    public void setCounselorName(String counselorName) {
        CounselorName = counselorName;
    }

    public String getLogindate() {
        return Logindate;
    }

    public void setLogindate(String logindate) {
        Logindate = logindate;
    }
}
