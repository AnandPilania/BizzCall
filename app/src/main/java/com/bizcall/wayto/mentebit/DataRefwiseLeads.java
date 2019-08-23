package com.bizcall.wayto.mentebit;

public class DataRefwiseLeads {
    String refid,refnames,totalleads;

    public DataRefwiseLeads(String refid, String refnames, String totalleads) {
        this.refid = refid;
        this.refnames = refnames;
        this.totalleads = totalleads;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getRefnames() {
        return refnames;
    }

    public void setRefnames(String refnames) {
        this.refnames = refnames;
    }

    public String getTotalleads() {
        return totalleads;
    }

    public void setTotalleads(String totalleads) {
        this.totalleads = totalleads;
    }
}
