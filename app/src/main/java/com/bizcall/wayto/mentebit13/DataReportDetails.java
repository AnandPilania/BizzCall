package com.bizcall.wayto.mentebit13;

public class DataReportDetails {
    String srno,report,cname,date1;

    public DataReportDetails(String srno, String report, String cname, String date1) {
        this.srno = srno;
        this.report = report;
        this.cname = cname;
        this.date1 = date1;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }
}
