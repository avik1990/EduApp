package com.app.eduapp.pojo;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Avik on 04-05-2018.
 */

public class Months extends BaseObservable {

    @SerializedName("data")
    @Expose
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("MonthsList")
        @Expose
        List<MonthsList> MonthsList;

        public List<Months.MonthsList> getMonthsList() {
            return MonthsList;
        }

        public void setMonthsList(List<Months.MonthsList> monthsList) {
            MonthsList = monthsList;
        }
    }


    public class MonthsList {
        String YearMonthDay;
        String YearMonth;
        String YearMonthFullName;

        public String getYearMonthDay() {
            return YearMonthDay;
        }

        public void setYearMonthDay(String yearMonthDay) {
            YearMonthDay = yearMonthDay;
        }

        public String getYearMonth() {
            return YearMonth;
        }

        public void setYearMonth(String yearMonth) {
            YearMonth = yearMonth;
        }

        public String getYearMonthFullName() {
            return YearMonthFullName;
        }

        public void setYearMonthFullName(String yearMonthFullName) {
            YearMonthFullName = yearMonthFullName;
        }
    }
}