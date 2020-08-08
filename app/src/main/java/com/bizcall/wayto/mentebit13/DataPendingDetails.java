package com.bizcall.wayto.mentebit13;

import java.io.Serializable;

public class DataPendingDetails implements Serializable {
    String pCashOutId, pExpenceDate, pAmountType, pAmount, pCategoryType, pMemo, pRemark, pCategoryName, pApprovedBy, pReceiptFileName, pFrom, pYou;

    public DataPendingDetails(String pCashOutId, String pExpenceDate, String pAmountType, String pAmount, String pCategoryType, String pMemo, String pRemark, String pCategoryName, String pApprovedBy, String pReceiptFileName, String pFrom, String pYou) {
        this.pCashOutId = pCashOutId;
        this.pExpenceDate = pExpenceDate;
        this.pAmountType = pAmountType;
        this.pAmount = pAmount;
        this.pCategoryType = pCategoryType;
        this.pMemo = pMemo;
        this.pRemark = pRemark;
        this.pCategoryName = pCategoryName;
        this.pApprovedBy = pApprovedBy;
        this.pReceiptFileName = pReceiptFileName;
        this.pFrom = pFrom;
        this.pYou = pYou;
    }

    public String getpCashOutId() {
        return pCashOutId;
    }

    public String getpExpenceDate() {
        return pExpenceDate;
    }

    public String getpAmountType() {
        return pAmountType;
    }

    public String getpAmount() {
        return pAmount;
    }

    public String getpCategoryType() {
        return pCategoryType;
    }

    public String getpMemo() {
        return pMemo;
    }

    public String getpRemark() {
        return pRemark;
    }

    public String getpCategoryName() {
        return pCategoryName;
    }

    public String getpApprovedBy() {
        return pApprovedBy;
    }

    public String getpReceiptFileName() {
        return pReceiptFileName;
    }

    public String getpFrom() {
        return pFrom;
    }

    public String getpYou() {
        return pYou;
    }
}
