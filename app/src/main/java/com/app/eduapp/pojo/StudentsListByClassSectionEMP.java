package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Avik on 04-05-2018.
 */

public class StudentsListByClassSectionEMP extends BaseObservable {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("Message")
    @Expose
    String Message;


    @SerializedName("ClassName")
    @Expose
    String ClassName;

    @SerializedName("SectionName")
    @Expose
    String SectionName;


    @SerializedName("data")
    @Expose
    Datum data;


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

        @SerializedName("StudentsList")
        @Expose
        List<StudentsList> StudentsList;

        public List<StudentsListByClassSectionEMP.StudentsList> getStudentsList() {
            return StudentsList;
        }

        public void setStudentsList(List<StudentsListByClassSectionEMP.StudentsList> studentsList) {
            StudentsList = studentsList;
        }
    }

    public static class StudentsList {

        @SerializedName("StudentsID")
        @Expose
        String StudentsID;

        @SerializedName("FullName")
        String FullName;

        @SerializedName("ProfileImage")
        String ProfileImage;

        @SerializedName("IsPresent")
        String IsPresent;

        @SerializedName("SchoolRollNumber")
        String SchoolRollNumber;

        public String getIsPresent() {
            return IsPresent;
        }

        public void setIsPresent(String isPresent) {
            IsPresent = isPresent;
        }

        //@Expose
        public String check_selected = "A";

        public String getCheck_selected() {
            return check_selected;
        }

        public void setCheck_selected(String check_selected) {
            this.check_selected = check_selected;
        }

        public String getStudentsID() {
            return StudentsID;
        }

        public void setStudentsID(String studentsID) {
            StudentsID = studentsID;
        }

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

        public String getSchoolRollNumber() {
            return SchoolRollNumber;
        }

        public void setSchoolRollNumber(String schoolRollNumber) {
            SchoolRollNumber = schoolRollNumber;
        }
    }
}