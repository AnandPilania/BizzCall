package com.bizcall.wayto.mentebit;

public class DataStudEducation {
    String strQualification, strCourseName, strCollegeName, passyear, markobt, markoutof, percentage;

    public DataStudEducation(String strQualification, String strCourseName, String strCollegeName, String passyear, String markobt, String markoutof, String percentage) {
        this.strQualification = strQualification;
        this.strCourseName = strCourseName;
        this.strCollegeName = strCollegeName;
        this.passyear = passyear;
        this.markobt = markobt;
        this.markoutof = markoutof;
        this.percentage = percentage;
    }

    public String getStrQualification() {
        return strQualification;
    }

    public String getStrCourseName() {
        return strCourseName;
    }

    public String getStrCollegeName() {
        return strCollegeName;
    }

    public String getPassyear() {
        return passyear;
    }

    public String getMarkobt() {
        return markobt;
    }

    public String getMarkoutof() {
        return markoutof;
    }

    public String getPercentage() {
        return percentage;
    }
}
