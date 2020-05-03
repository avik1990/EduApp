package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class NCommentBean extends BaseObservable {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("Message")
    @Expose
    public String Message;

    @SerializedName("data")
    @Expose
    public Data data;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Data {

        @SerializedName("NoticeCommnetsList")
        @Expose
        List<NoticeCommnetsList> NoticeCommnetsList;

        public List<NCommentBean.NoticeCommnetsList> getNoticeCommnetsList() {
            return NoticeCommnetsList;
        }

        public void setNoticeCommnetsList(List<NCommentBean.NoticeCommnetsList> noticeCommnetsList) {
            NoticeCommnetsList = noticeCommnetsList;
        }
    }

    public class NoticeCommnetsList {

        @SerializedName("CommentsID")
        @Expose
        public String CommentsID;

        @SerializedName("CommentsBody")
        @Expose
        public String CommentsBody;

        @SerializedName("DateAdded")
        @Expose
        public String DateAdded;

        @SerializedName("FullName")
        @Expose
        public String FullName;

        @SerializedName("ProfilePicture")
        @Expose
        public String ProfilePicture;


        public String getCommentsID() {
            return CommentsID;
        }

        public void setCommentsID(String commentsID) {
            CommentsID = commentsID;
        }

        public String getCommentsBody() {
            return CommentsBody;
        }

        public void setCommentsBody(String commentsBody) {
            CommentsBody = commentsBody;
        }

        public String getDateAdded() {
            return DateAdded;
        }

        public void setDateAdded(String dateAdded) {
            DateAdded = dateAdded;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public String getProfilePicture() {
            return ProfilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            ProfilePicture = profilePicture;
        }
    }

}
