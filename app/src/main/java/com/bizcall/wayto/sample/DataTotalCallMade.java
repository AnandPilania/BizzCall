package com.bizcall.wayto.sample;

public class DataTotalCallMade {
    String srno, duration, date1, filename;

    public DataTotalCallMade(String srno, String duration, String date1, String filename) {
        this.srno = srno;
        this.duration = duration;
        this.date1 = date1;
        this.filename = filename;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
