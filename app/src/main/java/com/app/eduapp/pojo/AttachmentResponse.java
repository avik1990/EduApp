package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttachmentResponse {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("Message")
    @Expose
    String Message;

    @SerializedName("data")
    @Expose
    Datum data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Datum getData() {
        return data;
    }

    public void setData(Datum data) {
        this.data = data;
    }

    public class Datum {
        @SerializedName("data")
        @Expose
        List<UploadedFileName> UploadedFileName;

        public List<AttachmentResponse.UploadedFileName> getUploadedFileName() {
            return UploadedFileName;
        }

        public void setUploadedFileName(List<AttachmentResponse.UploadedFileName> uploadedFileName) {
            UploadedFileName = uploadedFileName;
        }
    }

    public class UploadedFileName {
        @SerializedName("AttachemnetFile")
        @Expose
        String AttachemnetFile;

        public String getAttachemnetFile() {
            return AttachemnetFile;
        }

        public void setAttachemnetFile(String attachemnetFile) {
            AttachemnetFile = attachemnetFile;
        }
    }
}
