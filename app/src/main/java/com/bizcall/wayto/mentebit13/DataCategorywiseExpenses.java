package com.bizcall.wayto.mentebit13;

public class DataCategorywiseExpenses
{
    String categorytype,categoryname,amount,amounttype;

    public DataCategorywiseExpenses(String categorytype, String categoryname, String amount,String amounttype) {
        this.categorytype = categorytype;
        this.categoryname = categoryname;
        this.amount = amount;
        this.amounttype=amounttype;
    }

    public String getAmounttype() {
        return amounttype;
    }

    public String getCategorytype() {
        return categorytype;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public String getAmount() {
        return amount;
    }
}
