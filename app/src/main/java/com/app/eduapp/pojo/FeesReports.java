package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeesReports {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("Message")
    @Expose
    String Message;

    @SerializedName("data")
    @Expose
    Datum data;

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

        @SerializedName("FeesReportsDetails")
        @Expose
        FeesReportsDetails FeesReportsDetails;

        public FeesReports.FeesReportsDetails getFeesReportsDetails() {
            return FeesReportsDetails;
        }

        public void setFeesReportsDetails(FeesReports.FeesReportsDetails feesReportsDetails) {
            FeesReportsDetails = feesReportsDetails;
        }
    }

    public class FeesReportsDetails {

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

        @SerializedName("FeesPaidTillMonth")
        @Expose
        String FeesPaidTillMonth;

        @SerializedName("FeesPaidTillMonthDate")
        @Expose
        String FeesPaidTillMonthDate;

        @SerializedName("PaymentStatus")
        @Expose
        String PaymentStatus;

        @SerializedName("IsAnnualFeespaid")
        @Expose
        String IsAnnualFeespaid;

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

        public String getFeesPaidTillMonth() {
            return FeesPaidTillMonth;
        }

        public void setFeesPaidTillMonth(String feesPaidTillMonth) {
            FeesPaidTillMonth = feesPaidTillMonth;
        }

        public String getFeesPaidTillMonthDate() {
            return FeesPaidTillMonthDate;
        }

        public void setFeesPaidTillMonthDate(String feesPaidTillMonthDate) {
            FeesPaidTillMonthDate = feesPaidTillMonthDate;
        }

        public String getPaymentStatus() {
            return PaymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            PaymentStatus = paymentStatus;
        }

        public String getIsAnnualFeespaid() {
            return IsAnnualFeespaid;
        }

        public void setIsAnnualFeespaid(String isAnnualFeespaid) {
            IsAnnualFeespaid = isAnnualFeespaid;
        }
    }
}
