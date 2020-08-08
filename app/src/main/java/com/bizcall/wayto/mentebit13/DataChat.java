package com.bizcall.wayto.mentebit13;

public class DataChat {
    private String mDate, mUser, mChat;

    public DataChat(String mDate, String mUser, String mChat) {
        this.mDate = mDate;
        this.mUser = mUser;
        this.mChat = mChat;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmUser() {
        return mUser;
    }

    public void setmUser(String mUser) {
        this.mUser = mUser;
    }

    public String getmChat() {
        return mChat;
    }

    public void setmChat(String mChat) {
        this.mChat = mChat;
    }

}
