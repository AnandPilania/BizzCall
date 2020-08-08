package com.bizcall.wayto.mentebit13;

public class DataPendingApproval {
    String leaveID, dateFrom, dateTo, totalDays, remarks, applicationdate;

    public DataPendingApproval(String leaveID, String dateFrom, String dateTo, String totalDays, String remarks, String applicationdate) {
        this.leaveID = leaveID;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.totalDays = totalDays;
        this.remarks = remarks;
        this.applicationdate = applicationdate;
    }

    public String getLeaveID() {
        return leaveID;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getApplicationdate() {
        return applicationdate;
    }
}

