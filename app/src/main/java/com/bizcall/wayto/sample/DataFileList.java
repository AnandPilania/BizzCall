package com.bizcall.wayto.sample;

public class DataFileList {
    String filename;
    boolean isSelected;
    String isUploaded;

    public DataFileList(String filename) {
        this.filename = filename;
        // this.isUploaded=isUploaded;
    }

    public DataFileList() {

    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
