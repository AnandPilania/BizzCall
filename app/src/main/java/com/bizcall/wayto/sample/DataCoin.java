package com.bizcall.wayto.sample;

public class DataCoin
{
    String pointid,event,point,validfrom,validupto;

    public DataCoin(String pointid, String event, String point, String validfrom, String validupto) {
        this.pointid = pointid;
        this.event = event;
        this.point = point;
        this.validfrom = validfrom;
        this.validupto = validupto;
    }

    public String getPointid() {
        return pointid;
    }

    public void setPointid(String pointid) {
        this.pointid = pointid;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(String validfrom) {
        this.validfrom = validfrom;
    }

    public String getValidupto() {
        return validupto;
    }

    public void setValidupto(String validupto) {
        this.validupto = validupto;
    }
}
