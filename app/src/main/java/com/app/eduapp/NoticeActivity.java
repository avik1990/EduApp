package com.app.eduapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.app.eduapp.adapter.noticeadapter.NoticeExpAdapter;
import com.app.eduapp.databinding.ActivityNoticeIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.Notice;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ActivityNoticeIncBinding binding;
    Context mContext;
    NoticeExpAdapter adapterExp;
    Gson gson;
    ProgressDialog pDialog;
    Notice notice;
    ConnectionDetector cd;
    Calendar myCalendar = Calendar.getInstance();
    String st_todate = "", st_fromdate = "";
    LCDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        gson = new Gson();
        db = new LCDatabaseHandler(mContext);

        initView();

        st_fromdate = EdUtils.getCurrentDate1();
        st_todate = EdUtils.getCurrentDate();
        binding.activityNotice.tvFromDate.setText("From : " + st_fromdate);
        binding.activityNotice.tvToDate.setText("To : " + st_todate);
        binding.activityNotice.tvFromDay.setText(EdUtils.getDayfromdate(st_fromdate));
        binding.activityNotice.tvToDay.setText(EdUtils.getDayfromdate(st_todate));

        if (cd.isConnected()) {
            getNoticeList();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activityNotice.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityNotice.toolbar.tvCartcount.setText(String.valueOf(count));
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notice_inc);
        binding.activityNotice.setHandlers(this);
        binding.navView.setItemIconTintList(null);
        binding.activityNotice.toolbar.tvChild.setText("Notice");
        binding.navView.setNavigationItemSelectedListener(this);
        binding.activityNotice.toolbar.btnMenu.setOnClickListener(this);
        binding.activityNotice.vFromdate.setOnClickListener(this);
        binding.activityNotice.vTodate.setOnClickListener(this);
        binding.activityNotice.toolbar.cartView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == binding.activityNotice.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityNotice.vFromdate) {
            DatePickerDialog fromdateDialog = new DatePickerDialog(NoticeActivity.this, fromdate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            try {
                String st_fromdate = binding.activityNotice.tvToDate.getText().toString().replace("To : ", "").trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(st_fromdate);
                    long millis = date.getTime();
                    fromdateDialog.getDatePicker().setMaxDate(millis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            fromdateDialog.show();
        } else if (v == binding.activityNotice.vTodate) {
            DatePickerDialog todateDialog = new DatePickerDialog(NoticeActivity.this, todate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

            try {
                String st_fromdate = binding.activityNotice.tvFromDate.getText().toString().replace("From : ", "").trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(st_fromdate);
                    long millis = date.getTime();
                    todateDialog.getDatePicker().setMinDate(millis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            todateDialog.show();
        } else if (v == binding.activityNotice.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }

    DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String years;
            String month1 = "";
            years = "" + year;
            int month = monthOfYear;
            month = month + 1;
            if (month < 10) {
                month1 = "0" + month;
            } else {
                month1 = String.valueOf(month);
            }
            String strDateTime = dayOfMonth + "/" + month1 + "/" + years;
            st_fromdate = strDateTime;
            binding.activityNotice.tvFromDate.setText("From : " + strDateTime);
            binding.activityNotice.tvFromDay.setText(EdUtils.getDayfromdate(strDateTime));
            Log.d("Dateeeee", "" + strDateTime);


            if (cd.isConnected()) {
                getNoticeList();
            }
        }
    };
    DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String years;
            String month1 = "";
            years = "" + year;
            int month = monthOfYear;
            month = month + 1;
            if (month < 10) {
                month1 = "0" + month;
            } else {
                month1 = String.valueOf(month);
            }
            String strDateTime = dayOfMonth + "/" + month1 + "/" + years;
            st_todate = strDateTime;
            binding.activityNotice.tvToDate.setText("To : " + strDateTime);
            binding.activityNotice.tvToDay.setText(EdUtils.getDayfromdate(strDateTime));
            //21/09/2018
            if (cd.isConnected()) {
                getNoticeList();
            }
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void inflateadapter() {
        if (notice.getData().getNoticeData().size() > 0) {
            binding.activityNotice.rvRecycle.setVisibility(View.VISIBLE);
            adapterExp = new NoticeExpAdapter(mContext, notice.getData().getNoticeData());
            binding.activityNotice.rvRecycle.setLayoutManager(new LinearLayoutManager(this));
            binding.activityNotice.rvRecycle.setAdapter(adapterExp);
        } else {
            binding.activityNotice.rvRecycle.setVisibility(View.GONE);
            EdUtils.showToastShort(mContext, "No Notice Found");
        }
    }

    private void getNoticeList() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Notice> call = null;
        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            call = redditAPI.getNoticeListEMP(EdUtils.get_userID(mContext), st_fromdate.replaceAll("/", "-"), st_todate.replaceAll("/", "-"));
        } else if (EdUtils.get_userType(mContext).equalsIgnoreCase("PRN")) {
            call = redditAPI.getNoticeList(EdUtils.get_userID(mContext), st_fromdate.replaceAll("/", "-"), st_todate.replaceAll("/", "-"));
        }

        call.enqueue(new Callback<Notice>() {

            @Override
            public void onResponse(Call<Notice> call, retrofit2.Response<Notice> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    notice = response.body();
                    if (notice.getStatus().equalsIgnoreCase("1")) {
                        inflateadapter();
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Notice> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
