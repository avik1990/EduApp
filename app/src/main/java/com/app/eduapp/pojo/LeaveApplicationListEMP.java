package com.app.eduapp.pojo;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Avik on 04-05-2018.
 */

public class LeaveApplicationListEMP extends BaseObservable {


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
        @SerializedName("LeaveApplicationList")
        @Expose
        LeaveApplicationList LeaveApplicationList;

        public LeaveApplicationListEMP.LeaveApplicationList getLeaveApplicationList() {
            return LeaveApplicationList;
        }

        public void setLeaveApplicationList(LeaveApplicationListEMP.LeaveApplicationList leaveApplicationList) {
            LeaveApplicationList = leaveApplicationList;
        }
    }

    public class LeaveApplicationList {

        @SerializedName("MyLeaveApplicationCount")
        @Expose
        String MyLeaveApplicationCount;

        @SerializedName("StudentLeaveApplicationCount")
        @Expose
        String  StudentLeaveApplicationCount;


        @SerializedName("EmployeeLeaveApplicationCount")
        @Expose
        String EmployeeLeaveApplicationCount;

        public String getEmployeeLeaveApplicationCount() {
            return EmployeeLeaveApplicationCount;
        }

        public void setEmployeeLeaveApplicationCount(String employeeLeaveApplicationCount) {
            EmployeeLeaveApplicationCount = employeeLeaveApplicationCount;
        }

        public String getMyLeaveApplicationCount() {
            return MyLeaveApplicationCount;
        }

        public void setMyLeaveApplicationCount(String myLeaveApplicationCount) {
            MyLeaveApplicationCount = myLeaveApplicationCount;
        }

        public String getStudentLeaveApplicationCount() {
            return StudentLeaveApplicationCount;
        }

        public void setStudentLeaveApplicationCount(String studentLeaveApplicationCount) {
            StudentLeaveApplicationCount = studentLeaveApplicationCount;
        }
    }

}