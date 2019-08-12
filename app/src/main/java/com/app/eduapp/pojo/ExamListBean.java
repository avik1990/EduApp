package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class ExamListBean {

    @SerializedName("ClassName")
    @Expose
    private String ClassName;

    @SerializedName("SectionName")
    @Expose
    private String SectionName;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("Message")
    @Expose
    private String Message;

    @SerializedName("data")
    @Expose
    private Datum data;

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getSectionName() {
        return SectionName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

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

        @SerializedName("ExamList")
        @Expose
        List<ExamList> ExamList;

        public List<ExamListBean.ExamList> getExamList() {
            return ExamList;
        }

        public void setExamList(List<ExamListBean.ExamList> examList) {
            ExamList = examList;
        }
    }

    public class ExamList {

        @SerializedName("ExamID")
        @Expose
        private String ExamID;

        @SerializedName("ExamTitle")
        @Expose
        private String ExamTitle;

        @SerializedName("ExamDate")
        @Expose
        private String ExamDate;

        public String getExamID() {
            return ExamID;
        }

        public void setExamID(String examID) {
            ExamID = examID;
        }

        public String getExamTitle() {
            return ExamTitle;
        }

        public void setExamTitle(String examTitle) {
            ExamTitle = examTitle;
        }

        public String getExamDate() {
            return ExamDate;
        }

        public void setExamDate(String examDate) {
            ExamDate = examDate;
        }
    }


}
