package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMonthlyAttendance {

    @SerializedName("data")
    @Expose
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("MonthlyAttendanceList")
        @Expose
        List<MonthlyAttendanceList> MonthlyAttendanceList;

        public List<GetMonthlyAttendance.MonthlyAttendanceList> getMonthlyAttendanceList() {
            return MonthlyAttendanceList;
        }

        public void setMonthlyAttendanceList(List<GetMonthlyAttendance.MonthlyAttendanceList> monthlyAttendanceList) {
            MonthlyAttendanceList = monthlyAttendanceList;
        }
    }

    public class MonthlyAttendanceList {

        @SerializedName("Date")
        @Expose
        String Date;

        @SerializedName("FullDate")
        @Expose
        String FullDate;

        @SerializedName("Attendance")
        @Expose
        String Attendance;


        @SerializedName("InPunchTime")
        @Expose
        String InPunchTime;


        @SerializedName("OutPunchTime")
        @Expose
        String OutPunchTime;

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getFullDate() {
            return FullDate;
        }

        public void setFullDate(String fullDate) {
            FullDate = fullDate;
        }

        public String getAttendance() {
            return Attendance;
        }

        public void setAttendance(String attendance) {
            Attendance = attendance;
        }

        public String getInPunchTime() {
            return InPunchTime;
        }

        public void setInPunchTime(String inPunchTime) {
            InPunchTime = inPunchTime;
        }

        public String getOutPunchTime() {
            return OutPunchTime;
        }

        public void setOutPunchTime(String outPunchTime) {
            OutPunchTime = outPunchTime;
        }
    }

}
