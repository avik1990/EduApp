package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User1 on 09-03-2018.
 */

public class StudentPostList extends BaseObservable {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("Message")
    @Expose
    public String Message;

    @SerializedName("data")
    @Expose
    public Data data;

    @SerializedName("ClassName")
    @Expose
    public String ClassName;


    @SerializedName("SectionName")
    @Expose
    public String SectionName;

    @SerializedName("SubjectsName")
    @Expose
    public String SubjectsName;

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

        @SerializedName("DiaryData")
        @Expose
        List<DiaryData> DiaryData;

        public List<StudentPostList.DiaryData> getDiaryData() {
            return DiaryData;
        }

        public void setDiaryData(List<StudentPostList.DiaryData> diaryData) {
            DiaryData = diaryData;
        }
    }

    public class DiaryData implements Parent<DiaryList> {

        @SerializedName("DiaryPostDate")
        @Expose
        public String DiaryPostDate;

        @SerializedName("TotalDiaryPost")
        @Expose
        public String TotalDiaryPost;

        @SerializedName("DiaryList")
        @Expose
        public List<DiaryList> DiaryList;

        public String getDiaryPostDate() {
            return DiaryPostDate;
        }

        public void setDiaryPostDate(String diaryPostDate) {
            DiaryPostDate = diaryPostDate;
        }

        public String getTotalDiaryPost() {
            return TotalDiaryPost;
        }

        public void setTotalDiaryPost(String totalDiaryPost) {
            TotalDiaryPost = totalDiaryPost;
        }

        public List<StudentPostList.DiaryList> getDiaryList() {
            return DiaryList;
        }

        public void setDiaryList(List<StudentPostList.DiaryList> diaryList) {
            DiaryList = diaryList;
        }

        @Override
        public List<StudentPostList.DiaryList> getChildList() {
            return getDiaryList();
        }

        @Override
        public boolean isInitiallyExpanded() {
            return true;
        }
    }

    public class DiaryList {

        @SerializedName("DiaryPostID")
        @Expose
        public String DiaryPostID;

        @SerializedName("DiaryApplicationBy")
        @Expose
        public String DiaryApplicationBy;

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

        @SerializedName("ShowInDiaryList")
        @Expose
        public String ShowInDiaryList;

        public String getDiaryApplicationBy() {
            return DiaryApplicationBy;
        }

        public void setDiaryApplicationBy(String diaryApplicationBy) {
            DiaryApplicationBy = diaryApplicationBy;
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

        public String getShowInDiaryList() {
            return ShowInDiaryList;
        }

        public void setShowInDiaryList(String showInDiaryList) {
            ShowInDiaryList = showInDiaryList;
        }
    }
}
