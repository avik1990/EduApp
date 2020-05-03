package com.app.eduapp.pojo;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Avik on 04-05-2018.
 */

public class Attend extends BaseObservable {

    @SerializedName("date")
    @Expose
    String date;

    @SerializedName("day")
    @Expose
    String day;

    @SerializedName("attendstatus")
    @Expose
    String attendstatus;

    public Attend(String date, String day, String attendstatus) {
        this.date = date;
        this.day = day;
        this.attendstatus = attendstatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getAttendstatus() {
        return attendstatus;
    }

    public void setAttendstatus(String attendstatus) {
        this.attendstatus = attendstatus;
    }
}