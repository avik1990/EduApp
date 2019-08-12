package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Avik on 20-05-2018.
 */

public class GetParentsProfile {

    @SerializedName("data")
    @Expose
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @SerializedName("ParentsProfile")
        @Expose
        public ParentsProfile ParentsProfile;

        public GetParentsProfile.ParentsProfile getParentsProfile() {
            return ParentsProfile;
        }

        public void setParentsProfile(GetParentsProfile.ParentsProfile parentsProfile) {
            ParentsProfile = parentsProfile;
        }
    }

    public class ParentsProfile{

        @SerializedName("ParentsID")
        @Expose
        public String  ParentsID;

        @SerializedName("ProfilePicture")
        @Expose
        public String  ProfilePicture;

        @SerializedName("ParentsName")
        @Expose
        public String  ParentsName;

        @SerializedName("RelationshipWithStudent")
        @Expose
        public String  RelationshipWithStudent;


        @SerializedName("PrimaryMobileNumber")
        @Expose
        public String  PrimaryMobileNumber;

        @SerializedName("SecondaryMobileNumber")
        @Expose
        public String  SecondaryMobileNumber;

        @SerializedName("ParentsAddress")
        @Expose
        public String  ParentsAddress;

        @SerializedName("ParentsPassword")
        @Expose
        public String  ParentsPassword;

        @SerializedName("OTPNumber")
        @Expose
        public String  OTPNumber;

        @SerializedName("Status")
        @Expose
        public String  Status;

        public String getParentsID() {
            return ParentsID;
        }

        public void setParentsID(String parentsID) {
            ParentsID = parentsID;
        }

        public String getProfilePicture() {
            return ProfilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            ProfilePicture = profilePicture;
        }

        public String getParentsName() {
            return ParentsName;
        }

        public void setParentsName(String parentsName) {
            ParentsName = parentsName;
        }

        public String getRelationshipWithStudent() {
            return RelationshipWithStudent;
        }

        public void setRelationshipWithStudent(String relationshipWithStudent) {
            RelationshipWithStudent = relationshipWithStudent;
        }

        public String getPrimaryMobileNumber() {
            return PrimaryMobileNumber;
        }

        public void setPrimaryMobileNumber(String primaryMobileNumber) {
            PrimaryMobileNumber = primaryMobileNumber;
        }

        public String getSecondaryMobileNumber() {
            return SecondaryMobileNumber;
        }

        public void setSecondaryMobileNumber(String secondaryMobileNumber) {
            SecondaryMobileNumber = secondaryMobileNumber;
        }

        public String getParentsAddress() {
            return ParentsAddress;
        }

        public void setParentsAddress(String parentsAddress) {
            ParentsAddress = parentsAddress;
        }

        public String getParentsPassword() {
            return ParentsPassword;
        }

        public void setParentsPassword(String parentsPassword) {
            ParentsPassword = parentsPassword;
        }

        public String getOTPNumber() {
            return OTPNumber;
        }

        public void setOTPNumber(String OTPNumber) {
            this.OTPNumber = OTPNumber;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }
    }

}
