package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User1 on 09-03-2018.
 */

public class NoticeDetailsP extends BaseObservable {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("Message")
    @Expose
    public String Message;

    @SerializedName("data")
    @Expose
    public Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("NoticeDetails")
        @Expose
        NoticeDetails noticeData;

        public NoticeDetails getNoticeData() {
            return noticeData;
        }

        public void setNoticeData(NoticeDetails noticeData) {
            this.noticeData = noticeData;
        }
    }

    public class NoticeDetails {

        @SerializedName("NoticeID")
        @Expose
        public String NoticeID;

        @SerializedName("NoticeTitle")
        @Expose
        public String NoticeTitle;

        @SerializedName("NoticeDescription")
        @Expose
        public String NoticeDescription;

        @SerializedName("NoticeDate")
        @Expose
        public String NoticeDate;

        @SerializedName("AttachemnetFiles")
        @Expose
        public List<AttachemnetFiles> AttachemnetFiles;

        public String getNoticeID() {
            return NoticeID;
        }

        public void setNoticeID(String noticeID) {
            NoticeID = noticeID;
        }

        public String getNoticeTitle() {
            return NoticeTitle;
        }

        public void setNoticeTitle(String noticeTitle) {
            NoticeTitle = noticeTitle;
        }

        public String getNoticeDescription() {
            return NoticeDescription;
        }

        public void setNoticeDescription(String noticeDescription) {
            NoticeDescription = noticeDescription;
        }

        public String getNoticeDate() {
            return NoticeDate;
        }

        public void setNoticeDate(String noticeDate) {
            NoticeDate = noticeDate;
        }

        public List<NoticeDetailsP.AttachemnetFiles> getAttachemnetFiles() {
            return AttachemnetFiles;
        }

        public void setAttachemnetFiles(List<NoticeDetailsP.AttachemnetFiles> attachemnetFiles) {
            AttachemnetFiles = attachemnetFiles;
        }
    }

    public class AttachemnetFiles{

        @SerializedName("AttachemnetFile")
        @Expose
        String AttachemnetFile;

        @SerializedName("OriginalAttachemnetFile")
        @Expose
        String OriginalAttachemnetFile;


        public String getAttachemnetFile() {
            return AttachemnetFile;
        }

        public void setAttachemnetFile(String attachemnetFile) {
            AttachemnetFile = attachemnetFile;
        }

        public String getOriginalAttachemnetFile() {
            return OriginalAttachemnetFile;
        }

        public void setOriginalAttachemnetFile(String originalAttachemnetFile) {
            OriginalAttachemnetFile = originalAttachemnetFile;
        }
    }
}
