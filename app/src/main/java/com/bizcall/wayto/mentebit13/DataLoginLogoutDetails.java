package com.bizcall.wayto.mentebit13;

public class DataLoginLogoutDetails
{
    String logindate,logintime,logouttime,totalhrs;

    public DataLoginLogoutDetails(String logindate, String logintime, String logouttime,String totalhrs) {
        this.logindate = logindate;
        this.logintime = logintime;
        this.logouttime = logouttime;
        this.totalhrs=totalhrs;
    }

    public String getLogindate() {
        return logindate;
    }

    public String getTotalhrs() {
        return totalhrs;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getLogouttime() {
        return logouttime;
    }

    public void setLogouttime(String logouttime) {
        this.logouttime = logouttime;
    }
}
