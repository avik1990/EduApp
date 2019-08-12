package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEmployeeListBySubjectsClass {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("Message")
    @Expose
    public String Message;

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
        @SerializedName("EmployeeList")
        @Expose
        public List<EmployeeList> EmployeeList;

        public List<GetEmployeeListBySubjectsClass.EmployeeList> getEmployeeList() {
            return EmployeeList;
        }

        public void setEmployeeList(List<GetEmployeeListBySubjectsClass.EmployeeList> employeeList) {
            EmployeeList = employeeList;
        }
    }

    public class EmployeeList {

        @SerializedName("EmployeeID")
        @Expose
        public String EmployeeID;

        @SerializedName("EmployeeName")
        @Expose
        public String EmployeeName;

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
    }
}
