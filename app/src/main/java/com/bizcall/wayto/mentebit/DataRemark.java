package com.bizcall.wayto.mentebit;

public class DataRemark
{
    String remarks,remarkDateTime;

    public DataRemark(String remarks,String remarkDateTime)
    {
        this.remarks = remarks;
        this.remarkDateTime=remarkDateTime;

    }

    public String getRemarkDateTime() {
        return remarkDateTime;
    }

    public void setRemarkDateTime(String remarkDateTime) {
        this.remarkDateTime = remarkDateTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
