package com.app.eduapp.pojo;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Avik on 04-05-2018.
 */

public class StudentDiary extends BaseObservable {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("Message")
    @Expose
    String Message;

    @SerializedName("data")
    @Expose
    Data data;

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
        @SerializedName("DiaryListArr")
        @Expose
        public List<DiaryList> DiaryListArr;

        public List<DiaryList> getDiaryListArr() {
            return DiaryListArr;
        }

        public void setDiaryListArr(List<DiaryList> diaryListArr) {
            DiaryListArr = diaryListArr;
        }
    }

    public class DiaryList {

        @SerializedName("DiaryID")
        @Expose
        String DiaryId;

        @SerializedName("ClassID")
        @Expose
        String ClassID;

        @SerializedName("ClassName")
        @Expose
        String ClassName;

        @SerializedName("SubjectsID")
        @Expose
        String SubjectsID;

        @SerializedName("SubjectsName")
        @Expose
        String SubjectsName;

        @SerializedName("SectionID")
        @Expose
        String SectionID;

        @SerializedName("SectionName")
        @Expose
        String SectionName;

        public String getDiaryId() {
            return DiaryId;
        }

        public void setDiaryId(String diaryId) {
            DiaryId = diaryId;
        }

        public String getClassID() {
            return ClassID;
        }

        public void setClassID(String classID) {
            ClassID = classID;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
        }

        public String getSubjectsID() {
            return SubjectsID;
        }

        public void setSubjectsID(String subjectsID) {
            SubjectsID = subjectsID;
        }

        public String getSubjectsName() {
            return SubjectsName;
        }

        public void setSubjectsName(String subjectsName) {
            SubjectsName = subjectsName;
        }

        public String getSectionID() {
            return SectionID;
        }

        public void setSectionID(String sectionID) {
            SectionID = sectionID;
        }

        public String getSectionName() {
            return SectionName;
        }

        public void setSectionName(String sectionName) {
            SectionName = sectionName;
        }
    }

}