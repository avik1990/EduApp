package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User1 on 09-03-2018.
 */

public class ResultSubjectsList extends BaseObservable {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("ClassName")
    @Expose
    public String ClassName;

    @SerializedName("SectionName")
    @Expose
    public String SectionName;


    @SerializedName("Message")
    @Expose
    public String Message;

    @SerializedName("StudentsName")
    @Expose
    public String StudentsName;


    @SerializedName("ExamTitle")
    @Expose
    public String ExamTitle;

    @SerializedName("data")
    @Expose
    public Data data;

    public String getStudentsName() {
        return StudentsName;
    }

    public void setStudentsName(String studentsName) {
        StudentsName = studentsName;
    }

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("ResultSubjectsList")
        @Expose
        List<ResultSubjectsListData> ResultSubjectsList;

        public List<ResultSubjectsListData> getResultSubjectsList() {
            return ResultSubjectsList;
        }

        public void setResultSubjectsList(List<ResultSubjectsListData> resultSubjectsList) {
            ResultSubjectsList = resultSubjectsList;
        }
    }

    public class ResultSubjectsListData {

        @SerializedName("SubjectsName")
        @Expose
        public String SubjectsName;

        @SerializedName("ExamDate")
        @Expose
        public String ExamDate;

        @SerializedName("TotalMarks")
        @Expose
        public String TotalMarks;

        @SerializedName("PassMarks")
        @Expose
        public String PassMarks;


        @SerializedName("MarksObtained")
        @Expose
        public String MarksObtained;

        @SerializedName("PercentageMarks")
        @Expose
        public String PercentageMarks;

        public String getPercentageMarks() {
            return PercentageMarks;
        }

        public void setPercentageMarks(String percentageMarks) {
            PercentageMarks = percentageMarks;
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

        public String getMarksObtained() {
            return MarksObtained;
        }

        public void setMarksObtained(String marksObtained) {
            MarksObtained = marksObtained;
        }
    }


}
