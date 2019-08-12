package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Avik on 20-05-2018.
 */

public class EmployeeProfile {

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
        @SerializedName("EmployeeProfile")
        @Expose
        public EmployeeProfiles ParentsProfile;

        public EmployeeProfiles getParentsProfile() {
            return ParentsProfile;
        }

        public void setParentsProfile(EmployeeProfiles parentsProfile) {
            ParentsProfile = parentsProfile;
        }
    }

    public class EmployeeProfiles{

        @SerializedName("EmployeeID")
        @Expose
        public String  EmployeeID;

        @SerializedName("DesignationName")
        @Expose
        public String  DesignationName;

        @SerializedName("UserType")
        @Expose
        public String  UserType;

        @SerializedName("ProfilePicture")
        @Expose
        public String  ProfilePicture;

        @SerializedName("EmployeeName")
        @Expose
        public String  EmployeeName;

        @SerializedName("DateOfBirth")
        @Expose
        public String  DateOfBirth;

        @SerializedName("ShiftID")
        @Expose
        public String  ShiftID;

        @SerializedName("YearOfJoining")
        @Expose
        public String  YearOfJoining;

        @SerializedName("AadharCardNumber")
        @Expose
        public String  AadharCardNumber;

        @SerializedName("Gender")
        @Expose
        public String  Gender;

        @SerializedName("Nationality")
        @Expose
        public String  Nationality;

        @SerializedName("PrimaryMobileNumber")
        @Expose
        public String  PrimaryMobileNumber;

        @SerializedName("SecondaryMobileNumber")
        @Expose
        public String  SecondaryMobileNumber;

        @SerializedName("EmployeeCity")
        @Expose
        public String  EmployeeCity;

        @SerializedName("EmployeeState")
        @Expose
        public String  EmployeeState;

        @SerializedName("EmployeeZipCode")
        @Expose
        public String  EmployeeZipCode;

        @SerializedName("EmployeeCountryID")
        @Expose
        public String  EmployeeCountryID;

        @SerializedName("EmployeeAddress")
        @Expose
        public String  EmployeeAddress;

        @SerializedName("EmailAddress")
        @Expose
        public String  EmailAddress;

        @SerializedName("RFIDNumber")
        @Expose
        public String  RFIDNumber;

        @SerializedName("BloodGroup")
        @Expose
        public String  BloodGroup;

        public String getDesignationName() {
            return DesignationName;
        }

        public void setDesignationName(String designationName) {
            DesignationName = designationName;
        }

        public String getEmployeeID() {
            return EmployeeID;
        }

        public void setEmployeeID(String employeeID) {
            EmployeeID = employeeID;
        }

        public String getUserType() {
            return UserType;
        }

        public void setUserType(String userType) {
            UserType = userType;
        }

        public String getProfilePicture() {
            return ProfilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            ProfilePicture = profilePicture;
        }

        public String getEmployeeName() {
            return EmployeeName;
        }

        public void setEmployeeName(String employeeName) {
            EmployeeName = employeeName;
        }

        public String getDateOfBirth() {
            return DateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            DateOfBirth = dateOfBirth;
        }

        public String getShiftID() {
            return ShiftID;
        }

        public void setShiftID(String shiftID) {
            ShiftID = shiftID;
        }

        public String getYearOfJoining() {
            return YearOfJoining;
        }

        public void setYearOfJoining(String yearOfJoining) {
            YearOfJoining = yearOfJoining;
        }

        public String getAadharCardNumber() {
            return AadharCardNumber;
        }

        public void setAadharCardNumber(String aadharCardNumber) {
            AadharCardNumber = aadharCardNumber;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getNationality() {
            return Nationality;
        }

        public void setNationality(String nationality) {
            Nationality = nationality;
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

        public String getEmployeeCity() {
            return EmployeeCity;
        }

        public void setEmployeeCity(String employeeCity) {
            EmployeeCity = employeeCity;
        }

        public String getEmployeeState() {
            return EmployeeState;
        }

        public void setEmployeeState(String employeeState) {
            EmployeeState = employeeState;
        }

        public String getEmployeeZipCode() {
            return EmployeeZipCode;
        }

        public void setEmployeeZipCode(String employeeZipCode) {
            EmployeeZipCode = employeeZipCode;
        }

        public String getEmployeeCountryID() {
            return EmployeeCountryID;
        }

        public void setEmployeeCountryID(String employeeCountryID) {
            EmployeeCountryID = employeeCountryID;
        }

        public String getEmployeeAddress() {
            return EmployeeAddress;
        }

        public void setEmployeeAddress(String employeeAddress) {
            EmployeeAddress = employeeAddress;
        }

        public String getEmailAddress() {
            return EmailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            EmailAddress = emailAddress;
        }

        public String getRFIDNumber() {
            return RFIDNumber;
        }

        public void setRFIDNumber(String RFIDNumber) {
            this.RFIDNumber = RFIDNumber;
        }

        public String getBloodGroup() {
            return BloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            BloodGroup = bloodGroup;
        }
    }

}
