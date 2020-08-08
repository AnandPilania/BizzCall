package com.bizcall.wayto.mentebit13;

import java.io.Serializable;

public class SMSDetails implements Serializable {
        private String mSMSMobileNo, mSMStype, mSMSdate, mSMSbody, mSMSIMEI1, mSMSIMEI2;

    public SMSDetails(String mSMSMobileNo, String mSMStype, String mSMSdate, String mSMSbody, String mSMSIMEI1, String mSMSIMEI2) {
            this.mSMSMobileNo = mSMSMobileNo;
            this.mSMStype = mSMStype;
            this.mSMSdate = mSMSdate;
            this.mSMSbody = mSMSbody;
            this.mSMSIMEI1 = mSMSIMEI1;
            this.mSMSIMEI2 = mSMSIMEI2;
        }

        public String getmSMSMobileNo() {
            return mSMSMobileNo;
        }

        public String getmSMStype() {
            return mSMStype;
        }

        public String getmSMSdate() {
            return mSMSdate;
        }

        public String getmSMSbody() {
            return mSMSbody;
        }

        public String getmSMSIMEI1() {
            return mSMSIMEI1;
        }

        public String getmSMSIMEI2() {
            return mSMSIMEI2;
        }
    }



