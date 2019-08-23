package com.bizcall.wayto.mentebit;

public class DataStatusReport
{
    String refname,status;
    int totalcount;

    public DataStatusReport(String refname, String status, int totalcount) {
        this.refname = refname;
        this.status = status;
        this.totalcount = totalcount;
    }

    public DataStatusReport(String status, int totalcount) {
        this.status = status;
        this.totalcount = totalcount;
    }

    public String getRefname() {
        return refname;
    }

    public void setRefname(String refname) {
        this.refname = refname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }
}
