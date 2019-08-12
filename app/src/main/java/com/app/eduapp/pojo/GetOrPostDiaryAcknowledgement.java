package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOrPostDiaryAcknowledgement {

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
        @SerializedName("DiaryAcknowledgementList")
        @Expose
        List<DiaryAcknowledgementList> DiaryAcknowledgementList;

        public List<GetOrPostDiaryAcknowledgement.DiaryAcknowledgementList> getDiaryAcknowledgementList() {
            return DiaryAcknowledgementList;
        }

        public void setDiaryAcknowledgementList(List<GetOrPostDiaryAcknowledgement.DiaryAcknowledgementList> diaryAcknowledgementList) {
            DiaryAcknowledgementList = diaryAcknowledgementList;
        }
    }

    public class DiaryAcknowledgementList {

        @SerializedName("AcknowledgementID")
        @Expose
        String AcknowledgementID;

        @SerializedName("DateAdded")
        @Expose
        String DateAdded;

        @SerializedName("FullName")
        @Expose
        String FullName;

        @SerializedName("ProfilePicture")
        @Expose
        String ProfilePicture;

        @SerializedName("StudentsFullName")
        @Expose
        String StudentsFullName;

        @SerializedName("ClassName")
        @Expose
        String ClassName;

        @SerializedName("SectionName")
        @Expose
        String SectionName;

        public String getStudentsFullName() {
            return StudentsFullName;
        }

        public void setStudentsFullName(String studentsFullName) {
            StudentsFullName = studentsFullName;
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

        public String getAcknowledgementID() {
            return AcknowledgementID;
        }

        public void setAcknowledgementID(String acknowledgementID) {
            AcknowledgementID = acknowledgementID;
        }

        public String getDateAdded() {
            return DateAdded;
        }

        public void setDateAdded(String dateAdded) {
            DateAdded = dateAdded;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public String getProfilePicture() {
            return ProfilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            ProfilePicture = profilePicture;
        }
    }

}
