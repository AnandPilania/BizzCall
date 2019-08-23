package com.bizcall.wayto.mentebit;

public class DataUploadDocs
{
    String uploadedDocID, uploadedDocName;

public DataUploadDocs(String uploadedDocID, String uploadedDocName) {
        this.uploadedDocID = uploadedDocID;
        this.uploadedDocName = uploadedDocName;
        }

public String getUploadedDocID() {
        return uploadedDocID;
        }

public String getUploadedDocName() {
        return uploadedDocName;
        }
}
