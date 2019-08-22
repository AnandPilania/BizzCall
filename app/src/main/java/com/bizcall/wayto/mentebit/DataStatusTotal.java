package com.bizcall.wayto.mentebit;

public class DataStatusTotal {
    String status, currentstatus, total;

    public DataStatusTotal(String status, String currentstatus, String total) {
        this.status = status;
        this.currentstatus = currentstatus;
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentstatus() {
        return currentstatus;
    }

    public void setCurrentstatus(String currentstatus) {
        this.currentstatus = currentstatus;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
