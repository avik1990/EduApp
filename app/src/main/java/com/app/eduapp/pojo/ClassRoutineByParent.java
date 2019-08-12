package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassRoutineByParent {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("Message")
    @Expose
    public String Message;

    @SerializedName("ClassName")
    @Expose
    public String ClassName;

    @SerializedName("SectionName")
    @Expose
    public String SectionName;


    @SerializedName("data")
    @Expose
    public Datum data;

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

    public class Datum {

        public List<List<ClassRoutineList>> ClassRoutineList;
        public List<DayOfWeekList> DayOfWeekList;

        public List<List<ClassRoutineByParent.ClassRoutineList>> getClassRoutineList() {
            return ClassRoutineList;
        }

        public void setClassRoutineList(List<List<ClassRoutineByParent.ClassRoutineList>> classRoutineList) {
            ClassRoutineList = classRoutineList;
        }

        public List<ClassRoutineByParent.DayOfWeekList> getDayOfWeekList() {
            return DayOfWeekList;
        }

        public void setDayOfWeekList(List<ClassRoutineByParent.DayOfWeekList> dayOfWeekList) {
            DayOfWeekList = dayOfWeekList;
        }
    }

    public class ClassRoutineList {

        @SerializedName("SlotName")
        @Expose
        public String SlotName;

        @SerializedName("StartTime")
        @Expose
        public String StartTime;

        @SerializedName("EndsTime")
        @Expose
        public String EndsTime;

        @SerializedName("IsRecess")
        @Expose
        public String IsRecess;

        @SerializedName("DayOfWeekCode")
        @Expose
        public String DayOfWeekCode;

        @SerializedName("SubjectsName")
        @Expose
        public String SubjectsName;

        @SerializedName("EmployeeName")
        @Expose
        public String EmployeeName;

        public String getSlotName() {
            return SlotName;
        }

        public void setSlotName(String slotName) {
            SlotName = slotName;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }

        public String getEndsTime() {
            return EndsTime;
        }

        public void setEndsTime(String endsTime) {
            EndsTime = endsTime;
        }

        public String getIsRecess() {
            return IsRecess;
        }

        public void setIsRecess(String isRecess) {
            IsRecess = isRecess;
        }

        public String getDayOfWeekCode() {
            return DayOfWeekCode;
        }

        public void setDayOfWeekCode(String dayOfWeekCode) {
            DayOfWeekCode = dayOfWeekCode;
        }

        public String getSubjectsName() {
            return SubjectsName;
        }

        public void setSubjectsName(String subjectsName) {
            SubjectsName = subjectsName;
        }

        public String getEmployeeName() {
            return EmployeeName;
        }

        public void setEmployeeName(String employeeName) {
            EmployeeName = employeeName;
        }
    }

    public class DayOfWeekList {

        @SerializedName("DayOfWeekCode")
        @Expose
        public String DayOfWeekCode;

        public String getDayOfWeekCode() {
            return DayOfWeekCode;
        }

        public void setDayOfWeekCode(String dayOfWeekCode) {
            DayOfWeekCode = dayOfWeekCode;
        }
    }


}
