package com.app.eduapp.pojo;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Avik on 04-05-2018.
 */

public class SchoolDetails extends BaseObservable {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("Message")
    @Expose
    public String Message;

    @SerializedName("data")
    @Expose
    public Data data;

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

        @SerializedName("SchoolDetails")
        @Expose
        public SchoolDetailsDatum SchoolDetails;

        public SchoolDetailsDatum getSchoolDetails() {
            return SchoolDetails;
        }

        public void setSchoolDetails(SchoolDetailsDatum schoolDetails) {
            SchoolDetails = schoolDetails;
        }
    }

    public class SchoolDetailsDatum{

        @SerializedName("SchoolLogo")
        @Expose
        public String SchoolLogo;

        @SerializedName("SchoolName")
        @Expose
        public String SchoolName;

        @SerializedName("SchoolAddress")
        @Expose
        public String SchoolAddress;

        @SerializedName("SchoolPrimaryPhone")
        @Expose
        public String SchoolPrimaryPhone;

        @SerializedName("SchoolSecondryPhone")
        @Expose
        public String SchoolSecondryPhone;

        public String getSchoolLogo() {
            return SchoolLogo;
        }

        public void setSchoolLogo(String schoolLogo) {
            SchoolLogo = schoolLogo;
        }

        public String getSchoolName() {
            return SchoolName;
        }

        public void setSchoolName(String schoolName) {
            SchoolName = schoolName;
        }

        public String getSchoolAddress() {
            return SchoolAddress;
        }

        public void setSchoolAddress(String schoolAddress) {
            SchoolAddress = schoolAddress;
        }

        public String getSchoolPrimaryPhone() {
            return SchoolPrimaryPhone;
        }

        public void setSchoolPrimaryPhone(String schoolPrimaryPhone) {
            SchoolPrimaryPhone = schoolPrimaryPhone;
        }

        public String getSchoolSecondryPhone() {
            return SchoolSecondryPhone;
        }

        public void setSchoolSecondryPhone(String schoolSecondryPhone) {
            SchoolSecondryPhone = schoolSecondryPhone;
        }
    }

}