package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Avik on 04-05-2018.
 */

public class ApproveDisapprove extends BaseObservable {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("Message")
    @Expose
    String Message;

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
}