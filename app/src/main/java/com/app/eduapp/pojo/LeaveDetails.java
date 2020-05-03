package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Avik on 04-05-2018.
 */

public class LeaveDetails extends BaseObservable {

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

        @SerializedName("LeaveDetails")
        @Expose
        LeaveDetailsData LeaveDetails;

        public LeaveDetailsData getLeaveDetails() {
            return LeaveDetails;
        }

        public void setLeaveDetails(LeaveDetailsData leaveDetails) {
            LeaveDetails = leaveDetails;
        }
    }

    public class LeaveDetailsData {

        @SerializedName("LeaveID")
        @Expose
        String LeaveID;

        @SerializedName("ProfileImage")
        @Expose
        String ProfileImage;


        @SerializedName("EmployeeID")
        @Expose
        String EmployeeID;

        @SerializedName("StudentsID")
        @Expose
        String StudentsID;

        @SerializedName("FullName")
        @Expose
        String FullName;

        @SerializedName("ClassName")
        @Expose
        String ClassName;

        @SerializedName("SectionName")
        @Expose
        String SectionName;

        @SerializedName("LeaveTitle")
        @Expose
        String LeaveTitle;

        @SerializedName("LeaveDescription")
        @Expose
        String LeaveDescription;

        @SerializedName("LeaveStartDate")
        @Expose
        String LeaveStartDate;

        @SerializedName("LeaveEndDate")
        @Expose
        String LeaveEndDate;

        @SerializedName("IsApproved")
        @Expose
        String IsApproved;

        @SerializedName("LeaveApplicationDate")
        @Expose
        String LeaveApplicationDate;

        @SerializedName("ParentsName")
        @Expose
        String ParentsName;

        @SerializedName("ApprovedByByDetails")
        @Expose
        ApprovedByByDetails ApprovedByByDetails;

        @SerializedName("AttachemnetFiles")
        @Expose
        List<AttachemnetFiles> AttachemnetFiles;


        public String getProfileImage() {
            return ProfileImage;
        }

        public void setProfileImage(String profileImage) {
            ProfileImage = profileImage;
        }

        public String getEmployeeID() {
            return EmployeeID;
        }

        public void setEmployeeID(String employeeID) {
            EmployeeID = employeeID;
        }

        public LeaveDetails.ApprovedByByDetails getApprovedByByDetails() {
            return ApprovedByByDetails;
        }

        public void setApprovedByByDetails(LeaveDetails.ApprovedByByDetails approvedByByDetails) {
            ApprovedByByDetails = approvedByByDetails;
        }

        public List<LeaveDetails.AttachemnetFiles> getAttachemnetFiles() {
            return AttachemnetFiles;
        }

        public void setAttachemnetFiles(List<LeaveDetails.AttachemnetFiles> attachemnetFiles) {
            AttachemnetFiles = attachemnetFiles;
        }

        public String getLeaveID() {
            return LeaveID;
        }

        public void setLeaveID(String leaveID) {
            LeaveID = leaveID;
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

        public String getLeaveTitle() {
            return LeaveTitle;
        }

        public void setLeaveTitle(String leaveTitle) {
            LeaveTitle = leaveTitle;
        }

        public String getLeaveDescription() {
            return LeaveDescription;
        }

        public void setLeaveDescription(String leaveDescription) {
            LeaveDescription = leaveDescription;
        }

        public String getLeaveStartDate() {
            return LeaveStartDate;
        }

        public void setLeaveStartDate(String leaveStartDate) {
            LeaveStartDate = leaveStartDate;
        }

        public String getLeaveEndDate() {
            return LeaveEndDate;
        }

        public void setLeaveEndDate(String leaveEndDate) {
            LeaveEndDate = leaveEndDate;
        }

        public String getIsApproved() {
            return IsApproved;
        }

        public void setIsApproved(String isApproved) {
            IsApproved = isApproved;
        }

        public String getLeaveApplicationDate() {
            return LeaveApplicationDate;
        }

        public void setLeaveApplicationDate(String leaveApplicationDate) {
            LeaveApplicationDate = leaveApplicationDate;
        }

        public String getParentsName() {
            return ParentsName;
        }

        public void setParentsName(String parentsName) {
            ParentsName = parentsName;
        }
    }


    public class ApprovedByByDetails {
        @SerializedName("EmployeeName")
        @Expose
        String EmployeeName;

        @SerializedName("EmployeeID")
        @Expose
        String EmployeeID;

        @SerializedName("ProfileImage")
        @Expose
        String ProfileImage;

        public String getEmployeeName() {
            return EmployeeName;
        }

        public void setEmployeeName(String employeeName) {
            EmployeeName = employeeName;
        }

        public String getEmployeeID() {
            return EmployeeID;
        }

        public void setEmployeeID(String employeeID) {
            EmployeeID = employeeID;
        }

        public String getProfileImage() {
            return ProfileImage;
        }

        public void setProfileImage(String profileImage) {
            ProfileImage = profileImage;
        }
    }

    public class AttachemnetFiles {

        @SerializedName("AttachemnetFile")
        @Expose
        String AttachemnetFile;

        @SerializedName("OriginalAttachemnetFile")
        @Expose
        String OriginalAttachemnetFile;


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


}