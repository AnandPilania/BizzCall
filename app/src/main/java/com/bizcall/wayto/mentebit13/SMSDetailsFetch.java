package com.bizcall.wayto.mentebit13;

import java.io.Serializable;

public class SMSDetailsFetch implements Serializable {
    private String mfthSMSDate;

    public SMSDetailsFetch(String mfthSMSDate) {
        this.mfthSMSDate = mfthSMSDate;
    }

    public String getmfthSMSDate() {
        return mfthSMSDate;
    }
}
