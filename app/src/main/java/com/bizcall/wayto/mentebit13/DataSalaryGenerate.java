package com.bizcall.wayto.mentebit13;

public class DataSalaryGenerate {
    String counselorid,counselorname,empPan,empAccountNo,empBankName,salary,totalDays,numOfSundays,totalCount,fullDaySalary,leavebal;

    public DataSalaryGenerate(String counselorid, String counselorname, String empPan, String empAccountNo, String empBankName, String salary, String totalDays, String numOfSundays, String totalCount, String fullDaySalary,String leavebal) {
        this.counselorid = counselorid;
        this.counselorname = counselorname;
        this.empPan = empPan;
        this.empAccountNo = empAccountNo;
        this.empBankName = empBankName;
        this.salary = salary;
        this.totalDays = totalDays;
        this.numOfSundays = numOfSundays;
        this.totalCount = totalCount;
        this.fullDaySalary = fullDaySalary;
        this.leavebal=leavebal;

    }

    public String getLeavebal() {
        return leavebal;
    }

    public String getCounselorid() {
        return counselorid;
    }

    public String getCounselorname() {
        return counselorname;
    }

    public String getEmpPan() {
        return empPan;
    }

    public String getEmpAccountNo() {
        return empAccountNo;
    }

    public String getEmpBankName() {
        return empBankName;
    }

    public String getSalary() {
        return salary;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public String getNumOfSundays() {
        return numOfSundays;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public String getFullDaySalary() {
        return fullDaySalary;
    }
}
