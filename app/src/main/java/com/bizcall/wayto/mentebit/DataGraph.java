package com.bizcall.wayto.mentebit;

public class DataGraph
{

    String date,calltype;
    int time;

    public DataGraph(String date, int time,String calltype) {
        this.date = date;
        this.time = time;
        this.calltype=calltype;
    }

    public String getCalltype() {
        return calltype;
    }

    public void setCalltype(String calltype) {
        this.calltype = calltype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
