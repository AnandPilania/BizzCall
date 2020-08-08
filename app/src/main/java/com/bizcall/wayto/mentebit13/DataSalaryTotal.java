package com.bizcall.wayto.mentebit13;

public class DataSalaryTotal
{

    String counselorid,counselorname,empPan,empAccountNo,empBankName,salary,totalDays,numOfSundays,totalCountHD,totalCountFD,fullDaySalary,halfDaySalary,additionalholidays,datefrom,dateto,leavebal,cmonth,cyear;

    public DataSalaryTotal(String counselorid, String counselorname, String empPan, String empAccountNo, String empBankName, String salary, String totalDays,String numOfSundays, String totalCountHD,String totalCountFD, String fullDaySalary, String halfDaySalary,String additionalholidays,String datefrom,String dateto,String leavebal,String cmonth,String cyear) {
        this.counselorid = counselorid;
        this.counselorname = counselorname;
        this.empPan = empPan;
        this.empAccountNo = empAccountNo;
        this.empBankName = empBankName;
        this.salary = salary;
        this.totalDays = totalDays;
        this.numOfSundays=numOfSundays;
        this.totalCountHD = totalCountHD;
        this.totalCountFD=totalCountFD;
        this.fullDaySalary = fullDaySalary;
        this.halfDaySalary = halfDaySalary;
        this.additionalholidays=additionalholidays;
        this.datefrom=datefrom;
        this.dateto=dateto;
        this.leavebal=leavebal;
        this.cmonth=cmonth;
        this.cyear=cyear;
    }

    public String getCmonth() {
        return cmonth;
    }

    public String getCyear() {
        return cyear;
    }

    public String getLeavebal() {
        return leavebal;
    }

    public String getDatefrom() {
        return datefrom;
    }

    public String getDateto() {
        return dateto;
    }

    public String getAdditionalholidays() {
        return additionalholidays;
    }

    public String getNumOfSundays() {
        return numOfSundays;
    }

    public String getTotalCountHD() {
        return totalCountHD;
    }

    public String getTotalCountFD() {
        return totalCountFD;
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



    public String getFullDaySalary() {
        return fullDaySalary;
    }

    public String getHalfDaySalary() {
        return halfDaySalary;
    }

}
