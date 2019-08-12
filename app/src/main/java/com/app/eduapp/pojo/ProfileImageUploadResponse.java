package com.app.eduapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hakiki95 on 4/26/2017.
 */

public class ProfileImageUploadResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("IsSuccess")
        @Expose
        private String isSuccess;
        @SerializedName("UsersID")
        @Expose
        private String usersID;
        @SerializedName("ProfileImage")
        @Expose
        private String profileImage;
        @SerializedName("Message")
        @Expose
        private String message;

        public String getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(String isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getUsersID() {
            return usersID;
        }

        public void setUsersID(String usersID) {
            this.usersID = usersID;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }


}
