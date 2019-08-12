package com.app.eduapp.pojo;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Avik on 04-05-2018.
 */

public class StudentLeave extends BaseObservable {


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
        @SerializedName("LeaveList")
        @Expose
        public List<LeaveList> LeaveList;

        public List<StudentLeave.LeaveList> getLeaveList() {
            return LeaveList;
        }

        public void setLeaveList(List<StudentLeave.LeaveList> leaveList) {
            LeaveList = leaveList;
        }
    }

    public class LeaveList {

        @SerializedName("LeaveID")
        @Expose
        String LeaveID;

        @SerializedName("StudentsID")
        @Expose
        String  StudentsID;

        @SerializedName("FullName")
        @Expose
        String  FullName;

        @SerializedName("LeaveTitle")
        @Expose
        String  LeaveTitle;

        @SerializedName("IsApproved")
        @Expose
        String  IsApproved;

        @SerializedName("LeaveApplicationDate")
        @Expose
        String  LeaveApplicationDate;

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

        public String getLeaveTitle() {
            return LeaveTitle;
        }

        public void setLeaveTitle(String leaveTitle) {
            LeaveTitle = leaveTitle;
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
    }

}