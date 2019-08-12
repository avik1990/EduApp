package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Avik on 21-05-2018.
 */

public class GetStudentsList {

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("status")
    @Expose
    String  status;


    @SerializedName("Message")
    @Expose
    String Message;

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
        @SerializedName("StudentsList")
        @Expose
        public List<StudentsList> StudentsList;


        public List<GetStudentsList.StudentsList> getStudentsList() {
            return StudentsList;
        }

        public void setStudentsList(List<GetStudentsList.StudentsList> studentsList) {
            StudentsList = studentsList;
        }
    }

    public class StudentsList {
        @SerializedName("StudentsID")
        @Expose
        public String StudentsID;

        @SerializedName("RFIDNumber")
        @Expose
        public String RFIDNumber;

        @SerializedName("ProfileImage")
        @Expose
        public String ProfileImage;

        @SerializedName("FullName")
        @Expose
        public String FullName;

        @SerializedName("PrimaryMobileNumber")
        @Expose
        public String PrimaryMobileNumber;

        @SerializedName("Address")
        @Expose
        public String Address;

        @SerializedName("SectionName")
        @Expose
        public String SectionName;

        @SerializedName("BloodGroupName")
        @Expose
        public String BloodGroupName;

        @SerializedName("ClassName")
        @Expose
        public String ClassName;


        @SerializedName("SectionID")
        @Expose
        public String SectionID;

        @SerializedName("ClassID")
        @Expose
        public String ClassID;

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

        public String getStudentsID() {
            return StudentsID;
        }

        public void setStudentsID(String studentsID) {
            StudentsID = studentsID;
        }

        public String getRFIDNumber() {
            return RFIDNumber;
        }

        public void setRFIDNumber(String RFIDNumber) {
            this.RFIDNumber = RFIDNumber;
        }

        public String getProfileImage() {
            return ProfileImage;
        }

        public void setProfileImage(String profileImage) {
            ProfileImage = profileImage;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public String getPrimaryMobileNumber() {
            return PrimaryMobileNumber;
        }

        public void setPrimaryMobileNumber(String primaryMobileNumber) {
            PrimaryMobileNumber = primaryMobileNumber;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getSectionName() {
            return SectionName;
        }

        public void setSectionName(String sectionName) {
            SectionName = sectionName;
        }

        public String getBloodGroupName() {
            return BloodGroupName;
        }

        public void setBloodGroupName(String bloodGroupName) {
            BloodGroupName = bloodGroupName;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
        }
    }
}
