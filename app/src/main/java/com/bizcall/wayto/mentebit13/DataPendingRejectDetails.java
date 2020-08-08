package com.bizcall.wayto.mentebit13;

import java.io.Serializable;

public class DataPendingRejectDetails implements Serializable {
    String rExpenceDate, rAmountType, rAmount, rCategoryType, rMemo, rRemark, rCategoryName, rApprovedBy, rReceiptFileName, rFrom, rYou, rRejectedDate, rApprovedMarker;

    public DataPendingRejectDetails(String rExpenceDate, String rAmountType, String rAmount, String rCategoryType, String rMemo, String rRemark, String rCategoryName, String rApprovedBy, String rReceiptFileName, String rFrom, String rYou, String rRejectedDate, String rApprovedMarker) {
        this.rExpenceDate = rExpenceDate;
        this.rAmountType = rAmountType;
        this.rAmount = rAmount;
        this.rCategoryType = rCategoryType;
        this.rMemo = rMemo;
        this.rRemark = rRemark;
        this.rCategoryName = rCategoryName;
        this.rApprovedBy = rApprovedBy;
        this.rReceiptFileName = rReceiptFileName;
        this.rFrom = rFrom;
        this.rYou = rYou;
        this.rRejectedDate = rRejectedDate;
        this.rApprovedMarker = rApprovedMarker;
    }

    public String getrExpenceDate() {
        return rExpenceDate;
    }

    public String getrAmountType() {
        return rAmountType;
    }

    public String getrAmount() {
        return rAmount;
    }

    public String getrCategoryType() {
        return rCategoryType;
    }

    public String getrMemo() {
        return rMemo;
    }

    public String getrRemark() {
        return rRemark;
    }

    public String getrCategoryName() {
        return rCategoryName;
    }

    public String getrApprovedBy() {
        return rApprovedBy;
    }

    public String getrReceiptFileName() {
        return rReceiptFileName;
    }

    public String getrFrom() {
        return rFrom;
    }

    public String getrYou() {
        return rYou;
    }

    public String getrRejectedDate() {
        return rRejectedDate;
    }

    public String getrApprovedMarker() {
        return rApprovedMarker;
    }
}
