package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class ExamSubjects {

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

    @SerializedName("ExamTitle")
    @Expose
    private String ExamTitle;

    @SerializedName("data")
    @Expose
    private Datum data;

    public String getExamTitle() {
        return ExamTitle;
    }

    public void setExamTitle(String examTitle) {
        ExamTitle = examTitle;
    }

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

        @SerializedName("ExamSubjectsList")
        @Expose
        List<ExamSubjectsList> ExamSubjectsList;

        public List<ExamSubjectsList> getExamSubjectsList() {
            return ExamSubjectsList;
        }

        public void setExamSubjectsList(List<ExamSubjectsList> examSubjectsList) {
            ExamSubjectsList = examSubjectsList;
        }
    }

    public class ExamSubjectsList {

        @SerializedName("ExamScheduleID")
        @Expose
        private String ExamScheduleID;

        @SerializedName("SubjectsName")
        @Expose
        private String SubjectsName;

        @SerializedName("ExamDate")
        @Expose
        private String ExamDate;

        @SerializedName("TotalMarks")
        @Expose
        private String TotalMarks;

        @SerializedName("PassMarks")
        @Expose
        private String PassMarks;

        @SerializedName("StartTime")
        @Expose
        private String StartTime;

        @SerializedName("EndTime")
        @Expose
        private String EndTime;

        public String getExamScheduleID() {
            return ExamScheduleID;
        }

        public void setExamScheduleID(String examScheduleID) {
            ExamScheduleID = examScheduleID;
        }

        public String getSubjectsName() {
            return SubjectsName;
        }

        public void setSubjectsName(String subjectsName) {
            SubjectsName = subjectsName;
        }

        public String getExamDate() {
            return ExamDate;
        }

        public void setExamDate(String examDate) {
            ExamDate = examDate;
        }

        public String getTotalMarks() {
            return TotalMarks;
        }

        public void setTotalMarks(String totalMarks) {
            TotalMarks = totalMarks;
        }

        public String getPassMarks() {
            return PassMarks;
        }

        public void setPassMarks(String passMarks) {
            PassMarks = passMarks;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String endTime) {
            EndTime = endTime;
        }
    }


}
