package com.bizcall.wayto.mentebit13;

public class DataSalaryHalfDay {
    String counselorid,counselorname,salary,totalDays,totalCount,halfDaySalary;

    public DataSalaryHalfDay(String counselorid, String counselorname, String salary, String totalDays, String totalCount, String halfDaySalary) {
        this.counselorid = counselorid;
        this.counselorname = counselorname;
        this.salary = salary;
        this.totalDays = totalDays;
        this.totalCount = totalCount;
        this.halfDaySalary = halfDaySalary;
    }

    public String getCounselorid() {
        return counselorid;
    }

    public String getCounselorname() {
        return counselorname;
    }

    public String getSalary() {
        return salary;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public String getHalfDaySalary() {
        return halfDaySalary;
    }
}
