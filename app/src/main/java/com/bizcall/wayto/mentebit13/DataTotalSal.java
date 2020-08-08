package com.bizcall.wayto.mentebit13;

public class DataTotalSal {
    String totalAttendence,grossPay,basicpay,HRA,transport,medical,LTA,incentive,professiontax,incometax,netpay;

    public DataTotalSal(String totalAttendence, String grossPay, String basicpay, String HRA, String transport, String medical, String LTA, String incentive, String professiontax, String incometax, String netpay) {
        this.totalAttendence = totalAttendence;
        this.grossPay = grossPay;
        this.basicpay = basicpay;
        this.HRA = HRA;
        this.transport = transport;
        this.medical = medical;
        this.LTA = LTA;
        this.incentive = incentive;
        this.professiontax = professiontax;
        this.incometax = incometax;
        this.netpay = netpay;
    }

    public String getTotalAttendence() {
        return totalAttendence;
    }

    public String getGrossPay() {
        return grossPay;
    }

    public String getBasicpay() {
        return basicpay;
    }

    public String getHRA() {
        return HRA;
    }

    public String getTransport() {
        return transport;
    }

    public String getMedical() {
        return medical;
    }

    public String getLTA() {
        return LTA;
    }

    public String getIncentive() {
        return incentive;
    }

    public String getProfessiontax() {
        return professiontax;
    }

    public String getIncometax() {
        return incometax;
    }

    public String getNetpay() {
        return netpay;
    }
}
