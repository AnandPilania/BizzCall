package com.bizcall.wayto.mentebit;

import java.io.Serializable;

public class DatewiseLeads implements Serializable {
    String mDate, mTtlLeads;

    public DatewiseLeads(String mDate, String mTtlLeads) {
        this.mDate = mDate;
        this.mTtlLeads = mTtlLeads;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmTtlLeads() {
        return mTtlLeads;
    }
}
