package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherRoutine {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("Message")
    @Expose
    public String Message;

    @SerializedName("EmployeeName")
    @Expose
    public String EmployeeName;

    @SerializedName("DesignationName")
    @Expose
    public String DesignationName;

    @SerializedName("data")
    @Expose
    public Datum data;

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getDesignationName() {
        return DesignationName;
    }

    public void setDesignationName(String designationName) {
        DesignationName = designationName;
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
        @SerializedName("ClassRoutineList")
        @Expose
        public List<List<ClassRoutineList>> ClassRoutineList;

        @SerializedName("DayOfWeekList")
        @Expose
        public List<DayOfWeekList> DayOfWeekList;

        @SerializedName("ClassScheduleList")
        @Expose
        public List<ClassScheduleList> ClassScheduleList;

        public List<List<TeacherRoutine.ClassRoutineList>> getClassRoutineList() {
            return ClassRoutineList;
        }

        public void setClassRoutineList(List<List<TeacherRoutine.ClassRoutineList>> classRoutineList) {
            ClassRoutineList = classRoutineList;
        }

        public List<TeacherRoutine.DayOfWeekList> getDayOfWeekList() {
            return DayOfWeekList;
        }

        public void setDayOfWeekList(List<TeacherRoutine.DayOfWeekList> dayOfWeekList) {
            DayOfWeekList = dayOfWeekList;
        }

        public List<TeacherRoutine.ClassScheduleList> getClassScheduleList() {
            return ClassScheduleList;
        }

        public void setClassScheduleList(List<TeacherRoutine.ClassScheduleList> classScheduleList) {
            ClassScheduleList = classScheduleList;
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

        @SerializedName("ClassName")
        @Expose
        public String ClassName;

        @SerializedName("SectionName")
        @Expose
        public String SectionName;

        @SerializedName("SubjectsName")
        @Expose
        public String SubjectsName;

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

    public class ClassScheduleList {

        @SerializedName("ClassScheduleID")
        @Expose
        public String ClassScheduleID;

        @SerializedName("ClassScheduleName")
        @Expose
        public String ClassScheduleName;

        @SerializedName("IsSelected")
        @Expose
        public String IsSelected;

        public String getClassScheduleID() {
            return ClassScheduleID;
        }

        public void setClassScheduleID(String classScheduleID) {
            ClassScheduleID = classScheduleID;
        }

        public String getClassScheduleName() {
            return ClassScheduleName;
        }

        public void setClassScheduleName(String classScheduleName) {
            ClassScheduleName = classScheduleName;
        }

        public String getIsSelected() {
            return IsSelected;
        }

        public void setIsSelected(String isSelected) {
            IsSelected = isSelected;
        }
    }


}
