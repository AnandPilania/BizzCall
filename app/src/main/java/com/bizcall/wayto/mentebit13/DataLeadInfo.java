package com.bizcall.wayto.mentebit13;

import java.io.Serializable;

public class DataLeadInfo implements Serializable {
    String leadName, leadNumber, leadType, leadDate, leadTime, leadRecording, leadUploaded;

    public DataLeadInfo(String leadName, String leadNumber, String leadType, String leadDate, String leadTime, String leadRecording, String leadUploaded) {
        this.leadName = leadName;
        this.leadNumber = leadNumber;
        this.leadType = leadType;
        this.leadDate = leadDate;
        this.leadTime = leadTime;
        this.leadRecording = leadRecording;
        this.leadUploaded = leadUploaded;
    }

    public String getLeadName() {
        return leadName;
    }

    public String getLeadNumber() {
        return leadNumber;
    }

    public String getLeadType() {
        return leadType;
    }

    public String getLeadDate() {
        return leadDate;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public String getLeadRecording() {
        return leadRecording;
    }

    public String getLeadUploaded() {
        return leadUploaded;
    }
}
