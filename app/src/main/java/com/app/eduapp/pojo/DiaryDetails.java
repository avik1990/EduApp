package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User1 on 09-03-2018.
 */

public class DiaryDetails extends BaseObservable {

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

        @SerializedName("DiaryDetails")
        @Expose
        DiaryDetails1 DiaryDetails;

        public DiaryDetails1 getDiaryDetails() {
            return DiaryDetails;
        }

        public void setDiaryDetails(DiaryDetails1 diaryDetails) {
            DiaryDetails = diaryDetails;
        }
    }

    public class DiaryDetails1 {

        @SerializedName("ParentsName")
        @Expose
        public String ParentsName;

        @SerializedName("DiaryApplicationBy")
        @Expose
        public String DiaryApplicationBy;

        @SerializedName("PostByUserID")
        @Expose
        public String PostByUserID;

        @SerializedName("DiaryPostID")
        @Expose
        public String DiaryPostID;

        @SerializedName("ClassID")
        @Expose
        public String ClassID;

        @SerializedName("SectionID")
        @Expose
        public String SectionID;

        @SerializedName("SubjectsID")
        @Expose
        public String SubjectsID;

        @SerializedName("PostHeading")
        @Expose
        public String PostHeading;

        @SerializedName("PostDescription")
        @Expose
        public String PostDescription;

        @SerializedName("AcceptAcknowledgement")
        @Expose
        public String AcceptAcknowledgement;


        @SerializedName("AllowComments")
        @Expose
        public String AllowComments;


        @SerializedName("DiaryPostDate")
        @Expose
        public String DiaryPostDate;


        @SerializedName("EmployeeID")
        @Expose
        public String EmployeeID;

        @SerializedName("EmployeeName")
        @Expose
        public String EmployeeName;

        @SerializedName("EmployeeDesignation")
        @Expose
        public String EmployeeDesignation;

        @SerializedName("AttachemnetFiles")
        @Expose
        List<AttachemnetFiles> AttachemnetFiles;


        @SerializedName("DiaryStudentsList")
        @Expose
        List<DiaryStudentsList> DiaryStudentsList;

        @SerializedName("ClassName")
        @Expose
        public String ClassName;

        @SerializedName("SectionName")
        @Expose
        public String SectionName;

        @SerializedName("SubjectsName")
        @Expose
        public String SubjectsName;

        @SerializedName("PostType")
        @Expose
        public String PostType;


        public String getParentsName() {
            return ParentsName;
        }

        public void setParentsName(String parentsName) {
            ParentsName = parentsName;
        }

        public String getDiaryApplicationBy() {
            return DiaryApplicationBy;
        }

        public void setDiaryApplicationBy(String diaryApplicationBy) {
            DiaryApplicationBy = diaryApplicationBy;
        }

        public String getPostByUserID() {
            return PostByUserID;
        }

        public void setPostByUserID(String postByUserID) {
            PostByUserID = postByUserID;
        }

        public List<DiaryDetails.DiaryStudentsList> getDiaryStudentsList() {
            return DiaryStudentsList;
        }

        public void setDiaryStudentsList(List<DiaryDetails.DiaryStudentsList> diaryStudentsList) {
            DiaryStudentsList = diaryStudentsList;
        }

        public String getPostType() {
            return PostType;
        }

        public void setPostType(String postType) {
            PostType = postType;
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

        public String getSubjectsName() {
            return SubjectsName;
        }

        public void setSubjectsName(String subjectsName) {
            SubjectsName = subjectsName;
        }

        public String getDiaryPostID() {
            return DiaryPostID;
        }

        public void setDiaryPostID(String diaryPostID) {
            DiaryPostID = diaryPostID;
        }

        public String getClassID() {
            return ClassID;
        }

        public void setClassID(String classID) {
            ClassID = classID;
        }

        public String getSectionID() {
            return SectionID;
        }

        public void setSectionID(String sectionID) {
            SectionID = sectionID;
        }

        public String getSubjectsID() {
            return SubjectsID;
        }

        public void setSubjectsID(String subjectsID) {
            SubjectsID = subjectsID;
        }

        public String getPostHeading() {
            return PostHeading;
        }

        public void setPostHeading(String postHeading) {
            PostHeading = postHeading;
        }

        public String getPostDescription() {
            return PostDescription;
        }

        public void setPostDescription(String postDescription) {
            PostDescription = postDescription;
        }

        public String getAcceptAcknowledgement() {
            return AcceptAcknowledgement;
        }

        public void setAcceptAcknowledgement(String acceptAcknowledgement) {
            AcceptAcknowledgement = acceptAcknowledgement;
        }

        public String getAllowComments() {
            return AllowComments;
        }

        public void setAllowComments(String allowComments) {
            AllowComments = allowComments;
        }

        public String getDiaryPostDate() {
            return DiaryPostDate;
        }

        public void setDiaryPostDate(String diaryPostDate) {
            DiaryPostDate = diaryPostDate;
        }

        public String getEmployeeID() {
            return EmployeeID;
        }

        public void setEmployeeID(String employeeID) {
            EmployeeID = employeeID;
        }

        public String getEmployeeName() {
            return EmployeeName;
        }

        public void setEmployeeName(String employeeName) {
            EmployeeName = employeeName;
        }

        public String getEmployeeDesignation() {
            return EmployeeDesignation;
        }

        public void setEmployeeDesignation(String employeeDesignation) {
            EmployeeDesignation = employeeDesignation;
        }

        public List<DiaryDetails.AttachemnetFiles> getAttachemnetFiles() {
            return AttachemnetFiles;
        }

        public void setAttachemnetFiles(List<DiaryDetails.AttachemnetFiles> attachemnetFiles) {
            AttachemnetFiles = attachemnetFiles;
        }
    }

    public class AttachemnetFiles {
        @SerializedName("AttachemnetFile")
        @Expose
        public String AttachemnetFile;

        @SerializedName("OriginalAttachemnetFile")
        @Expose
        public String OriginalAttachemnetFile;

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

    public class DiaryStudentsList {
        @SerializedName("FullName")
        @Expose
        public String FullName;

        @SerializedName("ProfileImage")
        @Expose
        public String ProfileImage;

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public String getProfileImage() {
            return ProfileImage;
        }

        public void setProfileImage(String profileImage) {
            ProfileImage = profileImage;
        }
    }

}
