package com.bizcall.wayto.mentebit13;

public class DataAccounts {
    String strAmount,strCategory;

    public DataAccounts(String strAmount, String strCategory) {
        this.strAmount = strAmount;
        this.strCategory = strCategory;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }
}
