package com.bizcall.wayto.sample;

public class StatusInfo {
    String status1, statusId;

    public StatusInfo(String status1, String statusId) {
        this.status1 = status1;
        this.statusId = statusId;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
}
