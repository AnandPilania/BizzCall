package com.bizcall.wayto.mentebit;

public class DataStatus
{
    String status,status_dt_time;
    public DataStatus(String status,String status_dt_time)
    {
        this.status=status;
        this.status_dt_time=status_dt_time;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_dt_time() {
        return status_dt_time;
    }
    public void setStatus_dt_time(String status_dt_time) {
        this.status_dt_time = status_dt_time;
    }
}
