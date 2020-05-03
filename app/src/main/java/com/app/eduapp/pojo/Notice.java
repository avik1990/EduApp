package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User1 on 09-03-2018.
 */

public class Notice extends BaseObservable {

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

        @SerializedName("NoticeData")
        @Expose
        List<NoticeData> noticeData;

        public List<NoticeData> getNoticeData() {
            return noticeData;
        }

        public void setNoticeData(List<NoticeData> noticeData) {
            this.noticeData = noticeData;
        }
    }

    public class NoticeData implements Parent<NoticeList> {

        @SerializedName("NoticeDate")
        @Expose
        public String NoticeDate;

        @SerializedName("TotalNotice")
        @Expose
        public String TotalNotice;

        @SerializedName("NoticeList")
        @Expose
        public List<NoticeList> noticeList;

        public String getNoticeDate() {
            return NoticeDate;
        }

        public void setNoticeDate(String noticeDate) {
            NoticeDate = noticeDate;
        }

        public String getTotalNotice() {
            return TotalNotice;
        }

        public void setTotalNotice(String totalNotice) {
            TotalNotice = totalNotice;
        }

        public List<NoticeList> getNoticeList() {
            return noticeList;
        }

        public void setNoticeList(List<NoticeList> noticeList) {
            this.noticeList = noticeList;
        }

        /* public List<NoticeList> getAlloted_kids() {
            return alloted_kids;
        }

        public void setAlloted_kids(List<NoticeList> alloted_kids) {
            this.alloted_kids = alloted_kids;
        }

        @Override
        public List<NoticeList> getChildList() {
            return getAlloted_kids();
        }*/

        @Override
        public List<NoticeList> getChildList() {
            return getNoticeList();
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }
    }

    public class NoticeList {

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
    }
}
