package com.bizcall.wayto.mentebit13;

import java.io.Serializable;

public class DataCallLogs implements Serializable {
    private String mCallMobileNo, mCallType, mCallDate, mCallDuration, mIMEI1, mIMEI2, mphAccID;

    public DataCallLogs(String mCallMobileNo, String mCallType, String mCallDate, String mCallDuration,
                        String mIMEI1, String mIMEI2, String mphAccID) {
        this.mCallMobileNo = mCallMobileNo;
        this.mCallType = mCallType;
        this.mCallDate = mCallDate;
        this.mCallDuration = mCallDuration;
        this.mIMEI1 = mIMEI1;
        this.mIMEI2 = mIMEI2;
        this.mphAccID = mphAccID;
    }

    public String getmCallMobileNo() {
        return mCallMobileNo;
    }

    public String getmCallType() {
        return mCallType;
    }

    public String getmCallDate() {
        return mCallDate;
    }

    public String getmCallDuration() {
        return mCallDuration;
    }

    public String getmIMEI1() {
        return mIMEI1;
    }

    public String getmIMEI2() {
        return mIMEI2;
    }

    public String getmphAccID() {
        return mphAccID;
    }
}